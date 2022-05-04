package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.character.Character;

import java.util.ArrayList;

/** VirtualCharacter class is a simplified representation of a Character card.*/
public class VirtualCharacter {
    private final boolean alreadyPlayed;
    private final String description;
    private final int cost;

    /**Constructor VirtualCharacter creates a new VirtualCharacter instance.*/
    public VirtualCharacter(Character character) {
        this.cost = character.getCost();
        this.description = character.getDescription();
        this.alreadyPlayed = character.isAlreadyPlayed();
    }

    public boolean isAlreadyPlayed() {
        return alreadyPlayed;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }
}
