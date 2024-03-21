package com.example.demo.Entity;

import javafx.fxml.FXML;

public class Salle {

    private int id;
    private String codeSalle;
    private int etage;
    private boolean isModified;

    public Salle(boolean isModified) {
        this.isModified = isModified;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeSalle() {
        return codeSalle;
    }

    public void setCodeSalle(String codeSalle) {
        this.codeSalle = codeSalle;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public Salle(int id, String codeSalle, int etage) {
        this.id = id;
        this.codeSalle = codeSalle;
        this.etage = etage;
    }
}
