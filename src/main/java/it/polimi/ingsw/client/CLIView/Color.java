package it.polimi.ingsw.client.CLIView;

/**
 * Color Enum contains all ANSI escape codes to change the terminal's text color
 */
public enum Color {

    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_BLACK("\u001b[30;47m"),
    ANSI_GREY("\u001b[37m");

    static final String RESET =
            "\u001B[0m";

    private final String escape;

    /**
     * Constructor Color creates a new Color value.
     * @param escape String - the value of the enum.
     */
    Color(String escape)
    {
        this.escape = escape;
    }

    /**
     * Method toString returns the string c.
     * @return a string.
     */
    @Override
    public String toString()
    {
        return escape;
    }
}