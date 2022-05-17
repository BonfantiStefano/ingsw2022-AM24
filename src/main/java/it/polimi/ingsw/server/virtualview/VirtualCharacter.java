package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.character.Character;

import java.util.ArrayList;

/** VirtualCharacter class is a simplified representation of a Character card.*/
public class VirtualCharacter {
    private final boolean alreadyPlayed;
    private final String description;
    private final int cost;
    private boolean active;

    /**Constructor VirtualCharacter creates a new VirtualCharacter instance.*/
    public VirtualCharacter(Character character) {
        this.cost = character.getCost();
        this.description = character.getDescription();
        this.alreadyPlayed = character.isAlreadyPlayed();
        this.active = false;
    }

    /**
     * Method isAlreadyPlayed returns if the Character card has been already used
     * @return alreadyPlayed of type boolean - true if the card has been already played, false otherwise
     */
    public boolean isAlreadyPlayed() {
        return alreadyPlayed;
    }

    /**
     * Method getDescription returns the description of the virtual Character card
     * @return description - the virtual card's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method getCost returns the cost of the virtual Character card
     * @return cost - the virtual card's cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Method setActive sets the Character card's state
     * @param active - true if the Character is active, false otherwise
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Method isActive returns true if Character card is active, false otherwise
     * @return active - true if Character card is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }
}
