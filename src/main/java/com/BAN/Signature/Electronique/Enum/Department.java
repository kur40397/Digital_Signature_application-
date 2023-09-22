package com.BAN.Signature.Electronique.Enum;

import java.util.ArrayList;
import java.util.List;

public enum Department {

    IT("INFORMATION TECHNOLOGY"),
    S("SALES"),
    M("MARKETING"),
    HR("HUMAN RESOURCES"); // mnin katsali les constantes dialoulek dir ;

    // le constructeur kat5alik tinitilizi
    // les constante dialouek
    // bdabt l'instance variable dialou
    private  String desc;
    Department(String desc){
        this.desc=desc;
    }

    public String getDesc() {
        return desc;
    }

}
