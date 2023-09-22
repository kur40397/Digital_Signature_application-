package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Exceptions.Exception.*;
import com.BAN.Signature.Electronique.Model.Docfile;
import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Model.Signature;
import com.BAN.Signature.Electronique.Repository.DocfileRepository;
import com.BAN.Signature.Electronique.Repository.EmployeerRepository;
import com.BAN.Signature.Electronique.Repository.SignatureRepository;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// bach t'implimenti chi interface 5as ikoun fiha déja des methods
@Service // Autowired 5asha tdefina f un bean
public class SignatureService implements SignatureServiceInter {
    @Autowired
    private SignatureRepository signatureRepository;
    @Autowired
    private EmployeerRepository employeerRepository;
    @Autowired
    private DocfileRepository docfileRepository;
    // hna katgol spring chnahouwa le bean li britou itinjicta f had component
    // kaymchi iscani les components la5rin
    // et mn ba3d kayinjictiha , kaykriyi l'instance
    @Value("${spring.servlet.multipart.max-file-size}")
    private  String file_size;
    public static final Character[] INVALID_WINDOWS_SPECIFIC_CHARS = {'"', '*', '<', '>', '?', '|'};
    public static final String MessageExceptionEmployeeNotFound="Employee %d not found";
    public static final String MessageException="Signature %d not found";
    public static final String MessageException1="This signature can not be deleted because it is already used for signature for a file";
    public static final String MessageExceptionIOException="Problem while accessing the file";
    public static final String MessageExceptionInvalidFileName="invalid format exception";
    public static final String MessageExceptionSignatureAlreadyExiste="Signature already existe";
    public static final String MessageExceptionSign1="We couldn't found any signature for the employee %d";
    public static final String MessageExceptionNoneSignatureFoundException="We couldn't found any Signature";
    public static final String MessageExceptionNonePDFDocumentFoundException="We couldn't found any pdf documents for the employee %d";
    public static final String MessageExceptionNoneSignatureFoundInaPdfDocException="We couldn't found any Signature in the pdf document %d";
    public static final String MessageExceptionDoc="PDF Document %d not found";
    public static final String ImageDimensionException="problem of dimension";
    public static final String MessageExceptionFileNotFoundException="this file is not found";
    public static final String MessageExceptionInvalidFileNameException="invalid file name exception";

    @Override
    public Signature AddSignaturetoEmployee(MultipartFile image, Long id_emp) throws IOException {
        Employee employee= employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageExceptionEmployeeNotFound,id_emp)));
        String imageName = image.getOriginalFilename();
        long file_size1=Long.parseLong(file_size);
        if(!image.getResource().exists()){ // la resource hiya ayi 7aja kayna f fichier system
            // getResource() biha kanaccidiou wa7d le fichier f system , bla nkatbo lpath dialou
            // biha kana5do ayi 7aja 5asha lprogram mn fichier , bla nkatbo lpath dialou

            throw new FileNotFoundException(MessageExceptionFileNotFoundException);
        }
        List<Character> characters=new ArrayList<>(List.of(INVALID_WINDOWS_SPECIFIC_CHARS));
        // une list kat initializa b une liste
        // katcriyi lik une liste fixe non modifiable List.of


        if(imageName.isEmpty() || (imageName.length() < 2) || characters.stream().anyMatch(e->imageName.contains(e.toString())) ){
            throw new InvalidFileNameException(MessageExceptionInvalidFileNameException,imageName);
        }
        // equals kancompariou bih texte
        if(!image.getContentType().split("/")[1].equals("png")){

            throw  new InvalidContentTypeException(MessageExceptionInvalidFileName);
        }
        if(image.getSize()>file_size1){
            throw new MaxUploadSizeExceededException(file_size1);
        }

        BufferedImage imbu= ImageIO.read(image.getInputStream());
        // ImageIO kat9ra lik le contenu binaire dial une image
        // getInputStream  ==> InputStream object li fih le contenu binaire
        /*
        if(imbu.getHeight()>200 || imbu.getWidth()>400||imbu.getWidth()<100 || imbu.getHeight()<150){
            throw new ImageDimensionException(ImageDimensionException);// exception
        }
         */
        try{
            // katvalidi lik le parameter ida kan non null ma7ba ida makanch rah kat throwi null
            Signature signature1=new Signature();
            signature1.setName(imageName);
            //*********************************************//
            signature1.setImage(image.getBytes());
            //*********************************************//
            signature1.setType(image.getContentType());
            signature1.setSize(image.getSize());
            signature1.setEmpl2(employee);
            return signatureRepository.save(signature1);
            // return wa7d structure de code pach t5roj mn la fonction et 7ta l'exception kaytritiha le compilateur
            // comme une instruction d'exit
            // le compilateur makayforcich 3lik tretuni , exception kafiya
        }catch (IOException e){
            throw new IOException(MessageExceptionIOException);
        }
    }
    @Override
    public void DeleteSignature(Long id_emp, Long id_sign) {
        Employee employee = employeerRepository.findById(id_emp).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageExceptionEmployeeNotFound,id_emp)));
        if(employee.getSignature()==null){
            throw new SignatureNotFoundException(String.format(MessageExceptionSign1,employee.getId()));
        }
        if(Objects.equals(employee.getSignature().getId(), id_sign)) {
            Signature signature= employee.getSignature();
            if (!signature.getDocfiles().isEmpty()) {
                throw new SignatureAlreadyUsedException(MessageException1);
            }
            signatureRepository.deleteById(id_sign);
        }
            throw new SignatureNotFoundException(String.format(MessageException, id_sign));
    }

    @Override
    public Signature UpdateSignature(Long id_emp, MultipartFile image, Long id_sign) throws IOException {
        Employee employee = employeerRepository.findById(id_emp).orElseThrow(() -> new EmployeeNotFoundException(String.format(MessageExceptionEmployeeNotFound, id_emp)));
        if (employee.getSignature() == null) {
                throw new SignatureNotFoundException(String.format(MessageExceptionSign1,employee.getId()));
        }
        if (!Objects.equals(employee.getSignature().getId(), id_sign)) {
                throw new SignatureNotFoundException(String.format(MessageException, id_sign));
        }
        Signature signature= employee.getSignature();
        String imageName = image.getOriginalFilename();
        long file_size1=Long.parseLong(file_size);
        if(!image.getResource().exists()){ // la resource hiya ayi 7aja kayna f fichier system
            // getResource() biha kanaccidiou wa7d le fichier f system , bla nkatbo lpath dialou
            // biha kana5do ayi 7aja 5asha lprogram mn fichier , bla nkatbo lpath dialou
            throw new FileNotFoundException(MessageExceptionFileNotFoundException);
        }
        List<Character> characters=new ArrayList<>(List.of(INVALID_WINDOWS_SPECIFIC_CHARS));
        // une list kat initializa b une liste
        // katcriyi lik une liste fixe non modifiable List.of
        if(imageName.isEmpty() || (imageName.length() < 2) || characters.stream().anyMatch(e->imageName.contains(e.toString())) ){
            throw new InvalidFileNameException(MessageExceptionInvalidFileNameException,imageName);
        }
        // equals kancompariou bih texte
        if(!image.getContentType().split("/")[1].equals("png")){
            throw  new InvalidContentTypeException(MessageExceptionInvalidFileName);
        }
        if(image.getSize()>file_size1){
            throw new MaxUploadSizeExceededException(file_size1);
        }
        BufferedImage imbu= ImageIO.read(image.getInputStream());
        // ImageIO kat9ra lik le contenu binaire dial une image
        // getInputStream  ==> InputStream object li fih le contenu binaire
        if(imbu.getHeight()>200 || imbu.getWidth()>400||imbu.getWidth()<100 || imbu.getHeight()<150){
            throw new ImageDimensionException(ImageDimensionException);// exception
        }
        try{
                    signature.setName(image.getOriginalFilename());
                    // getName() kat3tina key parameter f Filemutipart request li value dialou howa hadak le fichier
                    signature.setType(image.getContentType());
                    signature.setImage(image.getBytes());// mnin katgiri binary data 5asek throwi IOException
                    signature.setSize(image.getSize());
                    return signatureRepository.save(signature);
        }catch(IOException ex){
                   throw new IOException(MessageExceptionIOException);
        }
    }
    @Override
    public Signature GetSignature(Long id_emp) {
        Employee employee = employeerRepository.findById(id_emp).orElseThrow(() -> new EmployeeNotFoundException(String.format(MessageExceptionEmployeeNotFound, id_emp)));
        if (employee.getSignature() == null) {

            throw new SignatureNotFoundException(String.format(MessageExceptionSign1,employee.getId()));

        }
        return employee.getSignature();

    }


    @Override
    public List<Signature> DisplayListOfSignaturesByIddoc(Long id_emp, Long id_doc) {
        Employee employee = employeerRepository.findById(id_emp).orElseThrow(() -> new EmployeeNotFoundException(String.format(MessageExceptionEmployeeNotFound, id_emp)));
        if (employee.getSignature() == null)
            throw new SignatureNotFoundException(String.format(MessageExceptionSign1,employee.getId()));

        if(employee.getFile1().isEmpty())
            throw new NonePdfFoundException(String.format(MessageExceptionNonePDFDocumentFoundException,employee.getId()));

        Docfile docfile=employee.getFile1().stream().filter(x-> Objects.equals(id_doc, x.getId())).findFirst().orElseThrow(()->new DocfileNotFoundException(String.format(MessageExceptionDoc,id_doc)));
        // filter : kat5dem m3a les streams , kat5od le premier resultat dial un filtre & katreturni object optional
        if (!Objects.equals(docfile.getId(), id_doc)) {
            throw new SignatureNotFoundException(String.format(MessageExceptionDoc, docfile.getId()));
        }
        if(docfile.isSigned())
            return docfile.getSignatures_file();

        throw new NoneSignatureExistInAPdfDocException(String.format(MessageExceptionNoneSignatureFoundInaPdfDocException,id_doc));
    }
}
