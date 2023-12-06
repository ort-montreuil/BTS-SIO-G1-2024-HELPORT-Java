package com.example.demo;

import Tools.ConnexionBDD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnexionController {

    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public ConnexionController() {
        cnx = ConnexionBDD.getCnx();
    }

    @FXML
    private PasswordField txtMdp;

    @FXML
    private Button motDePasseOublie;

    @FXML
    private TextField txtId;

    @FXML
    private Button btnLogin;

    private boolean verifierIdentifiants() {
        try {
            // Préparer la requête SQL
            ps = cnx.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ?");
            ps.setString(1, txtId.getText());
            ps.setString(2, txtMdp.getText()); // Vous devriez hacher le mot de passe dans une application réelle

            // Exécuter la requête
            ResultSet resultSet = ps.executeQuery();

            // Si une ligne est renvoyée, les identifiants sont corrects
            return resultSet.next();

        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les erreurs de base de données
            return false;
        }
    }

    public void ouvrirAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Accueil.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }








    private void afficherMessageErreur(String message) {
        // Vous pouvez personnaliser la façon dont vous affichez le message d'erreur
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void btnClicked(ActionEvent event) {

        ouvrirAccueil(event);
    }
}
