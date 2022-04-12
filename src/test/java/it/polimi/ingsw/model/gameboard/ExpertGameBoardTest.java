package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterWithNoEntry;
import it.polimi.ingsw.model.character.CharacterWithStudent;
import it.polimi.ingsw.model.mnstrategy.MNTwoSteps;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.Island;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.assertThat;
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
     * Tests MN's movement
     */
    @Test
    void moveMN() throws InvalidIndexException, InvalidMNStepsException {
        int indexMNStart = gb.getWorld().getMNPosition();
        int numMNSteps = 4;
        int oldMNPos = gb.getWorld().getMNPosition();
        gb.getPlayers().get(0).setPlaying(true);
        gb.setActivePlayer(gb.getPlayers().get(0));
        gb.chooseAssistants(gb.getPlayers().get(0), 9);
        Island islandMN = gb.getWorld().getIslandByIndex(gb.getWorld().getMNPosition());
        //MN moves on an Island with a NoEntry tile
        islandMN.setNumNoEntry(1);
        gb.moveMN(0);
        assertEquals(oldMNPos, gb.getWorld().getMNPosition());
        assertEquals(0, gb.getWorld().getIslandByIndex(oldMNPos).getNumNoEntry());
        //normal MN movement
        gb.moveMN(numMNSteps);
        assertEquals(indexMNStart + numMNSteps >= gb.getWorld().getSize() ? indexMNStart + numMNSteps - gb.getWorld().getSize()
                : indexMNStart + numMNSteps, gb.getWorld().getMNPosition());
    }
    /**
     * Method moveMNException checks the correct throwing of the InvalidMNStepsException
     * @throws InvalidIndexException if the index position of the card doesn't exist
     */
    @Test
    void moveMNException() throws InvalidIndexException {
        gb.setActivePlayer(gb.getPlayers().get(0));
        gb.chooseAssistants(gb.getPlayers().get(0), 8);
        assertThrows(InvalidMNStepsException.class, () -> {
            gb.moveMN(6);
        });
        gb.getPlayers().get(0).setStrategy(new MNTwoSteps());
    }
    /**
     * Adds 3 Blue Students to the Player's Hall and ensures they are present
     */
    @Test
    void entranceToHall() throws PlaceFullException, EmptyPlaceException {
        gb.entranceToHall(ColorS.BLUE);
        gb.entranceToHall(ColorS.BLUE);
        gb.entranceToHall(ColorS.BLUE);
        assertEquals(gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE), 3);
    }

    /**
     * Adds a Student to the Player's Hall, and ensures it has moved to the Entrance
     */
    @Test
    void hallToEntrance() throws PlaceFullException, EmptyPlaceException {
        gb.entranceToHall(ColorS.BLUE);
        gb.hallToEntrance(ColorS.BLUE);
        assertEquals(gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE), 0);
    }

    /**
     * Ensures that after adding directly to the Player's Hall the number of Students is higher
     */
    @Test
    void addToHall() throws PlaceFullException {
        int before=gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE);
        gb.addToHall(ColorS.BLUE);
        gb.addToHall(ColorS.BLUE);
        gb.addToHall(ColorS.BLUE);
        int after=gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE);
        assertEquals(before +3, after);
        //Ensure that the Player earned a coin
        assertEquals(2 ,gb.getActivePlayer().getCoins());
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
        Character c = gb.getCharacters().get(0);
        gb.getActivePlayer().setCoins(15);
        gb.playCharacter(c);
        assertEquals(gb.getActiveCharacter(), c);
        gb.getActivePlayer().setCoins(-15);
        assertThrows(NotEnoughCoinsException.class, () -> gb.playCharacter(c));
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
        Island island1 = gb.getWorld().getIslandByIndex(2);
        island1.add(ColorS.BLUE);
        island1.add(ColorS.BLUE);
        gb.checkIsland(island1);
        assertEquals(11, gb.getWorld().getSize());
        assertEquals(Optional.of(gb.getPlayers().get(1).getColorTower()), gb.getWorld().getIslandByIndex(1).getTowerColor());
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

    /**
     * Tests if CharacterWithStudent gets refilled with a Student
     */
    @Test
    void resetCharacterStudent() throws EmptyPlaceException {
        gb.setActiveCharacter(new CharacterWithNoEntry(1, "ciao"));
        assertThrows(ClassCastException.class,() -> gb.resetCharacterStudent());

        gb.setActiveCharacter(new CharacterWithStudent(1,"ciao", 4));
        gb.resetCharacterStudent();
    }

    /**
     * Method testRemoveHall checks the effect of the Character card that removes students from the hall
     */
    @Test
    void testRemoveHall() throws PlaceFullException {
        gb = new ExpertGameBoard(2);
        gb.addPlayer("Lisa", ColorT.BLACK, Mage.MAGE1);
        gb.addPlayer("Bob", ColorT.WHITE, Mage.MAGE2);

        gb.setActivePlayer(gb.getPlayerByNickname("Bob"));
        gb.addToHall(ColorS.GREEN);
        gb.addToHall(ColorS.GREEN);
        gb.addToHall(ColorS.GREEN);
        gb.addToHall(ColorS.BLUE);

        gb.setActivePlayer(gb.getPlayerByNickname("Lisa"));
        gb.addToHall(ColorS.GREEN);
        gb.addToHall(ColorS.YELLOW);
        gb.addToHall(ColorS.RED);

        gb.removeHall(ColorS.GREEN);

        assertEquals(gb.getPlayerByNickname("Bob").getMyBoard().getHall(ColorS.GREEN), 0);
        assertEquals(gb.getPlayerByNickname("Bob").getMyBoard().getHall(ColorS.BLUE), 1);

        assertEquals(gb.getPlayerByNickname("Lisa").getMyBoard().getHall(ColorS.GREEN), 0);
        assertEquals(gb.getPlayerByNickname("Lisa").getMyBoard().getHall(ColorS.YELLOW), 1);
        assertEquals(gb.getPlayerByNickname("Lisa").getMyBoard().getHall(ColorS.RED), 1);
    }

    /**
     * Method testSwitchStudents tests switchStudents method
     */
    @Test
    public void testSwitchStudents() throws NoSuchStudentException, PlaceFullException, EmptyPlaceException {
        gb = new ExpertGameBoard(2);
        gb.addPlayer("Lisa", ColorT.BLACK, Mage.MAGE1);
        gb.setActivePlayer(gb.getPlayerByNickname("Lisa"));
        gb.addToHall(ColorS.YELLOW);
        gb.addToHall(ColorS.RED);
        gb.getActivePlayer().getMyBoard().add(ColorS.GREEN);
        gb.getActivePlayer().getMyBoard().add(ColorS.YELLOW);

        gb.switchStudents(ColorS.RED, ColorS.YELLOW);
        assertEquals(gb.getPlayerByNickname("Lisa").getMyBoard().getHall(ColorS.YELLOW), 2);
    }

    /**
     * Method testExceptions checks the correct throwing of PlaceFullException and EmptyPlaceException
     * when switchStudents method is used
     */
    @Test
    public void testExceptionSwitch() throws PlaceFullException {
        gb = new ExpertGameBoard(2);
        gb.addPlayer("Lisa", ColorT.BLACK, Mage.MAGE1);
        Player lisa = gb.getPlayerByNickname("Lisa");
        gb.setActivePlayer(lisa);
        gb.addToHall(ColorS.YELLOW);
        gb.addToHall(ColorS.RED);
        for(int i = 0; i < 10; i++)
            gb.addToHall(ColorS.BLUE);

        int size = lisa.getMyBoard().getEntrance().size();
        ArrayList<ColorS> entrance= new ArrayList<>();
        for (int i = 0; i < size; i++)
            entrance.add(lisa.getMyBoard().getEntrance().get(i));
        lisa.getMyBoard().getEntrance().removeAll(entrance);
        assertTrue(lisa.getMyBoard().getEntrance().isEmpty());
        // Hall: 1 red, 1 yellow, 10 blue
        // Entrance : empty

        assertThrows(PlaceFullException.class,
                () -> gb.switchStudents(ColorS.GREEN, ColorS.BLUE));

        assertTrue(lisa.getMyBoard().getEntrance().isEmpty());
        //assertThrows(EmptyPlaceException.class,
        //        () -> gb.switchStudents(ColorS.GREEN, ColorS.BLUE));

        assertEquals(10, lisa.getMyBoard().getHall(ColorS.BLUE));
        assertEquals(0, lisa.getMyBoard().getEntrance().size());
    }

    /**
     * Method testExceptionAddToHall checks the correct throwing of PlaceFullException
     * when addToHall method is used
     */
    @Test
    public void testExceptionAddToHall() throws PlaceFullException {
        gb = new ExpertGameBoard(2);
        gb.addPlayer("Lisa", ColorT.BLACK, Mage.MAGE1);
        Player lisa = gb.getPlayerByNickname("Lisa");
        gb.setActivePlayer(lisa);
        for (int i = 0; i < 10; i++)
            gb.addToHall(ColorS.YELLOW);

        assertThrows(PlaceFullException.class,
                () -> lisa.getMyBoard().addToHall(ColorS.YELLOW));
    }

    /**
     * Method testExceptionRemoveFromEntrance checks the correct throwing of EmptyPlaceException
     * when remove method is used
     */
    @Test
    public void testExceptionRemoveFromEntrance() throws NoSuchStudentException{
        gb = new ExpertGameBoard(2);
        gb.addPlayer("Lisa", ColorT.BLACK, Mage.MAGE1);
        Player lisa = gb.getPlayerByNickname("Lisa");
        gb.setActivePlayer(lisa);
        int size = lisa.getMyBoard().getEntrance().size();
        ArrayList<ColorS> entrance = new ArrayList<>();
        for (int i = 0; i < size; i++)
            entrance.add(lisa.getMyBoard().getEntrance().get(i));
        lisa.getMyBoard().getEntrance().removeAll(entrance);

        assertThrows(EmptyPlaceException.class,
                ()->lisa.getMyBoard().remove(ColorS.RED) );
    }

}

