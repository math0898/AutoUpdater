package io.github.math0898.autoupdater.facades;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A facade for interacting with the spiget website.
 *
 * <a href="https://spiget.org/">spiget</a>
 *
 * @author Sugaku
 */
public class SpigetFacade {

    /**
     * The master list of resources listed on Spigot.
     */
    private static final Map<String, Integer> resourceList = new HashMap<>();

    /**
     * Pulls the full resource list from spiget.
     */
    public static void queryResources () {
        System.out.println("Pulling resources from spiget.");
        try {
            URL url = new URL("https://api.spiget.org/v2/resources?fields=name&size=100000");
            JSONArray array = (JSONArray) url.getContent();
            array.forEach((p) -> {
                if (p instanceof JSONObject obj) {
                    resourceList.put((String) obj.get("name"), (Integer) obj.get("id"));
                } else System.out.println("Not an instance of JSONObject");
            });
        } catch (Exception ignored) {
            ignored.printStackTrace();
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
}
