package com.medical.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatbotController {

    @FXML private VBox       chatBox;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField  tfMessage;

    private Map<String, String> reponses = new HashMap<>();

    @FXML
    public void initialize() {
        initialiserReponses();
        ajouterMessageBot("👋 Bonjour ! Je suis l'assistant Medicare+.\n" +
                "Je peux vous aider avec des informations sur les médicaments.\n" +
                "Posez-moi une question ou utilisez les boutons rapides !");
    }

    private void initialiserReponses() {
        // Effets secondaires
        reponses.put("effets secondaires", "⚠️ Les effets secondaires courants des médicaments incluent :\n" +
                "• Nausées et troubles digestifs\n• Maux de tête\n• Fatigue ou somnolence\n" +
                "• Réactions allergiques (rougeurs, démangeaisons)\n\n" +
                "Consultez toujours la notice ou votre médecin en cas de doute.");

        reponses.put("effets", "⚠️ Les effets secondaires varient selon le médicament. " +
                "Consultez la notice du médicament ou demandez à votre pharmacien.");

        // Quand prendre
        reponses.put("quand prendre", "⏰ Conseils généraux sur la prise de médicaments :\n" +
                "• Avant les repas : pour une absorption rapide\n" +
                "• Pendant les repas : pour réduire l'irritation gastrique\n" +
                "• Après les repas : pour les médicaments irritants\n" +
                "• Le soir : pour les médicaments sédatifs\n\n" +
                "Respectez toujours les instructions de votre médecin.");

        reponses.put("prendre", "⏰ L'heure de prise dépend du médicament. " +
                "Vérifiez la notice ou demandez conseil à votre pharmacien.");

        // Contre-indications
        reponses.put("contre-indications", "🚫 Les contre-indications fréquentes incluent :\n" +
                "• Allergie connue au principe actif\n• Grossesse ou allaitement\n" +
                "• Insuffisance rénale ou hépatique\n• Interactions avec d'autres médicaments\n\n" +
                "Ne prenez jamais un médicament sans vérifier ses contre-indications.");

        reponses.put("contre", "🚫 Certains médicaments sont contre-indiqués dans des situations spécifiques. " +
                "Consultez toujours votre médecin avant de commencer un traitement.");

        // Stock
        reponses.put("stock", "📦 Pour vérifier le stock disponible, rendez-vous dans l'onglet 📦 Stock " +
                "de l'application. Vous pouvez y voir les quantités disponibles et les alertes de stock faible.");

        reponses.put("disponible", "📦 Consultez l'onglet Stock pour vérifier la disponibilité des médicaments.");

        // Dosage
        reponses.put("dosage", "💊 Le dosage dépend de plusieurs facteurs :\n" +
                "• Âge et poids du patient\n• Sévérité de la maladie\n" +
                "• Fonction rénale et hépatique\n• Autres médicaments pris\n\n" +
                "Ne modifiez jamais votre dosage sans avis médical.");

        reponses.put("dose", "💊 Respectez toujours le dosage prescrit par votre médecin. " +
                "Un surdosage peut être dangereux.");

        // Antibiotiques
        reponses.put("antibiotique", "🦠 Conseils sur les antibiotiques :\n" +
                "• Toujours terminer le traitement complet\n• Ne pas partager avec d'autres personnes\n" +
                "• Prendre à heures régulières\n• Ne pas prendre sans ordonnance médicale\n\n" +
                "L'arrêt prématuré favorise la résistance bactérienne.");

        // Paracetamol
        reponses.put("paracetamol", "💊 Paracétamol (Dafalgan, Doliprane) :\n" +
                "• Antalgique et antipyrétique\n• Dose adulte : 500mg à 1g par prise, max 4g/jour\n" +
                "• Espacement minimum entre les prises : 4 à 6 heures\n" +
                "• Danger en cas de surdosage ou d'alcool\n• Disponible sans ordonnance");

        // Ibuprofène
        reponses.put("ibuprofene", "💊 Ibuprofène (Brufen, Advil) :\n" +
                "• Anti-inflammatoire non stéroïdien (AINS)\n• À prendre pendant les repas\n" +
                "• Contre-indiqué en cas de grossesse (3ème trimestre)\n" +
                "• Déconseillé en cas d'ulcère gastrique\n• Dose adulte : 200-400mg par prise");

        // Interactions
        reponses.put("interaction", "⚠️ Les interactions médicamenteuses sont importantes :\n" +
                "• Informez votre médecin de tous vos médicaments\n" +
                "• Certains médicaments peuvent se neutraliser\n" +
                "• D'autres peuvent augmenter les effets indésirables\n" +
                "• Attention aux compléments alimentaires et plantes\n\n" +
                "Consultez toujours un professionnel de santé.");

        // Conservation
        reponses.put("conservation", "🌡️ Conservation des médicaments :\n" +
                "• À température ambiante (15-25°C) sauf indication contraire\n" +
                "• À l'abri de la lumière et de l'humidité\n" +
                "• Certains nécessitent une réfrigération (2-8°C)\n" +
                "• Hors de portée des enfants\n• Vérifiez la date de péremption");

        // Bonjour / Salutations
        reponses.put("bonjour", "👋 Bonjour ! Comment puis-je vous aider aujourd'hui ?");
        reponses.put("bonsoir", "🌙 Bonsoir ! Comment puis-je vous aider ?");
        reponses.put("merci", "😊 De rien ! N'hésitez pas si vous avez d'autres questions.");
        reponses.put("aide", "🆘 Je peux vous aider sur :\n• Effets secondaires des médicaments\n" +
                "• Quand et comment prendre vos médicaments\n• Contre-indications\n" +
                "• Conservation des médicaments\n• Interactions médicamenteuses\n• Stock disponible");
    }

    @FXML
    public void envoyerMessage(ActionEvent e) {
        String msg = tfMessage.getText().trim();
        if (msg.isEmpty()) return;
        ajouterMessageUser(msg);
        tfMessage.clear();
        String reponse = genererReponse(msg.toLowerCase());
        ajouterMessageBot(reponse);
    }

    @FXML public void questionEffets(ActionEvent e)  { simulerQuestion("Quels sont les effets secondaires ?"); }
    @FXML public void questionQuand(ActionEvent e)   { simulerQuestion("Quand prendre mon médicament ?"); }
    @FXML public void questionContre(ActionEvent e)  { simulerQuestion("Quelles sont les contre-indications ?"); }
    @FXML public void questionStock(ActionEvent e)   { simulerQuestion("Comment vérifier le stock disponible ?"); }

    private void simulerQuestion(String question) {
        ajouterMessageUser(question);
        ajouterMessageBot(genererReponse(question.toLowerCase()));
    }

    private String genererReponse(String message) {
        for (Map.Entry<String, String> entry : reponses.entrySet()) {
            if (message.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "🤔 Je n'ai pas compris votre question. Essayez de demander sur :\n" +
                "• Les effets secondaires\n• Les contre-indications\n" +
                "• Le dosage\n• La conservation\n• Les interactions médicamenteuses\n\n" +
                "Ou consultez directement un professionnel de santé.";
    }

    private void ajouterMessageUser(String message) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(4, 0, 4, 60));

        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(550);
        label.setStyle("-fx-background-color: #f48fb1; -fx-text-fill: white; " +
                "-fx-padding: 10px 15px; -fx-background-radius: 18px 18px 4px 18px; " +
                "-fx-font-size: 13px;");

        Label avatar = new Label("👤");
        avatar.setStyle("-fx-font-size: 20px; -fx-padding: 0 0 0 8px;");

        hbox.getChildren().addAll(label, avatar);
        chatBox.getChildren().add(hbox);
        scrollToBottom();
    }

    private void ajouterMessageBot(String message) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(4, 60, 4, 0));

        Label avatar = new Label("🤖");
        avatar.setStyle("-fx-font-size: 20px; -fx-padding: 0 8px 0 0;");

        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(550);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setStyle("-fx-background-color: #fff8fb; -fx-text-fill: #ad1457; " +
                "-fx-padding: 10px 15px; -fx-background-radius: 18px 18px 18px 4px; " +
                "-fx-border-color: #f8bbd0; -fx-border-radius: 18px 18px 18px 4px; " +
                "-fx-font-size: 13px;");

        hbox.getChildren().addAll(avatar, label);
        chatBox.getChildren().add(hbox);
        scrollToBottom();
    }

    private void scrollToBottom() {
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { naviguer("/CategorieView.fxml"); }
    @FXML public void allerFabricants(ActionEvent e)  { naviguer("/FabricantView.fxml"); }
    @FXML public void allerFormes(ActionEvent e)      { naviguer("/FormeView.fxml"); }
    @FXML public void allerStock(ActionEvent e)       { naviguer("/StockView.fxml"); }
    @FXML public void allerAlertes(ActionEvent e)     { naviguer("/AlerteView.fxml"); }
    @FXML public void allerChatbot(ActionEvent e)     { /* déjà ici */ }
    @FXML public void allerCommande(ActionEvent e)    { naviguer("/CommandeView.fxml"); }
    @FXML public void allerPaiement(ActionEvent e)    { naviguer("/PaiementView.fxml"); }
    @FXML public void allerShop(ActionEvent e)        { naviguer("/ShopView.fxml"); }

    private void naviguer(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            chatBox.getScene().setRoot(root);
        } catch (IOException ex) { System.out.println(ex.getMessage()); }
    }
}