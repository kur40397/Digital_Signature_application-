package com.BAN.Signature.Electronique.Model;

import com.BAN.Signature.Electronique.Enum.Department;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.context.annotation.Lazy;
// fiha l'ensemble dial les interfaces et les annotation
// li kay5ali hibernate i3raf kifach imapi
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.FetchType;




@Getter
@Setter
@Entity(name = "tblemployee")

@NoArgsConstructor
@AllArgsConstructor

@JsonIgnoreProperties(value = {"createdAt","updatedAt"})
// 7awal ktkb des attributs m5talfin f chaque attributs bach maktkounch 3andek une ambiguité f la jointure f db
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L; // unique identifier for serializable class
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    // SEQUENCE oracle radi tginiri liya primary key en utilisant wa7d l'objet sequence

    // t5dem m3a ayi ds
    @Column(nullable = false,name = "idemp",precision=10)
    private Long Id;
    @NotBlank(message = "right down your fullname")
    //@NotEmpty : matsiftch null value & empty string
    //@NotBlank : matsiftch null value & empty string & white space
    //@NotNull :  insure that the user do not send null input
    @Size(min = 3, max = 15)
    @Column(name = "emplname",nullable = false)
    private String name;
    @NotBlank(message = "right down your fullname")
    @Email
    @Column(name = "empemail",nullable = false,length = 25)
    private String email;
    @Pattern(regexp = "^\\(\\+212\\)-[0-9]-[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}$")
    @Column(name = "empgsm",unique = true,nullable = false)
    private String gsm;
    @Pattern(regexp = "^\\(\\+212\\)-[0-9]-[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}$")
    @Column(name = "emptel",unique = true,nullable = false)
    // hna katgol la column f db rah unique makat9blach le null
    private String tel;
    //@NotBlank(message = "Departement") // katvalidi f string field
    @Enumerated(EnumType.STRING) // katdkor hna kifach l'enum radi tpersista;
    // kansifto  string representation of enum over json to make it easy for us as dev because it is understandable
    @Column(name = "empdepartment",length = 20,nullable = false)
    private Department department; // rir les enum
    @NotBlank(message = "right down your mission")
    @Column(name = "empmission",length = 20,nullable = false)
    private String mission;
    //*************************************//
    // f la db Date & time & timestamp
    // java.time.LocalDate ==> Date
    // java.time.LocalTime ==> Time
    // java.time.LocalDateTime ==> Datetime
    //*************************************//
    @CreationTimestamp //set value in the database when the entity is saved
    @Column(nullable = false,name = "createdAtEmp")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false,name = "updatedAtEmp")
    private LocalDateTime updatedAt;



    @OneToMany(cascade = CascadeType.REMOVE , orphanRemoval = true,mappedBy = "empl2",fetch = FetchType.LAZY)


    // mappedby: katgol bli l'autre coté rah referencilik b l'attribut empl2 ===> bidirectioanl ma5a // biha ka3arfo owner
    //cascade = CascadeType.REMOVE when you remove parent object you remove child object
    //CascadeType.ALL Using CascadeType.ALL means that all types of operations performed on the parent entity,
    // such as persisting, merging, refreshing, detaching, and removing, will also be applied to the associated child entities
    //***********************************************************************************************************************//
    // orphanRemoval = true  is like telling the system, "Hey, if a 'child' object is no longer connected to a 'parent' object,
    // go ahead and remove it. We don't want any 'orphans' hanging around."
    //***********************************************************************************************************************//
    //@LazyToOne(LazyToOneOption.NO_PROXY)  normalement f fetching kay39a3 wa7d delay nta kat7aydo
    // par defaut fetch lazy

    @JsonManagedReference

    private List<Docfile> file1;

    @OneToOne(cascade = CascadeType.REMOVE ,fetch = FetchType.LAZY,mappedBy = "empl2", orphanRemoval = true)
    @JsonManagedReference
    private Signature signature;
}
/*********************************************************************************/
/*
 onetoMany ===> cascade , orphanRemoval=true , mappedby
 cascadetype katdar f parent
 orphaneRemoval f parent side   & makadarch f manytomany
 */