package com.BAN.Signature.Electronique.Repository;

import com.BAN.Signature.Electronique.Model.Employee;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeerRepository extends JpaRepository<Employee,Long> {
  @Query(value = "SELECT * FROM tbl_Employee WHERE fullname LIKE CONCAT('%',:keyword,'%')" +
          "OR email LIKE CONCAT('%',:keyword,'%') " +
          "OR gsm LIKE CONCAT('%',:keyword,'%') " +
          "OR tel LIKE CONCAT('%',:keyword,'%') " +
          "OR department LIKE CONCAT('%',:keyword,'%')" +
          "OR mission LIKE CONCAT('%',:keyword,'%')",nativeQuery = true)
   List<Employee> Search(String keyword);

}
