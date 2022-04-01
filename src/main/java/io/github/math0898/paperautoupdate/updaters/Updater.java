package io.github.math0898.paperautoupdate.updaters;

/**
 * An updater is an interface used to update a specific plugin or server jar.
 *
 * @author Sugaku
 */
public interface Updater {

    /**
     * Updates the plugin or server jar associated with this updater. Should schedule another update after the interval.
     */
    void update ();

    /**
     * Updates the plugin or server jar associated with this updater. If asked, will schedule another update in the
     * future. This interval is 12 hours by default but will use any interval given to this updater previously.
     *
     * @param schedule Whether to schedule another update.
     */
    void update (boolean schedule);

    /**
     * Schedules an update to occur in the future. This interval is 12 hours by default.
     */
    void schedule ();

    /**
     * Schedules an update to occur in the future after the given interval. This interval is passed in seconds.
     *
     * @param interval The interval in seconds.
     */
    void schedule (long interval);
}
