package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
    private ComboBox cboMatiereSouhaitee;
    @FXML
    private MenuButton mnubtnAider;
    @FXML
    private MenuButton mnubtnCompte;
    @FXML
    private Button btnStat;
    @FXML
    private AnchorPane apVD;
    @FXML
    private ListView lsvSMS;
    @FXML
    private ListView lvsSousmatiere;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MenuItem faireDemandeItem = new MenuItem("Faire une demande");
        faireDemandeItem.setOnAction(event -> afficherFaireDemande());
        MenuItem visualiserDemandesItem = new MenuItem("Visualiser mes demandes");
        visualiserDemandesItem.setOnAction(event -> afficherVisualiserDemandes());
        MenuItem modifierDemandeItem = new MenuItem("Modifier une demande");
        modifierDemandeItem.setOnAction(event -> afficherModifierDemande());
        peuplerComboBoxMatiere();
        cboMatiereSouhaitee.setOnAction(event -> miseAJourSousMatieres());

        mnubtnDemande.getItems().addAll(faireDemandeItem, visualiserDemandesItem, modifierDemandeItem);

        btnAccueil.setOnAction(event -> afficherAccueil());
        btnDeco.setOnAction(event -> deconnexion());

        // Ajouter un gestionnaire d'événements à la lvsousmatiere
        lvsSousmatiere.setOnMouseClicked(event -> ajouterSousMatiereSelectionnee());
    }

    private void ajouterSousMatiereSelectionnee() {
        String sousMatiereSelectionnee = (String) lvsSousmatiere.getSelectionModel().getSelectedItem();
        if (sousMatiereSelectionnee != null) {
            lsvSMS.getItems().add(sousMatiereSelectionnee);
        }
    }

    private void afficherFaireDemande() {
        apSDemande.setVisible(true);
        apAccueil.setVisible(false);
    }

    private void afficherVisualiserDemandes() {
        // Mettez ici le code pour afficher l'AnchorPane approprié
    }

    private void afficherModifierDemande() {
        // Mettez ici le code pour afficher l'AnchorPane approprié pour la modification des demandes
    }

    private void afficherAccueil() {
        apAccueil.setVisible(true);
        apSDemande.setVisible(false);
    }

    private void deconnexion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ConnexionInscription.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnDeco.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void peuplerComboBoxMatiere() {
        RequeteSQLController requeteSQLController = new RequeteSQLController();
        List<String> designationsMatiere = requeteSQLController.getDesignationsMatiere();
        cboMatiereSouhaitee.getItems().addAll(designationsMatiere);
        cboMatiereSouhaitee.getSelectionModel().selectFirst();
    }

    private void miseAJourSousMatieres() {
        String matiereSelectionnee = (String) cboMatiereSouhaitee.getSelectionModel().getSelectedItem();

        if (matiereSelectionnee != null) {
            RequeteSQLController requeteSQLController = new RequeteSQLController();
            List<String> sousMatieres = requeteSQLController.getSousMatieresPourMatiere(matiereSelectionnee);
            Set<String> sousMatieresUniques = new HashSet<>();

            for (String sousMatiere : sousMatieres) {
                String[] sousMatiereSplit = sousMatiere.split("#");
                sousMatieresUniques.addAll(Arrays.stream(sousMatiereSplit)
                        .filter(part -> !part.isEmpty())
                        .collect(Collectors.toList()));
            }

            lvsSousmatiere.getItems().clear();
            lvsSousmatiere.getItems().addAll(sousMatieresUniques);
        }
    }
}
