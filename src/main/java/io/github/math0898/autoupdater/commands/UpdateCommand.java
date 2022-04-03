package io.github.math0898.autoupdater.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.List;

import static io.github.math0898.autoupdater.AutoUpdater.updateManager;

/**
 * The update command forcibly runs the updater method.
 *
 * @author Sugaku
 */
public class UpdateCommand {

    /**
     * The tab completer for the update command.
     */
    public static TabCompleter tabCompleter = (sender, command, alias, args) -> {
        if (command.getName().equalsIgnoreCase("update")) {
            List<String> toReturn = updateManager.getUpdaters();
            toReturn.add("all");
            if (args.length != 1) toReturn.removeIf(s -> !s.startsWith(args[0]));
            return toReturn;
        }
        return null;
    };

    /**
     * The command executor for the update command.
     */
    public static CommandExecutor executor = (commandSender, command, alias, args) -> {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Please specify an update target.");
            return true;
        }
        if (args[0].equalsIgnoreCase("all")) updateManager.updateAll();
        else updateManager.runUpdater(args[0]);
        commandSender.sendMessage("Started updating...");
        commandSender.sendMessage("Check console for updates.");
        return true;
    };
}
