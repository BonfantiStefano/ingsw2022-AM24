package it.polimi.ingsw.model.character;

/**
 * Values for cost and description of all Characters
 */
public enum CharacterDescription {
    //TODO add all descriptions
    CHAR1(1, "Take one Student from this card"),
    CHAR2(2, "2"),
    CHAR3(3,"3"),
    CHAR4(1,"4"),
    CHAR5(2,"5"),
    CHAR6(3,"6"),
    CHAR7(1,"7"),
    CHAR8(2,"8"),
    CHAR9(3,"9"),
    CHAR10(1,"10"),
    CHAR11(2,"11"),
    CHAR12(3,"12");


    private int cost;
    private String desc;

    CharacterDescription(int cost, String desc) {
        this.cost=cost;
        this.desc=desc;
    }

    public int getCost() {
        return cost;
    }

    public String getDesc() {
        return desc;
    }
}
