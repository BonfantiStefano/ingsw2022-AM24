package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SchoolBoard;
import it.polimi.ingsw.model.world.Island;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class ExpertGameBoardTest tests ExpertGameBoard class.
 *
 * @see ExpertGameBoard
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
    void playCharacter() throws NotEnoughCoinsException {
        ArrayList<Character> c = gb.getCharacters();
        gb.getActivePlayer().setCoins(15);
        gb.playCharacter(c.get(0));
        assertEquals(gb.getActiveCharacter(), c.get(0));
    }

    /**
     * Method checkIslandWithNoEntry tests the effect of the Character that calculate the influence on an Island with a noEntryTiles.
     */
    @Test
    @DisplayName("NoEntryTiles on the selected Island test")
    void checkIslandWithNoEntry() {
        Island island = gb.getWorld().getIslandByIndex(1);
        island.add(ColorS.GREEN);
        island.add(ColorS.GREEN);
        gb.getProfs().put(ColorS.GREEN, gb.getPlayers().get(0));
        island.setNumNoEntry(1);
        gb.checkIsland(island);
        assertEquals(0, island.getNumNoEntry());
        assertEquals(Optional.empty(), island.getTowerColor());
    }


    /**
     * Method checkIslandWithoutNoEntry tests the effect of the Character that calculate the influence on an Island without noEntryTiles.
     */
    @Test
    @DisplayName("checkIsland on the selected Island that has noEntryTiles")
    void checkIslandWithoutNoEntry() {
        gb.addPlayer("2", ColorT.BLACK, Mage.MAGE3);
        gb.getPlayers().get(0).chooseAssistant(9);
        gb.getPlayers().get(1).chooseAssistant(10);
        Island island = gb.getWorld().getIslandByIndex(1);
        island.add(ColorS.GREEN);
        island.add(ColorS.GREEN);
        gb.getProfs().put(ColorS.GREEN, gb.getPlayers().get(0));
        gb.conquest(gb.getPlayers().get(0), island);
        island.add(ColorS.BLUE);
        island.add(ColorS.BLUE);
        island.add(ColorS.BLUE);
        island.add(ColorS.YELLOW);
        island.add(ColorS.YELLOW);
        gb.getProfs().put(ColorS.BLUE, gb.getPlayers().get(1));
        gb.getProfs().put(ColorS.YELLOW, gb.getPlayers().get(1));
        gb.checkIsland(island);
        assertEquals(0, island.getNumNoEntry());
        assertEquals(Optional.of(gb.getPlayers().get(1).getColorTower()), island.getTowerColor());
    }

    /**
     * Ensures that the ExpertGameBoard loses coins when they are given to players
     */
    @Test
    void getAvailableCoins() throws NotEnoughCoinsException {
        //one player has been added so there are 20-1 coins available
        assertEquals(gb.getAvailableCoins(), 19);
        gb.getActivePlayer().setCoins(15);
        Character c = gb.getCharacters().get(0);
        gb.playCharacter(c);
        assertEquals(19+c.getCost(), gb.getAvailableCoins());
    }
}