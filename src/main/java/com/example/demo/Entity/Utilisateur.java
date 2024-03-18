package com.example.demo.Entity;

public class Utilisateur
{
    private static int id;
    private String nom;
    private String prenom;
    private String identifiant;
    private static String email;
    private static String motDePasse;
    private String role;
    private static String niveau;
    private int sexe;
    private String telephone;

    public Utilisateur(int id, String nom, String prenom, String identifiant, String email, String motDePasse, String role, String niveau, int sexe, String telephone)
    {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.identifiant = identifiant;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.niveau = niveau;
        this.sexe = sexe;
        this.telephone = telephone;
    }



    public static int getId() {
        return id;
    }

    public static void setId(int unId)  {
        id = unId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String unEmail) {
        email = unEmail;
    }

    public static String getMotDePasse() {
        return motDePasse;
    }

    public static void setMotDePasse(String unMotDePasse) {
        motDePasse = unMotDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static String getNiveau() {
        return niveau;
    }

    public static void setNiveau(String niveau) {
        Utilisateur.niveau = niveau;
    }

    public int getSexe() {
        return sexe;
    }

    public void setSexe(int sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
