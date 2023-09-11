package com.BAN.Signature.Electronique.Repository;


import com.BAN.Signature.Electronique.Model.Signature;
import oracle.sql.BLOB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;

public interface SignatureRepository extends JpaRepository<Signature,Long> { // katconfiguri les method dialoulek bach i5dmo b un had les type de data
// et d'autre objet simple  sawan kan query method wla method 3adiya
     // to reuse the interface with differente type of data


     @Query(value = "select count(*) from mysignature",nativeQuery = true)
     long count();

     @Query(value = "select * from mysignature where idsign in (select signatureid from docfilesignature where docfileid in " +
             "(select iddoc from mypdfdoc where iddoc=:id_doc))",nativeQuery = true)
     List<Signature> DisplayListOfSignaturesByIddoc(Long id_doc);


}
