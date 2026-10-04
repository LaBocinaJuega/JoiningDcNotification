package io.github.labocinajuega.commands;

import io.github.labocinajuega.JoiningDcNotificationPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JoinLogCommand implements CommandExecutor, TabCompleter {
    private final JoiningDcNotificationPlugin plugin;

    public JoinLogCommand(JoiningDcNotificationPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Uso: /joinlog reload", NamedTextColor.RED));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();

            sender.sendMessage(Component.text("Configuración recargada correctamente!", NamedTextColor.GREEN));
            return true;
        }
        sender.sendMessage(Component.text("Uso: /joinlog reload", NamedTextColor.RED));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> opciones = List.of( "reload");
        List<String> completaciones = new ArrayList<>();

        if (args.length == 1) {
            String inputActual = args[0].toLowerCase();

            for (String opcion : opciones) {
                if (opcion.startsWith(inputActual)) {
                    completaciones.add(opcion);
                }
            }
            return completaciones;
        }
        return List.of();
    }
}