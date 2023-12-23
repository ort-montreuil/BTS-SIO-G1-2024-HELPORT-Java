package com.example.demo;

import com.example.demo.Entity.Utilisateur;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private MenuButton mnubtnCompte;
    @FXML
    private Button btnStat;
    @FXML
    private AnchorPane apVD;
    @FXML
    private ListView<String> lvsSousmatiere;
    private List<String> sousMatieresSelectionnees = new ArrayList<>();

    @FXML
    private AnchorPane apPrincipal;
    @FXML
    private AnchorPane apAider;
    @FXML
    private ListView lstvAider;
    @FXML
    private Button btnAiderFinale;
    @FXML
    private Button btnAider;
    @FXML
    private Button btnModifDemande;
    @FXML
    private Label lblSelec;
    @FXML
    private ListView lstVMesdemandes;
    @FXML
    private AnchorPane apSDemande1;
    @FXML
    private ListView lvsSousmatiere1;
    @FXML
    private ComboBox cboMatiereSouhaitee1;
    @FXML
    private ListView lsvSMS1;
    @FXML
    private AnchorPane apModifDemande;
    @FXML
    private ListView lvsSousmatiere11;
    @FXML
    private ComboBox cboMatiereSouhaitee11;
    @FXML
    private ListView lsvSMS11;
    @FXML
    private BarChart graphDemande;
    @FXML
    private BarChart graphSoutiens;
    @FXML
    private AnchorPane apStats;
    @FXML
    private AnchorPane apCreerCompetence;
    @FXML
    private ListView lvsSousmatiereComp;
    @FXML
    private ComboBox cboMatiereSComp;
    @FXML
    private Button btnValidComp;
    @FXML
    private Button btnAnnuleComp;
    @FXML
    private Button btnValidCompFinale;
    @FXML
    private AnchorPane apVC;
    @FXML
    private Button btnModifCompetence;
    @FXML
    private Label lblMatiereCompModif;
    @FXML
    private ListView lstvModifComp;
    @FXML
    private Label lblSelec11;
    @FXML
    private ListView lstvRecap;

    private Set<String> matieresSelectionneesComp = new HashSet<>();
    private Set<String> sousMatieresSelectionneesComp = new HashSet<>();
    private String emailUtilisateur;


    @FXML
    private Label lblNomPrenom;
    private RequeteSQLController sqlController = new RequeteSQLController();

    @FXML
    private Button btnSoumettreDemande;
    private DatePicker datePickerFinDemande;
    @FXML
    private ListView lvSMS;
    @FXML
    private DatePicker DtpFinDemande;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MenuItem faireDemandeItem = new MenuItem("Faire une demande");
        faireDemandeItem.setOnAction(event -> afficherFaireDemande());
        MenuItem visualiserDemandesItem = new MenuItem("Visualiser mes demandes");
        visualiserDemandesItem.setOnAction(event -> afficherVisualiserDemandes());
        peuplerComboBoxMatiere();
        peuplerComboBoxMatieresC();
        updateDemandesListView();
        cboMatiereSouhaitee.setOnAction(event -> miseAJourSousMatieres());

        cboMatiereSComp.setOnAction(event -> miseAJourSousMatieresComp());
        btnSoumettreDemande.setOnAction(event -> soumettreDemande());
        btnValidCompFinale.setOnAction(event -> soumettreCompetence());

        lvsSousmatiere.setOnMouseClicked(event -> sousMatiereSelectionnee());

        mnubtnDemande.getItems().addAll(faireDemandeItem, visualiserDemandesItem);

        btnAccueil.setOnAction(event -> afficherAccueil());
        btnDeco.setOnAction(event -> deconnexion());
        btnAider.setOnAction(event -> afficherAider());
        btnStat.setOnAction(event -> afficherStats());



// pour lorsqu'on a selec une demande

        btnAiderFinale.setOnAction(event -> {
            // Récupérer la demande sélectionnée dans la ListView
            String demandeSelectionnee = (String) lstvAider.getSelectionModel().getSelectedItem();

            if (demandeSelectionnee != null) {
                // Afficher la pop-up avec les détails de la demande sélectionnée
                afficherPopUpAider(demandeSelectionnee);
            } else {
                // Afficher un message d'erreur si aucune demande n'est sélectionnée
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de sélection");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une demande.");
                alert.showAndWait();
            }
        });


        // Ajouter un gestionnaire d'événements à la lvsousmatiere
        lvsSousmatiere.setOnMouseClicked(event -> ajouterSousMatiereSelectionnee());
        lvsSousmatiereComp.setOnMouseClicked(event -> ajouterSousMatiereSelecC());


        MenuItem creerCompetencesItem = new MenuItem("Créer ses compétences");
        creerCompetencesItem.setOnAction(event -> afficherApCreerCompetence());

        MenuItem visualiserCompetencesItem = new MenuItem("Visualiser ses compétences");
        visualiserCompetencesItem.setOnAction(event -> afficherApVC());

        mnubtnCompte.getItems().addAll(creerCompetencesItem, visualiserCompetencesItem);

    }



    private void updateDemandesListView() {
        // Appeler la méthode pour récupérer toutes les demandes de la base de données
        List<String> toutesDemandes = sqlController.getToutesDemandes();

        // Effacer le contenu actuel de la ListView
        lstvAider.getItems().clear();

        // Ajouter toutes les demandes à la ListView
        lstvAider.getItems().addAll(toutesDemandes);
    }

    public int initialiserUtilisateur(String emailUtilisateur) {
        // Stocker l'e-mail de l'utilisateur connecté
        this.emailUtilisateur = emailUtilisateur;

        // Utiliser la classe RequeteSQLController pour obtenir le nom et le prénom
        RequeteSQLController requeteSQLController = new RequeteSQLController();
        Map<String, String> utilisateurInfo = requeteSQLController.getNomPrenomUtilisateur(emailUtilisateur);

        // Initialiser les attributs nomUtilisateur et prenomUtilisateur
        String nomUtilisateur = utilisateurInfo.get("nom");
        String prenomUtilisateur = utilisateurInfo.get("prenom");

        // Mettez ici le code pour afficher le nom et le prénom dans votre interface graphique
        lblNomPrenom.setText("Bienvenue, " + prenomUtilisateur + " " + nomUtilisateur);
        return 0;
    }

    private void ajouterSousMatiereSelecC() {
        String matiereSelectionnee = (String) cboMatiereSComp.getSelectionModel().getSelectedItem();
        String sousMatiereSelectionnee = (String) lvsSousmatiereComp.getSelectionModel().getSelectedItem();

        if (matiereSelectionnee != null && sousMatiereSelectionnee != null) {
            String matiereAvecSousMatiere = matiereSelectionnee + " : " + sousMatiereSelectionnee;
            ajouterSousMatiereComp(matiereAvecSousMatiere);
        }
    }


    private void miseAJourSousMatieresComp() {
        String matiereSelectionnee = (String) cboMatiereSComp.getSelectionModel().getSelectedItem();

        if (matiereSelectionnee != null && !matieresSelectionneesComp.contains(matiereSelectionnee)) {
            // Efface la liste des sous-matières sélectionnées précédemment
            lvsSousmatiereComp.getItems().clear();
            sousMatieresSelectionneesComp.clear();

            RequeteSQLController requeteSQLController = new RequeteSQLController();
            List<String> sousMatieres = requeteSQLController.getSousMatieresPourMatiere(matiereSelectionnee);
            Set<String> sousMatieresUniques = new HashSet<>();

            for (String sousMatiere : sousMatieres) {
                String[] sousMatiereSplit = sousMatiere.split("#");
                sousMatieresUniques.addAll(Arrays.stream(sousMatiereSplit)
                        .filter(part -> !part.isEmpty())
                        .collect(Collectors.toList()));
            }

            lvsSousmatiereComp.getItems().addAll(sousMatieresUniques);
            matieresSelectionneesComp.add(matiereSelectionnee);
        }
    }

    private void ajouterSousMatiereSelectionnee() {
        String matiereSelectionnee = (String) cboMatiereSouhaitee.getSelectionModel().getSelectedItem();
        String sousMatiereSelectionnee = (String) lvsSousmatiere.getSelectionModel().getSelectedItem();

        if (matiereSelectionnee != null && sousMatiereSelectionnee != null) {
            String matiereAvecSousMatiere = matiereSelectionnee + " : " + sousMatiereSelectionnee;
            if (!lvSMS.getItems().contains(matiereAvecSousMatiere)) {
                lvSMS.getItems().add(matiereAvecSousMatiere);
            }
        }
    }
    private void ajouterSousMatiereComp(String matiereAvecSousMatiere) {
        if (!sousMatieresSelectionneesComp.contains(matiereAvecSousMatiere)) {
            // Ajoute à la liste de récapitulatif
            lstvRecap.getItems().add(matiereAvecSousMatiere);
            sousMatieresSelectionneesComp.add(matiereAvecSousMatiere);
        } else {
            // Affiche un message d'erreur si la sous-matière est déjà sélectionnée dans le récapitulatif
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez déjà sélectionné cette sous-matière dans le récapitulatif.");
            alert.showAndWait();
        }
    }
    @FXML
    private void annulerDemande() {
        // Créer une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment annuler la demande?");

        // Ajouter les boutons "Oui" et "Non"
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(btnOui, btnNon);

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Optional<ButtonType> resultat = confirmationAlert.showAndWait();

        // Vérifier la réponse de l'utilisateur
        if (resultat.isPresent() && resultat.get() == btnOui) {
            // L'utilisateur a choisi "Oui"
            // Mettre à jour la visibilité des AnchorPane
            apAccueil.setVisible(true);
            apSDemande.setVisible(false);
            apVD.setVisible(false);
            apAider.setVisible(false);
            apModifDemande.setVisible(false);
            apStats.setVisible(false);
            apVC.setVisible(false);
            apCreerCompetence.setVisible(false);
        } else {
            // L'utilisateur a choisi "Non" ou a fermé la boîte de dialogue
            // Ne rien faire, la boîte de dialogue disparaîtra simplement
        }
    }


    private void afficherFaireDemande() {
        apSDemande.setVisible(true);
        apAccueil.setVisible(false);
        apVD.setVisible(false);
        apAider.setVisible(false);
        apModifDemande.setVisible(false);
        apStats.setVisible(false);
        apVC.setVisible(false);
        apCreerCompetence.setVisible(false);


    }

    private void afficherApCreerCompetence() {
        apCreerCompetence.setVisible(true);
        apSDemande.setVisible(false);
        apAccueil.setVisible(false);
        apVD.setVisible(false);
        apAider.setVisible(false);
        apModifDemande.setVisible(false);
        apStats.setVisible(false);
        apVC.setVisible(false);
    }

    private void afficherApVC() {
        apVC.setVisible(true);
        apSDemande.setVisible(false);
        apAccueil.setVisible(false);
        apVD.setVisible(false);
        apAider.setVisible(false);
        apModifDemande.setVisible(false);
        apStats.setVisible(false);
        apCreerCompetence.setVisible(false);
    }


    private void afficherVisualiserDemandes() {
        apVD.setVisible(true);
        apAccueil.setVisible(false);
        apSDemande.setVisible(false);
        apAider.setVisible(false);
        apModifDemande.setVisible(false);
        apStats.setVisible(false);
        apVC.setVisible(false);
        apCreerCompetence.setVisible(false);


    }

    private void afficherStats() {
        apStats.setVisible(true);
        apVD.setVisible(false);
        apAccueil.setVisible(false);
        apSDemande.setVisible(false);
        apAider.setVisible(false);
        apModifDemande.setVisible(false);
        apAccueil.setVisible(false);
        apVC.setVisible(false);
        apCreerCompetence.setVisible(false);


    }

    private void afficherAccueil() {
        apAccueil.setVisible(true);
        apSDemande.setVisible(false);
        apVD.setVisible(false);
        apAider.setVisible(false);
        apModifDemande.setVisible(false);
        apStats.setVisible(false);
        apVC.setVisible(false);
        apCreerCompetence.setVisible(false);


    }

    private void afficherAider() {
        apAider.setVisible(true);
        apAccueil.setVisible(false);
        apSDemande.setVisible(false);
        apVD.setVisible(false);
        apModifDemande.setVisible(false);
        apStats.setVisible(false);
        apVC.setVisible(false);
        apCreerCompetence.setVisible(false);


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

    private void peuplerComboBoxMatieresC() {
        RequeteSQLController requeteSQLController = new RequeteSQLController();
        List<String> designationsMatiere = requeteSQLController.getDesignationsMatiere();
        cboMatiereSComp.getItems().addAll(designationsMatiere);
        cboMatiereSComp.getSelectionModel().selectFirst();
    }

    private String matiereSelectionneeActuelle = null; // Variable pour stocker la matière sélectionnée

    private void miseAJourSousMatieres() {
        String matiereSelectionnee = (String) cboMatiereSouhaitee.getSelectionModel().getSelectedItem();

        if (matiereSelectionnee != null && (matiereSelectionneeActuelle == null || matiereSelectionnee.equals(matiereSelectionneeActuelle))) {
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

            // Met à jour la matière sélectionnée actuelle
            matiereSelectionneeActuelle = matiereSelectionnee;
        } else {
            // Affiche un message d'erreur si l'utilisateur sélectionne des sous-matières d'une autre matière
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText(null);
            alert.setContentText("Vous ne pouvez pas choisir une autre matière.");
            alert.showAndWait();

            // Désélectionne la matière incorrecte
            cboMatiereSouhaitee.getSelectionModel().select(matiereSelectionneeActuelle);
        }
    }


    private void afficherPopUpAider(String demandeSelectionnee) {
        try {
            // Charger le contenu de apPopUpAider depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/PopUpAider.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la pop-up
            PopUpAiderController popUpController = loader.getController();

            // Initialiser le contrôleur de la pop-up avec les détails de la demande sélectionnée
            popUpController.initialiserDemande(demandeSelectionnee);

            // Créer une nouvelle fenêtre (Stage) pour la pop-up
            Stage popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.setTitle("Pop-up Aider");

            // Définir la scène avec le contenu de apPopUpAider
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);

            // Afficher la pop-up
            popUpStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sousMatiereSelectionnee() {
        String nouvelleSousMatiere = lvsSousmatiere.getSelectionModel().getSelectedItem();

        if (nouvelleSousMatiere != null) {
            verifierReselectionSousMatiere(nouvelleSousMatiere);
        }
    }

    private void verifierReselectionSousMatiere(String nouvelleSousMatiere) {
        if (sousMatieresSelectionnees.contains(nouvelleSousMatiere)) {
            // Affiche un message d'erreur si la sous-matière est déjà sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez déjà sélectionné cette sous-matière.");
            alert.showAndWait();

            // Désélectionne la sous-matière incorrecte
            Platform.runLater(() -> lvsSousmatiere.getSelectionModel().clearSelection());
        } else {
            // Ajoute la sous-matière à la liste des sous-matières sélectionnées
            sousMatieresSelectionnees.add(nouvelleSousMatiere);
        }

    }
    private void soumettreDemande() {
        try {
            // Récupérer les informations nécessaires de l'interface utilisateur


            // Ajoutez une vérification pour vous assurer que la date de fin de demande est sélectionnée
            if (DtpFinDemande.getValue() == null) {
                // Afficher un message d'erreur si la date n'est pas sélectionnée
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une date de fin de demande.");
                alert.showAndWait();
                return; // Sortir de la méthode si la date n'est pas sélectionnée
            }

            Date dateFinDemande = getDateFinDemande(); // Méthode à implémenter pour récupérer la date de fin de demande
            String sousMatiereDemandee = getSousMatiereDemandee(); // Méthode à implémenter pour récupérer la sous-matière demandée
            String matiereSelectionnee = (String) cboMatiereSouhaitee.getSelectionModel().getSelectedItem(); // Supposons que vous avez une ComboBox pour la matière
            int idUtilisateur = Utilisateur.getId();
            System.out.println(idUtilisateur);// Utilisez la méthode existante pour récupérer l'ID de l'utilisateur

            // Appeler la méthode de votre RequeteSQLController pour créer la demande
            sqlController.creerDemandeUtilisateurConnecte(new Date(), dateFinDemande, sousMatiereDemandee, idUtilisateur, getIdMatiere(matiereSelectionnee), 1);

            // Afficher un message de réussite
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Demande soumise");
            alert.setHeaderText(null);
            alert.setContentText("Votre demande a été soumise avec succès.");
            alert.showAndWait();

            // Mettre à jour la ListView des demandes après l'ajout
            updateDemandesListView();

        } catch (Exception e) {
            e.printStackTrace();
            // Afficher un message d'erreur en cas d'échec
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la soumission de votre demande.");
            alert.showAndWait();
        }
    }


    private int getIdMatiere(String matiereSelectionnee) {

        RequeteSQLController requeteSQLController = new RequeteSQLController();
        int idMatiere = requeteSQLController.getIdMatiere(matiereSelectionnee);

        return idMatiere;
    }

    private Date getDateFinDemande()
    {


        LocalDate localDate = DtpFinDemande.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }
    private String getSousMatiereDemandee()
    {
        return String.valueOf(lvSMS.getSelectionModel().getSelectedItem());
    }
    private void soumettreCompetence() {
        try {
            // Obtenez la sous-matière demandée
            String sousMatiereDemandee = getSousMatiereDemandee2(); // Méthode à implémenter pour récupérer la sous-matière demandée

            // Obtenez la matière sélectionnée
            String matiereSelectionnee = (String) cboMatiereSComp.getSelectionModel().getSelectedItem(); // Supposons que vous avez une ComboBox pour la matière

            // Obtenez l'ID de l'utilisateur connecté
            int idUtilisateur = Utilisateur.getId(); // Utilisez la méthode existante pour récupérer l'ID de l'utilisateur

            // Appeler la méthode de votre RequeteSQLController pour créer la compétence
            sqlController.creerCompetenceConnecte(getIdMatiere(matiereSelectionnee), idUtilisateur, sousMatiereDemandee, 1);

            // Afficher un message de réussite
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Compétence ajoutée");
            alert.setHeaderText(null);
            alert.setContentText("Votre compétence a été ajoutée avec succès.");
            alert.showAndWait();

            // Mettre à jour la ListView des compétences après l'ajout (si nécessaire)
            // Vous pouvez implémenter cette méthode si vous avez besoin de mettre à jour la liste des compétences dans l'interface graphique.

        } catch (Exception e) {
            e.printStackTrace();
            // Afficher un message d'erreur en cas d'échec
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'ajout de votre compétence.");
            alert.showAndWait();
        }
    }
    private String getSousMatiereDemandee2()
    {
        return String.valueOf(lstvRecap.getSelectionModel().getSelectedItem());
    }
}
