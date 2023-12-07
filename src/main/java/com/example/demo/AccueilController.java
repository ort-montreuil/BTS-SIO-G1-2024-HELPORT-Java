package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Initializable {

    @FXML
    private MenuButton mnubtnDemande;
    @FXML
    private AnchorPane apSDemande;
    @FXML
    private AnchorPane apAccueil;
    @FXML
    private Button btnAccueil;
    @FXML
    private Button btnDeco;
    @FXML
    private MenuButton mnubtnAider;
    @FXML
    private MenuButton mnubtnCompte;
    @FXML
    private Button btnStat;
    @FXML
    private AnchorPane apVD;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // MenuButton "Demande" avec des choix
        MenuItem faireDemandeItem = new MenuItem("Faire une demande");
        faireDemandeItem.setOnAction(event -> afficherFaireDemande());
        MenuItem visualiserDemandesItem = new MenuItem("Visualiser mes demandes");
        visualiserDemandesItem.setOnAction(event -> afficherVisualiserDemandes());
        MenuItem modifierDemandeItem = new MenuItem("Modifier une demande");
        modifierDemandeItem.setOnAction(event -> afficherModifierDemande());

        // Inversion des éléments
        mnubtnDemande.getItems().addAll(faireDemandeItem, visualiserDemandesItem, modifierDemandeItem);

        // Lier la méthode afficherAccueil au bouton "Accueil"
        btnAccueil.setOnAction(event -> afficherAccueil());

        // Lier la méthode deconnexion au bouton "Déconnexion"
        btnDeco.setOnAction(event -> deconnexion());
    }

    private void afficherFaireDemande() {
        // Mettez ici le code pour afficher l'AnchorPane apSDemande
        apSDemande.setVisible(true);
        apAccueil.setVisible(false); // Vous pouvez masquer l'AnchorPane apAccueil si nécessaire
        apVD.setVisible(false);

    }

    private void afficherVisualiserDemandes() {
        // Mettez ici le code pour afficher l'AnchorPane apVD
        apVD.setVisible(true);

        // Assurez-vous de masquer les autres AnchorPane si nécessaire
        apSDemande.setVisible(false);
        apAccueil.setVisible(false);
        // Vous pouvez également masquer d'autres AnchorPane selon votre structure

        // Vous pouvez ajouter d'autres actions nécessaires ici
    }

    private void afficherModifierDemande() {
        // Mettez ici le code pour afficher l'AnchorPane approprié pour la modification des demandes
        // apModifierDemande.setVisible(true);
        // apAccueil.setVisible(false); // Vous pouvez masquer l'AnchorPane apAccueil si nécessaire
    }

    private void afficherAccueil() {
        // Mettez ici le code pour afficher l'AnchorPane apAccueil
        apAccueil.setVisible(true);
        apSDemande.setVisible(false); // Vous pouvez masquer l'AnchorPane apSDemande si nécessaire
        apVD.setVisible(false);

    }

    private void deconnexion() {
        // Mettez ici le code pour revenir à l'ancienne page FXML lors de la déconnexion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ConnexionInscription.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnDeco.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception en conséquence
        }
    }
}
