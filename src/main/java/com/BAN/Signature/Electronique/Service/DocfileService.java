package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Exceptions.Exception.*;
import com.BAN.Signature.Electronique.Model.Docfile;
import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Model.Signature;
import com.BAN.Signature.Electronique.Repository.DocfileRepository;
import com.BAN.Signature.Electronique.Repository.EmployeerRepository;
import com.BAN.Signature.Electronique.Repository.SignatureRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.qoppa.pdf.*;
import com.qoppa.pdf.form.SignatureField;
import com.qoppa.pdf.permissions.AllPDFPermissions;
import com.qoppa.pdf.permissions.PasswordPermissions;
import com.qoppa.pdfSecure.PDFSecure;
import jakarta.mail.MessagingException;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.DecryptionMaterial;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.encryption.SecurityHandler;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import  org.springframework.web.multipart.MultipartFile;


import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@Slf4j
// log biha kan3arfo l'etat dial program lors de l'execution message  + les erreurs
// biha kan3arfo ach wa9a3 f program bla mayw93 chi crash
public class DocfileService implements DocfileServiceInter {
    @Autowired // autowired 5asha tvalida f un bean
    EmployeerRepository employeerRepository;
    @Autowired
    DocfileRepository docfileRepository;
    @Autowired
    SignatureRepository signatureRepository;
    @Autowired
    EmailService emailService;
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${spring.keystore.password}")
    private String password;
    @Value("${spring.keystore.directory}")
    private String directory;
    public static final String MessageException="Employee %d not found";
    public static final String MessageExceptionDoc="PDF Document %d not found";
    public static final String MessageExceptionSign="Signature %d not found";
    public static final String MessageExceptionIOException="Problem while accessing the file";
    public static final String MessageExceptionNonePDFDocumentFoundException="We couldn't found any pdf documents for the employee %d";
    public static final String MessageExceptionNoneSignedPDFDocumentFoundException="We couldn't found any Signed pdf documents for the employee %d";
    public static final String MessageExceptionNoneNotSignedPDFDocumentFoundException="We couldn't found any not Signed pdf documents for the employee %d";
    public static final String MessageExceptionPdfNotSignedException="the PDF Document %d is not signed";
    public static final String MessageExceptionSignatureAlreadyUsedException="Signature %d already used";
    public static final String MessageExceptionDocFileAlreadySignedException="The PDF Document %d is already signed";
    public static final String MessageExceptionFileNotFoundException="this file is not found";
    public static final String EmailForNotification="<html>\n"+
            "<head><h1> Welcome to SignBAN Mr %s </h1></head>\n" +
            "<body>\n" +
            "<p> You have just signed %s your document go check it out!</p> \n" +
            "</body>\n" +
            "</html>";
    public static final String EmailForNotification1="<html>\n"+
            "<header><title> Welcome to SignBAN Mr %s </title></header>\n" +
            "<body>\n" +
            "<p> You have just update the signature %s of your document go check it out!</p> \n" +
            "</body>\n" +
            "</html>";
    public static final String EmailForNotification2="<html>\n"+
            "<head><h1> Welcome to SignBAN Mr %s </h1></head>\n" +
            "<body>\n" +
            "<p> You have just signed %s your document send it by mr %s </p>\n" +
            "</body>\n" +
            "</html>";

    public static final Character[] INVALID_WINDOWS_SPECIFIC_CHARS = {'"', '*', '<', '>', '?', '|'};
    public static final String Link="http://localhost:8090/DocPdf/DownloadPdf_Signed/";
    @Value("${spring.servlet.multipart.max-file-size}")
    private  String file_size;
    @Override
    public Docfile AdddocfiletoEmployee(MultipartFile file, Long id_emp) throws IOException, DocumentException {
        Employee employeeref = employeerRepository.findById(id_emp).orElseThrow(() -> new EmployeeNotFoundException(String.format(MessageException,id_emp)));
        // hadi docf.getOriginalFilename() rah  tkad traja3 lik null  donc remplacinaha b Objects.requireNonNull(docf.getOriginalFilename())
         String fileName = file.getOriginalFilename();
        //cleanPath katgad lik lpath : kat7ayad lik les chemin relative , et kay5ali lik le chemin absolue
        // pour simplifier le chemin , et katraj3o easy to read and short
        // et katn9os le taux de frod
        //Objects.requireNonNull(file.getOriginalFilename()) // katvalidi lik l'objet ida kan null
        long file_size1=Long.parseLong(file_size);
        if(!file.getResource().exists())
            // getResource() biha kanaccidiou wa7d le fichier f system , bla nkatbo lpath dialou
            // biha kana5do ayi 7aja 5asha lprogram mn fichier , bla nkatbo lpath dialou
            throw new FileNotFoundException(MessageExceptionFileNotFoundException);

        List<Character> characters=new ArrayList<>(List.of(INVALID_WINDOWS_SPECIFIC_CHARS));
        // une list kat initializa b une list
        // katcriyi lik une liste fixe non modifiable List.of

        if(fileName.isEmpty()  || characters.stream().anyMatch(e -> fileName.contains(e.toString())))
            throw new InvalidFileNameException("invalid file name exception",fileName);

        if(!file.getContentType().split("/")[1].equals("pdf"))
            throw  new InvalidContentTypeException("invalid format exception");

        if(file.getSize()>file_size1)
            throw new MaxUploadSizeExceededException(file_size1);

        try {
            Docfile docfile = new Docfile();
            docfile.setPdfsize(file.getSize());
            docfile.setPdfname(file.getOriginalFilename());
            docfile.setPdffile(file.getBytes());// k serializa sous forme  lpath // reference
            docfile.setEmpl2(employeeref);
            return docfileRepository.save(docfile);
        }catch (IOException e){
            throw  new IOException(MessageExceptionIOException);
        }

    }
    // affichez la list des documents pdf

    @Override
    public Page<Docfile> SearchSignedDocsOfAnEmployeeByKeyword(Long id_emp,String keyword,Integer page, Integer size, String sort) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC,sort));
        Page<Docfile> docfileList= docfileRepository.SearchSignedDocsByKeyword(id_emp,keyword,pageable);
        if(!docfileList.hasContent())
            throw new NonePdfFoundException(String.format(MessageExceptionNoneSignedPDFDocumentFoundException,id_emp));

        return docfileList;
    }
    @Override
    public Page<Docfile> SearchNotSignedDocsOfAnEmployeeByKeyword(Long id_emp, String keyword, Integer page, Integer size, String sort) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort));
        Page<Docfile> docfileList= docfileRepository.SearchNotSignedDocsByKeyword(id_emp,keyword,pageable);
        if(!docfileList.hasContent())
            throw new NonePdfFoundException(String.format(MessageExceptionNoneNotSignedPDFDocumentFoundException,id_emp));
        return docfileList;
    }


    // GeneralSecurityException : chi error f la cryptgraphi , f keystore, f key , f l'algorithme , cryptographic signature
    // PDFException : had l'exc
    @Override
    public void SignDocument(Long id_emp, Long id_doc, Long id_sign) throws IOException, GeneralSecurityException, PDFException, MessagingException { //les exception et kaydeclaraou hna rah kaydeclaraou f controller
        Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));
        if(employee.getFile1().isEmpty())
            throw new NonePdfFoundException(String.format(MessageExceptionNonePDFDocumentFoundException,employee.getId()));

        Docfile docfile=docfileRepository.findById(id_doc).orElseThrow(() -> new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc)));
        if(!Objects.equals(docfile.getEmpl2().getId(), id_emp))
            throw new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc));

        if(docfile.isSigned())
            throw new NoneSignedPdfFoundException(String.format(MessageExceptionDocFileAlreadySignedException,id_doc));

        Signature signature=signatureRepository.findById(id_sign).orElseThrow(()->new SignatureNotFoundException(String.format(MessageExceptionSign,id_sign)));
        if(!Objects.equals(signature.getEmpl2().getId(),id_emp))
            throw new SignatureNotFoundException(String.format(MessageExceptionSign, id_sign));

            File f1 = File.createTempFile("temp", ".pdf");

            FileOutputStream fout = new FileOutputStream(f1); // un objet li kay5alina npassiou le contenu binaire a l'aide de la method write vers le fichier li kayn f parametres
            fout.write(docfile.getPdffile());

            fout.close(); // en of stream
            // Load the document

            PDFSecure pdfDoc = new PDFSecure(f1.getAbsolutePath(), null);
            // PDFSecure wa7d java library that can digitally sign pdf documents
            // kanmodifi f les permission , la signature elec, password

            // \\ katpresenti backslach wa7da  \ kaychangi l'interpretation dial un character

            FileInputStream pkcs12Stream = new FileInputStream(directory);// FIleInputStream bach tkra data dial un fichier(signature.pfx) li fih private key + certificat
            // FileInputStream open connexion with the actual file
            //*********************************************************************************************************************//

            KeyStore store = KeyStore.getInstance("PKCS12");// PKCS12 file format li fin tkad tstoki private keys, public keys, and the corresponding certificates
            //  PKCS12 kay7mi ayi access non authorise
            store.load(pkcs12Stream, password.toCharArray());//toCharArray() katconverti string lwa7d larray of chars
            // had la line kaychargi le contenu dial lkeystore file avec le mot de passe dial la clé privée

            pkcs12Stream.close();// fima radi tkra chi fichier radi tsado (close streaming)

            //**********************************la definition de la signature customize the signature **************************************//
            // SigningInformation : fiha les informations necessaire to digitally sign a PDF file
            // fiha les outils de signatures
            // object keystore , le nom du mot de passe + le mot de passe
            SigningInformation signingInformation = new SigningInformation(store, "myalias", password);

            // myalias: wa7d surname dial la certificate bach idefirenci entre les certificats
            // mypassword: le mot de passe pour la clé privée

            SignatureAppearance signatureAppearance = signingInformation.getSignatureAppearance();
            //signatureAppearance= how signature will appear // la signature customized
            // blast matwarini le nom du signataire warini la signature
            signatureAppearance.setVisibleName(false);
            signatureAppearance.setImagePosition(SwingConstants.LEFT);
            //SwingConstants fiha l'ensemble dial les constant biha bach nbadlou la position dial chi un component graphiquement

            File img = File.createTempFile("temp", ".png");

            FileUtils.writeByteArrayToFile(img, signature.getImage()); // pass binary array from file to images


            signatureAppearance.setImageFile(img.getAbsolutePath());


            signatureAppearance.setVisibleDate(false);
            signatureAppearance.setVisibleDigitallySigned(false);
            signatureAppearance.setVisibleReason(false);
            signatureAppearance.setVisibleCommonName(false);
            signatureAppearance.setVisibleOrgUnit(false);
            signatureAppearance.setVisibleOrgName(false);
            signatureAppearance.setVisibleLocal(false);
            signatureAppearance.setVisibleState(false);
            signatureAppearance.setVisibleCountry(false);

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            docfile.setSignedAt(LocalDateTime.parse(LocalDateTime.now().format(format), format));
            // LocalDateTime.parse ==> biha kanconvertiou la representation dial la date en string lformat LocalDateTime date
            // le paramter formatter ===> katgol kifach ikra la date li maktoba en format string

            signatureAppearance.setTextRight("This file is signed by " + employee.getName() + " at " + docfile.getSignedAt());
            //*********************************les champs de la signature*************************************//
            Rectangle2D signBounds = new Rectangle2D.Double(326, 700, 194, 48);// katdir object rectangle ,la position dialou x and y and la largeur dialou weight and height
            SignatureField signatureField = pdfDoc.addSignatureField(0, "signature", signBounds); // kanlocaliziou le champs li fin radi tkoun la signature dialna
            // 0 la page li fin bari tjouti hadak  & signature smit had signature field
            // alias dial hadak le signatureField


            // Apply digital signature
            pdfDoc.signDocument(signatureField, signingInformation);
            log.info("the file has been signed");



            FileOutputStream fileOutputStream = new FileOutputStream(f1);
            pdfDoc.saveDocument(fileOutputStream);// radi istam3mal had l'object bach ipassi le contenu binaire
            fileOutputStream.close();

            // file makaypresentich le contenu dialou mais kaypresenti le path dialou
            Path p = Path.of(f1.getAbsolutePath());// kat3ti 7ta le nom dial fichiers

            docfile.setSigned(true);

            docfile.setPdfname(docfile.getPdfname().substring(0, (docfile.getPdfname().length() - 4)) + "_signed.pdf"); // mn srir lkbir

            // subString 5od mn string lkbir had la partie li drt liha limite
            docfile.setPdfsize(f1.length());// size dial lfile b length

            docfile.setSignedfile(Files.readAllBytes(p));
            // Files.readAllBytes(p) kat9ra lik le contenu binaire mn un path

            // Collections.singletonList(signature) ma5asekch t5dem biha 7it hiya non modifiyable


            docfileRepository.save(docfile);
            log.info("the signed document has been in add in the database");


            FileUtils.forceDelete(f1);
            FileUtils.forceDelete(img);

        // ima radi tpassi la reference dial file wla tapassi FileOutputStream reference li radi ipassi  lik data mn prog li like
        // katkriyi lik reference dial FileOutputStream , li radi passi lik data mn pdf li fichier
        //********************************************************************************************************//

        String[] email =new String[]{employee.getEmail()};

        emailService.sendMailWithAttachment(email,sender,String.format(EmailForNotification,employee.getName(),docfile.getPdfname()),"Your document you have about to signe",docfile.getSignedfile(),docfile.getPdfname());

    }
    // Files fiha des methods static le katgiri les fichiers et les directories
    // java.nio katjiri lik les inputs output dial les fichiers , ayi source
    // java.io
    // buffer : you can use a buffer to hold a bunch of data temporarily. Then, you process the data in the buffer all at once
    @Override
    public Docfile Update_File(Long id_emp,Long id_doc,MultipartFile docfile) throws IOException {
        try {
            Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_doc)));
            if(employee.getFile1().isEmpty())
                throw new NonePdfFoundException(String.format(MessageExceptionNonePDFDocumentFoundException,employee.getId()));

            Docfile docfileold=employee.getFile1().stream().filter(x-> Objects.equals(id_doc, x.getId())).findFirst().orElseThrow(()->new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc)));
            // findFirst kayreturni lik optional value ida matchate la condition sinon empty optional
            // Objects.equals :  it won't throw an error but instead will treat them as not equal. This prevents your program from crashing due to a NullPointerException
            docfileold.setPdfname(docfile.getOriginalFilename());
            docfileold.setPdfsize(docfile.getSize());
            docfileold.setPdffile(docfile.getBytes());
            if (docfileold.isSigned()) {
                docfileold.setSignedfile(null);
                docfileold.setSigned(false);
                docfileold.setSignedAt(null);
            }
            return docfileRepository.save(docfileold);
        }catch (IOException ex){
            throw new IOException(MessageExceptionIOException);
        }
    }
    @Override
    public void  Delete_File(Long id_emp,Long id_doc){
        Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_doc)));
        if(employee.getFile1().isEmpty())
            throw new NonePdfFoundException(String.format(MessageExceptionNonePDFDocumentFoundException,employee.getId()));

        if(!docfileRepository.existsById(id_doc))
            throw new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc));

        if(employee.getFile1().stream().noneMatch(x->Objects.equals(x.getId(),id_doc)))
            throw new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc));

        docfileRepository.deleteById(id_doc);
        // void method makatowich une exception
    }
    @Override
    public Integer GetNumberDocByEmpId(Long id_emp){
        Integer count= docfileRepository.countByEmpl2Id(id_emp);
        if(count==0) throw new NonePdfFoundException(String.format(MessageExceptionNonePDFDocumentFoundException,id_emp));
        return count;
    }
    @Override
    public Integer countByEmpl2IdAndSigned(Long id_emp){
        Integer count= docfileRepository.countByEmpl2IdAndSigned(id_emp,true);
        if(count==0) throw new NonePdfFoundException(String.format(MessageExceptionNoneSignedPDFDocumentFoundException,id_emp));
        return count;
    }
    @Override
    public Integer countByEmpl2IdAndNotSigned(Long id_emp){
        Integer count=docfileRepository.countByEmpl2IdAndSigned(id_emp,false);
        if(count==0) throw new NonePdfFoundException(String.format(MessageExceptionNoneNotSignedPDFDocumentFoundException,id_emp));
        return count;
    }
    @Override
    public Docfile GetDocByIdEmployeeAndByIdDoc(Long id_emp, Long id_doc) {
        return docfileRepository.findByEmpl2IdAndId(id_emp,id_doc).orElseThrow(
                ()->id_emp==null?new EmployeeNotFoundException(String.format(MessageException,id_emp))
                        :new DocfileNotFoundException(String.format(MessageException,id_doc))
        );
    }
    @Override
    public Page<Docfile> SearchDocuments(String keyword, Long id_emp, Integer page, Integer size, String sort) {

        Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));
        if(employee.getFile1().isEmpty())
            throw new NonePdfFoundException(String.format(MessageExceptionNonePDFDocumentFoundException,employee.getId()));

        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort));

        Page<Docfile> docfileList= docfileRepository.Search(keyword,id_emp,pageable);

        if(!docfileList.hasContent()){

            throw new NonePdfFoundException(MessageExceptionNonePDFDocumentFoundException);
        }

        return docfileList;
    }

    public Page<Docfile> SearchDocsBetweenDates(String from, String to, Long id_emp, Integer page, Integer size, String sort) {
        Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));
        if(employee.getFile1().isEmpty()){
            throw new NonePdfFoundException(MessageExceptionNonePDFDocumentFoundException);
        }
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort));
        DateTimeFormatter fmt=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return docfileRepository.findByCreatedAtBetweenAndEmpl2Id(LocalDateTime.parse(from,fmt),LocalDateTime.parse(to,fmt),id_emp,pageable);
    }

    @Override
    public void DeleteSignedDocument(Long id_emp, Long id_doc) {
        Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));

        if(docfileRepository.countByEmpl2IdAndSigned(id_emp,true)==0)
            throw new NoneSignedPdfFoundException(String.format(MessageExceptionNoneSignedPDFDocumentFoundException,id_emp));

         Docfile docfile =docfileRepository.findById(id_doc).orElseThrow(()->new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc)));
        //@Nullable imkan taccepti null
        if(!Objects.equals(docfile.getEmpl2().getId(), id_emp))
            throw new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc));

        if(!docfile.isSigned())
            throw new NoneSignedPdfFoundException(String.format(MessageExceptionPdfNotSignedException,id_doc));


        docfile.setSignatures_file(null);
        docfile.setSignedAt(null);
        docfile.setSigned(false);
        docfile.setSignedfile(null);
        log.info("signed file deleted");
    }

    @Override
    public boolean IsDocumentPDFSigned(Long id_emp, Long id_doc) {
        Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));

        if(docfileRepository.countByEmpl2IdAndSigned(id_emp,true)==0)
            throw new NoneSignedPdfFoundException(String.format(MessageExceptionNoneSignedPDFDocumentFoundException,id_emp));

        Docfile docfile =docfileRepository.findById(id_doc).orElseThrow(()->new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc)));
        //@Nullable imkan taccepti null
        if(!Objects.equals(docfile.getEmpl2().getId(), id_emp))
            throw new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc));

        return docfile.isSigned();

    }

    @Override
    public void UpdateYourPdfFileSignature(Long id_emp, Long id_doc, Long id_sign) throws IOException, PDFException,
            GeneralSecurityException, MessagingException /* chi error f key chi error fla cryptographie, l'algo rsa makaych*/ {

         Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));

         if(docfileRepository.countByEmpl2IdAndSigned(id_emp,true)==0)
            throw new NoneSignedPdfFoundException(String.format(MessageExceptionNoneSignedPDFDocumentFoundException,id_emp));

         Docfile docfile=docfileRepository.findById(id_doc).orElseThrow(() -> new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc)));
         if(!Objects.equals(docfile.getEmpl2().getId(), id_emp))
             throw new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc));

         if(!docfile.isSigned())
             throw new NoneSignedPdfFoundException(String.format(MessageExceptionPdfNotSignedException,id_doc));

        Signature signature=signatureRepository.findById(id_sign).orElseThrow(()->new SignatureNotFoundException(String.format(MessageExceptionSign,id_sign)));
         if(!Objects.equals(signature.getEmpl2().getId(),id_emp))
             throw new SignatureNotFoundException(String.format(MessageExceptionSign,id_sign));



        if(docfile.getSignatures_file().stream().anyMatch(x->Arrays.equals(x.getImage(),signature.getImage())))
            throw new SignatureAlreadyUsedException(String.format(MessageExceptionSignatureAlreadyUsedException,id_sign));


        File f1 = File.createTempFile("tempo", ".pdf");
        // bach tpassi le contenu binaire li dak le fichier li 7tinah f parametre
        FileOutputStream fout = new FileOutputStream(f1);
        fout.write(docfile.getSignedfile());
        fout.close(); // banch tdiniqui program banaka saliti le flux dialek
        // Load the document
        PDFSecure pdfDoc = new PDFSecure(f1.getAbsolutePath(), null);
        SignatureField signField =  pdfDoc.getSignatureFields().get(0);
        FileInputStream pkcs12Stream = new FileInputStream(directory);// FIleInputStream bach tkra data dial un fichier(signature.pfx) li fih electronic signature


        KeyStore store = KeyStore.getInstance("PKCS12");
        store.load(pkcs12Stream, password.toCharArray());
        pkcs12Stream.close();
        SigningInformation signingInformation = new SigningInformation(store, "myalias", password);
        SignatureAppearance signatureAppearance = signingInformation.getSignatureAppearance();
        //signatureAppearance= how signature will appear // la signature customized
        signatureAppearance.setVisibleName(false);// show me the signature not signer's name
        signatureAppearance.setImagePosition(SwingConstants.LEFT);// c'est une interface

        File img = File.createTempFile("temp", ".png");
        FileUtils.writeByteArrayToFile(img, signature.getImage());
        signatureAppearance.setImageFile(img.getAbsolutePath());

        signatureAppearance.setVisibleDate(false);
        signatureAppearance.setVisibleDigitallySigned(false);
        signatureAppearance.setVisibleReason(false);
        signatureAppearance.setVisibleCommonName(false);
        signatureAppearance.setVisibleOrgUnit(false);
        signatureAppearance.setVisibleOrgName(false);
        signatureAppearance.setVisibleLocal(false);
        signatureAppearance.setVisibleState(false);
        signatureAppearance.setVisibleCountry(false);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        docfile.setSignedAt(LocalDateTime.parse(LocalDateTime.now().format(format), format));
        signatureAppearance.setTextRight("This file is signed by " + employee.getName() + " at " + docfile.getSignedAt());

        pdfDoc.signDocument(signField, signingInformation);
        log.info("the file has been signed");


        FileOutputStream fileOutputStream = new FileOutputStream(f1);
        pdfDoc.saveDocument(fileOutputStream);// radi istam3mal had l'object bach ipassi le contenu binaire mn
        fileOutputStream.close();

        // file makaypresentich le contenu dialou
        Path p = Path.of(f1.getAbsolutePath());// kat3ti 7ta le nom dial fichiers

         // mn srir lkbir
        // subString 5od mn string lkbir had la partie li drt liha limite
        docfile.setPdfsize(f1.length());// size dial lfile b length
        docfile.setSignedfile(Files.readAllBytes(p));
        docfileRepository.save(docfile);

        log.info("the signed document has been in add in the database");


        FileUtils.forceDelete(f1);
        FileUtils.forceDelete(img);
        String[] email =new String[]{employee.getEmail()};
        // mnin kayinisitaliza l'array makandirouch le nombre dial les elements f la case
        emailService.sendMailWithAttachment(email,sender,String.format(EmailForNotification,employee.getName(),docfile.getPdfname()),"your document's Signature Update",docfile.getSignedfile(),docfile.getPdfname());
    }

    @Override
    public void MakeMultipleSignatures(Long idemp_s,Long id_emp, Long id_doc, Long id_sign) throws IOException, PDFException, GeneralSecurityException, MessagingException{

        Employee employeesender=employeerRepository.findById(idemp_s).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));

        Employee employee=employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id_emp)));

        Docfile docfile=docfileRepository.findById(id_doc).orElseThrow(() -> new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc)));

        Signature signature=signatureRepository.findById(id_sign).orElseThrow(()->new SignatureNotFoundException(String.format(MessageExceptionSign,id_sign)));

        if(!Objects.equals(signature.getEmpl2().getId(),id_emp))
            throw new SignatureNotFoundException(String.format(MessageExceptionSign,id_sign));


        File f1 = File.createTempFile("temp", ".pdf");
        // bach tpassi le contenu binaire li dak le fichier li 7tinah f parametre
        FileOutputStream fout = new FileOutputStream(f1);
        if(docfile.getSignedfile()==null) {
            fout.write(docfile.getPdffile());
        }else{
            fout.write(docfile.getSignedfile());
        }
        fout.close(); // banch tdiniqui program banaka saliti le flux dialek
        // Load the document
        PDFSecure pdfDoc = new PDFSecure(f1.getAbsolutePath(), null);

        SignatureField signatureField1;
        if(pdfDoc.getSignatureFields()==null ) {
            Rectangle2D signBounds = new Rectangle2D.Double(400, 700, 194, 48);// katdir object rectangle ,la position dialou x and y and la largeur dialou weight and height
             signatureField1 = pdfDoc.addSignatureField(0, "signature", signBounds);
        }else if(!pdfDoc.getSignatureFields().isEmpty() && pdfDoc.getSignatureFields().size()<2  ){
            Rectangle2D signBounds = new Rectangle2D.Double(140, 700, 194, 48);// katdir object rectangle ,la position dialou x and y and la largeur dialou weight and height
             signatureField1 = pdfDoc.addSignatureField(0, "signature", signBounds);
        }else{
            Rectangle2D signBounds = new Rectangle2D.Double(200, 460, 194, 48);// katdir object rectangle ,la position dialou x and y and la largeur dialou weight and height
             signatureField1 = pdfDoc.addSignatureField(0, "signature", signBounds);
        }
        FileInputStream pkcs12Stream = new FileInputStream(directory);

        KeyStore store = KeyStore.getInstance("PKCS12");
        store.load(pkcs12Stream, password.toCharArray());
        pkcs12Stream.close();

        SigningInformation signingInformation = new SigningInformation(store, "myalias", password);
        SignatureAppearance signatureAppearance = signingInformation.getSignatureAppearance();
        //signatureAppearance= how signature will appear // la signature customized
        signatureAppearance.setVisibleName(false);// show me the signature not signer's name
        signatureAppearance.setImagePosition(SwingConstants.LEFT);// c'est une interface
        File img = File.createTempFile("temp", ".png");
        FileUtils.writeByteArrayToFile(img, signature.getImage());
        signatureAppearance.setImageFile(img.getAbsolutePath());
        signatureAppearance.setVisibleDate(false);
        signatureAppearance.setVisibleDigitallySigned(false);
        signatureAppearance.setVisibleReason(false);
        signatureAppearance.setVisibleCommonName(false);
        signatureAppearance.setVisibleOrgUnit(false);
        signatureAppearance.setVisibleOrgName(false);
        signatureAppearance.setVisibleLocal(false);
        signatureAppearance.setVisibleState(false);
        signatureAppearance.setVisibleCountry(false);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        docfile.setSignedAt(LocalDateTime.parse(LocalDateTime.now().format(format), format));
        signatureAppearance.setTextRight("This file is signed by " + employee.getName() + " at " + docfile.getSignedAt());

        pdfDoc.signDocument(signatureField1, signingInformation);
        log.info("the file has been signed");


        FileOutputStream fileOutputStream = new FileOutputStream(f1);
        pdfDoc.saveDocument(fileOutputStream);// radi istam3mal had l'object bach ipassi le contenu binaire mn
        fileOutputStream.close();

        // file makaypresentich le contenu dialou
        Path p = Path.of(f1.getAbsolutePath());// kat3ti 7ta le nom dial fichiers

        // mn srir lkbir
        // subString 5od mn string lkbir had la partie li drt liha limite
        docfile.setPdfsize(f1.length());// size dial lfile b length
        docfile.setSignedfile(Files.readAllBytes(p));
        docfileRepository.save(docfile);
        log.info("the signed document has been  add in the database");

        FileUtils.forceDelete(f1);
        FileUtils.forceDelete(img);
        String[] email =new String[]{employee.getEmail()};
        emailService.sendMailWithAttachment(email,sender,String.format(EmailForNotification2,employee.getName(),docfile.getPdfname(),employeesender.getName()),"Your document you have about to signe",docfile.getSignedfile(),docfile.getPdfname());
    }
}
