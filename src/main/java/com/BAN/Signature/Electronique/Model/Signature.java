package com.BAN.Signature.Electronique.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "Signature")
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "empty name !")
    private String name;

    private String type;
    @Lob // katgol bli had l'attribute radi istoki large data
    // text kbir wla binary data / object binaire (image , video )
    private byte[] imageData;
    @CreationTimestamp //set value in the database when the entity is saved
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;


}
