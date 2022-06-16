package it.polimi.ingsw.client.CLIView;

/**
 * Enum REGEX lists all Request message types and their corresponding Pattern used to parse User input
 */
public enum REGEX {
    ENTR_HALL("^move (blue|pink|green|red|yellow) entr hall$", "move *color* entr hall"),
    TO_ISLAND("^move (blue|pink|green|red|yellow) entr ([1-9]|1[0-2])$", "move *color* entr *numIsland*"),
    SPECIAL_MOVE("^move (blue|pink|green|red|yellow) isle ([1-9]|1[0-2])$", "move *color* entr *numIsland*"),
    TWO_COLORS("^(blue|pink|green|red|yellow) (blue|pink|green|red|yellow)$", "*color* *color*"),
    ONE_COLOR("^(blue|pink|green|red|yellow)$", "*color*"),
    MOVE_MN("^move mn ([1-9]|1[0-2])$", "move mn *numIsland*"),
    PLAY_CHAR("^play [1-3]$", "play *numCharacter*"),
    CHOOSE_ASSISTANT("^assistant ([1-9]|10)$", "assistant *numAssistant*"),
    CHOOSE_CLOUD("^cloud [1-3]$", "cloud *numCloud*"),
    CHOOSE_ISLAND("^island ([1-9]|1[0-2])$", "island *numIsland*"),
    DISCONNECT("^disconnect$", "disconnect"),
    SHOW_HAND("^hand$","hand"),
    HELP("^help$", "help")
    ;

    private final String s, desc;

    /**
     * Constructor Color creates a new Color value.
     * @param s String - the value of the enum.
     * @param desc String - the description of a value.
     */
    REGEX(String s, String desc) {
        this.s = s;
        this.desc = desc;
    }

    /**
     * Method toString returns the string c.
     * @return a string.
     */
    @Override
    public String toString() {
        return s;
    }

    /**
     * Method getDesc returns the description of a value.
     * @return a string.
     */
    public String getDesc(){
        return desc;
    }
}
