package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConnexionController {
    @FXML
    private TextField identifiant;

    @FXML
    private TextField motDePasse;

    @FXML
    private Button seConnecter;

    @FXML
    private Button motDePasseOublie;

    @FXML
    public void connecter() {
        // Code pour vérifier les identifiants
    }

    @FXML
    public void motDePasseOublie() {
        // Code pour réinitialiser le mot de passe
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Mot de passe oublié");
        alert.setContentText("Veuillez contacter l'administrateur pour réinitialiser votre mot de passe.");
        alert.showAndWait();
    }
}