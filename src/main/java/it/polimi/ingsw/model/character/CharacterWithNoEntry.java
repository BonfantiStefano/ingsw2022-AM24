package it.polimi.ingsw.model.character;

/**
 * Class CharacterWithNoEntry models the Character that can limit MN's action on Islands
 */
public class CharacterWithNoEntry extends Character {
    private int numNoEntry;

    /**
     * Creates a new CharacterWithNoEntry object
     */
    public CharacterWithNoEntry(int cost, String description) {
        super(cost, description);
        numNoEntry=4;
    }

    /**
     * Gets the number of remaining noEntry tiles
     * @return the number of noEntry
     */
    public int getNumNoEntry(){
        return numNoEntry;
    }

    /**
     * Removes a noEntry tile from the card
     */
    public void removeNoEntry(){
        numNoEntry--;
    }
    /**
     * Restores a NoEntry tile on the card
     */
    public void resetNoEntry(){ numNoEntry++; }
}
