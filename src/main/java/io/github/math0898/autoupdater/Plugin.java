package io.github.math0898.autoupdater;

import io.github.math0898.autoupdater.commands.PackageCommand;
import io.github.math0898.autoupdater.commands.UpdateCommand;
import io.github.math0898.autoupdater.facades.SpigetFacade;
import io.github.math0898.autoupdater.updaters.GradleBuilder;
import io.github.math0898.autoupdater.updaters.PaperUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static io.github.math0898.autoupdater.AutoUpdater.*;

/**
 * The main class for the plugin side of the auto-updater.
 *
 * @author Sugaku
 */
public final class Plugin extends JavaPlugin {

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
            Bukkit.getScheduler().runTaskLater(this, Plugin::restart, configManager.getRestartInterval());
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
