package io.github.math0898.autoupdater;

import org.bukkit.Bukkit;

/**
 * Essentially a mock for Bukkit.ChatColor which is safe for testing.
 *
 * @author Sugaku
 */
public enum ChatColor {

    DARK_GRAY("&8", "\u001B[30m"), // todo currently is ANSI black

    GRAY("&7", "\u001B[0m"),

    RED("&c", "\u001B[31m"),

    GREEN("&a", "\u001B[32m"),

    DARK_AQUA("&b", "\u001B[36m");

    /**
     * The bukkit value of this color.
     */
    private final String bukkit;

    /**
     * The ansi value of this color.
     */
    private final String ansi;

    /**
     * Creates a new ChatColor with the given values when converted to a string in different contexts.
     *
     * @param bukkit The color code for when the color is being used in a Bukkit instance.
     * @param ansi   The color code to use when not being run in Bukkit. Hopefully the console supports ANSI colors.
     */
    ChatColor (String bukkit, String ansi) {
        this.bukkit = bukkit;
        this.ansi = ansi;
    }

    /**
     * Overrides the toString() method as there are two different ways this enum can become a string whether the program
     * has access to Bukkit or not.
     *
     * @return The string form of this enum value.
     */
    @Override
    public String toString () {
        try {
            Bukkit.getConsoleSender();
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', this.bukkit);
        } catch (Error error) { // Not running in a bukkit instance.
            return ansi;
        }
    }
}
