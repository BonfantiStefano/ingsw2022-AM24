package it.polimi.ingsw.client.CLIView;

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


    private BOX(String c){
        this.c=c;
    }

    @Override
    public String toString() {
        return c;
    }
}