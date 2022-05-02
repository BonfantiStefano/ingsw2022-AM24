package it.polimi.ingsw.client.CLIView;

public enum Color {

    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_BLACK("\u001b[30m"),
    ANSI_GREY("\u001b[47;m");

    static final String RESET =
            "\u001B[0m";

    private final String escape;

    Color(String escape)
    {
        this.escape = escape;
    }

    public String getEscape()
    {
        return escape;
    }

    @Override
    public String toString()
    {
        return escape;
    }
}