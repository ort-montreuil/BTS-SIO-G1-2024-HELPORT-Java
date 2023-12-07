package com.example.demo;


import com.example.demo.Tools.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequeteSQLController
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public RequeteSQLController()
    {
        cnx = ConnexionBDD.getCnx();
        if (cnx != null) {
            System.out.println("Connexion à la base de données réussie.");
        } else {
            System.err.println("Échec de la connexion à la base de données.");
        }
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
            // Utiliser une requête préparée pour éviter les injections SQL
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


}

