package io.github.math0898.autoupdater.facades;

import org.bukkit.ChatColor;

import java.io.File;

import static io.github.math0898.autoupdater.AutoUpdater.console;

/**
 * The gradle facade sits between running the given gradlew and the AutoUpdater plugin.
 *
 * @author Sugaku
 */
public class GradleFacade {

    /**
     * Runs the given gradlew file with the given parameters.
     *
     * @param gradlew The gradlew file to run.
     * @param args    The arguments to pass to the gradlew file.
     */
    public static void runGradle (File gradlew, String args) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(gradlew.getAbsolutePath(), args);
            processBuilder.directory(gradlew.getParentFile());
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            console("Failed to build project.", ChatColor.DARK_GRAY);
        } finally {
            console("Built project.", ChatColor.DARK_GRAY);
        }
    }
}
