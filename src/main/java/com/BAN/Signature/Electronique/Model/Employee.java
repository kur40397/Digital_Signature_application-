package com.BAN.Signature.Electronique.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
// fiha l'ensemble dial les interfaces et les annotation
// li kay5ali hibernate i3raf kifach imapi
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "tbl_Employee")
//@JsonIgnoreProperties("{created_at,updated_at}")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // t5dem m3a ayi ds
    private Long id;
    @NotBlank(message = "right something peal")// 5asha tkoun machi null & at least one non space character
    @Size(min = 3, max = 15)
    private String fullname;

    @NotBlank(message = "this is email boy")
    private String email;
    @Pattern(regexp = "^\\(\\+212\\)-[0-9]-[0-9]{2}-[0-9]{2}-[0-9]{2}$")
    private String gsm;
    @Pattern(regexp = "^\\(\\+212\\)-[0-9]-[0-9]{2}-[0-9]{2}-[0-9]{2}$")
    private String tel;
    @NotBlank(message = "Departement")

    private String department;
    @NotBlank(message = "you should add functionnality")
    private String mission;
    //*************************************//
    // f la db Date & time & timestamp
    // java.time.LocalDate ==> Date
    // java.time.LocalTime ==> Time
    // java.time.LocalDateTime ==> Datetime
    //*************************************//
    @CreationTimestamp //set value in the database when the entity is saved
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
    @OneToMany(mappedBy = "employee")
    private List<Docfile> file;



}
