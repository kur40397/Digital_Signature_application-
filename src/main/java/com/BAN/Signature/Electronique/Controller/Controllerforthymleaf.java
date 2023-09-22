package com.BAN.Signature.Electronique.Controller;

import com.BAN.Signature.Electronique.Enum.Department;
import com.BAN.Signature.Electronique.Model.Docfile;
import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Model.Signature;
import com.BAN.Signature.Electronique.Service.DocfileService;
import com.BAN.Signature.Electronique.Service.EmployeeService;
import com.BAN.Signature.Electronique.Service.SignatureService;
import com.itextpdf.text.DocumentException;
import com.qoppa.pdf.PDFException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;


@Controller

public class Controllerforthymleaf {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    DocfileService docfileService;
    @Autowired
    SignatureService signatureService;
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    @RequestMapping(method = RequestMethod.GET,path = "/")
    public String showAddEmployeeForm(Model model) { // model interface li katpresenti lik ldata li radi tafficha f front
        Employee employee=new Employee();
        model.addAttribute("employee",employee);
        // List.of une list immutable
        model.addAttribute("departmentslist", Department.values());
        return "employee";
    }
    @RequestMapping(method = RequestMethod.POST,path = "/save")
    public String saveEmployee(@Valid  Employee employee,Model model, BindingResult bindingResult) {
        // ModelAttribute howa hadak
        if(bindingResult.hasErrors()){
            return "employee";
        }
       Employee emp= employeeService.SaveEmployee(employee);
        model.addAttribute("Id", emp.getId());
        return "Addfile";
    }
    @RequestMapping(method = RequestMethod.POST,path = "/uploadfile")
    public String Uploadfile(@Valid MultipartFile file,@ModelAttribute(name = "id") Long id, Model model,BindingResult bindingResult ) throws DocumentException, IOException {
        if(bindingResult.hasErrors()){
            return "employee";
        }
        //ModelAttribute(name = "id") biha catchiou data li t'envoyate f formulaire
        docfileService.AdddocfiletoEmployee(file,id);
        model.addAttribute("file",new File(UPLOAD_DIRECTORY));
        model.addAttribute("Id",id);
        return "AddSignature";
    }
    @RequestMapping(method = RequestMethod.POST,path = "/uploadSignature")
    public String UploadSignature(@Valid  MultipartFile image,@ModelAttribute(name = "id") Long id, Model model,BindingResult bindingResult ) throws DocumentException, IOException {
        if(bindingResult.hasErrors()){
            return "employee";
        }
        //ModelAttribute(name = "id") biha catchiou data li t'envoyate f formulaire
        // kan7ato le nom f formulaire

        signatureService.AddSignaturetoEmployee(image,id);
        model.addAttribute("Id",id);
        return "Signefile";
    }
    @RequestMapping(method = RequestMethod.POST,path = "/sign")
    public String Signeyourfile(@ModelAttribute(name = "id") Long id, Model model,BindingResult bindingResult) throws MessagingException, GeneralSecurityException, PDFException, IOException {
        if(bindingResult.hasErrors()){
            return "employee";
        }
        Employee employee= employeeService.getById(id);
        Docfile docfile=employee.getFile1().get(0);
        Signature signature=employee.getSignature();
        docfileService.SignDocument(id,docfile.getId(),signature.getId());
        model.addAttribute("successMsg", "signature signed successfully go see check you email");
        return "success";
    }

}
