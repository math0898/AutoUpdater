package io.github.math0898.paperautoupdate;

import org.bukkit.Bukkit;

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
        if (!IS_REMOTE_CLONED) GitFacade.clone(REMOTE_URL, "./plugins/PaperUpdater/Paper/");
        GitFacade.pull("./plugins/PaperUpdater/Paper/");
        // todo gradle build, move jar to main folder named paper.jar
        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> new Thread(Updater::update).start(), 20 * 60 * 60 * 12); // Run every 12 hours
    }
}
