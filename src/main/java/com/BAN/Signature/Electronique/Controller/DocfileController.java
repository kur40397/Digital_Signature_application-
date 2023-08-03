package com.BAN.Signature.Electronique.Controller;

import com.BAN.Signature.Electronique.Model.Docfile;
import com.BAN.Signature.Electronique.Service.DocfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.servlet.function.ServerRequest;

@RestController // bach timplimenti restful api
// bach iraj3o format json
public class DocfileController {
    @Autowired
    DocfileService docfileService;
    @PostMapping(value = "/AddFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)

  public void AddDocument(@RequestBody MultipartFile docfile){

      //return new ResponseEntity<>(docfileService.Addpdffile(docfile), HttpStatus.CREATED);
  }
}
