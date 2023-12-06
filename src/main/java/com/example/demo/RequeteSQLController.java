package com.example.demo;


import com.example.demo.Tools.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}

