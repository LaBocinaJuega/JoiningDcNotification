package io.github.labocinajuega.services;

import com.google.gson.JsonObject;
import io.github.labocinajuega.JoiningDcNotificationPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WebHookService {
    private final JoiningDcNotificationPlugin plugin;
    private final HttpClient httpClient;

    public WebHookService(JoiningDcNotificationPlugin plugin) {
        this.plugin = plugin;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public void sendMessage(String webhookUrl, String message) {
        JsonObject json = new JsonObject();
        json.addProperty("content", message);

        String jsonString = json.toString();

        URI uri;

        try {
            uri = URI.create(webhookUrl);
        } catch (IllegalArgumentException exception) {
            plugin.getLogger().warning(
                    "La URL del webhook no es válida."
            );
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        plugin.getLogger().info("Webhook enviado correctamente.");
                    } else {
                        plugin.getLogger().warning("Discord respondió con código HTTP: " + response.statusCode());
                    }
                }).exceptionally(error -> {
                    plugin.getLogger().warning("No se pudo enviar el webhook: " + error.getMessage());
                    return null;
                });
    }
}
