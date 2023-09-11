package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Model.Docfile;
import com.itextpdf.text.DocumentException;
import com.qoppa.pdf.PDFException;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.List;

public interface DocfileServiceInter {
    Docfile AdddocfiletoEmployee(MultipartFile file, Long id) throws IOException, DocumentException;


    // affichez la list des documents pdf


    public Page<Docfile> SearchSignedDocsOfAnEmployeeByKeyword(Long id_emp,String keyword,Integer page, Integer size, String sort);

    public Page<Docfile> SearchNotSignedDocsOfAnEmployeeByKeyword(Long id_emp, String keyword, Integer page, Integer size, String sort);


    void SignDocument(Long id_emp, Long id_doc, Long id_sign) throws IOException, GeneralSecurityException, PDFException, MessagingException;

    // Files fiha des methods static le katgiri les fichiers et les directories
    // java.nio katjiri lik les inputs output dial les fichiers , ayi source
    // java.io
    // buffer : you can use a buffer to hold a bunch of data temporarily. Then, you process the data in the buffer all at once
    Docfile Update_File(Long id_emp, Long id_doc, MultipartFile docfile) throws IOException;

    void  Delete_File(Long id_emp, Long id_doc);

    Integer GetNumberDocByEmpId(Long id_emp);

    Integer countByEmpl2IdAndSigned(Long id_emp);

    Integer countByEmpl2IdAndNotSigned(Long id_emp);
    Docfile GetDocByIdEmployeeAndByIdDoc(Long id_emp, Long id_doc);
     Page<Docfile> SearchDocuments(String keyword, Long id_emp, Integer page, Integer size, String sort);
     Page<Docfile> SearchDocsBetweenDates(String from, String to, Long id_emp, Integer page, Integer size, String sort);
     void DeleteSignedDocument(Long id_emp, Long id_doc);
     boolean IsDocumentPDFSigned(Long id_emp, Long id_doc);
     void UpdateYourPdfFileSignature(Long id_emp, Long id_doc, Long id_sign) throws IOException, PDFException, GeneralSecurityException, MessagingException;
     void MakeMultipleSignatures(Long id_emp_s,Long id_emp,Long id_doc,Long id_sign)throws IOException, PDFException, GeneralSecurityException, MessagingException;
    public boolean checkIfEmployeeSignTheDocument(Long id_emp,Long id_doc);
}
