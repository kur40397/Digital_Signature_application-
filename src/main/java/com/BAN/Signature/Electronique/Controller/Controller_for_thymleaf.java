package com.BAN.Signature.Electronique.Controller;

import com.BAN.Signature.Electronique.Enum.Department;
import com.BAN.Signature.Electronique.Model.Employee;
import com.BAN.Signature.Electronique.Repository.EmployeerRepository;
import com.BAN.Signature.Electronique.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class Controller_for_thymleaf {
    @Autowired
    EmployeeService employeeService;
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) { // model interface li katpresenti lik ldata li radi tafficha f front
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        List<String> listarray= Arrays.asList("Developer", "Tester", "Architect");
        model.addAttribute("departmentslist", listarray);
        return "newemployee";
    }
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        // ModelAttribute howa hadak
        employeeService.SaveEmployee(employee);
        return "redirect:/";
    }
}
