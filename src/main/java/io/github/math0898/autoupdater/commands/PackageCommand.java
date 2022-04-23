package io.github.math0898.autoupdater.commands;

import io.github.math0898.autoupdater.facades.SpigetFacade;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.math0898.autoupdater.AutoUpdater.packageManager;
import static io.github.math0898.autoupdater.AutoUpdater.updateManager;

/**
 * The package command is used to access the package manager features in this plugin.
 *
 * @author Sugaku
 */
public class PackageCommand {

    /**
     * The tab completer for the update command.
     */
    public static TabCompleter tabCompleter = (sender, command, alias, args) -> {
        if (command.getName().equalsIgnoreCase("au")) {
            List<String> toReturn = new ArrayList<>();
            if (args.length == 1) toReturn.addAll(Arrays.asList("update", "list", "install"));
            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("update")) {
                    toReturn.addAll(updateManager.getUpdaters());
                    toReturn.add("all");
                } else if (args[0].equalsIgnoreCase("install")) {
                    toReturn.addAll(SpigetFacade.getResources());
                    toReturn.addAll(packageManager.getPackages());
                }
            }
            if (args.length != 1) toReturn.removeIf(s -> !s.startsWith(args[args.length - 1]));
            return toReturn;
        }
        return null;
    };

    /**
     * The command executor for the update command.
     */
    public static CommandExecutor executor = (commandSender, command, alias, args) -> { // todo refine.
        if (args.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "That doesn't seem to be right.");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "install" -> packageManager.install(args[1]);
            default -> commandSender.sendMessage(ChatColor.RED + "What verb would you like?");
        }
        return true;
    };
}
