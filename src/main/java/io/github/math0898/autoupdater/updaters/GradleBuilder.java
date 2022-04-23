package io.github.math0898.autoupdater.updaters;

import io.github.math0898.autoupdater.ChatColor;
import io.github.math0898.autoupdater.facades.GitFacade;
import io.github.math0898.autoupdater.facades.GradleFacade;
import org.bukkit.Bukkit;

import java.io.File;

import static io.github.math0898.autoupdater.AutoUpdater.*;

/**
 * A GradleBuilder is an updater which, after cloning and updating a git repository, builds the project using
 * `gradle build`.
 *
 * @author Sugaku
 */
public class GradleBuilder implements Updater {

    /**
     * A boolean which specifies whether the repository has been cloned.
     */
    private boolean isCloned;

    /**
     * The url of the git repository to clone.
     */
    private final String url;

    /**
     * The name of the git repository.
     */
    private final String name;

    /**
     * The interval between update checks.
     */
    private long interval;

    /**
     * Creates a new GradleBuilder with the given repository target of the given name.
     *
     * @param url  The url of the repository to clone.
     * @param name The name of the repository.
     */
    public GradleBuilder (String url, String name) {
        this.url = url;
        this.name = name;
        isCloned = new File("./plugins/AutoUpdater/" + name + "/gradlew").exists();
    }

    /**
     * Updates the plugin or server jar associated with this updater. Should schedule another update after the interval.
     */
    @Override
    public void update () {
        update(true);
    }

    /**
     * Updates the plugin or server jar associated with this updater. If asked, will schedule another update in the
     * future. This interval is 12 hours by default but will use any interval given to this updater previously.
     *
     * @param schedule Whether to schedule another update.
     */
    @Override
    public void update (boolean schedule) {
        console("Attempting to update " + name + "...");
        if (!isCloned) {
            console("Cloning repository...", ChatColor.DARK_GRAY);
            GitFacade.clone(url, "./plugins/AutoUpdater/" + name);
            console("Cloned repository.", ChatColor.DARK_GRAY);
            isCloned = true;
        }
        console("Fetching updates...", ChatColor.DARK_GRAY);
        GitFacade.pull("./plugins/AutoUpdater/" + name);
        console("Fetched updates.", ChatColor.DARK_GRAY);
        File[] files = new File("./plugins/AutoUpdater/" + name + "/build/libs").listFiles();
        if (files != null) for (File file : files) if (file.getName().endsWith(".jar")) //noinspection ResultOfMethodCallIgnored
            file.delete();
        console("Building project...", ChatColor.DARK_GRAY);
        File gradlew = new File("./plugins/AutoUpdater/" + name + "/gradlew");
        GradleFacade.runGradle(gradlew, "build");
        files = new File("plugins/AutoUpdater/" + name + "/build/libs").listFiles();
        if (files != null) for (File file : files) if (file.getName().endsWith(".jar")) {
            console("Copying jar to plugins folder...", ChatColor.DARK_GRAY);
            if (file.renameTo(new File("./plugins/" + name + ".jar"))) console("Copied jar to plugins folder.", ChatColor.DARK_GRAY);
            else console("Failed to copy jar to plugins folder.", ChatColor.RED);
            break;
        }
        console("Successfully updated " + name + ". New version will be applied after restart.", ChatColor.GREEN);
        if (schedule) Bukkit.getScheduler().runTaskLater(plugin, () -> update(), interval);
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
