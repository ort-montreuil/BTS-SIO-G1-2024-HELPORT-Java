package com.example.demo;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ModifierDemandeController {

    // Ajoutez une référence à la scène principale (Stage)
    private Stage stage;

    // Méthode pour injecter la scène principale
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @javafx.fxml.FXML
    private void AnnulerModifD() {
        // Fermez simplement la scène principale (fenêtre pop-up)
        if (stage != null) {
            stage.close();
        }
    }

    public void initialiserDemande(String demandeSelectionnee) {
        // Initialisation de la demande
    }
}
