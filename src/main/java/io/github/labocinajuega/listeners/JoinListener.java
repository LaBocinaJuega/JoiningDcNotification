package io.github.labocinajuega.listeners;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.github.labocinajuega.JoiningDcNotificationPlugin;
import io.github.labocinajuega.services.WebHookService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JoinListener implements Listener {
    private final JoiningDcNotificationPlugin plugin;
    private final WebHookService webHookService;

    private static final DateTimeFormatter Format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public JoinListener(JoiningDcNotificationPlugin plugin, WebHookService webhookService) {
        this.plugin = plugin;
        this.webHookService = webhookService;
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        LocalDateTime TimeNow = LocalDateTime.now();

        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();
        String PlayerConnectionTime = TimeNow.format(Format);
        String PlayerVersion = getPlayerVersion(player);

        boolean enabled = plugin.getConfig().getBoolean("enabled");
        if (!enabled) {
            return;
        }
        String webhookUrl = plugin.getConfig().getString("webhook.url");

        if (webhookUrl == null || webhookUrl.isBlank()) {
            plugin.getLogger().warning(
                    "El webhook está activado pero no se ha configurado una URL."
            );
            return;
        }
        String message = plugin.getConfig().getString("message", "");
        if (message.isBlank()) {
            plugin.getLogger().warning(
                    "El formato del mensaje está vacío."
            );
            return;
        }
        message = message
                .replace("{player}", playerName)
                .replace("{uuid}", playerUUID)
                .replace("{version}", PlayerVersion)
                .replace("{time}", PlayerConnectionTime);
        webHookService.sendMessage(webhookUrl, message);
    }

    public String getPlayerVersion(Player player){
        if (org.bukkit.Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
            int playerVersionId = Via.getAPI().getPlayerVersion(player.getUniqueId());

            ProtocolVersion version = ProtocolVersion.getProtocol(playerVersionId);
            return version.getName();
        } else {
           return org.bukkit.Bukkit.getMinecraftVersion();
        }
    }
}
