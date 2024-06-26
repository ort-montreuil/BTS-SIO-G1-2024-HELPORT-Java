package com.example.demo;

import com.example.demo.Entity.Matiere;
import com.example.demo.Entity.Salle;
import com.example.demo.Entity.Utilisateur;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminController implements Initializable {
    private static Connection cnx;
    private static PreparedStatement ps;
    private ResultSet rs;

    @javafx.fxml.FXML
    private Label lblNomAdmin;
    @javafx.fxml.FXML
    private AnchorPane apAccueilAd;
    @javafx.fxml.FXML
    private AnchorPane apStatistique;
    @javafx.fxml.FXML
    private AnchorPane apSalles;
    @javafx.fxml.FXML
    private AnchorPane apMatieres;
    @javafx.fxml.FXML
    private BarChart barchStat2;
    @javafx.fxml.FXML
    private StackedBarChart stbarchStat1;
    @javafx.fxml.FXML
    private PieChart piechart3;
    @javafx.fxml.FXML
    private Button btnNavAccueil;
    @javafx.fxml.FXML
    private Button btnNavStats;
    private String emailUtilisateur;
    @javafx.fxml.FXML
    private Button btnNavSalles;
    @javafx.fxml.FXML
    private TableView<Salle> tbvSalle;
    @FXML
    private TableColumn<Salle,Integer> colEtage;
    @FXML
    private TableColumn<Salle, Integer> colId;
    @FXML
    private TableColumn<Salle, String> colCodeSalle;
    @javafx.fxml.FXML
    private Button btnEnregistrerSalle;
    @javafx.fxml.FXML
    private Button btnNavMatieres;
    @javafx.fxml.FXML
    private TextField txtNomMat;
    @javafx.fxml.FXML
    private TextArea txtAreaSousMat;
    @javafx.fxml.FXML
    private Button btnAjouterMat;
    @javafx.fxml.FXML
    private ComboBox cboEtage;
    @javafx.fxml.FXML
    private Button btnAjouterSalle;
    @javafx.fxml.FXML
    private TextField txtNumSalle;
    @FXML
    private TableView<Matiere> tbvMatSousMat;
    @FXML
    private TableColumn<Matiere, Integer> colIdMat;
    @FXML
    private TableColumn<Matiere, String> designationMat;
    @FXML
    private TableColumn<Matiere, Integer> codeMat;
    @FXML
    private TableColumn<Matiere,String> sousMat;

    private RequeteSQLController sqlController = new RequeteSQLController();
    @FXML
    private Button btnEnregisterMat;
    @FXML
    private Button btnDeco;


    @Deprecated
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apAccueilAd.toFront();
        afficherAdStatistique1();
        afficherAdStatistique2();
        afficherAdStatistique3();
        remplirComboBoxEtage();

        colId.setEditable(false);
        colCodeSalle.setEditable(true);
        colEtage.setEditable(true);
        designationMat.setEditable(true);
        sousMat.setEditable(true);



        // Définir des méthodes pour gérer les événements d'édition dans la colonne colCodeSalle
        colCodeSalle.setCellFactory(TextFieldTableCell.forTableColumn());
        designationMat.setCellFactory(TextFieldTableCell.forTableColumn());
        sousMat.setCellFactory(TextFieldTableCell.forTableColumn());
        colCodeSalle.setOnEditCommit(new EventHandler<CellEditEvent<Salle, String>>() {
            @Override
            public void handle(CellEditEvent<Salle, String> event) {
                Salle salle = event.getRowValue();
                salle.setCodeSalle(event.getNewValue());
                salle.setModified(true); // Définir la salle comme modifiée
                sqlController.updateSalle(salle);
                tbvSalle.refresh();
            }
        });


        colEtage.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colEtage.setOnEditCommit((TableColumn.CellEditEvent<Salle, Integer>event) -> {
            // Récupérer la salle et la nouvelle valeur modifiée
            Salle salle = event.getRowValue();
            Integer newEtage = event.getNewValue();
            salle.setEtage(newEtage);            // Mettre à jour la propriété etage de la salle avec la nouvelle valeur
            salle.setModified(true); // Définir la salle comme modifiée
            sqlController.updateSalle(salle); // Mettre à jour la salle dans la base de données
            tbvSalle.refresh();  // Rafraîchir la TableView pour refléter les modifications
        });

        // Liaison des colonnes de TableView aux propriétés de la classe Salle
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodeSalle.setCellValueFactory(new PropertyValueFactory<>("codeSalle"));
        colEtage.setCellValueFactory(new PropertyValueFactory<>("etage"));

        // Récupération de la liste des salles depuis RequeteSqlController
        tbvSalle.setItems(sqlController.getListOfSalles());

        // Liaison des colonnes de TableView aux propriétés de la classe Matiere
        colIdMat.setCellValueFactory(new PropertyValueFactory<>("id"));
        designationMat.setCellValueFactory(new PropertyValueFactory<>("designation"));
        codeMat.setCellValueFactory(new PropertyValueFactory<>("code"));
        sousMat.setCellValueFactory(new PropertyValueFactory<>("sousMatiere"));

        designationMat.setCellFactory(TextFieldTableCell.forTableColumn());
        designationMat.setOnEditCommit(event -> {
            Matiere matiere = event.getRowValue();
            matiere.setDesignation(event.getNewValue());
            matiere.setModified(true);
            sqlController.updateMatiere(matiere);
            tbvMatSousMat.refresh();
        });

        sousMat.setCellFactory(TextFieldTableCell.forTableColumn());
        sousMat.setOnEditCommit(event -> {
            Matiere matiere = event.getRowValue();
            matiere.setSousMatiere(event.getNewValue());
            matiere.setModified(true);
            sqlController.updateMatiere(matiere);
            tbvMatSousMat.refresh();
        });

        // Récupération de la liste des matières depuis RequeteSqlController
        tbvMatSousMat.setItems(sqlController.getListOfMatiere());
    }


    @javafx.fxml.FXML
    private void AfficherAccueilAdmin() {
        apAccueilAd.setVisible(true);
        apSalles.setVisible(false);
        apMatieres.setVisible(false);
        apStatistique.setVisible(false);
    }
    @javafx.fxml.FXML
    public void AfficherSallesAdmin() {
        apAccueilAd.setVisible(false);
        apSalles.setVisible(true);
        apMatieres.setVisible(false);
        apStatistique.setVisible(false);
    }

    @javafx.fxml.FXML
    public void AfficherMatiereAdmin() {
        apAccueilAd.setVisible(false);
        apSalles.setVisible(false);
        apMatieres.setVisible(true);
        apStatistique.setVisible(false);
    }


    @javafx.fxml.FXML
    private void AfficherStatistiqueAdmin() {
        apAccueilAd.setVisible(false);
        apSalles.setVisible(false);
        apMatieres.setVisible(false);
        apStatistique.setVisible(true);
    }


    public int initialiserUtilisateur(String emailUtilisateur) {

        this.emailUtilisateur = emailUtilisateur;

        Map<String, String> utilisateurInfo = sqlController.getNomPrenomUtilisateur(emailUtilisateur);

        String nomUtilisateur = utilisateurInfo.get("nom");
        String prenomUtilisateur = utilisateurInfo.get("prenom");

        lblNomAdmin.setText("Bienvenue, " + prenomUtilisateur + " " + nomUtilisateur);
        return 0;
    }


    @javafx.fxml.FXML
    public void AjouterUneMatiere() {
        String nomMatiere = txtNomMat.getText();
        String sousMatieres = txtAreaSousMat.getText();

        // Vérifier si le nom de la matière est vide
        if (nomMatiere.isEmpty()) {
            afficherErreur("Vous devez entrer le nom de la matière.");

        }
        // Vérifier si la liste des sous-matières est vide
        else if(sousMatieres.isEmpty()) {
            afficherErreur("La liste des sous-matières ne peut pas être vide.");
        }
        else {
            // Récupérer le texte du TextArea
            String sousMatieresText = txtAreaSousMat.getText();

            // Diviser le texte en lignes
            String[] lines = sousMatieresText.split("\\n");

            // Initialiser un ensemble pour stocker les sous-matières uniques
            Set<String> sousMatieresUniques = new HashSet<>();

            // Parcourir chaque ligne du TextArea
            for (String line : lines) {
                // Diviser chaque ligne en sous-matières en utilisant "#"
                String[] sousMatiereSplit = line.split("#");
                // Ajouter les sous-matières à l'ensemble
                sousMatieresUniques.addAll(Arrays.stream(sousMatiereSplit)
                        .filter(part -> !part.isEmpty())
                        .collect(Collectors.toList()));
            }
            // Appeler la méthode pour ajouter la matière dans la base de données
            sqlController.ajouterMatiere(nomMatiere, String.join("#", sousMatieresUniques));
            afficherSuccess("La matière " + nomMatiere + " a été ajoutée avec succès.");
        }
    }


    private void afficherSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private StackedBarChart<String, Number> afficherAdStatistique1() {
        // Récupérer les données depuis le RequeteSqlController
        List<HashMap<String, Object>> statistiques = sqlController.afficherAdminStatistique1();

        // Créer le StackedBarChart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        StackedBarChart<String, Number> chart = new StackedBarChart<>(xAxis, yAxis);
        chart.setTitle("Nombre de demandes par niveau et par matière");

        // Créer les séries de données pour chaque niveau
        HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
        for (HashMap<String, Object> stat : statistiques) {
            String matiere = (String) stat.get("matiere");
            String niveau = (String) stat.get("niveau");
            int nombreDemandes = (int) stat.get("nombreDemandes");

            // Vérifier si la série pour ce niveau existe déjà, sinon la créer
            XYChart.Series<String, Number> series = seriesMap.get(niveau);
            if (series == null) {
                series = new XYChart.Series<>();
                series.setName(niveau);
                seriesMap.put(niveau, series);
            }

            // Ajouter les données à la série
            series.getData().add(new XYChart.Data<>(matiere, nombreDemandes));
        }

        // Ajouter les séries au graphique
        for (XYChart.Series<String, Number> series : seriesMap.values()) {
            stbarchStat1.getData().add(series);
        }

        return chart;
    }

    private void afficherAdStatistique2() {
        barchStat2.setPrefSize(400, 300);
        try {
            rs = sqlController.afficherAdminStatistique2();

            // Création d'une nouvelle série de données
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Nombre de soutiens par utilisateur");

            while (rs.next()) {
                // Récupération des données de la ligne courante du résultat
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int nombreSoutiens = rs.getInt("nombre_soutiens");
                String nomComplet = nom + " " + prenom;

                series.getData().add(new XYChart.Data<>(nomComplet, nombreSoutiens));
            }

            // Effacer les données précédentes du graphique
            barchStat2.getData().clear();

            // Ajout de la série au graphique à barres
            barchStat2.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void afficherAdStatistique3() {
        try {
            rs = sqlController.afficherAdminStatistique3();

            // Création d'une nouvelle liste pour les sections du pie chart
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();


            while (rs.next()) {
                // Récupération des données de la ligne courante du résultat
                String sousMatiere = rs.getString("sous_matiere");
                int nombreSolicitations = rs.getInt("nombre_solicitations");
                pieChartData.add(new PieChart.Data(sousMatiere, nombreSolicitations));
            }

            // Effacer les données précédentes du graphique
            // (si nécessaire, selon l'implémentation de votre graphique)
            piechart3.getData().clear();

            // Ajout de la série au graphique à barres
            piechart3.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void AjouterUneSalle(Event event) {
        String numSalleText = txtNumSalle.getText();
        String etageText = "";

        if (cboEtage.getValue() == null || numSalleText.isEmpty()) {
            afficherErreur("Veuillez entrer un numéro de salle et sélectionner un étage.");
        } else {
            etageText = cboEtage.getValue().toString();

            if (!numSalleText.matches("\\d{1,3}")) {
                afficherErreur("Vous devez saisir un numéro de salle valide.");
            } else {
                int numSalle = Integer.parseInt(numSalleText);

                if (sqlController.salleExisteDeja(numSalle)) {
                    afficherErreur("Le numéro de salle a déjà été ajouté.");
                } else {
                    String premierChiffreSalle = numSalleText.substring(0, 1);
                    if (!premierChiffreSalle.equals(etageText)) {
                        afficherErreur("Le premier chiffre du numéro de salle ne correspond pas à l'étage sélectionné.");
                    } else {
                        int etage = Integer.parseInt(etageText);

                        sqlController.ajouterSalle(numSalleText, etage);
                        afficherSuccess("La salle " + numSalleText + " à l'étage " + etage + " a été ajoutée avec succès.");
                    }
                }
            }
        }
    }

    @javafx.fxml.FXML
    public void ModifierSalle(Event event) {
        boolean modificationEffectuee = false; // Variable pour vérifier si au moins une modification a été effectuée

        // Parcourir les éléments de la TableView
        for (Salle salle : tbvSalle.getItems()) {
            // Vérifier si la salle a été modifiée
            if (salle.isModified()) {
                // Mettre à jour la salle dans la base de données
                sqlController.updateSalle(salle);
                salle.setModified(false);
                modificationEffectuee = true;
            }
        }
        // Afficher un message de succès si au moins une modification a été effectuée
        if (modificationEffectuee) {
            afficherSuccess("La modification a été enregistrés avec succès !");
        }
    }
    private void remplirComboBoxEtage() {
        // Ajouter les étages à la ComboBox
        cboEtage.getItems().addAll("1", "2", "3", "4", "5", "6"); // Ajoutez ici tous les étages nécessaires
    }

    @FXML
    public void ModifierMatiere(Event event) {
        boolean modificationEffectuee = false; // Variable pour vérifier si au moins une modification a été effectuée

        // Parcourir les éléments de la TableView
        for (Matiere matiere : tbvMatSousMat.getItems()) {
            // Vérifier si la matière a été modifiée
            if (matiere.isModified()) {
                // Vérifier si la colonne "Designation" est vide ou contient un chiffre
                if (matiere.getDesignation().isEmpty() || contientChiffre(matiere.getDesignation())) {
                    afficherErreur("La colonne Designation ne peut pas être vide et ne peut pas contenir de chiffres.");
                }
                // Vérifier si la colonne "Sous-mat" est vide ou contient un chiffre
                else if (matiere.getSousMatiere().isEmpty() || contientChiffre(matiere.getSousMatiere())) {
                    afficherErreur("La colonne Sous-mat ne peut pas être vide et ne peut pas contenir de chiffres.");
                }
                // Si les colonnes "Designation" et "Sous-mat" ne sont ni vides ni contiennent de chiffres, mettre à jour la matière dans la base de données
                else {
                    sqlController.updateMatiere(matiere);
                    matiere.setModified(false);
                    modificationEffectuee = true;
                }
            }
        }
        // Afficher un message de succès si au moins une modification a été effectuée
        if (modificationEffectuee) {
            afficherSuccess("La modification a été enregistrée avec succès !");
        }
    }

    // Méthode pour vérifier si une chaîne contient un chiffre
    private boolean contientChiffre(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    public void SeDeconnecter(Event event) {
        try {
            Utilisateur.setId(-1);
            Stage currentStage = (Stage) btnDeco.getScene().getWindow();
            currentStage.close(); // Ferme le stage actuel

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ConnexionInscription.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}