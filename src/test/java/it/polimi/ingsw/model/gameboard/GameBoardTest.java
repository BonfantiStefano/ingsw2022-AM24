package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.StudentContainer;
import it.polimi.ingsw.model.mnstrategy.MNTwoSteps;
import it.polimi.ingsw.model.player.Assistant;
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

public class GameBoardTest {

    GameBoard gb, gb2;

    @BeforeEach
    public void initialization() throws InvalidIndexException {
        gb = new GameBoard(3);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9);
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9);

        boolean r = gb.chooseAssistants(lisa, 8);
        boolean s = gb.chooseAssistants(bob, 7);
        boolean t = gb.chooseAssistants(alice, 4);

        if (r && s && t){
            gb.addPlayer(lisa);
            gb.addPlayer(bob);
            gb.addPlayer(alice);
        }

    }

    //Test in progress
    /**
     * Method moveMN tests the GameBoard's moveMN.
     */
    @Test
    @DisplayName("GameBoard's moveMn method test")
    void moveMN() throws InvalidMNStepsException, InvalidIndexException{
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
        //spostamento in isola in cui vi è una tessera divieto
        islandMN.setNumNoEntry(1);
        gb.moveMN(0);
        assertEquals(oldMNPos, gb.getWorld().getMNPosition());
        assertEquals(0, gb.getWorld().getIslandByIndex(oldMNPos).getNumNoEntry());
        //spostamento in cui si uniscono le isole
        int nextMNPos = oldMNPos+1 < gb.getWorld().getSize() ? oldMNPos+1 : 0;
        Island nextMNIsland = gb.getWorld().getIslandByIndex(nextMNPos);
        nextMNIsland.add(ColorS.GREEN);
        nextMNIsland.add(ColorS.GREEN);
        nextMNIsland.add(ColorS.GREEN);
        gb.moveMN(1);
        assertEquals(oldMNPos < nextMNPos ?oldMNPos : nextMNPos, gb.getWorld().getMNPosition());
        assertEquals(Optional.of(gb.getPlayers().get(0).getColorTower()), islandMN.getTowerColor());
        assertEquals(2, gb.getWorld().getIslandByIndex(gb.getWorld().getMNPosition()).getNumSubIsland());
    }


    @Test
    void moveMNException() throws InvalidIndexException{
        gb.setActivePlayer(gb.getPlayers().get(0));
        gb.chooseAssistants(gb.getPlayers().get(0), 8);
        assertThrows(InvalidMNStepsException.class, () -> {gb.moveMN(6);});
        gb.getPlayers().get(0).setStrategy(new MNTwoSteps());
        assertThrows(InvalidMNStepsException.class, () -> {gb.moveMN(8);});
        int indexMNStart = gb.getWorld().getMNPosition();
        try {
            gb.moveMN(6);
            assertEquals((indexMNStart + 6) % 12, gb.getWorld().getMNPosition());
        } catch (InvalidMNStepsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testFirstPlayer_testNextPlayer(){
        int firstPlayer = gb.getFirstPlayer();
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Alice");
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Lisa");
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Bob");

    }

    @Test
    public void chooseAssistants() throws InvalidIndexException {
        gb = new GameBoard(3);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9);
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9);
        int bobCards = bob.getNumCards();
        assertTrue(gb.chooseAssistants(bob,5));
        assertEquals(bob.getNumCards(), bobCards-1);
        assertEquals(bob.getLastAssistant().getTurn(), 5);
        int lisaCards = lisa.getNumCards();
        assertTrue(!gb.chooseAssistants(lisa,5));
        assertEquals(lisa.getNumCards(), lisaCards);
        assertTrue(gb.chooseAssistants(lisa,1));
        assertEquals(lisa.getNumCards(),lisaCards-1);
        int aliceCards = alice.getNumCards();
        for(int i = 0; i<10; i++){
            assertTrue(gb.chooseAssistants(alice,1));
        }
        assertEquals(alice.getNumCards(),0);
    }


    @Test
    public void testAddPlayer(){
        gb = new GameBoard(3);
        gb.addPlayer("Bob",ColorT.GREY, Mage.MAGE1);
        gb.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        gb.addPlayer("Alice", ColorT.BLACK, Mage.MAGE3);
        assertEquals(6, gb.getPlayers().get(0).getMyBoard().getTowers().size());
        assertEquals(9, gb.getPlayers().get(0).getMyBoard().getEntrance().size());

        gb2 = new GameBoard(2);
        gb2.addPlayer("Bob",ColorT.GREY, Mage.MAGE1);
        gb2.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        assertEquals(gb2.getPlayers().get(1).getMyBoard().getTowers().size(),8);
        assertEquals(gb2.getPlayers().get(1).getMyBoard().getEntrance().size(),7);
    }

    @Test
    public void testGetPlayerByNickname(){
        Player p = gb.getPlayerByNickname("BOB");
        assertEquals(p.getNickname(), "Bob");
    }


    @Test
    public void TestActivePlayer(){
        gb = new GameBoard(2);
        gb.addPlayer("Bob",ColorT.GREY, Mage.MAGE1);
        gb.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        gb.setActivePlayer(gb.getPlayerByNickname("Bob"));
        assertEquals(gb.getActivePlayer().getNickname(), "Bob");
        assertTrue(gb.getPlayerByNickname("Bob").isPlaying());

    }

    @Test
    public void moveTower(){
        gb = new GameBoard(2);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 8);
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 8);
        gb.addPlayer(lisa);
        gb.addPlayer(bob);
        gb.moveTower(lisa.getColorTower(), lisa.getMyBoard(), gb.getWorld().getIslandByIndex(5));
        assertTrue(gb.getWorld().getIslandByIndex(5).getTowerColor().equals(Optional.of(lisa.getColorTower())));
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
        Island island =gb.getWorld().getIslandByIndex((gb.getWorld().getMNPosition() + 6) % gb.getWorld().getSize());
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
    }

    /**
     * Method moveStudent tests the move of a Student.
     */
    @Test
    @DisplayName("GameBoard's move test")
    void moveStudent() {
        Player lisa = gb.getPlayers().get(0);
        lisa.getMyBoard().add(ColorS.GREEN);
        int initialSIze = lisa.getMyBoard().getEntrance().size();
        Island island = gb.getWorld().getIslandByIndex(gb.getWorld().getMNPosition());
        gb.moveStudent(ColorS.GREEN, lisa.getMyBoard(), island);
        assertEquals(0, lisa.getMyBoard().getEntrance().size());
        assertEquals(1, island.getNumStudentByColor(ColorS.GREEN));
    }
}
