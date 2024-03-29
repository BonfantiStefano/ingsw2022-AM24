package it.polimi.ingsw.client.CLIView;

/**
 * BOX Enum contains all unicode characters used to print the CLI interface
 */
public enum BOX {
    TOP_LEFT("\u250C"),
    TOP_RIGHT("\u2510"),
    VERT("\u2502"),
    HORIZ("\u2500"),
    BOT_LEFT("\u2514"),
    CIRCLE("\u25CF"),
    FORBIDDEN("\u20e0"),
    BOT_RIGHT("\u2518");

    private final String c;

    /**
     * Constructor BOX creates a new BOX value.
     * @param c String - the value of the enum.
     */
    BOX(String c){
        this.c=c;
    }

    /**
     * Method toString returns the string c.
     * @return a string.
     */
    @Override
    public String toString() {
        return c;
    }
}