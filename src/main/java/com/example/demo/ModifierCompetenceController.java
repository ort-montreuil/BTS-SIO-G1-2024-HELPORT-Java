package com.example.demo;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ModifierCompetenceController {

    // Ajoutez une référence à la scène principale (Stage)
    private Stage stage;

    // Méthode pour injecter la scène principale
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @javafx.fxml.FXML
    private void AnnulerModifC() {
        // Fermez simplement la scène principale (fenêtre pop-up)
        if (stage != null) {
            stage.close();
        }
    }

    public void initialiserCompetence(String CompetenceSelectionnee) {
        // Initialisation de la Competence
    }
}
