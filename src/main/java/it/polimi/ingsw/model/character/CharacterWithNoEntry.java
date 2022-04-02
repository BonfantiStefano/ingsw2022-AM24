package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.character.Character;

public class CharacterWithNoEntry extends Character {
    private int numNoEntry;

    public CharacterWithNoEntry(int cost, String description) {
        super(cost, description);
        numNoEntry=4;
    }

    public int getNumNoEntry(){
        return numNoEntry;
    }

}
