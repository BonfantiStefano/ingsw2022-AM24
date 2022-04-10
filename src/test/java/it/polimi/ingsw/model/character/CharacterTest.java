package it.polimi.ingsw.model.character;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Since Character is an abstract class the tests are conducted with objects from a subclass
 */
class CharacterTest {
    /**
     * Ensures that the method returns the correct value
     */
    @Test
    void getCost() {
        Character c=new CharacterWithStudent(1, "I am a character",1);
        assertEquals(1, c.getCost());
    }
    /**
     * Ensures that the method returns the correct value
     */
    @Test
    void getDescription() {
        Character c=new CharacterWithStudent(1, "I am a character",1);
        assertEquals("I am a character", c.getDescription());
    }

    /**
     * Ensures that after being played the cost increases by one
     */
    @Test
    void play() {
        Character c=new CharacterWithStudent(1, "I am a character",1);
        c.play();
        assertEquals(2, c.getCost());
    }
}