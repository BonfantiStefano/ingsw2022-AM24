package it.polimi.ingsw.model.character;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Since Character is an abstract class the tests are conducted with objects from a subclass
 */
class CharacterTest {

    @Test
    void getCost() {
        Character c=new CharacterWithStudent(1, "I am a character");
        assertEquals(1, c.getCost());
    }

    @Test
    void getDescription() {
        Character c=new CharacterWithStudent(1, "I am a character");
        assertEquals("I am a character", c.getDescription());
    }

    @Test
    void play() {
        Character c=new CharacterWithStudent(1, "I am a character");
        c.play();
        assertEquals(2, c.getCost());
    }
}