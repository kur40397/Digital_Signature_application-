package com.BAN.Signature.Electronique.Controller;

import com.BAN.Signature.Electronique.Model.Signature;
import com.BAN.Signature.Electronique.Service.SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@RestController
// katraja3 l'application dialna restful api
@RequestMapping("/Signature")
public class SignatureController {
    @Autowired
    private SignatureService signatureService;
  @DeleteMapping("/DeleteSignature/{id_emp}/{id_doc}")
    public ResponseEntity<HttpStatus> DeleteSignature(@PathVariable Long id_emp,
                                                      @PathVariable Long id_doc){
      signatureService.DeleteSignature(id_emp,id_doc);
      return new ResponseEntity<>(HttpStatus.OK);
  }
  @PutMapping(value = "/UpdateSignature/{id_emp}/{id_sign}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  // mutlipart http request ==> sending differente type of data in single http method
    public ResponseEntity<Signature> UpdateSignature(@PathVariable Long id_emp,
                                                     @PathVariable Long id_sign,
                                                     @RequestParam MultipartFile file1)throws IOException {
      return new ResponseEntity<>(signatureService.UpdateSignature(id_emp,file1,id_sign),HttpStatus.OK);
  }
  @GetMapping("/GetSignature/{id_emp}")
  public ResponseEntity<Signature> GetSignature(@PathVariable Long id_emp){ // ma5ach ikoun chi path variable zayd f la method ida makanch f request
    return new ResponseEntity<>(signatureService.GetSignature(id_emp),HttpStatus.OK);
  }

  @PostMapping(value = "/AddSignaturetoAnEmployee/{id_emp}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Signature> AddSignaturetoEmployee(@PathVariable Long id_emp,
                                                          @RequestParam("file2") MultipartFile file) throws IOException {

    return new ResponseEntity<>(signatureService.AddSignaturetoEmployee(file,id_emp),HttpStatus.OK);
  }
  @GetMapping(value = "/DisplaySignaturesOfADocument/{id_emp}/{id_doc}")
  public ResponseEntity<List<Signature>> DisplayListOfSignaturesByIddoc(@PathVariable Long id_emp,
                                                                        @PathVariable Long id_doc){// set makaynch duplicated values
    return new ResponseEntity<>(signatureService.DisplayListOfSignaturesByIddoc(id_emp,id_doc),HttpStatus.OK);
  }
}
