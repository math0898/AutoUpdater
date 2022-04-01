package io.github.math0898.paperautoupdate;

import io.github.math0898.paperautoupdate.updaters.Updater;

import java.util.HashMap;
import java.util.Map;

/**
 * The update manager is used to run the update processes.
 *
 * @author Sugaku
 */
public class UpdateManager {

    /**
     * A map of updaters.
     */
    private final Map<String, Updater> updaters = new HashMap<>();

    /**
     * Adds an updater to the update manager so that it can be run by other parts of the plugin.
     *
     * @param name    The name of the updater.
     * @param updater The updater to add.
     */
    public void addUpdater (String name, Updater updater) {
        updaters.put(name, updater);
    }

    /**
     * Runs the updater with the given name.
     *
     * @param name The name of the updater to run.
     */
    public void runUpdater (String name) {
        Updater updater = updaters.get(name);
        if (updater == null) return;
        new Thread(() -> updater.update(false)).start();
    }

    /**
     * Schedules the updater with the given name to run in the future. This will schedule them every 12 hours.
     *
     * @param name The name of the updater to schedule.
     */
    public void scheduleUpdater (String name) {
        scheduleUpdater(name, 60 * 60 * 12);
    }

    /**
     * Schedules the updater with the given name to run in the future after the given number of seconds.
     *
     * @param name     The name of the updater to schedule.
     * @param interval The interval to schedule the updater at.
     */
    public void scheduleUpdater (String name, long interval) {
        Updater updater = updaters.get(name);
        if (updater == null) return;
        updater.schedule(interval);
    }
}
