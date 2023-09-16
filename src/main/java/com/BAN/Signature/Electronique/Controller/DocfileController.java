package com.BAN.Signature.Electronique.Controller;

import com.BAN.Signature.Electronique.Model.Docfile;
import com.BAN.Signature.Electronique.Model.Signature;
import com.BAN.Signature.Electronique.Service.DocfileService;
import com.BAN.Signature.Electronique.Service.EmailService;

import com.itextpdf.text.DocumentException;
import com.qoppa.pdf.PDFException;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@RestController // bach timplimenti restful api
// bach iraj3o format json
@RequestMapping("/DocPdf")
public class DocfileController {
    @Autowired
    DocfileService docfileService;
    private Long idEmp;
    private String from;
    private String to;
    private Integer page;
    private Integer size;
    private String sort;

    @PostMapping(value = "/AddDocumenttoAnEmployee/{id_emp}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    // consumes kat specifi media type ==> type du dial data li kaykoun f request

    // MediaType.MULTIPART_FORM_DATA_VALUE ==> multipart/form-data ==> li knti bari tsift wa7d mix dial data f requests => json+image+file

    // @RequestParam("file") MultipartFile file : katgol spring chof an f had la requete http de type Multipart
    // f parameter dialha kayn un fichier
    // MultipartFile : representation dial hadak lfichier li ja mn request multipart request

    public ResponseEntity<Docfile> AddDocFile(@PathVariable Long id_emp,
                                              @RequestParam("file2") MultipartFile file) throws IOException, DocumentException {
        return new ResponseEntity<>(docfileService.AdddocfiletoEmployee(file,id_emp),HttpStatus.OK);
    }
    // checked exception should be managed in both of them

    @GetMapping(value = "/SignYourPdf/{id_emp}/{id_doc}/{id_sign}")
    public ResponseEntity<HttpStatus> SignDocument(@PathVariable Long id_emp,
                                                   @PathVariable Long id_doc,
                                                   @PathVariable Long id_sign) throws IOException, GeneralSecurityException, PDFException, MessagingException {
        docfileService.SignDocument(id_emp,id_doc,id_sign);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping(value = "UpdateYourSignature/{id_emp}/{id_doc}/{id_sign}")
    public ResponseEntity<HttpStatus> UpdateYourSignature(@PathVariable Long id_emp,
                                                          @PathVariable Long id_doc,
                                                          @PathVariable Long id_sign) throws IOException, GeneralSecurityException, PDFException, MessagingException {
        docfileService.UpdateYourPdfFileSignature(id_emp,id_doc,id_sign);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping(value = "MakeMultipleSignature/{idemp_se}/{id_emp}/{id_doc}/{id_sign}")
    public ResponseEntity<HttpStatus> MakeMultipleSignature(@PathVariable Long idemp_se,
                                                            @PathVariable Long id_emp,
                                                            @PathVariable Long id_doc,
                                                            @PathVariable Long id_sign) throws IOException, GeneralSecurityException, PDFException, MessagingException {
        docfileService.MakeMultipleSignatures(idemp_se,id_emp,id_doc,id_sign);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
 /*
    @GetMapping(value = "/DownloadPdf_Signed/{id_doc}",produces = MediaType.APPLICATION_PDF_VALUE)
    // katindici lik un controller method radi produit lik output in a pdf format
    public ResponseEntity<byte[]> download1(@PathVariable Long id_doc, HttpServletResponse response) throws PDFException, IOException {
          Docfile docfile=docfileService.DownloadDocument_After_signature(id_doc);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=myfile-1.pdf");
        response.setContentLength(docfile.getSignedfile().length);
        response.flushBuffer();
        // katgol lbrowser dialek blast matsna data 3ad iafichiha, katgol lbrowser bla matsna affichi data li kayna
        OutputStream out = response.getOutputStream();
        out.write(docfile.getSignedfile());

       // Resource byteArrayResource=new ByteArrayResource(docfile.getSignedfile());
        // ByteArrayResource to package data in a specific way bach itshal 3lina l5dma
        // katfaciliti lik l'envoie dial data et maktkoun 3andna une perte dial data


        //InputStreamResource : lprogram can read the file's content piece by piece, part by part
        // good if you are working with big size
        //FilesUtils ==> library for working with files and directory
        // manipulation, reading , writing , copying , moving , deleting

           return new ResponseEntity<>(docfile.getSignedfile(),HttpStatus.OK);
    }

  */
    @GetMapping(value = "/SearchAllSignedDocuments/{id_emp}")
    public ResponseEntity<Page<Docfile>> DisplayFileSigned(@PathVariable Long id_emp,
                                                           @RequestParam String keyword,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "5") Integer size,
                                                           @RequestParam(defaultValue = "iddoc") String sort ){
        return  new ResponseEntity<>(docfileService.SearchSignedDocsOfAnEmployeeByKeyword(id_emp,keyword,page,size,sort),HttpStatus.OK);
    }
    // to change
    @GetMapping(value = "/SearchAllNotSignedDocuments/{id_emp}")
    public ResponseEntity<Page<Docfile>> DisplayFileNotSigned(@PathVariable Long id_emp,
                                                              @RequestParam String keyword,
                                                              @RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "5") Integer size,
                                                              @RequestParam(defaultValue = "iddoc") String sort){
        return  new ResponseEntity<>(docfileService.SearchNotSignedDocsOfAnEmployeeByKeyword(id_emp,keyword, page,size,sort),HttpStatus.OK);
    }

    @PutMapping(value = "/UpdateDocfile/{id_emp}/{id_doc}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Docfile> UpdateDocfile(@PathVariable Long id_emp,
                                                 @PathVariable Long id_doc,
                                                 @RequestParam("file") MultipartFile docfile)throws IOException{
        return new ResponseEntity<>(docfileService.Update_File(id_emp,id_doc,docfile),HttpStatus.OK);
    }

    @DeleteMapping(value = "/DeleteDocfile/{id_emp}/{id_doc}")
    public ResponseEntity<HttpStatus> DeleteDocfile(@PathVariable Long id_emp,
                                                    @PathVariable Long id_doc){
        docfileService.Delete_File(id_emp,id_doc);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value ="/GetTotalOfDocumentsOfAnEmployee/{id_emp}" )
    public ResponseEntity<Integer> GetNumberDocById(@PathVariable Long id_emp){
        return new ResponseEntity<>(docfileService.GetNumberDocByEmpId(id_emp),HttpStatus.OK);
    }
    @GetMapping(value = "/GetTotalOfSignedDocumentsOfAnEmployee/{id_emp}")
    public ResponseEntity<Integer> countByEmpl2IdAndSigned(@PathVariable Long id_emp){
        return new ResponseEntity<>(docfileService.countByEmpl2IdAndSigned(id_emp),HttpStatus.OK);
    }
    @GetMapping(value = "/GetTotalNumberOfNotSignedDocumentsOfAnEmployee/{id_emp}")
    public ResponseEntity<Integer> countByEmpl2IdAndNotSigned(@PathVariable Long id_emp){
        return new ResponseEntity<>(docfileService.countByEmpl2IdAndNotSigned(id_emp),HttpStatus.OK);
    }

    @GetMapping(value = "/GetDocumentOfAnEmployee/{id_emp}/{id_doc}")
    public ResponseEntity<Docfile> GetDocByIdEmpAndByIdDoc(@PathVariable Long id_emp,
                                                           @PathVariable Long id_doc){

        return new ResponseEntity<>(docfileService.GetDocByIdEmployeeAndByIdDoc(id_emp,id_doc),HttpStatus.OK);
    }


    @GetMapping(value = "/SearchAllDocuments/{id_emp}")
    public ResponseEntity<Page<Docfile>> SearchAllDocs(@PathVariable Long id_emp,
                                                       @RequestParam String keyword,
                                                       @RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "5") Integer size,
                                                       @RequestParam("iddoc") String sort){
        return new ResponseEntity<>(docfileService.SearchDocuments(keyword,id_emp,page,size,sort),HttpStatus.OK);
        // makaynch chi constructeur  ResponseEntity li kat9bal rir body , (ima status bou7do,status+body,status+body+header)
    }
    @GetMapping(value = "/GetDocsbetweenDates/{id_emp}")
    // default
    public ResponseEntity<Page<Docfile>> GetDocsBetween(@PathVariable Long id_emp,
                                                        @RequestParam String from,
                                                        @RequestParam String to,
                                                        @RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "5") Integer size,
                                                        @RequestParam(defaultValue = "iddoc") String sort){

        return new ResponseEntity<>(docfileService.SearchDocsBetweenDates(from,to,id_emp,page,size,sort),HttpStatus.OK);
    }
    @GetMapping(value = "/DeleteSignedDocument/{id_emp}/{id_doc}")
    public ResponseEntity<HttpStatus> DeleteSignedDocument(@PathVariable Long id_emp,
                                                           @PathVariable Long id_doc){
        docfileService.DeleteSignedDocument(id_emp, id_doc);
        // maymknch le type d'argument f chi method ikoun void
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping(value = "/IsDocumentSigned/{id_emp}/{id_doc}")
    public ResponseEntity<Boolean>IsSignedDocument(@PathVariable Long id_emp,
                                                   @PathVariable Long id_doc){
       return new ResponseEntity<>(docfileService.IsDocumentPDFSigned(id_emp,id_doc),HttpStatus.OK);
    }

}
