package io.github.math0898.autoupdater;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static io.github.math0898.autoupdater.AutoUpdater.plugin;

/**
 * The ConfigManager class is used to manage the config file.
 *
 * @author Sugaku
 */
public class ConfigManager {

    /**
     * The current config file version to expect.
     */
    private static final String configVersion = "0.1"; // todo grab from configuration file resource.

    /**
     * The current interval, after which to restart the server.
     */
    private long restartInterval;

    /**
     * Whether to automatically restart the server.
     */
    private boolean autoRestart;

    /**
     * Loads the configuration file.
     */
    public void load () {
        if (!new File("./plugins/AutoUpdater/config.yml").exists()) plugin.saveResource("config.yml", false);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("./plugins/AutoUpdater/config.yml"));
        restartInterval = config.getInt("restart.interval");
        autoRestart = config.getBoolean("restart.enabled", false);
    }

    /**
     * Saves an updated configuration file without overwriting the values set in the current one.
     */
    public void save () {
        // todo implement.
    }

    /**
     * Accessor method for the current server restart interval.
     *
     * @return The current interval, after which to restart the server.
     */
    public long getRestartInterval () {
        return restartInterval * 20;
    }

    /**
     * Accessor method for whether to automatically restart the server.
     *
     * @return Whether to automatically restart the server.
     */
    public boolean getAutoRestart () {
        return autoRestart;
    }
}
