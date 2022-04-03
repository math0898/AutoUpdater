package io.github.math0898.autoupdater;

import io.github.math0898.autoupdater.packages.EssentialsX;
import io.github.math0898.autoupdater.packages.Package;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The package manager class handles package installation, removal and in some cases updates.
 *
 * @author Sugaku
 */
public class PackageManager {

    /**
     * The list of packages being managed by the package manager.
     */
    private final Map<String, Package> packages = new HashMap<>();

    /**
     * Loads all the packages defined in packages.yml.
     */
    public void load () {
        YamlConfiguration file = new YamlConfiguration();
        try {
            file.load("./plugins/AutoUpdater/packages.yml");
            for (String s : file.getKeys(false)) {
                if (file.getString(s + ".special") != null) {
                    String special = file.getString(s + ".special", "");
                    switch (special) {
                        case "EssentialsX" -> packages.put(s, new EssentialsX());
                    }
                    continue;
                }
                // todo implement for most packages.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Accessor method for the list of packages which are currently supported.
     *
     * @return The list of packages which are currently supported.
     */
    public List<String> getPackages () {
        return new ArrayList<>(packages.keySet());
    }

    /**
     * Installs the given package to the server.
     *
     * @param name The name of the package to install.
     */
    public void install (String name) {
        packages.get(name).install();
    }
}