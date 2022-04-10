package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ColorS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Class CharacterWithStudentTest tests CharacterWithStudent class
 */
class CharacterWithStudentTest {

    /**
     * Ensures that the method add() adds a Student to the list
     */
    @Test
    void add() {
        CharacterWithStudent c = new CharacterWithStudent(1, "test",  4);
        c.add(ColorS.BLUE);
        assertTrue(c.getStudents().contains(ColorS.BLUE));
    }

    /**
     * Ensures that the method remove() removes a Student from the list
     */
    @Test
    void remove() {
        CharacterWithStudent c = new CharacterWithStudent(1, "test",  4);
        c.add(ColorS.BLUE);
        c.remove(ColorS.BLUE);
        assertFalse(c.getStudents().contains(ColorS.BLUE));
    }
}