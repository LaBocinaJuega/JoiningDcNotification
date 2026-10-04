package io.github.labocinajuega;

import io.github.labocinajuega.commands.JoinLogCommand;
import io.github.labocinajuega.listeners.JoinListener;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.labocinajuega.services.WebHookService;

public class JoiningDcNotificationPlugin extends JavaPlugin {
    private WebHookService webhookService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        webhookService = new WebHookService(this);
        getServer().getPluginManager().registerEvents(new JoinListener(this, webhookService), this);
        getCommand("joinlog").setExecutor(new JoinLogCommand(this));
        getLogger().info("Plugin iniciado correctamente");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin desactivado correctamente");
    }

}
