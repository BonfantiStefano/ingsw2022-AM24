package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.character.Character;

import java.util.ArrayList;

/** VirtualCharacter class is a simplified representation of a Character card.*/
public class VirtualCharacter {
    private boolean alreadyPlayed;
    private String description;
    private int cost;

    /**Constructor VirtualCharacter creates a new VirtualCharacter instance.*/
    public VirtualCharacter(Character character) {
        this.cost = character.getCost();
        this.description = character.getDescription();
        this.alreadyPlayed = character.isAlreadyPlayed();

    }
}
