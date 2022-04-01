package io.github.math0898.paperautoupdate;

import org.bukkit.command.CommandExecutor;

/**
 * The update command forcibly runs the updater method.
 *
 * @author Sugaku
 */
public class UpdateCommand {

    /**
     * The command executor for the update command.
     */
    public static CommandExecutor executor = (commandSender, command, string, strings) -> {
        commandSender.sendMessage("Started updating...");
        commandSender.sendMessage("Check console for updates.");
        new Thread(() -> Updater.update(false)).start();
        return true;
    };
}
