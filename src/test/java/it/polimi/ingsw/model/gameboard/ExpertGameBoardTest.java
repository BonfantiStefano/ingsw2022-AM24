package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests ExpertGameBoard
 */
class ExpertGameBoardTest {
    private ExpertGameBoard gb;

    /**
     * Creates a new ExpertGameBoard, adds a Player and activates it
     */
    @BeforeEach
    void init(){
        gb = new ExpertGameBoard(4);
        gb.addPlayer("1", ColorT.WHITE, Mage.MAGE2);
        gb.nextPlayer();
    }

    /**
     * Adds 3 Blue Students to the Player's Hall and ensures they are present
     */
    @Test
    void entranceToHall() {
        gb.entranceToHall(ColorS.BLUE);
        gb.entranceToHall(ColorS.BLUE);
        gb.entranceToHall(ColorS.BLUE);
        assertEquals(gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE), 3);
    }

    /**
     * Adds a Student to the Player's Hall, and ensures it has moved to the Entrance
     */
    @Test
    void hallToEntrance() {
        gb.entranceToHall(ColorS.BLUE);
        gb.hallToEntrance(ColorS.BLUE);
        assertEquals(gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE), 0);
    }

    /**
     * Ensures that after adding directly to the Player's Hall the number of Students is higher
     */
    @Test
    void addToHall() {
        int before=gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE);
        gb.addToHall(ColorS.BLUE);
        int after=gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE);
        assertEquals(before +1, after);
    }

    /**
     * Ensures that if the Player has more than 3 Students of the selected Color it will lose only those 3,
     * otherwise he will lose all of them
     * */
    @Test
    void removeHall() {
        gb.getActivePlayer().getMyBoard().getHall().put(ColorS.BLUE, 10);
        gb.removeHall(ColorS.BLUE);
        assertEquals(7,gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE));
        gb.getActivePlayer().getMyBoard().getHall().put(ColorS.BLUE, 2);
        gb.removeHall(ColorS.BLUE);
        assertEquals(0, gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE));
    }

    /**
     * Ensures that the ActivePlayer can afford a Character and activates it
     */
    @Test
    void playCharacter() {
        ArrayList<Character> c = gb.getCharacters();
        gb.getActivePlayer().setCoins(15);
        gb.playCharacter(c.get(0));
        assertEquals(gb.getActiveCharacter(), c.get(0));
    }

    @Test
    void checkIsland() {
    }

    /**
     * Ensures that the ExpertGameBoard loses coins when they are given to players
     */
    @Test
    void getAvailableCoins() {
        //one player has been added so there are 20-1 coins available
        assertEquals(gb.getAvailableCoins(), 19);
        gb.getActivePlayer().setCoins(15);
        Character c = gb.getCharacters().get(0);
        gb.playCharacter(c);
        assertEquals(19+c.getCost(), gb.getAvailableCoins());
    }
}