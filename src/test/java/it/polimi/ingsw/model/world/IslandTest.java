package it.polimi.ingsw.model.world;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.pawn.Tower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class IslandTest tests Island class.
 *
 * @author Bonfanti Stefano
 * @see Island
 */
class IslandTest {
    Island island;

    /** Method setup creates an island that will be used by every test.*/
    @BeforeEach
    void setup() {
        island = new Island();
    }

    /** Method addStudent checks the addition of a single Student on the Island.*/
    @Test
    @DisplayName("Add Student test")
    void addStudent() {
        Student s1 = new Student(ColorS.GREEN);
        island.add(s1);
        assertTrue(island.getNumStudentByColor(ColorS.GREEN) == 1 &&
                island.getNumStudentByColor(ColorS.RED) == 0 &&
                island.getNumStudentByColor(ColorS.PINK) == 0 &&
                island.getNumStudentByColor(ColorS.BLUE) == 0 &&
                island.getNumStudentByColor(ColorS.YELLOW) == 0);
        Student s2 = new Student(ColorS.YELLOW);
        island.add(s2);
        assertTrue(island.getNumStudentByColor(ColorS.GREEN) == 1 &&
                island.getNumStudentByColor(ColorS.RED) == 0 &&
                island.getNumStudentByColor(ColorS.PINK) == 0 &&
                island.getNumStudentByColor(ColorS.BLUE) == 0 &&
                island.getNumStudentByColor(ColorS.YELLOW) == 1);
    }

    /** Method addTower checks the addition of a single Tower on the Island.*/
    @Test
    @DisplayName("Add Tower test")
    void addTower() {
        Tower t1 = new Tower(ColorT.WHITE);
        island.add(t1);
        assertEquals(1, island.getNumSubIsland());
        assertEquals(Optional.of(ColorT.WHITE), island.getTowerColor());
    }

    /** Method removeTower checks the subtraction of a single Tower on the Island.*/
    @Test
    @DisplayName("Remove Tower test")
    void removeTower() {
        Tower t1 = new Tower(ColorT.WHITE);
        Tower t2 = new Tower(ColorT.WHITE);
        island.add(t1);
        island.add(t2);
        island.remove(t1);
        assertEquals(1, island.getNumSubIsland());
        assertEquals(Optional.of(ColorT.WHITE), island.getTowerColor());
        island.remove(t2);
        assertEquals(1, island.getNumSubIsland());
        assertEquals(null, island.getTowerColor());

    }

    /** Method getTowerColor tests the color of the Tower's getter.*/
    @Test
    @DisplayName("Tower's color getter test")
    void getTowerColor() {
        assertEquals(null, island.getTowerColor());
        Tower t1 = new Tower(ColorT.BLACK);
        island.add(t1);
        assertEquals(Optional.of(ColorT.BLACK), island.getTowerColor());
        island.remove(t1);
        Tower t2 = new Tower(ColorT.GREY);
        island.add(t2);
        assertEquals(Optional.of(ColorT.GREY), island.getTowerColor());
        island.remove(t2);
        Tower t3 = new Tower(ColorT.WHITE);
        island.add(t3);
        assertEquals(Optional.of(ColorT.WHITE), island.getTowerColor());
    }

    /** Method getNumSubIsland checks the number of the Island's getter.*/
    @Test
    @DisplayName("Island's number getter test")
    void getNumSubIsland() {
        Island island2 = new Island();
        Island islandJoin = new Island(island, island2);
        assertEquals(2, islandJoin.getNumSubIsland());
        Island island3 = new Island();
        Island islandJoin2 = new Island(islandJoin, island3);
        assertEquals(3, islandJoin2.getNumSubIsland());
    }

    /** Method numNoEntryTest checks both getter and setter of numNoEntry attribute.*/
    @Test
    @DisplayName("NoNumber's number getter and setter test")
    void numNoEntryTest() {
        assertEquals(0, island.getNumNoEntry());
        island.setNumNoEntry(1);
        assertEquals(1, island.getNumNoEntry());
        island.setNumNoEntry(2);
        assertEquals(3, island.getNumNoEntry());
        island.setNumNoEntry(-1);
        assertEquals(2, island.getNumNoEntry());
    }

    /** Method getNumStudentByColor checks the getter of the Student's number for each Color.*/
    @Test
    @DisplayName("Stundent's number by color getter test")
    void getNumStudentByColor() {
        Student s1 = new Student(ColorS.GREEN);
        island.add(s1);
        Student s2 = new Student(ColorS.GREEN);
        island.add(s2);
        Student s3 = new Student(ColorS.RED);
        island.add(s3);
        Student s4 = new Student(ColorS.PINK);
        island.add(s4);
        Student s5 = new Student(ColorS.BLUE);
        island.add(s5);
        Student s6 = new Student(ColorS.YELLOW);
        island.add(s6);
        Student s7 = new Student(ColorS.YELLOW);
        island.add(s7);
        assertEquals(2, island.getNumStudentByColor(ColorS.GREEN));
        assertEquals(1, island.getNumStudentByColor(ColorS.RED));
        assertEquals(1, island.getNumStudentByColor(ColorS.PINK));
        assertEquals(1, island.getNumStudentByColor(ColorS.BLUE));
        assertEquals(2, island.getNumStudentByColor(ColorS.YELLOW));
    }
}