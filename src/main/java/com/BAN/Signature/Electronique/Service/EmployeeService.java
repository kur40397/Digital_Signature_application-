package com.BAN.Signature.Electronique.Service;

import com.BAN.Signature.Electronique.Exceptions.Exception.EmployeeNotFoundException;
import com.BAN.Signature.Electronique.Exceptions.Exception.NoneEmployeeFoundException;
import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Repository.DocfileRepository;
import com.BAN.Signature.Electronique.Repository.EmployeerRepository;
import com.BAN.Signature.Electronique.Repository.SignatureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
//@Transactional(propagation = Propagation.REQUIRED,readOnly = false)
public class EmployeeService implements EmployeeServiceInter{
    @Autowired // autowired 5asha tvalida f un bean
    EmployeerRepository employeerRepository;
    @Autowired
    DocfileRepository docfileRepository;
    @Autowired
    SignatureRepository signatureRepository;

    public static final Character[] INVALID_WINDOWS_SPECIFIC_CHARS = {'"', '*', '<', '>', '?', '|'};
    public static final String MessageException="Employee %d not found";
    public static final String MessageExceptionNoneEmployeeFoundException="We couldn't found any Employee";
    @Value("${spring.servlet.multipart.max-file-size}")
    private  String file_size;


    @Override
    public Employee SaveEmployee(Employee employee) {
        // la  recommandé
        Employee employeeInst=new Employee();
        // les setters
        employeeInst.setName(employee.getName());
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
            throw new EmployeeNotFoundException(String.format(MessageException,id));// finma kayn chi format specifiyer kantremplacih
            // b la valeur li deuxieme argument
        }
        employeerRepository.deleteById(id);
    }



    @Override
    public Employee UpdateEmployee(Long id, Employee employee) {
        Employee employeeold=employeerRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(String.format(MessageException,id)));

        employeeold.setName(employee.getName());
        employeeold.setEmail(employee.getEmail());
        employeeold.setGsm(employee.getGsm());
        employeeold.setDepartment(employee.getDepartment());
        employeeold.setMission(employee.getMission());

        return employeerRepository.save(employeeold);
        // ida kan already existe katupditih  ida kan jdid insrih f une ligne jdida
    }


    @Override
    public Employee getById(Long id) {
      return employeerRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException(String.format(MessageException,id)));
        // Hibernate.initializa katgib lik les object relié bla mat demandihoum ==> b7al eager
    }

    public Page<Employee> Search(String name, Integer page, Integer size, String sort) {
        Pageable p= PageRequest.of(page,size, Sort.by(Sort.Direction.ASC,sort));

        //Pageable un objet li kaypresenti lina la page, size and sort
        Page<Employee> page1=employeerRepository.Search(name,p);
        if(!page1.hasContent()){
            throw new NoneEmployeeFoundException(String.format(MessageExceptionNoneEmployeeFoundException));
        }
        return page1;
    }
    // page: wa7d le resultat dial la pagination
    /*
     * data total
     * actual data of the current page
     * size of chaque page
     * wech kyna chi next page wla previous page
     * wech hiya la page la5ra wla lawla
     */

    @Override
    public Page<Employee> GroupeByDepartement(String dep,Integer page,Integer size,String sort ) {
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort));
        Page<Employee> page1= employeerRepository.findByDepartment(dep,pageable);
        if(!page1.hasContent()){
            throw new NoneEmployeeFoundException(String.format(MessageExceptionNoneEmployeeFoundException));
        }
        return page1;
    }

    @Override
    public Long NumberOfEmployees() {
        Long numberEmployee= employeerRepository.count();
        if(numberEmployee==0){
            throw new NoneEmployeeFoundException(String.format(MessageExceptionNoneEmployeeFoundException));
        }
        return numberEmployee;
    }
    public  Integer CountBydepartement(String dep){
        Integer numberEmployee= employeerRepository.countByDepartment(dep);
        if(numberEmployee==0){
            throw new NoneEmployeeFoundException(String.format(MessageExceptionNoneEmployeeFoundException));
        }
        return numberEmployee;
    }
    // countBymission

    public Page<Employee> findbetweenDates(LocalDateTime from, LocalDateTime to, Integer page, Integer size, String sort) {
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sort));
                // pageable in an interface bach i5aliou les dev bach ipersonnliziou custom la pagination dialhoum
        Page<Employee> page1=  employeerRepository.findByCreatedAtBetween(from,to,pageable);
        if(!page1.hasContent()){
            throw new NoneEmployeeFoundException(String.format(MessageExceptionNoneEmployeeFoundException));
        }
        return page1;
    }

    @Override
    public List<String> findAllDepartements() {
        return employeerRepository.SelectDepartment();
    }

    @Override
    public List<String> findAllMissions() {
        return employeerRepository.SelectMissions();
    }


}



