package com.BAN.Signature.Electronique.Repository;

import com.BAN.Signature.Electronique.Model.Docfile;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocfileRepository extends JpaRepository<Docfile,Long> {


     // f query method imkan lina mn une reference tmchi les attributs dial l'autre entity li referencna liha
     @Query(value = "SELECT * from mypdfdoc , tblemployee  where idemp = :id_emp " +
             "AND (docname LIKE '%' || :keyword || '%' " +
             "OR emplname LIKE '%' || :keyword || '%' " +
             "OR empemail LIKE '%' || :keyword || '%'" +
             "OR empgsm LIKE '%' || :keyword || '%' " +
             "OR emptel LIKE '%' || :keyword || '%') " +
             "AND idemp = docemployeeid AND docsigned=1 ",nativeQuery = true)
     Page<Docfile> SearchSignedDocsByKeyword(Long id_emp,String keyword,Pageable pageable);

    @Query(value = "SELECT * from mypdfdoc , tblemployee  where idemp = :id_emp " +
            "AND (docname LIKE '%' || :keyword || '%' " +
            "OR emplname LIKE '%' || :keyword || '%' " +
            "OR empemail LIKE '%' || :keyword || '%'" +
            "OR empgsm LIKE '%' || :keyword || '%' " +
            "OR emptel LIKE '%' || :keyword || '%') " +
            "AND idemp = docemployeeid AND docsigned=0 ",nativeQuery = true)
    Page<Docfile> SearchNotSignedDocsByKeyword(Long id_emp,String keyword,Pageable pageable);

     Integer countByEmpl2Id(Long Id);
     Integer countByEmpl2IdAndSigned(Long Id, Boolean signed);
     Optional<Docfile> findByEmpl2IdAndId(Long id_emp, Long id_doc);
     // parameter lawal 5aso ikoun howa compatible le premier attribut f query method
     // ida kan 3ande return type Option darouri 5asek tgiri le cas li fin optional is empty

     // 7it ida kan empty exception occure
     List<Docfile> findByIdIn(List<Long> ids_doc);
     // la recherche general
      @Query(value = "SELECT * from mypdfdoc , tblemployee  where idemp = :id_emp " +
              "AND (docname LIKE '%' || :keyword || '%' " +
              "OR emplname LIKE '%' || :keyword || '%' " +
              "OR empemail LIKE '%' || :keyword || '%'" +
              "OR empgsm LIKE '%' || :keyword || '%' " +
              "OR emptel LIKE '%' || :keyword || '%') " +
              "AND idemp = docemployeeid",nativeQuery = true)
      // "%" katpresenti zero or more characters
      // f nativeQuery 5asni ndkor les nom dial les column f db machi les attributs 7it c'est une requet dial la db
      // : bach tpresenti wa7d lparamter li f quer f la requet dialek
      // binding :biha katrempaci les parameters f query method f placeholder li kaynin f query
      Page<Docfile> Search(String keyword,Long id_emp,Pageable pageable);
      // query method biha kay3awan spring bach idetermini sql query  mn tamk kay3raf les champs, la table ...

      Page<Docfile> findByCreatedAtBetweenAndEmpl2Id(LocalDateTime from,LocalDateTime to,Long id_emp,Pageable pageable);

      boolean existsByEmpl2IdAndIdIn(Long id_emp,List<Long> ids_doc);
      boolean existsByIdInAndSigned(List<Long> ids_doc,boolean signed);
}
