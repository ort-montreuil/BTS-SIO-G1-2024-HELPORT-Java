package com.example.demo.Entity;

import java.sql.Date;

public class Demande {
    public static int id;

    public void setId(int id) {
        this.id = id;

    }

    public static int getId() {
        return id;
    }

    private Date dateFin;

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    private String sousMatiere;

    public void setSousMatiere(String sousMatiere) {
        this.sousMatiere = sousMatiere;
    }
}
