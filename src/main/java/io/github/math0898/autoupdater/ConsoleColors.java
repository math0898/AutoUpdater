package io.github.math0898.autoupdater;

/**
 * Console colors is a work around that allows printing colors to the console using ANSI escape codes.
 *
 * @author Suagku
 */
public enum ConsoleColors {

    BLACK("\\e[0;30m"),

    RED("\\e[0;31m"),

    GREEN("\\e[0;32m"),

    YELLOW("\\e[0;33m"),

    BLUE("\\e[0;34m"),

    PURPLE("\\e[0;35m"),

    CYAN("\\e[0;36m"),

    WHITE("\\e[0;37m"),

    RESET("\\e[0m");


    /**
     * The String value for this console color.
     */
    private final String value;

    /**
     * Creates a new ConsoleColor with the given string value.
     *
     * @param value The string value of the ConsoleColor.
     */
    ConsoleColors (String value) {
        this.value = value;
    }

    /**
     * Returns the string ANSI escape code for this ConsoleColor.
     *
     * @return The escape code for this ConsoleColor.
     */
    public String getValue () {
        return value;
    }
}
