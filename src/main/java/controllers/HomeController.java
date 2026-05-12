package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

public class HomeController {

    @FXML
    private void ouvrirLogin(ActionEvent event) {
        changerVue(event, "/Login.fxml");
    }

    @FXML
    private void ouvrirCreationCompte(ActionEvent event) {
        changerVue(event, "/ChoixCompte.fxml");
    }

    @FXML
    private void ouvrirAdmin(ActionEvent event) {
        changerVue(event, "/AdminLogin.fxml");
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

}
