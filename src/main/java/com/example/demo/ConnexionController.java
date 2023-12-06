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

import java.net.URL;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnexionController implements Initializable

{

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

    @FXML
    private PasswordField txtMdp;

    @FXML
    private Button motDePasseOublie;

    @FXML
    private TextField txtId;

    private  RequeteSQLController RequeteSql;

    private  ConnexionBDD maCnx;

    @FXML
    private Button btnLogin;



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
        if (RequeteSql.verifierIdentifiants(txtId.getText(), txtMdp.getText())) {
            ouvrirAccueil(event);
        }
        else {
                afficherMessageErreur("Identifiants incorrects. Veuillez réessayer.");
            }
    }


}
