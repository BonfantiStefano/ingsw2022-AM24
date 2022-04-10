package it.polimi.ingsw.model.character;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class CharacterWithNoEntryTest tests the correct behaviour of CharacterWithNoEntry
 */
class CharacterWithNoEntryTest {
    /**
     * Ensures the method returns the correct number of NoEntry tiles
     */
    @Test
    void getNumNoEntry(){
        CharacterWithNoEntry c =new CharacterWithNoEntry(1, "test");
        assertEquals(4, c.getNumNoEntry());
    }

    /**
     * Ensures that resetting adds a NoEntry tile
     */
    @Test
    void resetNoEntry(){
        CharacterWithNoEntry c = new CharacterWithNoEntry(1, "test");
        c.resetNoEntry();
        assertEquals(5, c.getNumNoEntry());
    }

}