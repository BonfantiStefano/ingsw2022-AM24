package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.StudentContainer;
import it.polimi.ingsw.model.mnstrategy.MNTwoSteps;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.profstrategy.EqualProf;
import it.polimi.ingsw.model.profstrategy.StandardProf;
import it.polimi.ingsw.model.world.Island;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class GameBoardTest tests GameBoard.
 *
 * @see GameBoard
 */

public class GameBoardTest {

    GameBoard gb, gb2;

    /**
     * Method init initializes values used for the tests.
     *
     * @throws InvalidIndexException if the index position of the card doesn't exist
     */
    @BeforeEach
    public void init() throws InvalidIndexException {
        gb = new GameBoard(3);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9);
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9);

        boolean r = gb.chooseAssistants(lisa, 8);
        boolean s = gb.chooseAssistants(bob, 7);
        boolean t = gb.chooseAssistants(alice, 4);

        if (r && s && t) {
            gb.addPlayer(lisa);
            gb.addPlayer(bob);
            gb.addPlayer(alice);
        }

    }

    /**
     * Method testSortedPlayer checks if all the players are correctly sorted
     */
    @Test
    public void testSortedPlayer() throws InvalidIndexException{
        ArrayList<Player> sortedPlayers;
        sortedPlayers = gb.getSortedPlayers();
        assertEquals("Alice", sortedPlayers.get(0).getNickname());
        assertEquals("Lisa", sortedPlayers.get(1).getNickname());
        assertEquals("Bob", sortedPlayers.get(2).getNickname());
        assertEquals(gb.numPlayers, sortedPlayers.size());

        gb.chooseAssistants(gb.getPlayers().get(0), 2);
        gb.chooseAssistants(gb.getPlayers().get(1), 1);
        gb.chooseAssistants(gb.getPlayers().get(2), 2);
        sortedPlayers = gb.getSortedPlayers();
        assertEquals("Bob", sortedPlayers.get(0).getNickname());
        assertEquals("Alice", sortedPlayers.get(1).getNickname());
        assertEquals("Lisa", sortedPlayers.get(2).getNickname());
        assertEquals(gb.numPlayers, sortedPlayers.size());

        gb.chooseAssistants(gb.getPlayers().get(0), 1);
        gb.chooseAssistants(gb.getPlayers().get(1), 2);
        gb.chooseAssistants(gb.getPlayers().get(2), 1);
        sortedPlayers = gb.getSortedPlayers();
        assertEquals("Lisa", sortedPlayers.get(0).getNickname());
        assertEquals("Bob", sortedPlayers.get(1).getNickname());
        assertEquals("Alice", sortedPlayers.get(2).getNickname());
        assertEquals(gb.numPlayers, sortedPlayers.size());
    }

    /**
     * Method moveMN tests the GameBoard's moveMN.
     */
    @Test
    @DisplayName("GameBoard's moveMn method test")
    void moveMN() throws InvalidMNStepsException, InvalidIndexException {
        int indexMNStart = gb.getWorld().getMNPosition();
        int numMNSteps = 4;
        gb.getPlayers().get(0).setPlaying(true);
        gb.setActivePlayer(gb.getPlayers().get(0));
        gb.chooseAssistants(gb.getPlayers().get(0), 9);
        //spostamento di Mother Nature senza dover cambiare niente
        gb.moveMN(numMNSteps);
        assertEquals(indexMNStart + numMNSteps >= gb.getWorld().getSize() ? indexMNStart + numMNSteps - gb.getWorld().getSize()
                : indexMNStart + numMNSteps, gb.getWorld().getMNPosition());
        //spostamento di madre natura in cui cambia l'owner dell'isola(e quindi le torri su di essa)
        Island islandMN = gb.getWorld().getIslandByIndex(gb.getWorld().getMNPosition());
        islandMN.add(ColorS.GREEN);
        islandMN.add(ColorS.GREEN);
        islandMN.add(ColorS.GREEN);
        gb.getProfs().put(ColorS.GREEN, gb.getPlayers().get(0));
        int oldMNPos = gb.getWorld().getMNPosition();
        gb.moveMN(0);
        assertEquals(oldMNPos, gb.getWorld().getMNPosition());
        assertEquals(Optional.of(gb.getPlayers().get(0).getColorTower()), islandMN.getTowerColor());
        assertEquals(1, islandMN.getNumSubIsland());
        //spostamento in cui si uniscono le isole
        int nextMNPos = oldMNPos + 1 < gb.getWorld().getSize() ? oldMNPos + 1 : 0;
        Island nextMNIsland = gb.getWorld().getIslandByIndex(nextMNPos);
        nextMNIsland.add(ColorS.GREEN);
        nextMNIsland.add(ColorS.GREEN);
        nextMNIsland.add(ColorS.GREEN);
        gb.moveMN(1);
        assertEquals(Math.min(oldMNPos, nextMNPos), gb.getWorld().getMNPosition());
        assertEquals(Optional.of(gb.getPlayers().get(0).getColorTower()), islandMN.getTowerColor());
        assertEquals(2, gb.getWorld().getIslandByIndex(gb.getWorld().getMNPosition()).getNumSubIsland());
    }

    /**
     * Method moveMNException checks the correct throwing of the InvalidMNStepsException
     *
     * @throws InvalidIndexException if the index position of the card doesn't exist
     */
    @Test
    void moveMNException() throws InvalidIndexException {
        gb.setActivePlayer(gb.getPlayers().get(0));
        gb.chooseAssistants(gb.getPlayers().get(0), 8);
        assertThrows(InvalidMNStepsException.class, () ->
            gb.moveMN(6)
        );
        gb.getPlayers().get(0).setStrategy(new MNTwoSteps());
        assertThrows(InvalidMNStepsException.class, () ->
            gb.moveMN(8)
        );
        int indexMNStart = gb.getWorld().getMNPosition();
        try {
            gb.moveMN(6);
            assertEquals((indexMNStart + 6) % 12, gb.getWorld().getMNPosition());
        } catch (InvalidMNStepsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method testFirstPlayer_testNextPlayer checks if the first player and the following next ones are correctly
     * selected in the list of all the players
     */
    @Test
    public void testFirstPlayer_testNextPlayer() {
        int firstPlayer = gb.getFirstPlayer();
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Alice");
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Lisa");
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Bob");
    }

    /**
     * Method testChooseAssistants tests the choice procedure of an Assistant card
     *
     * @throws InvalidIndexException if the index position of the card doesn't exist
     */
    @Test
    public void testChooseAssistants() throws InvalidIndexException {
        gb = new GameBoard(3);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9);
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9);
        int bobCards = bob.getNumCards();
        assertTrue(gb.chooseAssistants(bob, 5));
        assertEquals(bob.getNumCards(), bobCards - 1);
        assertEquals(bob.getLastAssistant().getTurn(), 5);
        int lisaCards = lisa.getNumCards();
        assertTrue(!gb.chooseAssistants(lisa, 5));
        assertEquals(lisa.getNumCards(), lisaCards);
        assertTrue(gb.chooseAssistants(lisa, 1));
        assertEquals(lisa.getNumCards(), lisaCards - 1);
        // at this point lisa played card 1 and bob card 5
        for (int i = 1; i <= 10 && i != 1 && i != 5; i++) {
            assertTrue(gb.chooseAssistants(alice, i));
        }
        //alice played all her cards except for card 1 and 5 (already player by other two players)
        //now alice tries to play card 1 or card 5
        assertTrue(gb.chooseAssistants(alice, 1));
        assertTrue(gb.chooseAssistants(alice, 5));
    }

    /**
     * Method testChooseAssistantsNewRound checks if when a new round starts the list of Assistant cards is empty
     */
    @Test
    public void testChooseAssistantsNewRound() throws InvalidIndexException {
        gb = new GameBoard(3);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9);
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9);

        assertTrue(gb.chooseAssistants(lisa, 1));
        assertTrue(gb.chooseAssistants(bob, 2));
        assertTrue(gb.chooseAssistants(alice, 3));
        assertEquals(gb.getSizeList(), 3);
        assertTrue(gb.chooseAssistants(lisa, 4));
        assertEquals(gb.getSizeList(), 1);
    }

    @Test
    public void testChosenCardException() throws  InvalidIndexException {
        gb = new GameBoard(3);
        gb.addPlayer("Lisa", ColorT.BLACK, Mage.MAGE1);
        Player lisa = gb.getPlayerByNickname("Lisa");
        assertTrue(gb.chooseAssistants(lisa, 3));
        assertEquals(9, lisa.getNumCards());
        assertThrows(InvalidIndexException.class,
                () -> gb.chooseAssistants(lisa, 18));
        assertThrows(InvalidIndexException.class,
                () -> gb.chooseAssistants(lisa, 10));
    }

    /**
     * Method testAddPlayer tests the addition of different players in the game and checks the correctness of the number
     * of pawns in their SchoolBoards
     */
    @Test
    public void testAddPlayer() {
        gb = new GameBoard(3);
        gb.addPlayer("Bob", ColorT.GREY, Mage.MAGE1);
        gb.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        gb.addPlayer("Alice", ColorT.BLACK, Mage.MAGE3);
        assertEquals(6, gb.getPlayers().get(0).getMyBoard().getTowers().size());
        assertEquals(9, gb.getPlayers().get(0).getMyBoard().getEntrance().size());

        gb2 = new GameBoard(2);
        gb2.addPlayer("Bob", ColorT.GREY, Mage.MAGE1);
        gb2.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        assertEquals(gb2.getPlayers().get(1).getMyBoard().getTowers().size(), 8);
        assertEquals(gb2.getPlayers().get(1).getMyBoard().getEntrance().size(), 7);
    }

    /**
     * Method testGetPlayerByNickname checks if a player is correctly selected by his nickname
     */
    @Test
    public void testGetPlayerByNickname() {
        Player p = gb.getPlayerByNickname("BOB");
        assertEquals(p.getNickname(), "Bob");
        assertNull(gb.getPlayerByNickname("test"));
    }

    /**
     * Method testActivePlayer tests if the chosen player is correctly set as the Active player
     */
    @Test
    public void TestActivePlayer() throws EmptyPlaceException {
        gb = new GameBoard(2);
        gb.addPlayer("Bob", ColorT.GREY, Mage.MAGE1);
        gb.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        gb.setActivePlayer(gb.getPlayerByNickname("Bob"));
        assertEquals(gb.getActivePlayer().getNickname(), "Bob");
        assertTrue(gb.getPlayerByNickname("Bob").isPlaying());

    }

    /**
     * Method moveTower checks the correctness of the tower's position when it's moved from the player's SchoolBoard
     * to an island and the other way
     */
    @Test
    public void moveTower() {
        gb = new GameBoard(2);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 8);
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 8);
        gb.addPlayer(lisa);
        gb.addPlayer(bob);
        gb.moveTower(lisa.getColorTower(), lisa.getMyBoard(), gb.getWorld().getIslandByIndex(5));
        assertEquals(gb.getWorld().getIslandByIndex(5).getTowerColor(), (Optional.of(lisa.getColorTower())));
        int towers = bob.getMyBoard().getTowers().size();
        gb.moveTower(bob.getColorTower(), gb.getWorld().getIslandByIndex(6), bob.getMyBoard());
        assertEquals(bob.getMyBoard().getTowers().size(), towers + 1);
    }

    /**
     * Method strategyProf tests the setting, resetting and getting of the strategy.
     */
    @Test
    @DisplayName("GameBoard's prof strategy tests")
    void strategyProf() {
        assertTrue(gb.getStrategy() instanceof StandardProf);
        gb.setStrategy(new EqualProf());
        assertTrue(gb.getStrategy() instanceof EqualProf);
        gb.resetStrategy();
        assertTrue(gb.getStrategy() instanceof StandardProf);
    }

    /**
     * Method conquest tests the conquest of an Island.
     */
    @Test
    @DisplayName("GameBoard's conquest test")
    void conquest() {
        Island island = gb.getWorld().getIslandByIndex((gb.getWorld().getMNPosition() + 6) % gb.getWorld().getSize());
        Player lisa = gb.getPlayers().get(0);
        //Caso conquista senza vecchio proprietario
        gb.conquest(lisa, island);
        assertEquals(1, island.getNumSubIsland());
        assertEquals(Optional.of(lisa.getColorTower()), island.getTowerColor());
        assertEquals(8, lisa.getMyBoard().getTowers().size());
        //Caso conquista con vecchio proprietario
        Player bob = gb.getPlayers().get(1);
        gb.conquest(bob, island);
        assertEquals(1, island.getNumSubIsland());
        assertEquals(Optional.of(bob.getColorTower()), island.getTowerColor());
        assertEquals(8, bob.getMyBoard().getTowers().size());
        assertEquals(9, lisa.getMyBoard().getTowers().size());
        Player alice = new Player("Alice2", ColorT.GREY, Mage.MAGE3, 1);
        gb.getPlayers().remove(2);
        gb.getPlayers().add(alice);
        Island island1 = gb.getWorld().getIslandByIndex(1);
        Island island2 = gb.getWorld().getIslandByIndex(2);
        gb.getWorld().join(island1, island2);
        island1 = gb.getWorld().getIslandByIndex(1);
        gb.conquest(alice, island1);
        assertEquals(0, alice.getMyBoard().getTowers().size());
        assertEquals(Optional.of(ColorT.GREY), island1.getTowerColor());
    }

    /**
     * Method moveStudent tests the move of a Student.
     */
    @Test
    @DisplayName("GameBoard's move test")
    void moveStudent() throws EmptyPlaceException, NoSuchStudentException {
        Player lisa = gb.getPlayers().get(0);
        lisa.getMyBoard().add(ColorS.GREEN);
        int initialSIze = lisa.getMyBoard().getEntrance().size();
        Island island = gb.getWorld().getIslandByIndex(gb.getWorld().getMNPosition());
        gb.moveStudent(ColorS.GREEN, lisa.getMyBoard(), island);
        assertEquals(0, lisa.getMyBoard().getEntrance().size());
        assertEquals(1, island.getNumStudentByColor(ColorS.GREEN));
    }

    /**
     * Tests if Clouds are filled correctly
     */
    @Test
    @DisplayName("GameBoard's newClouds test")
    void newClouds() throws EmptyPlaceException {
        gb.newClouds();
        for (int i = 0; i < gb.getNumPlayers(); i++)
            assertEquals(4, gb.getCloudByIndex(0).getStudents().size());
    }

    /**
     * Tests if Strategies are changed correctly
     */
    @Test
    @DisplayName("GameBoard's Strategy test")
    void strategyTest() {
        //check if the GameBoard is created with the standard strategy
        assertTrue(gb.getStrategy() instanceof StandardProf);
        gb.setStrategy(new EqualProf());
        //check if the Strategy has changed
        assertTrue(gb.getStrategy() instanceof EqualProf);
        gb.resetStrategy();
        //check if the Strategy has been reset
        assertTrue(gb.getStrategy() instanceof StandardProf);
    }

    /**
     * Tests if the GameBoard correctly evaluates the condition to end the game when all Students have been played
     */
    @Test
    void gameMustEndTestStudents() throws EmptyPlaceException {
        StudentContainer temp = gb.getContainer();
        //draw all Students
        for (int i = 0; i < 120; i++) {
            temp.draw();
        }
        gb.newClouds();
        assertTrue(gb.checkGameMustEnd());
    }

    /**
     * Tests if the GameBoard correctly evaluates the condition to end the game when all Assistants have been played
     */
    @Test
    void gameMustEndTestAssistants() throws InvalidIndexException, EmptyPlaceException {
        gb2 = new GameBoard(3);
        gb2.addPlayer("test", ColorT.WHITE, Mage.MAGE2);
        for (int i = 1; i <= 10; i++) {
            gb2.chooseAssistants(gb2.getPlayerByNickname("test"), 1);
        }
        assertTrue(gb2.checkGameMustEnd());
    }

    /**
     * Method checkWinIsland tests the calculation of the winner when there are 3 Island.
     */
    @Test
    @DisplayName("case with 3 island remaining")
    void checkWinIsland() {
        for (int i = 0; i < 5; i++) {
            gb.getWorld().getIslandByIndex(i).add(ColorT.GREY);
            gb.getPlayers().get(2).getMyBoard().remove(ColorT.GREY);
        }
        for (int i = 5; i < 10; i++) {
            gb.getWorld().getIslandByIndex(i).add(ColorT.BLACK);
            gb.getPlayers().get(0).getMyBoard().remove(ColorT.BLACK);
        }
        gb.getWorld().checkJoin(gb.getWorld().getIslandByIndex(0));
        gb.getWorld().checkJoin(gb.getWorld().getIslandByIndex(1));
        assertEquals(Optional.empty(), gb.checkWin());
        gb.getWorld().getIslandByIndex(2).add(ColorT.BLACK);
        gb.getPlayers().get(0).getMyBoard().remove(ColorT.BLACK);
        gb.getWorld().checkJoin(gb.getWorld().getIslandByIndex(1));
        assertEquals(Optional.of(gb.getPlayers().get(0)), gb.checkWin());
    }

    /**
     * Method checkDrawIsland tests the calculation of the winner when there are 3 Island, in particular game must end with a draw.
     */
    @Test
    @DisplayName("draw with 3 island remaining")
    void checkDrawIsland(){
        for (int i = 0; i < 5; i++) {
            gb.getWorld().getIslandByIndex(i).add(ColorT.GREY);
            gb.getPlayers().get(2).getMyBoard().remove(ColorT.GREY);
        }
        for (int i = 5; i < 10; i++) {
            gb.getWorld().getIslandByIndex(i).add(ColorT.BLACK);
            gb.getPlayers().get(0).getMyBoard().remove(ColorT.BLACK);
        }
        gb.getWorld().checkJoin(gb.getWorld().getIslandByIndex(0));
        gb.getWorld().checkJoin(gb.getWorld().getIslandByIndex(1));
        assertEquals(Optional.empty(), gb.checkWin());
        gb.getWorld().getIslandByIndex(2).add(ColorT.WHITE);
        gb.getPlayers().get(1).getMyBoard().remove(ColorT.WHITE);
        gb.getWorld().getIslandByIndex(3).add(ColorT.WHITE);
        gb.getPlayers().get(1).getMyBoard().remove(ColorT.WHITE);
        gb.getWorld().checkJoin(gb.getWorld().getIslandByIndex(2));
        assertEquals(Optional.empty(), gb.checkWin());
    }

    /**
     * Method checkWinTower tests the calculation of the winner when a Player finishes his towers.
     */
    @Test
    @DisplayName("case with a player without tower")
    void checkWinTower() {
        for (int i = 0; i < 8; i++) {
            gb.getPlayers().get(0).getMyBoard().remove(ColorT.BLACK);
        }
        assertEquals(Optional.empty(), gb.checkWin());
        gb.getPlayers().get(0).getMyBoard().remove(ColorT.BLACK);
        assertEquals(Optional.of(gb.getPlayers().get(0)), gb.checkWin());
    }

    /**
     * Method checkWinStudentCard tests the calculation of the winner when the bag is empty or when the Assistants are finished.
     */
    @Test
    @DisplayName("case when gameMustEnd is true")
    void checkWinStudentCard() throws EmptyPlaceException {
        StudentContainer temp = gb.getContainer();
        for (int i = 0; i < 120; i++) {
            temp.draw();
        }
        assertEquals(Optional.empty(), gb.checkWin());
        gb.newClouds();
        gb.getProfs().put(ColorS.BLUE, gb.getPlayers().get(1));
        gb.getProfs().put(ColorS.RED, gb.getPlayers().get(1));
        gb.getProfs().put(ColorS.GREEN, gb.getPlayers().get(0));
        assertEquals(Optional.of(gb.getPlayers().get(1)), gb.checkWin());
    }

    /**
     * Method checkProfs tests the changing of the profs' owner.
     */
    @Test
    void checkProfs() {
        int i = 1;
        for (Player p : gb.players) {
            for (ColorS c : ColorS.values()) {
                p.getMyBoard().getHall().put(c, i);
            }
            i++;
        }
        for (ColorS c : ColorS.values()) {
            gb.getProfs().put(c, gb.getPlayers().get(0));
        }
        gb.checkProfs();
        for (ColorS c : ColorS.values()) {
            assertEquals("Alice", gb.getProfs().get(c).getNickname());
        }
    }
}
