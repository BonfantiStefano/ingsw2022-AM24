package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.PlaceFullException;
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

    SchoolBoard schoolBoard, sb;

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
    void entranceToHall() throws PlaceFullException {
        schoolBoard.entranceToHall(ColorS.BLUE);
        assertEquals(1,schoolBoard.getEntrance().size());
        assertEquals(2,schoolBoard.getHall().get(ColorS.BLUE));
    }

    /** Method testExceptionEntraceToHall checks if entranceToHall method is capable of throwing PlaceFullException */
    @Test
    public void testExceptionEntraceToHall() throws PlaceFullException {
        sb = new SchoolBoard(ColorT.BLACK, 8);
        for(int i = 0; i < 3; i++){
            sb.addToHall(ColorS.GREEN);
        }
        for(int i = 0; i < 7; i++){
            sb.getEntrance().add(ColorS.GREEN);
            sb.entranceToHall(ColorS.GREEN);
        }
        sb.getEntrance().add(ColorS.GREEN);
        assertThrows(PlaceFullException.class,
                () -> sb.entranceToHall(ColorS.GREEN));
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
    void addToHall() throws PlaceFullException {
        schoolBoard.addToHall(ColorS.RED);
        assertEquals(1, schoolBoard.getHall(ColorS.RED));
    }

    /** Method testExceptionToHall checks if addToHall method is capable of throwing PlaceFullException */
    @Test
    public void testExceptionAddToHall() throws PlaceFullException {
        sb = new SchoolBoard(ColorT.GREY, 8);
        for(int i = 0; i < 10; i++){
            sb.addToHall(ColorS.RED);
        }
        assertThrows(PlaceFullException.class,
                () -> sb.addToHall(ColorS.RED));
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
        assertEquals(8, schoolBoard.getTowers().size());
        schoolBoard.remove(ColorT.WHITE);
        assertEquals(7, schoolBoard.getTowers().size());
        schoolBoard.add(ColorT.WHITE);
        assertEquals(8, schoolBoard.getTowers().size());
    }
}