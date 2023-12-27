package com.example.demo;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PopUpAiderController {

    private Stage stage;
    @javafx.fxml.FXML
    private AnchorPane apPopUpModif;
    @javafx.fxml.FXML
    private DatePicker dateModifDemande;
    @javafx.fxml.FXML
    private ComboBox cboModifDemande;
    @javafx.fxml.FXML
    private ListView lstvModifDemande;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @javafx.fxml.FXML
    private void AnnulerAider() {
        if (stage != null) {
            stage.close();
        }
    }

    public void initialiserDemande(String demandeSelectionnee) {
    }
}
