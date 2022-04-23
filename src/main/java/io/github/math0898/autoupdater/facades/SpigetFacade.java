package io.github.math0898.autoupdater.facades;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    private static final Map<String, Long> resourceList = new HashMap<>();

    /**
     * Pulls the full resource list from spiget.
     */
    public static void queryResources () {
        System.out.println("Pulling resources from spiget.");
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.spiget.org/v2/resources?fields=name&size=100000")).build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenApply(SpigetFacade::parseResources);
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

    /**
     * Parses the server response for the resource list into the map.
     *
     * @param responseBody The body of the response.
     * @return The string body of the response.
     */
    public static String parseResources (String responseBody) {
        try {
            JSONArray array = (JSONArray) new JSONParser().parse(responseBody);
            array.forEach((p) -> {
                if (p instanceof JSONObject obj) {
                    resourceList.put((String) obj.get("name"), (Long) obj.get("id"));
                } else System.out.println("Not an instance of JSONObject");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBody;
    }
}
