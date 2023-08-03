package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Model.Employee;

import java.awt.print.Pageable;
import java.util.List;

public interface EmployeeServiceInter {
    Employee SaveEmployee(Employee employee);
    void DeleteEmployee(Long id);
    Employee UpdateEmployee(Long id,Employee employee);

    List<Employee> getAllEmployee(Integer page,Integer size,String sort);
    Employee getById(Long id);
    List<Employee> Search(String complete_name);





}
