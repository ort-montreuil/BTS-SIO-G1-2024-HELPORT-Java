package com.example.demo;


import com.example.demo.Tools.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RequeteSQLController {
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public RequeteSQLController() {
        cnx = ConnexionBDD.getCnx();

    }

    // Partie connexion et check dans la base de données
    public boolean verifierIdentifiants(String emailUtilisateur, String mdpUtilisateur) {
        boolean verification = false;
        try {
            ps = cnx.prepareStatement("SELECT user.email, user.password FROM user WHERE user.email = ? AND user.password = ?;");
            ps.setString(1, emailUtilisateur);
            ps.setString(2, mdpUtilisateur);
            rs = ps.executeQuery();
            while (rs.next()) {
                verification = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return verification;
    }

    public Map<String, String> getNomPrenomUtilisateur(String emailUtilisateur) {
        Map<String, String> utilisateurInfo = new HashMap<>();
        try {
            ps = cnx.prepareStatement("SELECT user.nom, user.prenom FROM user WHERE user.email = ?");
            ps.setString(1, emailUtilisateur);
            rs = ps.executeQuery();
            if (rs.next()) {
                utilisateurInfo.put("nom", rs.getString("nom"));
                utilisateurInfo.put("prenom", rs.getString("prenom"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utilisateurInfo;
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
    public List<String> getToutesDemandes() {
        List<String> demandesList = new ArrayList<>();

        try {

            // Créer une instruction SQL pour sélectionner toutes les demandes
            String sqlQuery = "SELECT * FROM demande";

            try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlQuery)) {
                // Exécuter la requête SQL
                ResultSet resultSet = preparedStatement.executeQuery();

                // Parcourir les résultats et ajouter chaque demande à la liste
                while (resultSet.next()) {
                    String sousMatiere = resultSet.getString("sous_matiere");
                    Date date = resultSet.getDate("date_updated");
                    Date date2 = resultSet.getDate("date_fin_demande");
                    int status = resultSet.getInt("status");

                    String informationDemande = String.format(" Date Examen: %s, Sous-matière: %s",
                         date2.toString(), sousMatiere);



                    demandesList.add(informationDemande);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les exceptions SQL correctement dans votre application
        }

        // Retourner la liste de demandes
        return demandesList;
    }




}