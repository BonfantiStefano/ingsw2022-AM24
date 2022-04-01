package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.pawn.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  NOT FINAL
 */
class CharacterWithStudentTest {

    @Test
    void add() {
        CharacterWithStudent c = new CharacterWithStudent(1, "test",  4);
        c.add(new Student(ColorS.BLUE));
    }

    @Test
    void remove() {
        CharacterWithStudent c = new CharacterWithStudent(1, "test",  4);
        c.add(new Student(ColorS.BLUE));
        c.remove(new Student(ColorS.BLUE));
    }
}