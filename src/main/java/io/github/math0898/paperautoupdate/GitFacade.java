package io.github.math0898.paperautoupdate;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.logging.Level;
import static io.github.math0898.paperautoupdate.PaperAutoupdater.PLUGIN;

/**
 * The GitFacade functions as a layer between JGit and the PaperAutoUpdater.
 *
 * @author Sugaku
 */
public class GitFacade {

    /**
     * Clones the given repository to the given directory.
     *
     * @param url The URL of the repository to clone.
     * @param directory The directory to clone the repository to.
     */
    public static void clone (String url, String directory) {
        try {
            Git.cloneRepository().setCloneSubmodules(true).setURI(url).setDirectory(new java.io.File(directory)).call();
        } catch (GitAPIException ex) {
            PLUGIN.getLogger().log(Level.SEVERE, "Failed to clone repository.", ex);
        }
    }

    /**
     * Pulls the remote repository attached to the directory git instance.
     *
     * @param directory The directory to pull the repository to.
     */
    public static void pull (String directory) {
        try {
            Git.open(new java.io.File(directory)).pull().call();
        } catch (Exception ex) {
            PLUGIN.getLogger().log(Level.SEVERE, "Failed to pull repository.", ex);
        }
    }
}
