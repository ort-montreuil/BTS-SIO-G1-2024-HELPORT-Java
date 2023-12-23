package com.example.demo;


import com.example.demo.Entity.Demande;
import com.example.demo.Tools.ConnexionBDD;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class RequeteSQLController {
    private  Connection cnx;
    private  PreparedStatement ps;
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

    public void creerDemandeUtilisateurConnecte(Date dateupdated, Date dateFinDemande, String sousMatiereDemandee, int idUtilisateur, int idMatiere, int status) {
        try {
            ps = cnx.prepareStatement("INSERT INTO demande (date_updated, date_fin_demande, sous_matiere, id_user,id_matiere, status) " +
                    "VALUES (NOW(), ?, ?, ?, ?,?)");

            // Paramètres de la requête

            ps.setDate(1, new java.sql.Date(dateFinDemande.getTime()));
            ps.setString(2, sousMatiereDemandee);
            ps.setInt(3, idUtilisateur);
            ps.setInt(4, idMatiere);
            ps.setInt(5, status);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public int getIdMatiere(String matiereSelectionnee) {
        int idMatiere = -1;

        try {
            ps = cnx.prepareStatement("SELECT id FROM matiere WHERE designation = ?");
            ps.setString(1, matiereSelectionnee);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idMatiere = rs.getInt("id");
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return idMatiere;
    }

    public void enregistrerDemande(Demande demande) {
        try {
            String query = "INSERT INTO demande (date_updated, date_fin_demande, sous_matiere, id_user, id_matiere, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            ps = cnx.prepareStatement(query);

            ps.setDate(1, new java.sql.Date(demande.getDateUpdated().getTime()));
            ps.setDate(2, new java.sql.Date(demande.getDateFinDemande().getTime()));
            ps.setString(3, demande.getSousMatiere());
            ps.setInt(4, demande.getIdUser());
            ps.setInt(5, demande.getIdMatiere());
            ps.setInt(6, demande.getStatus());


            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public Map<String, Integer> getNombreDemandesParMatiere() {
        Map<String, Integer> nombreDemandesParMatiere = new HashMap<>();

        try {

            // Préparer la requête SQL
            String sql = "SELECT m.designation AS matiere_designation, COUNT(*) " +
                    "AS nombreDemandes FROM demande d JOIN matiere m ON d.id_matiere = m.id GROUP BY d.id_matiere;\n";
            ps = cnx.prepareStatement(sql);

            // Exécuter la requête
            rs = ps.executeQuery();

            // Récupérer les résultats
            while (rs.next()) {
                String designation = rs.getString("matiere_designation");
                int nombreDemandes = rs.getInt("nombreDemandes");
                nombreDemandesParMatiere.put(designation, nombreDemandes);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
        }
        return nombreDemandesParMatiere;
    }

    /*
    public Map<String, Integer> getNombreSoutiensParUtilisateurConnecte(int idUtilisateurConnecte) {
        Map<String, Integer> soutiensParMatiere = new HashMap<>();

        try {

            // Préparer la requête SQL
            String sql = "SELECT m.designation AS matiere, COUNT(s.id) AS nombre_soutiens " +
                    "FROM user u " +
                    "JOIN competence c ON u.id = c.id_user " +
                    "JOIN matiere m ON c.id_matiere = m.id " +
                    "LEFT JOIN soutien s ON c.id = s.id_competence " +
                    "WHERE u.id = ? " +
                    "GROUP BY m.id, m.designation " +
                    "ORDER BY m.designation";

            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, idUtilisateurConnecte);

            // Exécuter la requête
            ResultSet rs = ps.executeQuery();

            // Récupérer le résultat
            while (rs.next()) {
                String matiere = rs.getString("matiere");
                int nombreSoutiens = rs.getInt("nombres_soutiens");
                soutiensParMatiere.put(matiere, nombreSoutiens);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
        }

        return soutiensParMatiere;
    }
     */



}






