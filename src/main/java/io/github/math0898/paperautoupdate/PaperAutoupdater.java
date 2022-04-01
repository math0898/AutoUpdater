package io.github.math0898.paperautoupdate;

import io.github.math0898.paperautoupdate.updaters.GradleBuilder;
import io.github.math0898.paperautoupdate.updaters.PaperUpdater;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Main class for PaperAutoupdater.
 *
 * @author Sugaku
 */
public final class PaperAutoupdater extends JavaPlugin {

    /**
     * Whether this Java program is running on a Windows system or not. If not on Windows then on Linux and hopefully
     * have access to Bash.
     */
    public static final boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;

    /**
     * Whether the remote PaperMC repository has been cloned or not yet.
     */
    public static boolean IS_REMOTE_CLONED = new File("./plugins/PaperUpdater/Paper/gradlew").exists();

    /**
     * The url of the remote PaperMC repository.
     */
    public static final String REMOTE_URL = "https://github.com/PaperMC/Paper.git";

    /**
     * The current plugin instance.
     */
    public static JavaPlugin PLUGIN;

    /**
     * The UpdateManager instance to be used with this plugin.
     */
    public static UpdateManager updateManager = new UpdateManager();

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
        PLUGIN.getLogger().log(Level.INFO, color + message);
    }

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable () {
        PLUGIN = this;
        File container = new File("./plugins/PaperUpdater/");
        if (!container.exists()) container.mkdirs();
        Objects.requireNonNull(Bukkit.getPluginCommand("update")).setExecutor(UpdateCommand.executor);
        Objects.requireNonNull(Bukkit.getPluginCommand("update")).setTabCompleter(UpdateCommand.tabCompleter);
        updateManager.addUpdater("paper", new PaperUpdater());
        updateManager.addUpdater("auto-updater",new GradleBuilder("https://github.com/math0898/PaperUpdater.git", "PaperUpdater"));
        updateManager.scheduleUpdater("paper");
        updateManager.scheduleUpdater("auto-updater");
    }
}
