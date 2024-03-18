package com.example.demo.Entity;

import java.sql.Date;

public class Demande {
    private static int id;
    private Date dateFin;
    private String sousMatiere;
    private String matiere;

    public static int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getSousMatiere() {
        return sousMatiere;
    }

    public void setSousMatiere(String sousMatiere) {
        this.sousMatiere = sousMatiere;
    }
    // Ajoutez la méthode getter pour la matière
    public String getMatiere() {
        return matiere;
    }

    // Ajoutez la méthode setter pour la matière si nécessaire
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }
}
