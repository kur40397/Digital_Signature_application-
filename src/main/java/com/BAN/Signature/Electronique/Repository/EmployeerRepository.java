package com.BAN.Signature.Electronique.Repository;

import com.BAN.Signature.Electronique.Model.Employee;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface EmployeerRepository extends JpaRepository<Employee,Long> {
    @Query(value = "SELECT * from tblemployee WHERE" +
            "(emplname LIKE '%' || :keyword || '%' " +
            "OR empemail LIKE '%' || :keyword || '%'" +
            "OR empgsm LIKE '%' || :keyword || '%' " +
            "OR emptel LIKE '%' || :keyword || '%'" +
            "OR empdepartment LIKE  '%' || :keyword || '%'" +
            "OR empmission LIKE '%' || :keyword || '%')",nativeQuery = true)

  Page<Employee> Search(String keyword, Pageable pageable);
    // 5as tous les parametres f querymethod ikounou f query

  // spring jpa take care of adding pagination in the query
  @Query(value = "SELECT * FROM tblemployee WHERE empdepartment = :department",nativeQuery = true)
  Page<Employee> findByDepartment(String department,Pageable pageable);
  // query method makasupprotich GroupBy
    // ida kan3andek problem f type dir query  7it maklkach problem f stockage f la variable
    @Query(value = "SELECT COUNT(*) FROM tblemployee WHERE empdepartment=:dep",nativeQuery = true)
    Integer countByDepartment(String dep);
    // 7sab liya les employees li 3andhoum departement ba7al hadak li kayn f params

     List<Employee> findByIdIn(List<Long> employeesid);
     //In drnaha , bach nchofo les ids dial les entités 3andna wech kaymatchiou les id li 3anda f la list
    // searching for multiple instance that match this list of entites
    // kanchofouhoum wech kaymatchiou kamline

     Page<Employee> findByCreatedAtBetween(LocalDateTime from,LocalDateTime to,Pageable pageable);
     @Query(value = "select empdepartment from tblemployee",nativeQuery = true)
     List<String> SelectDepartment();
     @Query(value = "select empmission from tblemployee",nativeQuery = true)
    List<String> SelectMissions();





}
