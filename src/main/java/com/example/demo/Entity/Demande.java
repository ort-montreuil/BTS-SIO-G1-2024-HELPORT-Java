package com.example.demo.Entity;

import java.util.Date;

public class Demande {
    private int id;
    private Date dateUpdated;
    private Date dateFinDemande;
    private String sousMatiere;
    private int idUser;
    private int idMatiere;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Date getDateFinDemande() {
        return dateFinDemande;
    }

    public void setDateFinDemande(Date dateFinDemande) {
        this.dateFinDemande = dateFinDemande;
    }

    public String getSousMatiere() {
        return sousMatiere;
    }

    public void setSousMatiere(String sousMatiere) {
        this.sousMatiere = sousMatiere;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
