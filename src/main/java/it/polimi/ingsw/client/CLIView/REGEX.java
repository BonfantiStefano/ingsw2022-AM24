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
    HELP("^help$", "help")
    ;

    REGEX(String s, String desc) {
        this.s = s;
        this.desc = desc;
    }

    private final String s, desc;


    @Override
    public String toString() {
        return s;
    }

    public String getDesc(){
        return desc;
    }
}
