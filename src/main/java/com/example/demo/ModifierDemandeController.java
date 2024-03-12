package com.example.demo;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ModifierDemandeController {
    private RequeteSQLController requeteSQLController;
    @javafx.fxml.FXML
    private AnchorPane apPopUpModif;
    @javafx.fxml.FXML
    private DatePicker dateModifDemande;
    @javafx.fxml.FXML
    private ComboBox cboModifDemande;
    @javafx.fxml.FXML
    private ListView lstvModifDemande;
    @javafx.fxml.FXML
    private TextField txtDescModifDemande;
    @javafx.fxml.FXML
    private Button btnAnnulerModifD;
    @javafx.fxml.FXML
    private Button btnModifDemandeValider;

    public void setRequeteSQLController(RequeteSQLController requeteSQLController) {
        this.requeteSQLController = requeteSQLController;
    }


    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @javafx.fxml.FXML
    private void AnnulerModifD() {
        if (stage != null) {
            stage.close();
        }
    }


    public void initialiserDemande(String demandeSelectionnee) {
    }

    }

