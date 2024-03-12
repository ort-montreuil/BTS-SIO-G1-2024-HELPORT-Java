package com.example.demo;

import com.example.demo.Entity.Utilisateur;
import com.example.demo.PopUpAiderController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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

    List<String> designationsMatiere = sqlController.getDesignationsMatiere();


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
        btnStat.setOnAction(event -> {
            afficherStats();
            afficherStatistiques1();
            afficherStatistiques2();
        });

        btnModifDemande.setOnAction(actionEvent -> {

            String demandeSelectionnee = (String) lstVMesdemandes.getSelectionModel().getSelectedItem();

            if (demandeSelectionnee != null) {
                // Affiche la pop-up avec la demande sélectionnée
                afficherPopUpModifDemande(demandeSelectionnee);
            } else {
                // Affiche un message d'erreur si aucune demande n'est sélectionnée
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de sélection");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une demande.");
                alert.showAndWait();

            }
        });
        btnModifCompetence.setOnAction(actionEvent -> {

            String competenceSelectionnee = (String) lstvModifComp.getSelectionModel().getSelectedItem();

            if (competenceSelectionnee != null) {
                // Affiche la pop-up avec la compétence sélectionnée
                AfficherModifCompetence(competenceSelectionnee);
            } else {
                // Afficher un message d'erreur si aucune compétences n'est sélectionnée
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de sélection");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une compétence.");
                alert.showAndWait();

            }
        });
        // pour lorsqu'on a selec une demande

        btnAiderFinale.setOnAction(event -> {
            // Récupére la demande sélectionnée dans la ListView
            String demandeSelectionnee = (String) lstvAider.getSelectionModel().getSelectedItem();

            if (demandeSelectionnee != null) {
                // Affiche la pop-up de la demande sélectionnée
                afficherPopUpAider(demandeSelectionnee);
            } else {
                // Affiche un message d'erreur si aucune demande n'est sélectionnée
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de sélection");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une demande.");
                alert.showAndWait();
            }
        });

        lvsSousmatiere.setOnMouseClicked(event -> ajouterSousMatiereSelectionnee());
        lvsSousmatiereComp.setOnMouseClicked(event -> ajouterSousMatiereSelecC());


        MenuItem creerCompetencesItem = new MenuItem("Créer ses compétences");
        creerCompetencesItem.setOnAction(event -> afficherApCreerCompetence());

        MenuItem visualiserCompetencesItem = new MenuItem("Visualiser ses compétences");
        visualiserCompetencesItem.setOnAction(event -> afficherApVC());

        mnubtnCompte.getItems().addAll(creerCompetencesItem, visualiserCompetencesItem);

    }

    public void recupererInfosDemande(int numeroDemande, DatePicker dateModifDemande, ComboBox<String> cboModifDemande, ListView<String> lstvModifDemande, TextField txtDescModifDemande) {
        // Ici, vous devrez implémenter le code pour récupérer les informations de la demande
        // en fonction du numéro de demande passé en paramètre

        // Par exemple, vous pouvez accéder à votre liste de demandes
        // et rechercher la demande correspondante en fonction de son numéro
        // Ensuite, vous pouvez extraire les informations pertinentes et les mettre dans les composants de la pop-up

        // Exemple fictif pour illustrer le remplissage des composants de la pop-up avec les informations de la demande
        // Remplacez ces exemples par votre propre logique de récupération des données

        // Date de la demande (exemple fictif)
        dateModifDemande.setValue(LocalDate.now());

        // Matière de la demande (exemple fictif)
        cboModifDemande.setValue("Matière");

        // Sous-matière de la demande (exemple fictif)
        ObservableList<String> sousMatieres = FXCollections.observableArrayList("Sous-Matière 1", "Sous-Matière 2", "Sous-Matière 3");
        lstvModifDemande.setItems(sousMatieres);
        lstvModifDemande.getSelectionModel().select(0); // Sélectionnez une sous-matière par défaut

        // Description de la demande (exemple fictif)
        txtDescModifDemande.setText("Description de la demande");
    }



    private void updateDemandesListView() {
        List<String> toutesDemandes = sqlController.getToutesDemandes();
        lstvAider.getItems().clear();
        lstvAider.getItems().addAll(toutesDemandes);
    }

    public int initialiserUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;

        Map<String, String> utilisateurInfo = sqlController.getNomPrenomUtilisateur(emailUtilisateur);

        String nomUtilisateur = utilisateurInfo.get("nom");
        String prenomUtilisateur = utilisateurInfo.get("prenom");

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
            lvsSousmatiereComp.getItems().clear();
            sousMatieresSelectionneesComp.clear();


            List<String> sousMatieres = sqlController.getSousMatieresPourMatiere(matiereSelectionnee);
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
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment annuler la demande?");

        // Ajoute les boutons "Oui" et "Non"
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(btnOui, btnNon);

        Optional<ButtonType> resultat = confirmationAlert.showAndWait();

        if (resultat.isPresent() && resultat.get() == btnOui) {

            apAccueil.setVisible(true);
            apSDemande.setVisible(false);
            apVD.setVisible(false);
            apAider.setVisible(false);
            apModifDemande.setVisible(false);
            apStats.setVisible(false);
            apVC.setVisible(false);
            apCreerCompetence.setVisible(false);
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
        afficherVisualiserCompetences();
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
        afficherDemandesUtilisateurConnecte();

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

        designationsMatiere = sqlController.getDesignationsMatiere();
        cboMatiereSouhaitee.getItems().addAll(designationsMatiere);
        cboMatiereSouhaitee.getSelectionModel().selectFirst();
    }

    private void peuplerComboBoxMatieresC() {
        designationsMatiere = sqlController.getDesignationsMatiere();
        cboMatiereSComp.getItems().addAll(designationsMatiere);
        cboMatiereSComp.getSelectionModel().selectFirst();
    }

    private String matiereSelectionneeActuelle = null; // c'est la variable pour stocker la matière sélectionnée

    private void miseAJourSousMatieres() {
        String matiereSelectionnee = (String) cboMatiereSouhaitee.getSelectionModel().getSelectedItem();

        if (matiereSelectionnee != null && (matiereSelectionneeActuelle == null || matiereSelectionnee.equals(matiereSelectionneeActuelle))) {

            List<String> sousMatieres = sqlController.getSousMatieresPourMatiere(matiereSelectionnee);
            Set<String> sousMatieresUniques = new HashSet<>();

            for (String sousMatiere : sousMatieres) {
                String[] sousMatiereSplit = sousMatiere.split("#");
                sousMatieresUniques.addAll(Arrays.stream(sousMatiereSplit)
                        .filter(part -> !part.isEmpty())
                        .collect(Collectors.toList()));
            }

            lvsSousmatiere.getItems().clear();
            lvsSousmatiere.getItems().addAll(sousMatieresUniques);

            matiereSelectionneeActuelle = matiereSelectionnee;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Vous ne pouvez pas choisir plusieurs matieres lors d'une demande.");
            alert.showAndWait();

            cboMatiereSouhaitee.getSelectionModel().select(matiereSelectionneeActuelle);
        }
    }


    private void afficherPopUpAider(String demandeSelectionnee) {
        try {
            // Charge le contenu de apPopUpAider depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/PopUpAider.fxml"));
            Parent root = loader.load();

            // Récupére le contrôleur de la pop-up
            PopUpAiderController popUpController = loader.getController();

            // Initialise le contrôleur de la pop-up avec les détails de la demande sélectionnée
            popUpController.initialiserDemande(demandeSelectionnee);

            // Crée une nouvelle fenêtre pour la pop-up
            Stage popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.setTitle("Pop-up Aider");

            popUpController.setStage(popUpStage);

            Scene scene = new Scene(root);
            popUpStage.setScene(scene);

            // Affiche la pop-up
            popUpStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void afficherPopUpModifDemande(String demandeSelectionnee) {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ModifierDemande.fxml"));
            Parent root = loader.load();

            ModifierDemandeController popUpController = loader.getController();

            popUpController.initialiserDemande(demandeSelectionnee);

            Stage popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.setTitle("Pop-up Modifier");

            popUpController.setStage(popUpStage);

            Scene scene = new Scene(root);
            popUpStage.setScene(scene);

            popUpStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AfficherModifCompetence(String demandeSelectionnee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ModifierCompetence.fxml"));
            Parent root = loader.load();

            ModifierCompetenceController popUpController = loader.getController();

            popUpController.initialiserCompetence(demandeSelectionnee);

            Stage popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.setTitle("Pop-up Modifier");

            popUpController.setStage(popUpStage);

            Scene scene = new Scene(root);
            popUpStage.setScene(scene);

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez déjà sélectionné cette sous-matière.");
            alert.showAndWait();

            Platform.runLater(() -> lvsSousmatiere.getSelectionModel().clearSelection());
        } else {
            sousMatieresSelectionnees.add(nouvelleSousMatiere);
        }

    }

    private void soumettreDemande() {
        try {


            if (DtpFinDemande.getValue() == null) {
                // Affiche un message d'erreur si la date n'est pas sélectionnée
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une date de fin de demande.");
                alert.showAndWait();
                return;
            }

            Date dateFinDemande = getDateFinDemande();
            String sousMatiereDemandee = getSousMatiereDemandee();
            String matiereSelectionnee = (String) cboMatiereSouhaitee.getSelectionModel().getSelectedItem();
            int idUtilisateur = Utilisateur.getId();
            System.out.println(idUtilisateur);

            sqlController.creerDemandeUtilisateurConnecte(new Date(), dateFinDemande, sousMatiereDemandee, idUtilisateur, getIdMatiere(matiereSelectionnee), 1);

            // Affiche un message de réussite
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Demande soumise");
            alert.setHeaderText(null);
            alert.setContentText("Votre demande a été soumise avec succès.");
            alert.showAndWait();

            // Mets à jour la ListView des demandes après l'ajout
            updateDemandesListView();

        } catch (Exception e) {
            e.printStackTrace();
            // Affiche un message d'erreur en cas d'échec
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la soumission de votre demande.");
            alert.showAndWait();
        }
    }


    private int getIdMatiere(String matiereSelectionnee) {


        int idMatiere = sqlController.getIdMatiere(matiereSelectionnee);

        return idMatiere;
    }

    private Date getDateFinDemande() {


        LocalDate localDate = DtpFinDemande.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

    private String getSousMatiereDemandee() {
        return String.valueOf(lvSMS.getSelectionModel().getSelectedItem());
    }

    private void soumettreCompetence() {
        try {

            String sousMatiereDemandee = getSousMatiereDemandee2();
            String matiereSelectionnee = (String) cboMatiereSComp.getSelectionModel().getSelectedItem();
            int idUtilisateur = Utilisateur.getId();

            sqlController.creerCompetenceConnecte(getIdMatiere(matiereSelectionnee), idUtilisateur, sousMatiereDemandee, 1);

            // Affiche un message de réussite
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Compétence ajoutée");
            alert.setHeaderText(null);
            alert.setContentText("Votre compétence a été ajoutée avec succès.");
            alert.showAndWait();


        } catch (Exception e) {
            e.printStackTrace();
            // Affiche un message d'erreur en cas d'échec
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'ajout de votre compétence.");
            alert.showAndWait();
        }
    }

    private String getSousMatiereDemandee2() {
        return String.valueOf(lstvRecap.getSelectionModel().getSelectedItem());
    }

    public void afficherDemandesUtilisateurConnecte() {
        int idUtilisateur = Utilisateur.getId();
        System.out.println("ID Utilisateur: " + idUtilisateur);

        // Vide la ListView avant d'ajouter de nouvelles demandes
        lstVMesdemandes.getItems().clear();

        List<String> demandesUtilisateur = sqlController.getDemandesUtilisateurConnecte(idUtilisateur);

        System.out.println("Demandes Utilisateur: " + demandesUtilisateur);
        lstVMesdemandes.getItems().addAll(demandesUtilisateur);
    }


    private void afficherStatistiques1() {

        int idUtilisateur = Utilisateur.getId();
        HashMap<String, Integer> nombreDemandesParMatiere = sqlController.getNombreDemandesParMatiere(idUtilisateur);

        graphDemande.getData().clear();

        // Ajoute les données au BarChart
        for (Map.Entry<String, Integer> entry : nombreDemandesParMatiere.entrySet()) {
            String matiere_designation = entry.getKey();
            int nombreDemandes = entry.getValue();
            String matiere = matiere_designation;

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(matiere_designation);
            series.getData().add(new XYChart.Data<>(matiere, nombreDemandes));

            graphDemande.setStyle("-fx-bar-width: 1px");

            graphDemande.getData().add(series);

        }
    }

    private void afficherVisualiserCompetences() {
        int idUtilisateur = Utilisateur.getId();

        List<String> competences = RequeteSQLController.getCompetencesUtilisateurConnecte(idUtilisateur);
        ObservableList<String> items = FXCollections.observableArrayList(competences);
        lstvModifComp.setItems(items);

        for (String competence : competences) {
            System.out.println("Compétence : " + competence);
        }

    }

    public void afficherStatistiques2() {
        int idUtilisateur = Utilisateur.getId();
        HashMap<String, Integer> soutiensParMatiere = sqlController.getNombreSoutiensParUtilisateurConnecte(idUtilisateur);

        graphSoutiens.getData().clear();


        soutiensParMatiere.forEach((matiere, nombreSoutiens) -> {
            if (nombreSoutiens > 0) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(matiere);
                series.getData().add(new XYChart.Data<>(matiere, nombreSoutiens));
                graphSoutiens.getData().add(series);

            }
        });

    }

    @FXML
    public void afficherPopUpModifDemande(ActionEvent actionEvent) {
    }

    @Deprecated
    public void AfficherModifCompetence(ActionEvent actionEvent) {
    }

    @Deprecated
    private void AnnulerModifD(ActionEvent event) {

    }

    @FXML
    public void lstvMesDemandesMouseClicked(MouseEvent event) {
        // Récupérer l'élément sélectionné
        Object selectedObject = lstVMesdemandes.getSelectionModel().getSelectedItem();

        // Vérifier si un élément est sélectionné et si c'est bien une chaîne de caractères
        if (selectedObject != null && selectedObject instanceof String) {
            // Convertir l'élément sélectionné en String
            String demandeSelectionnee = (String) selectedObject;

            // Récupérer les informations de la demande à partir de la liste des demandes
            // Supposons que vous ayez une méthode pour récupérer ces informations à partir du numéro de demande
       //     String[] infosDemande = recupererInfosDemande(demandeSelectionnee);

            // Afficher les informations de la demande dans la pop-up de modification

            // Afficher la pop-up de modification
            // Code pour afficher la pop-up...
        } else {
            // Afficher un message d'erreur si aucun élément n'est sélectionné ou si ce n'est pas une chaîne de caractères
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une demande valide.");
            alert.showAndWait();
        }
    }



    @FXML
    private void annulerCompetence() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment annuler la création de compétence?");

        // Ajoute les boutons "Oui" et "Non"
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(btnOui, btnNon);

        Optional<ButtonType> resultat = confirmationAlert.showAndWait();

        if (resultat.isPresent() && resultat.get() == btnOui) {

            apAccueil.setVisible(true);
            apSDemande.setVisible(false);
            apVD.setVisible(false);
            apAider.setVisible(false);
            apModifDemande.setVisible(false);
            apStats.setVisible(false);
            apVC.setVisible(false);
            apCreerCompetence.setVisible(false);
        }
    }

}
