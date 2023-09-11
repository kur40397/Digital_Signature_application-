package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Model.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;

import java.util.List;

public interface EmailServiceInter {
  //  void sendSimpleMail(EmailDetails details) throws MessagingException;
    // send simple text
    void sendMailWithAttachment(String[] recipient, String From, String msgBody, String subject, byte[] attachment,String file_name)throws MessagingException ;
    // send with attachement

    void sendMailWithMultipleAttachment(List<Long> recipient,List<Long> ids_doc,Long id_emp)throws MessagingException;
}
