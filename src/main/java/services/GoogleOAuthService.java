package services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import models.GoogleUserProfile;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

/**
 * Opens the system browser for Google consent, receives the redirect on localhost,
 * then loads the signed-in user's email from the userinfo endpoint.
 */
public final class GoogleOAuthService {

    private static final List<String> SCOPES = List.of(
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile"
    );
    private static final String USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final String clientId;
    private final String clientSecret;

    public GoogleOAuthService() throws IOException {
        Properties p = new Properties();
        try (InputStream in = GoogleOAuthService.class.getResourceAsStream("/google-oauth.properties")) {
            if (in == null) {
                throw new IOException(
                        "Fichier manquant : src/main/resources/google-oauth.properties. "
                                + "Copiez google-oauth.properties.example vers google-oauth.properties "
                                + "et renseignez le client OAuth (type Application de bureau).");
            }
            p.load(in);
        }
        this.clientId = p.getProperty("google.oauth.clientId", "").trim();
        this.clientSecret = p.getProperty("google.oauth.clientSecret", "").trim();
        if (clientId.isEmpty() || clientSecret.isEmpty()) {
            throw new IOException(
                    "Configurez google.oauth.clientId et google.oauth.clientSecret dans google-oauth.properties "
                            + "(Console Google Cloud : identifiants OAuth 2.0, type Application de bureau).");
        }
    }

    /**
     * Blocks until the user finishes in the browser (run off the JavaFX thread).
     */
    public GoogleUserProfile authorizeAndFetchProfile() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets secrets = new GoogleClientSecrets();
        GoogleClientSecrets.Details installed = new GoogleClientSecrets.Details();
        installed.setClientId(clientId);
        installed.setClientSecret(clientSecret);
        installed.setAuthUri("https://accounts.google.com/o/oauth2/auth");
        installed.setTokenUri("https://oauth2.googleapis.com/token");
        secrets.setInstalled(installed);

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(0).build();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, GsonFactory.getDefaultInstance(), secrets, SCOPES)
                .setAccessType("online")
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(USERINFO_URL));
        request.setParser(new JsonObjectParser(GsonFactory.getDefaultInstance()));
        HttpResponse response = request.execute();
        GenericJson data = response.parseAs(GenericJson.class);
        if (data == null) {
            throw new IOException("Reponse userinfo Google vide.");
        }
        Object emailObj = data.get("email");
        if (!(emailObj instanceof String email) || email.isBlank()) {
            throw new IOException("Google n'a pas renvoye d'email (verifiez les scopes du projet OAuth).");
        }
        Object nameObj = data.get("name");
        String name = nameObj instanceof String s ? s : null;
        return new GoogleUserProfile(email, name);
    }
}
