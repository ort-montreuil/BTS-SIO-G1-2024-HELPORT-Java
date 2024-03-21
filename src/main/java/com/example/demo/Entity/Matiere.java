package com.example.demo.Entity;

public class Matiere {
    private int id;
    private String designation;
    private int code;
    private String sousMatiere;
    boolean isModified;


    public Matiere(int id, String designation, int code, String sousMatiere) {
        this.id = id;
        this.designation = designation;
        this.code = code;
        this.sousMatiere = sousMatiere;
    }

    public boolean isModified() {
        return isModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSousMatiere() {
        return sousMatiere;
    }

    public void setSousMatiere(String sousMatiere) {
        this.sousMatiere = sousMatiere;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }
}

