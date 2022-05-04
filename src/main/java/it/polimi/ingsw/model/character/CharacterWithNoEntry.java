package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.EVENT;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;

import java.beans.PropertyChangeSupport;

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
        getListener().firePropertyChange(String.valueOf(EVENT.CHANGE_CHARACTER_NE), null, this);
    }
    /**
     * Restores a NoEntry tile on the card
     */
    public void resetNoEntry(){
        numNoEntry++;
        getListener().firePropertyChange(String.valueOf(EVENT.CHANGE_CHARACTER_NE), null, this);
    }
}
