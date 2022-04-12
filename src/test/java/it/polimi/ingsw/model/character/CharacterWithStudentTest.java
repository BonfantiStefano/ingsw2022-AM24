package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.model.ColorS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Class CharacterWithStudentTest tests CharacterWithStudent class
 */
class CharacterWithStudentTest {
    CharacterWithStudent c;
    @BeforeEach
    void init(){
        c = new CharacterWithStudent(1, "test",  4);
        c.add(ColorS.BLUE);
    }
    /**
     * Ensures that the method add() adds a Student to the list
     */
    @Test
    void add() {
        assertTrue(c.getStudents().contains(ColorS.BLUE));
    }

    /**
     * Ensures that the method remove() removes a Student from the list
     */
    @Test
    void remove() throws NoSuchStudentException {
        c.remove(ColorS.BLUE);
        assertFalse(c.getStudents().contains(ColorS.BLUE));
    }

    /**
     * Ensures that an exception is thrown when trying to remove a Student not present in the Character
     */
    @Test
    void removeException(){
        try {
            c.remove(ColorS.BLUE);
        }
        catch (NoSuchStudentException ignored){}
        assertThrows(NoSuchStudentException.class,() -> c.remove(ColorS.BLUE));
    }
}