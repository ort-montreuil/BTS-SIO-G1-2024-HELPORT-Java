package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ModifierDemandeController {

    private RequeteSQLController requeteSQLController;
    public void setRequeteSQLController(RequeteSQLController requeteSQLController) {
        this.requeteSQLController = requeteSQLController;
    }
    @FXML
    private DatePicker dateModifDemande;

    @FXML
    private ComboBox<String> cboModifDemande;

    @FXML
    private ListView<String> lstvModifDemande;

    @FXML
    private TextField txtDescModifDemande;

    @FXML
    private Button btnModifierDemande;

    @FXML
    private Button btnAnnulerModifDemande;



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

