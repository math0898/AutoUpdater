package io.github.math0898.paperautoupdate;

import org.bukkit.Bukkit;

import java.io.File;
import java.util.logging.Level;

import static io.github.math0898.paperautoupdate.PaperAutoupdater.*;

/**
 * The updater does the actual work of updating paper.
 *
 * @author Sugaku
 */
public class Updater {

    /**
     * Attempts to update paper.
     */
    public static void update () {
        PLUGIN.getLogger().log(Level.INFO, "Attempting to update paper...");
        if (!IS_REMOTE_CLONED) {
            PLUGIN.getLogger().log(Level.INFO, "Cloning remote repository...");
            GitFacade.clone(REMOTE_URL, "./plugins/PaperUpdater/Paper/");
            PLUGIN.getLogger().log(Level.INFO, "Cloned remote repository.");
            IS_REMOTE_CLONED = true;
        }
        PLUGIN.getLogger().log(Level.INFO, "Fetching updates...");
        GitFacade.pull("./plugins/PaperUpdater/Paper/");
        PLUGIN.getLogger().log(Level.INFO, "Fetched updates.");
        PLUGIN.getLogger().log(Level.INFO, "Building paper.jar...");
        File gradlew = new File("./plugins/PaperUpdater/Paper/gradlew");
        try {
            // Apply patches
            ProcessBuilder pb = new ProcessBuilder(gradlew.getAbsolutePath(), "applyPatches");
            pb.directory(new File("./plugins/PaperUpdater/Paper/"));
            Process p = pb.start();
            p.waitFor();
            // Create jar
            pb = new ProcessBuilder(gradlew.getAbsolutePath(), "createReobfBundlerJar");
            pb.directory(new File("./plugins/PaperUpdater/Paper/"));
            p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            PLUGIN.getLogger().log(Level.SEVERE, "Failed to build paper.jar!");
            return;
        } finally {
            PLUGIN.getLogger().log(Level.INFO, "Built paper.jar.");
        }
        // todo move jar to main folder named paper.jar
        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> new Thread(Updater::update).start(), 20 * 60 * 60 * 12); // Run every 12 hours
    }
}
