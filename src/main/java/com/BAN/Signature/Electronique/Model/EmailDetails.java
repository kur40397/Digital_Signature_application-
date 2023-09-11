package com.BAN.Signature.Electronique.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
// insure that required fields are set during object creation
// valid value
// Hibernate kay3awal 3la had constructeur bach idir 5dmto , bach iretivi ldata mn db
@NoArgsConstructor
public class EmailDetails {
    private String[] recipient;
    private String From;
    private String msgBody;
    private String subject;
    private byte[] attachment;
    private String file_name;
    private boolean accepted;


}
