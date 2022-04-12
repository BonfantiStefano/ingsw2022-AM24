package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.EmptyPlaceException;
import it.polimi.ingsw.exceptions.NoSuchStudentException;
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
    void entranceToHall() throws PlaceFullException, EmptyPlaceException {
        schoolBoard.entranceToHall(ColorS.BLUE);
        assertEquals(1,schoolBoard.getEntrance().size());
        assertEquals(2,schoolBoard.getHall().get(ColorS.BLUE));
    }

    /**
     * Method removeHall tests the removal of a Student from the Hall.
     */
    @Test
    void removeHall() throws EmptyPlaceException {
        schoolBoard.removeHall(ColorS.BLUE);
        assertEquals(0, schoolBoard.getHall(ColorS.BLUE));
    }

    /**
     * Method hallToEntrance tests the move of a Student from the Hall to the Entrance.
     */
    @Test
    void hallToEntrance() throws EmptyPlaceException {
        schoolBoard.hallToEntrance(ColorS.BLUE);
        assertEquals(3,schoolBoard.getEntrance().size());
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
    void removeStudent() throws EmptyPlaceException, NoSuchStudentException {
        schoolBoard.remove(ColorS.GREEN);
        assertEquals(1, schoolBoard.getEntrance().size());
        assertThrows(NoSuchStudentException.class, () -> schoolBoard.remove(ColorS.GREEN));
    }

    /**
     * Method addRemoveTower tests the addition and the removal of a Tower from the List of towers.
     */
    @Test
    void addRemoveTower() throws EmptyPlaceException {
        assertEquals(8, schoolBoard.getTowers().size());
        schoolBoard.remove(ColorT.WHITE);
        assertEquals(7, schoolBoard.getTowers().size());
        schoolBoard.add(ColorT.WHITE);
        assertEquals(8, schoolBoard.getTowers().size());
    }

    /** Method testFullHallException checks if entranceToHall and addHall methods are capable of throwing
     * PlaceFullException when the hall is full
     */
    @Test
    void testFullHallException() throws PlaceFullException, EmptyPlaceException {
        SchoolBoard board = new SchoolBoard(ColorT.WHITE, 8);
        for(int i = 0; i < 7; i++)
            board.add(ColorS.YELLOW);
        for(int i = 0; i < 6; i++)
            board.entranceToHall(ColorS.YELLOW);
        for(int i = 0; i < 4; i++)
            board.addToHall(ColorS.YELLOW);
        assertThrows(PlaceFullException.class,
                () -> board.entranceToHall(ColorS.YELLOW));
        assertThrows(PlaceFullException.class,
                () -> board.addToHall(ColorS.YELLOW));

    }

    /**
     * Method testEmptyEntrance checks if entranceToHall and remove methods are capable of throwing EmptyPlaceException
     * when the entrance is empty
     */
    @Test
    void testEmptyEntrance(){
        SchoolBoard board = new SchoolBoard(ColorT.WHITE, 8);
        assertEquals(board.getEntrance().size(), 0);
        assertThrows(EmptyPlaceException.class,
                () -> board.entranceToHall(ColorS.GREEN));
        assertThrows(EmptyPlaceException.class,
                () -> board.remove(ColorS.GREEN));
    }

    /**
     * Method testEmptyHall checks if removeHall and hallToEntrance methods are capable of throwing EmptyPlaceException
     * when the hall is empty
     */
    @Test
    void testEmptyHall(){
        SchoolBoard board = new SchoolBoard(ColorT.WHITE, 8);
        assertEquals(board.getHall(ColorS.BLUE), 0);
        assertThrows(EmptyPlaceException.class,
                () -> board.removeHall(ColorS.BLUE));
        assertThrows(EmptyPlaceException.class,
                () -> board.hallToEntrance(ColorS.BLUE));

    }

    /**
     * Method testNoTowers checks if remove method is capable of throwing EmptyPlaceException
     * when there is no towers in SchoolBoard
     */
    @Test
    void testNoTowers() throws EmptyPlaceException {
        SchoolBoard board = new SchoolBoard(ColorT.WHITE, 8);
        assertEquals(board.getTowers().size(), 8);
        for(int i = 0; i < 8; i++)
            board.remove(ColorT.WHITE);
        assertThrows(EmptyPlaceException.class,
                () -> board.remove(ColorT.WHITE));
    }

}