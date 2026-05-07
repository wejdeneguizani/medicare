package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

public class ChoixCompteController {

    @FXML
    private void choisirPatient(ActionEvent event) {
        changerVue(event, "/InscriptionPatient.fxml");
    }

    @FXML
    private void choisirMedecin(ActionEvent event) {
        changerVue(event, "/InscriptionMedecin.fxml");
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        changerVue(event, "/Home.fxml");
    }

    private void changerVue(ActionEvent event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setHeaderText("Erreur");
            alert.showAndWait();
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
