package com.BAN.Signature.Electronique.Controller;

import com.BAN.Signature.Electronique.Model.Docfile;
import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Model.Signature;
import com.BAN.Signature.Electronique.Service.DocfileService;
import com.BAN.Signature.Electronique.Service.EmployeeService;
import com.qoppa.pdf.PDFException;
import com.qoppa.pdfProcess.PDFDocument;
import com.qoppa.pdfSecure.PDFSecure;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DocfileService docfileService;

    @PostMapping("/AddEmployee")
    public ResponseEntity<Employee> AddEmployee(@Valid @RequestBody Employee employee){
        // kat presenti ldata li envoyaha f un formulaire

         return new ResponseEntity<>(employeeService.SaveEmployee(employee),HttpStatus.OK);
    }
    @GetMapping("/GetEmployees/{dep}")
    public ResponseEntity<Page<Employee>> GetEmployee(@PathVariable String dep,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "3") Integer size,
                                                      @RequestParam(defaultValue = "idemp") String sort){

        return new ResponseEntity<>(employeeService.GroupeByDepartement(dep,page,size,sort),HttpStatus.OK);
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
    public ResponseEntity<Employee> UpdateEmployee(@PathVariable Long id,
                                                   @RequestBody Employee employee){
        return new ResponseEntity<>(employeeService.UpdateEmployee(id,employee),HttpStatus.OK);
    }
    @GetMapping("/GetEmployeeById/{id}")
    public ResponseEntity<Employee> getByEmployee(@PathVariable Long id){
        return new ResponseEntity<>(employeeService.getById(id),HttpStatus.OK);
    }
    @GetMapping("/SearchEmployee")
    public ResponseEntity<Page<Employee>> SearchEmployee(@RequestParam String name,
                                                         @RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "5") Integer size,
                                                         @RequestParam(defaultValue = "idemp") String sort) {
        return new ResponseEntity<>(employeeService.Search(name,page,size,sort),HttpStatus.OK);
    }
    @GetMapping("/CountEmployeeByDepartment")
    public ResponseEntity<Integer> CountByDepartement(@RequestParam String dep){
        return new ResponseEntity<>(employeeService.CountBydepartement(dep),HttpStatus.OK);
    }
    @GetMapping("/GetEmployeeNumber")
    public ResponseEntity<Long> GetEmployeeNumber(){
        return new ResponseEntity<>(employeeService.NumberOfEmployees(),HttpStatus.OK);
    }
    // not in the request body because it has limitation , params la dir li briti
    // la method prefere , 7it security
    @GetMapping("/GetbetweenDates")
    public ResponseEntity<Page<Employee>> GetEmployeeBetween(@RequestParam String from,
                                                             @RequestParam String to,
                                                             @RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "5") Integer size,
                                                             @RequestParam(defaultValue = "idemp") String sort){
        DateTimeFormatter fmt=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // formatter: une expression régulière pour les dates
        return new ResponseEntity<>(employeeService.findbetweenDates(LocalDateTime.parse(from,fmt),LocalDateTime.parse(to,fmt),page,size,sort),HttpStatus.OK);
        //LocalDateTime.parse(from,fmt) jbd liya mn un string une instance localDateTime

        // kagolih kifach ikra had string bach i3raf finahowa lyear mounth , and day ... et mn tmak irj3o localDateTime
        // katgolih kifach ikra la date li f string finahouma finahiya la date & les heurs
    }
    @GetMapping("/GetAllDepartment")
    public ResponseEntity<List<String>> GetAllDepartements(){
       return new ResponseEntity<>(employeeService.findAllDepartements(),HttpStatus.OK);
    }
    @GetMapping("/GetAllMissions")
    public ResponseEntity<List<String>> GetAllMission(){
        return new ResponseEntity<>(employeeService.findAllMissions(),HttpStatus.OK);
    }
}
