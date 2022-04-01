package io.github.math0898.paperautoupdate;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;

import static io.github.math0898.paperautoupdate.PaperAutoupdater.updateManager;

/**
 * The update command forcibly runs the updater method.
 *
 * @author Sugaku
 */
public class UpdateCommand {

    /**
     * The command executor for the update command.
     */
    public static CommandExecutor executor = (commandSender, command, alias, args) -> {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Please specify an update target.");
            return true;
        }
        updateManager.runUpdater(args[0]);
        commandSender.sendMessage("Started updating...");
        commandSender.sendMessage("Check console for updates.");
        return true;
    };
}
