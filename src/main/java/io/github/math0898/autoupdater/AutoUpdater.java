package io.github.math0898.autoupdater;

import io.github.math0898.autoupdater.commands.PackageCommand;
import io.github.math0898.autoupdater.commands.UpdateCommand;
import io.github.math0898.autoupdater.facades.SpigetFacade;
import io.github.math0898.autoupdater.updaters.GradleBuilder;
import io.github.math0898.autoupdater.updaters.PaperUpdater;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * Main class for AutoUpdater.
 *
 * @author Sugaku
 */
public final class AutoUpdater extends JavaPlugin {

    /**
     * Whether this Java program is running on a Windows system or not. If not on Windows then on Linux and hopefully
     * have access to Bash.
     */
    public static final boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;

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
        Bukkit.getConsoleSender().sendMessage(prefix + color + message);
    }

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable () {
        plugin = this;
        saveResources();
        configManager.load();
        Objects.requireNonNull(Bukkit.getPluginCommand("update")).setExecutor(UpdateCommand.executor);
        Objects.requireNonNull(Bukkit.getPluginCommand("update")).setTabCompleter(UpdateCommand.tabCompleter);
        Objects.requireNonNull(Bukkit.getPluginCommand("au")).setExecutor(PackageCommand.executor);
        Objects.requireNonNull(Bukkit.getPluginCommand("au")).setTabCompleter(PackageCommand.tabCompleter);
        updateManager.addUpdater("Paper", new PaperUpdater());
        updateManager.addUpdater("AutoUpdater",new GradleBuilder("https://github.com/math0898/AutoUpdater.git", "AutoUpdater"));
        updateManager.scheduleUpdater("Paper");
        updateManager.scheduleUpdater("AutoUpdater");
        packageManager.load();
        SpigetFacade.queryResources();
        if (configManager.getAutoRestart())
            Bukkit.getScheduler().runTaskLater(this, AutoUpdater::restart, configManager.getRestartInterval());
    }

    /**
     * Saves all the resources in this plugin to the plugin folder.
     */
    public void saveResources () {
        this.saveResource("packages.yml", true);
        for (String s : Arrays.asList("Paper.yml", "config.yml", "auto-updater.yml"))
            if (!new File("./plugins/AutoUpdater/" + s).exists())
                this.saveResource(s, false);
    }

    /**
     * Restarts the server so that updates can be applied.
     */
    public static void restart () {
        Bukkit.shutdown();
    }
}
