package io.github.math0898.paperautoupdate;

import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

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
    public static final boolean IS_REMOTE_CLONED = false;

    /**
     * The url of the remote PaperMC repository.
     */
    public static final String REMOTE_URL = "https://github.com/PaperMC/Paper.git";

    /**
     * The current plugin instance.
     */
    public static JavaPlugin PLUGIN;

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable () {
        PLUGIN = this;
        File container = new File("./plugins/PaperUpdater/Paper/");
        Objects.requireNonNull(Bukkit.getPluginCommand("update")).setExecutor(UpdateCommand.executor);
        if (!container.exists()) container.mkdirs();
        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> new Thread(Updater::update).start(), 20 * 60 * 60 * 12); // Run every 12 hours
    }
}
