package com.BAN.Signature.Electronique.Model;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "mysignature")
@JsonIgnoreProperties(value = {"createdAt","updatedAt"})
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="idsign",nullable = false,precision=10)
    private Long id;
    @NotBlank(message = "image name is blank")
    @Column(name="signname",nullable = false)
    private String name;
    @NotBlank(message = "type of an image not found")
    @Column(name="signtype",nullable = false)
    private String type;
    @Column(name="signsize",nullable = false)
    private Long size;
    @Lob
    @Column(name="signimage")
    // katgol bli had l'attribut kaystoki les objets kbar contenu binaire ou bien les fichier kbar
    private byte[] image;
    @CreationTimestamp //set value in the database when the entity is saved
    @Column(name="created_at")

    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name="updated_at")

    private LocalDateTime updatedAt;

    @OneToOne // orphane removal katdar fchild et nafs lwa9t katdir lik une clé etrangère

    @JoinColumn(name="fkemp", referencedColumnName = "idemp") // katreferenci la column f db

    // foreign key
    // kay5ali le owning side ijbed lina  non owning side
    @JsonBackReference // l'entity li fiha la referance , had la reference ga3ma katserializa

    private Employee empl2;


   @ManyToMany(fetch = FetchType.LAZY,mappedBy = "signatures_file")
   // mappedby : f manytomany katdirha f non owning side
   // ida madrtich mappedBy la table join maradich t3mar
   // f bidireactionnal nonn owning side fiha mappedby ==> many to many
   @JsonIgnore
   private List<Docfile> docfiles;
}
