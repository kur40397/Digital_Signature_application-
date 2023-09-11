package com.BAN.Signature.Electronique.Enum;

public enum Department {

    IT("INFORMATION TECHNOLOGY"),
    S("SALES"),
    M("MARKETING"),
    HR("HUMAN RESOURCES"); // mnin katsali les constantes dialoulek dir ;

    // le constructeur kat5alik tinitilizi
    // les constante dialouek
    // bdabt l'instance variable dialou
    private  String desc;
    private Department(String desc){
        this.desc=desc;
    }
}
