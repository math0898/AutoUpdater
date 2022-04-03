package io.github.math0898.autoupdater;

import io.github.math0898.autoupdater.packages.Package;

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
    private Map<String, Package> packages = new HashMap<>();

    /**
     * Loads all the packages defined in packages.yml.
     */
    public void load () {
        // todo implement.
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
