package io.github.math0898.autoupdater.packages;

/**
 * A Package is an item that can be installed on the server by running a command.
 *
 * @author Sugaku
 */
public interface Package {

    /**
     * Installs the package in the server.
     */
    void install ();
}
