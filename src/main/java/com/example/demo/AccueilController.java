    package com.example.demo;

    import javafx.application.Platform;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.chart.BarChart;
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
        private MenuButton mnubtnCompte;
        @FXML
        private Button btnStat;
        @FXML
        private AnchorPane apVD;
        @FXML
        private ListView lsvSMS;
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



            lvsSousmatiere.setOnMouseClicked(event -> sousMatiereSelectionnee());

            mnubtnDemande.getItems().addAll(faireDemandeItem, visualiserDemandesItem);

            btnAccueil.setOnAction(event -> afficherAccueil());
            btnDeco.setOnAction(event -> deconnexion());
            btnAider.setOnAction(event -> afficherAider());
            btnStat.setOnAction(event -> afficherStats());


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

        public void initialiserUtilisateur(String emailUtilisateur) {
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
        }

        private void ajouterSousMatiereSelecC() {
            String matiereSelectionnee = (String) cboMatiereSComp.getSelectionModel().getSelectedItem();
            String sousMatiereSelectionnee = (String) lvsSousmatiereComp.getSelectionModel().getSelectedItem();

            if (matiereSelectionnee != null && sousMatiereSelectionnee != null) {
                String matiereAvecSousMatiere = matiereSelectionnee + " : " + sousMatiereSelectionnee;

                if (!sousMatieresSelectionneesComp.contains(matiereAvecSousMatiere)) {
                    lvsSousmatiereComp.getItems().add(matiereAvecSousMatiere);
                    sousMatieresSelectionneesComp.add(matiereAvecSousMatiere);
                    lstvRecap.getItems().add(matiereAvecSousMatiere);
                } else {
                    // Affiche un message d'erreur si la sous-matière est déjà sélectionnée
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de sélection");
                    alert.setHeaderText(null);
                    alert.setContentText("Vous avez déjà sélectionné cette sous-matière.");
                    alert.showAndWait();
                }
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

                if (!lsvSMS.getItems().contains(matiereAvecSousMatiere)) {
                    lsvSMS.getItems().add(matiereAvecSousMatiere);
                } else {
                    // Affiche un message d'erreur si la sous-matière est déjà sélectionnée
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de sélection");
                    alert.setHeaderText(null);
                    alert.setContentText("Vous avez déjà sélectionné cette sous-matière.");
                    alert.showAndWait();
                }
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

    }

