package com.example.demo;


import com.example.demo.Tools.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RequeteSQLController
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public RequeteSQLController()
    {
        cnx = ConnexionBDD.getCnx();

    }
// Partie connexion et check dans la base de données
    public boolean verifierIdentifiants(String emailUtilisateur, String mdpUtilisateur)
    {
        boolean verification = false;
        try
        {
            ps = cnx.prepareStatement("SELECT user.email, user.password FROM user WHERE user.email = ? AND user.password = ?;");
            ps.setString(1, emailUtilisateur);
            ps.setString(2, mdpUtilisateur);
            rs = ps.executeQuery();
            while (rs.next())
            {
                verification = true;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return verification;
    }
    public List<String> getDesignationsMatiere() {
        List<String> designations = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("SELECT matiere.designation FROM matiere");
            rs = ps.executeQuery();
            while (rs.next()) {
                String designation = rs.getString("designation");
                designations.add(designation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return designations;
    }
    public List<String> getSousMatieresPourMatiere(String matiereSelectionnee) {
        List<String> sousMatieres = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT sous_matiere FROM competence WHERE id_matiere = (SELECT id FROM matiere WHERE designation = ?)";
            ps = cnx.prepareStatement(query);
            ps.setString(1, matiereSelectionnee);

            rs = ps.executeQuery();

            while (rs.next()) {
                String sousMatiere = rs.getString("sous_matiere");
                sousMatieres.add(sousMatiere);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sousMatieres;
    }


    public List<DemandeAide> getDemandesUtilisateur(int idUtilisateur) {
        List<DemandeAide> demandes = new ArrayList<>();

        try {
            String query = "SELECT id, date_updated, date_fin_demande, sous_matiere, id_user, id_matiere, status " +
                    "FROM demande " +
                    "WHERE id_user = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, idUtilisateur);
            rs = ps.executeQuery();

            while (rs.next()) {
                DemandeAide demande = new DemandeAide();
                demande.setId(rs.getInt("id"));
                demande.setDateUpdated(rs.getDate("date_updated"));
                demande.setDateFinDemande(rs.getDate("date_fin_demande"));
                demande.setSousMatiere(rs.getString("sous_matiere"));
                demande.setIdUser(rs.getInt("id_user"));
                demande.setIdMatiere(rs.getInt("id_matiere"));
                demande.setStatus(rs.getString("status"));
                demandes.add(demande);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return demandes;
    }



    public int getIdUtilisateur(String emailUtilisateur, String mdpUtilisateur) {
        int idUtilisateur = -1; // Valeur par défaut si l'utilisateur n'est pas trouvé

        try {
            ps = cnx.prepareStatement("SELECT id FROM user WHERE email = ? AND password = ?");
            ps.setString(1, emailUtilisateur);
            ps.setString(2, mdpUtilisateur);
            rs = ps.executeQuery();

            if (rs.next()) {
                idUtilisateur = rs.getInt("id");
                System.out.println("ID de l'utilisateur récupéré : " + idUtilisateur);
            } else {
                System.out.println("Aucun utilisateur trouvé avec ces identifiants.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return idUtilisateur;
    }



    public class DemandeAide {
        private int id;
        private Date dateUpdated;
        private Date dateFinDemande;
        private String sousMatiere;
        private int idUser;
        private int idMatiere;
        private String status;

        // Ajoutez d'autres attributs nécessaires

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        // Ajoutez des getters et setters pour les autres attributs si nécessaire
    }





}

