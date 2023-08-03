package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Repository.EmployeerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements EmployeeServiceInter{
    @Autowired // autowired 5asha tvalida f un bean
    EmployeerRepository employeerRepository;
    @Override
    public Employee SaveEmployee(Employee employee) {
        // la  recommandé
        Employee employeeInst=new Employee();
        // les setters
        employeeInst.setFullname(employee.getFullname());
        employeeInst.setEmail(employee.getEmail());
        employeeInst.setTel(employee.getTel());
        employeeInst.setGsm(employee.getGsm());
        employeeInst.setDepartment(employee.getDepartment());
        employeeInst.setMission(employee.getMission());

        return employeerRepository.save(employeeInst);
    }
    // l'exception katthowa peut importe le type de retour
    @Override
    public void DeleteEmployee(Long id) {
        if(!employeerRepository.existsById(id)){
            throw new EntityNotFoundException("id not found");
        }
        employeerRepository.deleteById(id);
    }


    @Override
    public Employee UpdateEmployee(Long id, Employee employee) {
        Employee employeeold=employeerRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("id not found"));

        employeeold.setFullname(employee.getFullname());
        employeeold.setEmail(employee.getEmail());
        employeeold.setGsm(employee.getGsm());
        employeeold.setDepartment(employee.getDepartment());
        employeeold.setMission(employee.getMission());
        return employeerRepository.save(employeeold);
        // ida kan already existe katupditih  ida kan jdid insrih f une ligne jdida
    }

    @Override
    public List<Employee> getAllEmployee(Integer page,Integer size,String sort) {

           Sort s=Sort.by(Sort.Direction.ASC,sort);
            Pageable p= PageRequest.of(page, size,s);
           List<Employee> employee = new ArrayList<>();
           employeerRepository.findAll(p).forEach((e) -> employee.add(e));
           if (employee.isEmpty()) {
               return null;
           }
           return employee;
    }

    @Override
    public Employee getById(Long id) {
       return employeerRepository.findById(id).orElseThrow(()->new EntityNotFoundException("id"+id+"is not found"));
    }

    @Override
    public List<Employee> Search(String complete_name) {
        List<Employee> list=employeerRepository.Search(complete_name);
        if(list.isEmpty()){
            return null;
        }
        return list;
    }



}
