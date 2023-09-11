package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Model.Employee;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeServiceInter {
    Employee SaveEmployee(Employee employee);
    void DeleteEmployee(Long id);
    Employee UpdateEmployee(Long id,Employee employee);


    Employee getById(Long id);
    Page<Employee> Search(String name, Integer page, Integer size, String sort);
     Page<Employee> GroupeByDepartement(String dep,Integer page,Integer size,String sort );
    Long NumberOfEmployees();
    Integer CountBydepartement(String dep);

     Page<Employee> findbetweenDates(LocalDateTime from,LocalDateTime to, Integer page, Integer size, String sort);

    List<String> findAllDepartements();
    List<String> findAllMissions();



}
