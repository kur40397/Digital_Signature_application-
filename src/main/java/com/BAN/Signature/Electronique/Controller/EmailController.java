package com.BAN.Signature.Electronique.Controller;

import com.BAN.Signature.Electronique.Model.EmailDetails;
import com.BAN.Signature.Electronique.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/NotificationEmail")
public class EmailController {
    @Autowired
    EmailService emailService;

    @GetMapping(value = "/SendMultipleEmails/{id_emp}")
    // f Request params 5asak tdir les smiya
    public  ResponseEntity<HttpStatus> SendMultipleEmails(@RequestParam(value = "ids_emp") List<Long> ids_emp
                                                  , @PathVariable Long id_emp
                                                   ,@RequestParam(value = "ids_doc") List<Long> ids_doc  ) throws MessagingException {
        // ida insiritch params , 3ando howa b7al madrti walou

        emailService.sendMailWithMultipleAttachment(ids_emp,ids_doc, id_emp);
        return new ResponseEntity<>( HttpStatus.OK);
    }

}
