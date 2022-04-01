package io.github.math0898.autoupdater.updaters;

import io.github.math0898.autoupdater.GitFacade;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.logging.Level;

import static io.github.math0898.autoupdater.AutoUpdater.*;

/**
 * The updater does the actual work of updating paper.
 *
 * @author Sugaku
 */
public class PaperUpdater implements Updater {

    /**
     * The interval between update checks.
     */
    private long interval;

    /**
     * Attempts to update paper.
     */
    @Override
    public void update () {
        update(true);
    }

    /**
     * Attempts to update paper.
     *
     * @param schedule Whether to schedule the next update or not.
     */
    @Override
    public void update (boolean schedule) {
        plugin.getLogger().log(Level.INFO, "Attempting to update paper...");
        if (!IS_REMOTE_CLONED) {
            plugin.getLogger().log(Level.INFO, "Cloning remote repository...");
            GitFacade.clone(REMOTE_URL, "./plugins/AutoUpdater/Paper/");
            plugin.getLogger().log(Level.INFO, "Cloned remote repository.");
            IS_REMOTE_CLONED = true;
        }
        plugin.getLogger().log(Level.INFO, "Fetching updates...");
        GitFacade.pull("./plugins/AutoUpdater/Paper/");
        plugin.getLogger().log(Level.INFO, "Fetched updates.");
        File[] files = new File("./plugins/AutoUpdater/Paper/build/libs").listFiles();
        if (files != null) for (File file : files) if (file.getName().endsWith(".jar")) file.delete();
        plugin.getLogger().log(Level.INFO, "Building paper.jar...");
        File gradlew = new File("./plugins/AutoUpdater/Paper/gradlew");
        try {
            // Apply patches
            ProcessBuilder pb = new ProcessBuilder(gradlew.getAbsolutePath(), "applyPatches");
            pb.directory(new File("./plugins/AutoUpdater/Paper/"));
            Process p = pb.start();
            p.waitFor();
            // Create jar
            pb = new ProcessBuilder(gradlew.getAbsolutePath(), "createReobfBundlerJar");
            pb.directory(new File("./plugins/AutoUpdater/Paper/"));
            p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to build paper.jar!");
            return;
        } finally {
            plugin.getLogger().log(Level.INFO, "Built paper.jar.");
        }
        files = new File("./plugins/AutoUpdater/Paper/build/libs").listFiles();
        if (files != null) for (File file : files) if (file.getName().endsWith(".jar")) {
            plugin.getLogger().log(Level.INFO, "Copying paper.jar to server folder...");
            file.renameTo(new File("./paper.jar"));
            break;
        }
        plugin.getLogger().log(Level.INFO, "Copied paper.jar to server folder.");
        plugin.getLogger().log(Level.INFO, "Paper updated! This will be applied on the next server restart.");
        if (schedule) Bukkit.getScheduler().runTaskLater(plugin, () -> new Thread(this::update).start(), interval); // Run every 12 hours
    }

    /**
     * Schedules an update to occur in the future. This interval is 12 hours by default.
     */
    @Override
    public void schedule () {
        schedule(60 * 60 * 12);
    }

    /**
     * Schedules an update to occur in the future after the given interval. This interval is passed in seconds.
     *
     * @param interval The interval in seconds.
     */
    @Override
    public void schedule (long interval) {
        this.interval = interval * 20;
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> new Thread(this::update).start(), this.interval);
    }
}
