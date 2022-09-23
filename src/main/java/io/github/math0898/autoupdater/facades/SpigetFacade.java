package io.github.math0898.autoupdater.facades;

import io.github.math0898.autoupdater.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.github.math0898.autoupdater.AutoUpdater.console;

/**
 * A facade for interacting with the spiget website.
 *
 * <a href="https://spiget.org/">spiget</a>
 *
 * @author Sugaku
 */
public class SpigetFacade {

    /**
     * A user agent to identify this process with.
     */
    private static final String USER_AGENT = "math0898/AutoUpdater";

    /**
     * The url for requests.
     */
    private static final String REQUEST_URL = "https://api.spiget.org/v2/";

    /**
     * The total number of resources found.
     */
    private static long resourcesFound = 0;

    /**
     * The number of resources that are functional with the package manager.
     */
    private static long resourcesSupported = 0;

    /**
     * The master list of resources listed on Spigot.
     */
    private static final Map<String, Long> resourceList = new HashMap<>();

    /**
     * Pulls the full resource list from spiget. This should be run async so as not to crash the server.
     */
    public static void queryResources () {
        try {
            console("Polling resources from Spiget.", ChatColor.GRAY);

            URL url = new URL(REQUEST_URL + "resources?size=100000&fields=name,id,premium,external"); // todo perhaps there's a better way to do parameters.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JSONArray response = (JSONArray) JSONValue.parseWithException(reader);

            for (Object json : response) {
                resourcesFound++;
                if (json instanceof JSONObject obj) {
                    if (obj.get("premium") != null) if ((Boolean) obj.get("premium")) continue;
                    if (obj.get("external") != null) if ((Boolean) obj.get("external")) continue;
                    resourcesSupported++;
                    resourceList.put((String) obj.get("name"), (Long) obj.get("id"));
                }
            }

            console("Found " + resourcesFound + " resources and supporting " + resourcesSupported + ". " + ChatColor.DARK_GRAY
                    + String.format("%.2f", (resourcesSupported * 1.0 / resourcesFound) * 100) + "%", ChatColor.GREEN);

            cacheResources();
        } catch (Exception e) {
            console(e.getMessage(), ChatColor.RED);
            for (StackTraceElement se : e.getStackTrace()) console(se.toString(), ChatColor.RED);
        }
    }

    /**
     * Returns the list of resources.
     *
     * @return The master list of resources.
     */
    public static Set<String> getResources () {
        return resourceList.keySet();
    }

    /**
     * Caches a list of the resources the plugin found from Spiget.
     */
    public static void cacheResources () {
        try {
            YamlConfiguration cache = new YamlConfiguration();
            for (String key : resourceList.keySet()) {
                cache.set(key, resourceList.get(key));
            }
            cache.save("./AutoUpdater/cache.yml");
        } catch (Exception e) {
            console(e.getMessage(), ChatColor.RED);
            for (StackTraceElement se : e.getStackTrace()) console(se.toString(), ChatColor.RED);
        }
    }

    /**
     * Downloads the given resource and places it at the given path.
     *
     * @param name The plugin to download.
     * @param path The path to save the plugin at.
     */
    public static void downloadResource (String name, String path) {
        try {
            console("Downloading " + name + " and putting it in " + path, ChatColor.GRAY);

            Long resourceID = resourceList.get(name);
            if (resourceID == null) {
                console(name + " not found!", ChatColor.RED);
                return;
            }
            URL url = new URL(REQUEST_URL + "resources/" + resourceID + "/download");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream stream = connection.getInputStream();
            File file = new File(path + name + ".jar");
            Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            console("Finished downloading " + name + ".", ChatColor.GRAY);
        } catch (Exception e) {
            console(e.getMessage(), ChatColor.RED);
            for (StackTraceElement se : e.getStackTrace()) console(se.toString(), ChatColor.RED);
        }
    }
}
