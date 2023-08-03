package com.BAN.Signature.Electronique.Controller;

import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/AddEmployee")
    public ResponseEntity<Employee> AddEmployee(@RequestBody @Valid Employee employee){
        return new ResponseEntity<>(employeeService.SaveEmployee(employee), HttpStatus.CREATED);
    }
    @GetMapping("/GetEmployee")
    public ResponseEntity<List<Employee>> GetEmployee(@RequestParam(defaultValue = "0")Integer page
                                                     ,@RequestParam(defaultValue = "3") Integer size
                                                      ,@RequestParam(defaultValue = "id") String sort ){

        return new ResponseEntity<>(employeeService.getAllEmployee(page,size,sort),HttpStatus.OK);

    }

    @DeleteMapping("/DeleteEmployee/{id}")
    // generic type kat5lik tspecifi f ldata li method dialek radi treturni
    // bach ndirou l'appel la method
    public ResponseEntity<Employee> DeleteEmployee(@PathVariable Long id ){
        employeeService.DeleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        // f body dir liya une method li katreturni , bima anaka drti une method ya3ni katsna mnek un body ,
    }
    @PutMapping("/UpdateEmployee/{id}")
    public ResponseEntity<Employee> UpdateEmployee(@PathVariable Long id,@RequestBody Employee employee){
        return new ResponseEntity<>(employeeService.UpdateEmployee(id,employee),HttpStatus.OK);
    }
    @GetMapping("/GetEmployeeById/{id}")
    public ResponseEntity<Employee> getByEmployee(@PathVariable Long id){
        return new ResponseEntity<>(employeeService.getById(id),HttpStatus.OK);
    }
    @GetMapping("/GetEmployeeByName")
    public ResponseEntity<List<Employee>> getByEmployee(@RequestParam String name){
        return new ResponseEntity<>(employeeService.Search(name),HttpStatus.OK);
    }

}
