package it.polimi.ingsw.model.character;

/**
 * Values for cost and description of all Characters
 */
public enum CharacterDescription {
    //load from a Json?
    CHAR1(1, "Take one Student from this card and place it on an Island of your choice. Then draw a new Student" +
            "from the bag and place it on this card."),
    CHAR2(2, "During this turn, you take control of any number of Professors even if you have the same number of Students" +
            "as the Player who currently controls them."),
    CHAR3(3,"Choose an Island and resolve the Island as if Mother Nature had ended her movement there. Mother Nature will still move" +
            "and the Island where she ends her movement will also be resolved."),
    CHAR4(1,"You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card tou played."),
    CHAR5(2,"Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there, put the No Entry" +
            "tile back onto this card DO NOT calculate influence on that Island, or place any Towers."),
    CHAR6(3,"When resolving a Conquering on an Island, Towers do not count towards influence."),
    CHAR7(1,"You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance."),
    CHAR8(2,"During the influence calculation this turn, you count as having 2 more influence."),
    CHAR9(3,"Choose a color of Student; during the influence calculation this turn, that color adds no influence."),
    CHAR10(1,"You may exchange up to 2 Students between your Entrance and your Dining Room."),
    CHAR11(2,"Take 1 Student from this card and place it in your Dining Room. Then, draw a new Student from the Bag and place it on this card."),
    CHAR12(3,"Choose a type of Student: every Player (including yourself) must return 3 Students of that type from their Dining Room" +
            "to the Bag. If any player has fewer than 3 Students of that type, return as many Students as they have.");


    private int cost;
    private String desc;

    /**
     * Creates a new Character
     * @param cost Character's cost
     * @param desc Character's description
     */
    CharacterDescription(int cost, String desc) {
        this.cost=cost;
        this.desc=desc;
    }

    /**
     * Gets the Character's cost
     * @return the Character's cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the Character's description
     * @return the Character's description
     */
    public String getDesc() {
        return desc;
    }
}
