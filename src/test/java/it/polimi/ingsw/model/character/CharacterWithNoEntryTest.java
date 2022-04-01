package it.polimi.ingsw.model.character;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterWithNoEntryTest {

    @Test
    void getNumNoEntry(){
        CharacterWithNoEntry c =new CharacterWithNoEntry(1, "test");
        assertEquals(4, c.getNumNoEntry());
    }

}