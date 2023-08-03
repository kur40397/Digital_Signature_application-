package com.BAN.Signature.Electronique.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity(name = "Docfile")
public class Docfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //id 5aso ikoun long
    private Long id;
    @NotBlank(message = "insert the file name")
    private String fileName;
    // pdf katstoka sous forme de byte array dial byte
    // byte ==> 8 bit
    private byte[] fileContent;
    private Long size;
    @CreationTimestamp //set value in the database when the entity is saved

    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @ManyToOne // rah many to one fin kayna la clé étrangère
    @JoinColumn(name = "Employee_id")
    private Employee employee;

}
