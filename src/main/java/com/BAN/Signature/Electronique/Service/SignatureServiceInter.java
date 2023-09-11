package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Model.Signature;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface SignatureServiceInter {
    Signature AddSignaturetoEmployee(MultipartFile image, Long id_emp) throws IOException, SQLException;

    void DeleteSignature(Long id_emp, Long id_doc);
    Signature UpdateSignature(Long id_emp, MultipartFile file, Long id_sign) throws IOException;

    Signature GetSignature(Long id_emp);




    List<Signature> DisplayListOfSignaturesByIddoc(Long id_emp, Long id_doc);

}
