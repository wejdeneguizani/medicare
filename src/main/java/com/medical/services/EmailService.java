package com.medical.services;

import com.medical.model.Commande;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * CONFIGURATION GMAIL — ÉTAPES À FAIRE UNE SEULE FOIS
 * ─────────────────────────────────────────────────────────────────────────────
 * 1. Crée un compte Gmail (ou utilise un existant)
 * 2. Va sur https://myaccount.google.com/security
 * 3. Active "Validation en deux étapes"
 * 4. Cherche "Mots de passe des applications"
 * 5. Crée un mot de passe → sélectionne "Autre" → nomme-le "Medicare+"
 * 6. Copie les 16 caractères (ex: abcd efgh ijkl mnop)
 * 7. Colle-les dans GMAIL_PASS ci-dessous (avec ou sans espaces)
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class EmailService {

    // ⬇️ MODIFIE CES DEUX LIGNES avec ton Gmail et ton mot de passe d'application
    private static final String GMAIL_USER = "ton.email@gmail.com";
    private static final String GMAIL_PASS = "abcd efgh ijkl mnop";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Envoie le reçu de paiement par email.
     * @param commande  La commande confirmée
     * @param pdfPath   Chemin du PDF généré (null si pas encore généré)
     * @return true si succès
     */
    public boolean envoyerRecu(Commande commande, String pdfPath) {
        if (commande.getEmailClient() == null || !commande.getEmailClient().contains("@")) {
            System.out.println("⚠️ Email invalide : " + commande.getEmailClient());
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        props.put("mail.smtp.ssl.trust",       "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_USER, GMAIL_PASS);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(GMAIL_USER, "Medicare+"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(commande.getEmailClient()));

            boolean estClient = "CLIENT".equals(commande.getTypeCommande());
            msg.setSubject(estClient
                    ? "✅ Votre reçu de paiement — Medicare+ #" + String.format("%05d", commande.getIdCommande())
                    : "📦 Confirmation de réapprovisionnement — Medicare+ #" + String.format("%05d", commande.getIdCommande()));

            MimeMultipart multipart = new MimeMultipart();

            // Corps HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(construireHtml(commande), "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);

            // Pièce jointe PDF (si disponible)
            if (pdfPath != null && new File(pdfPath).exists()) {
                MimeBodyPart pdf = new MimeBodyPart();
                pdf.attachFile(new File(pdfPath));
                pdf.setFileName("recu_medicare_" + commande.getIdCommande() + ".pdf");
                multipart.addBodyPart(pdf);
            }

            msg.setContent(multipart);
            Transport.send(msg);
            System.out.println("✅ Email envoyé à : " + commande.getEmailClient());
            return true;

        } catch (Exception e) {
            System.out.println("❌ Erreur envoi email : " + e.getMessage());
            return false;
        }
    }

    // ─── CORPS HTML DE L'EMAIL ────────────────────────────────────────────────
    private String construireHtml(Commande c) {
        boolean estClient = "CLIENT".equals(c.getTypeCommande());
        String titre      = estClient ? "Reçu de paiement" : "Confirmation de réapprovisionnement";
        String destinataire = estClient ? "Bonjour " + c.getNomClient() + "," :
                "Bonjour,<br>La commande de réapprovisionnement a été confirmée.";
        String typeLabel  = estClient ? "Client" : "Fournisseur";
        String qtLabel    = estClient ? "Quantité vendue" : "Quantité commandée";

        String header = "<div style='background:linear-gradient(135deg,#e91e8c,#f48fb1);padding:28px;text-align:center'>" +
                "<h1 style='color:white;margin:0;font-size:30px'>Medicare+</h1>" +
                "<p style='color:#fce4ec;margin:6px 0 0;font-style:italic'>Votre santé, notre priorité</p>" +
                "</div>";

        String corps = "<div style='padding:28px'>" +
                "<h2 style='color:#333'>" + destinataire + "</h2>" +
                "<p style='color:#555'>Ci-dessous le récapitulatif de votre " + titre.toLowerCase() + " :</p>" +
                "<table style='width:100%;border-collapse:collapse;margin:18px 0'>" +
                tr("#fce4ec", "Numéro", "#" + String.format("%05d", c.getIdCommande())) +
                tr("#fff",    "Type",   titre) +
                tr("#fce4ec", typeLabel, nvl(c.getNomClient())) +
                tr("#fff",    "Médicament", nvl(c.getNomMedicament())) +
                tr("#fce4ec", qtLabel,  c.getQuantite() + " unité(s)") +
                tr("#fff",    "Prix unitaire", String.format("%.2f DT", c.getPrixUnitaire())) +
                (estClient ? tr("#fce4ec", "Mode de paiement", nvl(c.getModePaiement())) : "") +
                tr("#e91e8c", "<span style='color:white;font-weight:bold'>MONTANT TOTAL</span>",
                        "<span style='color:white;font-weight:bold;font-size:18px'>" +
                                String.format("%.2f DT", c.getMontantTotal()) + "</span>") +
                "</table>" +
                "<p style='color:#888;font-size:12px'>Date : " + SDF.format(new Date()) + "</p>" +
                (pdfPath() ? "<p style='color:#1565c0'>📎 Le reçu PDF est en pièce jointe.</p>" : "") +
                "</div>";

        String footer = "<div style='background:#fce4ec;padding:14px;text-align:center'>" +
                "<p style='color:#e91e8c;margin:0'>Merci de votre confiance — Medicare+</p>" +
                "</div>";

        return "<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;background:#f5f5f5;padding:20px'>" +
                "<div style='max-width:600px;margin:auto;background:white;border-radius:12px;overflow:hidden;" +
                "box-shadow:0 4px 15px rgba(0,0,0,.1)'>" +
                header + corps + footer +
                "</div></body></html>";
    }

    private String tr(String bg, String label, String val) {
        return "<tr style='background:" + bg + "'>" +
                "<td style='padding:9px;font-weight:bold'>" + label + "</td>" +
                "<td style='padding:9px'>" + val + "</td></tr>";
    }
    private String nvl(String s)  { return s != null ? s : "—"; }
    private boolean pdfPath()     { return false; } // juste pour le template HTML
}