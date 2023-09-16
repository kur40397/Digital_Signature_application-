package com.BAN.Signature.Electronique.Model;

import com.fasterxml.jackson.annotation.*;
import com.qoppa.pdf.k.b;
import jakarta.persistence.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import oracle.jdbc.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import java.io.File;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static oracle.jdbc.OracleType.BLOB;

@Setter
@Getter
@Entity
@Table(name = "mypdfdoc")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonIgnoreProperties(value = {"createdAt","updatedAt"})
// to avoid hadak stuckoverflow ,jackson llibrary kay3ti l'id lkola object kbal matserializa , bach i3kal 3lih et may3awadch isyalizih
public class Docfile {
    @Id
    //id 5aso ikoun long
    @Column(nullable = false,name = "iddoc",precision=10)
    //select mysignature_seq.nextval generate unique sequence number
    // mysignature_seq objet oracle li & next.val method bach tginiri lina the next value
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank(message = "right down your fullname")

    @Column(name = "docname",nullable = false)
    private String pdfname;
    // pdf katstoka sous forme de byte array dial byte
    // byte ==> 8 bit
    @Column(name = "docsize")
    private Long pdfsize;

    // katgol bli had l'attribut kaystoki les objets kbar contenu binaire ou bien les fichier kbar

    @Column(name="docsigned",nullable = false)
    private boolean signed=false;

    @Lob
    @Column(name="docsignedfile")
    // columnDefinition katdefini lik le type dial la base de donnée
    private byte[] signedfile;
    @Lob
    private byte[] pdffile;
    @CreationTimestamp //mnin katcriya wa7d l'entité autoquement la field dial l'ajout
    @Column(name = "createdAtDoc",nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updatedAtDoc",nullable = false)
    private LocalDateTime updatedAt; // _ un signe dial la fin du mot ==> 7aydo f entité
    @Column(name="docsignedAt")
    private LocalDateTime SignedAt;

    //@JsonBackReference
   @ManyToOne() // rah many to one fin kayna la clé éetrangère

    @JsonBackReference

    @JoinColumn(name="docemployeeid", referencedColumnName = "idemp",nullable = true)// referencedColumnName ==> smit la column li katreferenci la clé étrangère
    // mnin radi convertiou a Document object to JSON
    // json maradich iserializi la referance la clé etrangère
    // katgol lik bli @jsonmanagerReference rah
    // kadya lrad
    // Employee own documents , machi l3aks
    private Employee empl2;
   // many to many relation ship
    @ManyToMany(fetch = FetchType.LAZY) // f owning side 5asni nspecifie type de cascade & mappedby

    @JoinTable(
            name = "docfilesignature",
            joinColumns = @JoinColumn(name = "docfileid"),// la clé etrangère le katreferenci lowning side
            inverseJoinColumns = @JoinColumn(name = "signatureid"))// la clé etrangère li katreferenci non owning side
    // owing side hiya li 5as ikoun fiha jointable ama no owning side 5as ikoun fiha mappedby

    private List<Signature> signatures_file; // la collection dial l'autre coté

    // on ajout l'entity docfile, automatiquement la list dial les docfile katdar liha l'update
    // orphan makaykounch f manytomany relationship

}

