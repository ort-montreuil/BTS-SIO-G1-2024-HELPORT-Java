package com.example.demo.Entity;

import java.sql.Date;

public class Demande {
    private int id;

    public void setId(int id) {
        this.id = id;

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
