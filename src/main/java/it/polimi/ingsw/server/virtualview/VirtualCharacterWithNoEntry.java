package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterWithNoEntry;

/** VirtualCharacterWithNoEntry class is a simplified representation of a Character card containing no entry tiles.*/
public class VirtualCharacterWithNoEntry extends VirtualCharacter {
    private final int noEntry;

    /**Constructor VirtualCharacterWithNoEntry creates a new VirtualCharacterWithNoEntry instance.*/
    public VirtualCharacterWithNoEntry(Character character) {
        super(character);
        this.noEntry = ((CharacterWithNoEntry) character).getNumNoEntry();
    }

    public int getNoEntry() {
        return noEntry;
    }
}
