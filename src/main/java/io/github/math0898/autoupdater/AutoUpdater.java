package io.github.math0898.autoupdater;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static io.github.math0898.autoupdater.facades.SpigetFacade.downloadResource;
import static io.github.math0898.autoupdater.facades.SpigetFacade.queryResources;

/**
 * Main class for AutoUpdater.
 *
 * @author Sugaku
 */
public final class AutoUpdater {

    /**
     * Whether this Java program is running on a Windows system or not. If not on Windows then on Linux and hopefully
     * have access to Bash.
     */
//    public static final boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;

    /**
     * Whether the remote PaperMC repository has been cloned or not yet.
     */
    public static boolean IS_REMOTE_CLONED = new File("./plugins/AutoUpdater/Paper/gradlew").exists();

    /**
     * The url of the remote PaperMC repository.
     */
    public static final String REMOTE_URL = "https://github.com/PaperMC/Paper.git";

    /**
     * The current plugin instance.
     */
    public static JavaPlugin plugin;

    /**
     * The UpdateManager instance to be used with this plugin.
     */
    public static UpdateManager updateManager = new UpdateManager();

    /**
     * The PacketManager instance to be used with this plugin.
     */
    public static PackageManager packageManager = new PackageManager();

    /**
     * The ConfigManager instance to be used with this plugin.
     */
    public static ConfigManager configManager = new ConfigManager();

    /**
     * Prefix for messages sent to the server.
     */
    public static final String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "AutoUpdater" + ChatColor.DARK_GRAY + "] ";

    /**
     * The main execution point for the standalone package manager component.
     *
     * @param args Arguments given to the program.
     */
    public static void main (String[] args) {
        queryResources();
        if (args.length == 2) downloadResource(args[0], args[1]);
    }

    /**
     * Sends the given string to the console at the information level.
     *
     * @param message The message to send to the console.
     */
    public static void console (String message) {
        console(message, ChatColor.GRAY);
    }

    /**
     * Sends the given string to console at the information level with the given color.
     *
     * @param message The message to send to the console.
     * @param color   The color of the message.
     */
    public static void console (String message, ChatColor color) {
        try {
            Bukkit.getConsoleSender().sendMessage(prefix + color + message);
        } catch (Error e) { // Not running in a bukkit instance.
            System.out.println(prefix + color + message + ChatColor.GRAY);
        }
    }
}
