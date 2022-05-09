package it.polimi.ingsw.client.CLIView;

/**
 * Enum REGEX lists all Request message types and their corresponding Pattern used to parse User input
 */
public enum REGEX {
    ENTR_HALL("^move (blue|pink|green|red|yellow) entr hall$"),
    TO_ISLAND("^move (blue|pink|green|red|yellow) entr ([1-9]|1[0-2])$"),
    SPECIAL_MOVE("^move (blue|pink|green|red|yellow) char ([1-9]|1[0-2])$"),
    TWO_COLORS("^(blue|pink|green|red|yellow) (blue|pink|green|red|yellow)$"),
    ONE_COLOR("^(blue|pink|green|red|yellow)$"),
    MOVE_MN("^move mn ([1-9]|1[0-2])$"),
    PLAY_CHAR("^play [1-3]$"),
    CHOOSE_ASSISTANT("^assistant ([1-9]|10)$"),
    CHOOSE_CLOUD("^cloud [1-3]$"),
    DISCONNECT("^disconnect$")
    ;

    REGEX(String s) {
    }

    private String s;

    @Override
    public String toString() {
        return s;
    }
}
