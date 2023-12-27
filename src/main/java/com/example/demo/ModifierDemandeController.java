package com.example.demo;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ModifierDemandeController {
    private RequeteSQLController requeteSQLController;
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

