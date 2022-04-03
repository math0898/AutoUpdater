package io.github.math0898.autoupdater.packages;

import io.github.math0898.autoupdater.facades.GitFacade;
import io.github.math0898.autoupdater.facades.GradleFacade;
import org.bukkit.ChatColor;

import java.io.File;

import static io.github.math0898.autoupdater.AutoUpdater.console;

/**
 * EssentialsX is an odd duck when it comes to packages. Multiple plugins share a common repository.
 *
 * @author Sugaku
 */
public class EssentialsX implements Package {

    /**
     * The repository for EssentialsX.
     */
    public static final String url = "https://github.com/EssentialsX/Essentials.git";

    /**
     * Whether the repository has been cloned or not.
     */
    private static boolean isCloned = new File("./plugins/AutoUpdater/Essentials/gradlew").exists();

    /**
     * Installs the package in the server.
     */
    @Override
    public void install () {
        console("Attempting to install EssentialsX...");
        if (!isCloned) {
            console("Cloning repository...", ChatColor.DARK_GRAY);
            GitFacade.clone(url, "./plugins/AutoUpdater/Essentials");
            console("Cloned repository.", ChatColor.DARK_GRAY);
            isCloned = true;
        }
        console("Fetching source...", ChatColor.DARK_GRAY);
        GitFacade.pull("./plugins/AutoUpdater/Essentials");
        console("Fetched source.", ChatColor.DARK_GRAY);
        File[] files = new File("./plugins/AutoUpdater/EssentialsXbuild/libs").listFiles();
        if (files != null) for (File file : files) if (file.getName().endsWith(".jar")) //noinspection ResultOfMethodCallIgnored
            file.delete();
        console("Building project...", ChatColor.DARK_GRAY);
        File gradlew = new File("./plugins/AutoUpdater/Essentials/gradlew");
        GradleFacade.runGradle(gradlew, "build");
        files = new File("plugins/AutoUpdater/Essentials/Essentials/build/libs").listFiles();
        if (files != null) for (File file : files) if (file.getName().endsWith(".jar")) {
            if (file.getName().contains("javadoc") || file.getName().contains("sources") || file.getName().contains("unshaded")) continue;
            console("Copying jar to plugins folder...", ChatColor.DARK_GRAY);
            if (file.renameTo(new File("./plugins/EssentialsX.jar"))) console("Copied jar to plugins folder.", ChatColor.DARK_GRAY);
            else console("Failed to copy jar to plugins folder.", ChatColor.RED);
            break;
        }
        console("Successfully installed EssentialsX. Will be available after restart.", ChatColor.GREEN);
    }
}
