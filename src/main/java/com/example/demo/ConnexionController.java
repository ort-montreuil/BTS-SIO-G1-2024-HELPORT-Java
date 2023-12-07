package com.example.demo;

import com.example.demo.Tools.ConnexionBDD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnexionController implements Initializable

{

    @FXML
    private Button btnMotDePasseOublie;



    @FXML
    private PasswordField txtMdp;

    @FXML
    private TextField txtId;

    private  RequeteSQLController RequeteSql;

    private  ConnexionBDD maCnx;

    @FXML
    private Button btnLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            maCnx = new ConnexionBDD();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequeteSql = new RequeteSQLController();

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

    @FXML
    public void btnClickedMotDePasseOublie(ActionEvent event) {
        try {
            // Ouvrir le lien dans le navigateur par défaut
            URI uri = new URI("https://api.ecoledirecte.com/mot-de-passe-oublie.awp");
            java.awt.Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            // Gérer l'exception en conséquence
        }
    }








// Méthode pour message erreur
    private void afficherMessageErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /// Méthode connexion a partir du bouton click
    @FXML
    public void btnClicked(ActionEvent event) {
        if (RequeteSql.verifierIdentifiants(txtId.getText(), txtMdp.getText())) {
            ouvrirAccueil(event);
        }
        else {
                afficherMessageErreur("Identifiants incorrects. Veuillez réessayer.");
            }
    }


}
