package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Exceptions.Exception.*;
import com.BAN.Signature.Electronique.Model.Docfile;
import com.BAN.Signature.Electronique.Model.EmailDetails;
import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Repository.DocfileRepository;
import com.BAN.Signature.Electronique.Repository.EmployeerRepository;
import jakarta.activation.DataSource;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j // katsimplifi lik le logging

public class EmailService implements EmailServiceInter{

   // Spring will look for a bean that implements this interface and inject an instance of that bean into the field.
    @Autowired
    private JavaMailSender javaMailSender;// katgolih chnhouwa le bean li britih itinjicta
    // iyi 7aja katinjicta jaya mn import org.springframework.beans.factory.annotation
    // biha kanenvoyiou les email
    @Autowired
    private EmployeerRepository employeerRepository;
    @Autowired
    private DocfileRepository docfileRepository;


    @Value("${spring.mail.username}")
    private String sender;
    private final static String LinkMail="http://localhost:3000/DocPdf/SignYourPdf/";
    public static final String MessageException="Employee %d not found";
    public static final String MessageExceptionNoneEmployeeFoundException="We couldn't found any Employee";
    public static final String MessageExceptionNonePDFDocumentFoundException="We couldn't found any pdf documents";
    public static final String MessageExceptionMessagingException="Error while sending an email";
    public static final String MessageExceptionPDFDocFileAlreadySignedException="your Document you about to send is already signed!";

    @Override
    public void sendMailWithAttachment( String[] recipient, String From, String msgBody, String subject, byte[] attachment,String file_name) throws MessagingException { // finma trowi une exception h a
       // la création de l'object
        EmailDetails emailDetails=new EmailDetails();
        emailDetails.setFrom(From);
        emailDetails.setRecipient(recipient);
        emailDetails.setMsgBody(msgBody);
        emailDetails.setSubject(subject);
        emailDetails.setAttachment(attachment);
        emailDetails.setFile_name(file_name);

        // create MimeMessage from javaMailSender
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper createMessageHelper = new MimeMessageHelper(message, true);


        // java mail API

        // javaMailSender katenvoyi lik lmail (en se basant 3la des configure) et kat katkiryi lik MimeMessage
        try {
            createMessageHelper.setFrom(sender);
            createMessageHelper.setTo(recipient);
            createMessageHelper.setSubject(emailDetails.getSubject());
            createMessageHelper.setText(emailDetails.getMsgBody(),true);
            // charset=utf-8 tell the computer how to decode had les characters et kifach displayihoum
            // wa7d decoder bach ifham les characters et ktbhoum the right way
            createMessageHelper.addAttachment(emailDetails.getFile_name(), new ByteArrayDataSource(emailDetails.getAttachment(), "application/pdf;"));

            // DataSource : wa7d container dial hadik data li radi tenvoya f email
            // ByteArrayDataSource : hiya data li presentiya as a byteArray

            javaMailSender.send(message);
            log.info("the email has been sent");
        }catch (MessagingException e){
            throw new MessagingException(MessageExceptionMessagingException);
        }
    }
    // MIME protocol pour l'envoie des emails
    @Override
    public void sendMailWithMultipleAttachment( List<Long> recipient,List<Long> ids_doc,Long id_emp) throws MessagingException,AddressException {
        // sender
        Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));
        // check if findByIdIn return all it recipent in the params

        List<Employee> listEmployee= employeerRepository.findByIdIn(recipient);

        if(listEmployee.isEmpty())
            throw new NoneEmployeeFoundException(MessageExceptionNoneEmployeeFoundException);

        List<Docfile> listdocs=docfileRepository.findByIdIn(ids_doc);
        if(listdocs.isEmpty())
            throw new NonePdfFoundException(MessageExceptionNonePDFDocumentFoundException);
        // hna kanchouf wech tous les documents tab3in lhad lsender
        if(!docfileRepository.existsByEmpl2IdAndIdIn(id_emp,ids_doc))
            throw new NonePdfFoundException(MessageExceptionNonePDFDocumentFoundException);

        // ma5ashoumch ikoun signé
        if(docfileRepository.existsByIdInAndSigned(ids_doc,true))
            throw new DocFileAlreadySignedException(MessageExceptionPDFDocFileAlreadySignedException);



        // mnin katkriyi un array darouri tinisializih
        // Collections.singletonList fiha single element
        // ida briti tdir une try/catch 5asak tdir directment f la method

        int size=listEmployee.size();
        InternetAddress[] addresses = new InternetAddress[listEmployee.size()];
        // une method li parameter dialha abstruct kat9bal kat9bal les object li kayimplimentiouha

        for(int i=0;i<size;i++){
               addresses[i] = new InternetAddress(listEmployee.get(i).getEmail());
        }
        MimeMessage message=javaMailSender.createMimeMessage();
        // MimeMessage katpresenti lik email et kat5alik tconstruit une structure dialou
        // katsimplifi lik l5dma dial mimeMessage & 3andha des methods bach t5dem for sending attachement
        // parse ==> bach tparcilik one email or more email
        MimeMessageHelper createMessageHelper = new MimeMessageHelper(message, true);// katsimplifi lik createMimeMessage
        List<Long> links=new ArrayList<>();
        for (Docfile listdoc : listdocs) {
            links.add(listdoc.getId());
        }
        for(int i=0;i<listEmployee.size() ;i++) {
            try {
                createMessageHelper.setFrom(sender);
                createMessageHelper.setTo(addresses[i]);
                createMessageHelper.setCc(employee.getEmail());
                createMessageHelper.setSubject("Notification from Sign documents");
                // ByteArrayDataSource: kaypresenti lik wa7d datasource li fiha byteArray
                //  DataSource : katpresenti lik wa7d lcontenu li radi tenvoya f email
                createMessageHelper.setText("Mr "+employee.getName()+" want from you to sign this document his the link "+
                        LinkMail+listEmployee.get(i).getId()+"?id_docs="+links.toString().replace("[", "").replace("]", "").replace(" ",""));

                for (int j=0;j<listdocs.size();j++) {
                    createMessageHelper.addAttachment(listdocs.get(j).getPdfname(), new ByteArrayDataSource(listdocs.get(j).getPdffile(), "application/pdf;"));
                }
                javaMailSender.send(message);
                log.info("the email has been sent");
            } catch (MessagingException e) {
                throw new MessagingException(MessageExceptionMessagingException);// trowiti une exception 5ask tgiriha
            }
        }
    }
}
