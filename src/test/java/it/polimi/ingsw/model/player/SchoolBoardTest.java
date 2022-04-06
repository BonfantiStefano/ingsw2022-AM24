package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.world.Island;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class SchoolBoardTest tests SchoolBoard class.
 *
 * @author Bonfanti Stefano
 * @see SchoolBoard
 */
class SchoolBoardTest {

    SchoolBoard schoolBoard;

    /** Method setup creates a SchoolBoard that will be used by every test.*/
    @BeforeEach
    void setup() {
        schoolBoard = new SchoolBoard(ColorT.WHITE, 8);
        schoolBoard.getEntrance().add(ColorS.BLUE);
        schoolBoard.getEntrance().add(ColorS.GREEN);
        schoolBoard.getHall().put(ColorS.BLUE, 1);
    }

    /**
     * Method entranceToHall tests the move of a Student from the entrance to the hall.
     */
    @Test
    void entranceToHall() {
        schoolBoard.entranceToHall(ColorS.BLUE);
        assertEquals(1,schoolBoard.getEntrance().size());
        assertEquals(2,schoolBoard.getHall().get(ColorS.BLUE));
    }

    /**
     * Method removeHall tests the removal of a Student from the Hall.
     */
    @Test
    void removeHall() {
        schoolBoard.removeHall(ColorS.BLUE);
        assertEquals(0, schoolBoard.getHall(ColorS.BLUE));
    }

    /**
     * Method hallToEntrance tests the move of a Student from the Hall to the Entrance.
     */
    @Test
    void hallToEntrance() {
        schoolBoard.hallToEntrance(ColorS.BLUE);
        assertEquals(2,schoolBoard.getEntrance().size());
        assertEquals(0,schoolBoard.getHall().get(ColorS.BLUE));

    }

    /**
     * Method addToHall tests the addition of a Student to the Hall.
     */
    @Test
    void addToHall() {
        schoolBoard.addToHall(ColorS.RED);
        assertEquals(1, schoolBoard.getHall(ColorS.RED));
    }

    /**
     * Method addStudent tests the addition of a Student to the Entrance.
     */
    @Test
    void addStudent() {
        schoolBoard.add(ColorS.PINK);
        assertEquals(3, schoolBoard.getEntrance().size());
        schoolBoard.add(ColorS.PINK);
        assertEquals(4, schoolBoard.getEntrance().size());
    }

    /**
     * Method removeStudent tests the removal of a Student from the Entrance.
     */
    @Test
    void removeStudent() {
        schoolBoard.remove(ColorS.GREEN);
        assertEquals(1, schoolBoard.getEntrance().size());
    }

    /**
     * Method addRemoveTower tests the addition and the removal of a Tower from the List of towers.
     */
    @Test
    void addRemoveTower() {
        schoolBoard.remove(ColorT.WHITE);
        assertEquals(7, schoolBoard.getTowers().size());
        schoolBoard.add(ColorT.WHITE);
        assertEquals(8, schoolBoard.getTowers().size());
    }
}