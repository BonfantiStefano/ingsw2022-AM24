package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.CharacterDescription;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.virtualview.VirtualIsland;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;
import it.polimi.ingsw.server.virtualview.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller c, controller;
    Lobby lobby = new Lobby();

    @BeforeEach
    void init() {
        c = new Controller(lobby, new GameParams(2, false, "test", Mage.MAGE2, ColorT.WHITE));
    }

    void addSecondPlayer() {
        Join join1 = new Join("player2", Mage.MAGE1, ColorT.BLACK, 1);
        c.handleMessage(join1, "player2");
    }

    void chooseAssistants() {
        c.handleMessage(new ChooseAssistant(1), "test");
        c.handleMessage(new ChooseAssistant(2), "player2");
    }

    /**
     * Ensures that a Join messages are handled correctly
     */
    @Test
    void join() {
        //normal situation
        addSecondPlayer();
        assertEquals(2, c.getModel().getPlayers().size());
    }

    /**
     * Ensures that Disconnect message are handled correctly
     */
    @Test
    void disconnect() {
        c.handleMessage(new Join("player2", Mage.MAGE1, ColorT.BLACK, 1), "player2");
        c.handleMessage(new Disconnect(), "test");
        chooseAssistants();
        assertFalse(c.getModel().getPlayers().stream().filter(p -> p.getNickname().equals("test")).findFirst().get().isConnected());
    }

    /**
     * Ensures that a Player is able to rejoin a Game
     */
    @Test
    void rejoin() {
        addSecondPlayer();
        //System.out.println(c.getPhase());
        chooseAssistants();

        c.handleMessage(new Disconnect(), "test");
        c.handleMessage(new Join("test", Mage.MAGE2, ColorT.WHITE, 1), "test");
        assertTrue(c.getModel().getPlayers().stream().filter(p -> p.getNickname().equals("test")).findFirst().get().isConnected());
    }

    /**
     * Handle messages to complete a Turn
     */
    void completeTurn() {
        addSecondPlayer();
        chooseAssistants();
        assertEquals(PHASE.MOVE_STUDENTS, c.getPhase());
        ArrayList<ColorS> entrance = new ArrayList<>(c.getModel().getPlayerByNickname("test").getMyBoard().getEntrance());
        int i = 0;
        for (ColorS color : entrance) {
            if (i == 3)
                break;
            c.handleMessage(new EntranceToHall(color), "test");
            i++;
        }
        assertEquals(PHASE.MOVE_MN, c.getPhase());
        c.handleMessage(new MoveMN(1), "test");
        assertEquals(PHASE.CHOOSE_CLOUD, c.getPhase());
        c.handleMessage(new ChooseCloud(0), "test");
        assertEquals(PHASE.MOVE_STUDENTS, c.getPhase());
    }

    /**
     * Ensures that no Character messages are accepted
     */
    @Test
    void handleCharacter() {
        addSecondPlayer();
        chooseAssistants();
        GameBoard gb = (GameBoard) c.getModel();
        gb.setActivePlayer(c.getModel().getPlayerByNickname("test"));
        c.handleMessage(new PlayCharacter(CharacterDescription.CHAR5), "test");
    }

    /**
     * Ensures that turns are completed and after a Round the game continues
     */
    @Test
    void resetTurnAndRound() throws InvalidIndexException {
        //first player plays his whole turn
        completeTurn();
        //both choose assistants
        chooseAssistants();
        //second Player's turn
        assertEquals(PHASE.MOVE_STUDENTS, c.getPhase());
        ArrayList<ColorS> entrance = new ArrayList<>(c.getModel().getPlayerByNickname("player2").getMyBoard().getEntrance());
        int i = 0;
        for (ColorS color : entrance) {
            if (i == 3)
                break;
            c.handleMessage(new EntranceToHall(color), "player2");
            i++;
        }
        assertEquals(PHASE.MOVE_MN, c.getPhase());
        c.handleMessage(new MoveMN(1), "player2");
        assertEquals(PHASE.CHOOSE_CLOUD, c.getPhase());
        c.handleMessage(new ChooseCloud(1), "player2");
        assertEquals(PHASE.PLANNING, c.getPhase());
    }

    /**
     * Send incorrect messages
     * NOT FINAL
     */
    @Test
    void incorrectMessages() {
        Join join2 = new Join("test", Mage.MAGE1, ColorT.BLACK, 1);
        c.handleMessage(join2, "test");
        addSecondPlayer();
        c.handleMessage(new ChooseAssistant(34), "test");
    }

    @Test
    void getTurnController() {
        assertNotNull(c.getTurnController());
    }

    /**
     * The Controller should ignore these messages because the game isn't in expert mode
     */
    @Test
    void sendCharacterMessages() {
        c.handleMessage(new PlayCharacter(CharacterDescription.CHAR5), "test");
        c.handleMessage(new ChooseColor(ColorS.BLUE), "test");
        c.handleMessage(new ChooseTwoColors(ColorS.BLUE, ColorS.GREEN), "test");
        c.handleMessage(new ChooseIsland(1), "test");
        c.handleMessage(new SpecialMoveIsland(ColorS.GREEN, 1), "test");
    }

    /**
     * Method testListeners checks if view update itself each time an update event is emitted.
     */
    @Test
    void testListeners() {
        //--------------SETUP PHASE-----------------------
        controller = new Controller(lobby, new GameParams(2, false, "Leo", Mage.MAGE1, ColorT.BLACK));
        Join join = new Join("Lisa", Mage.MAGE2, ColorT.WHITE, 1);
        controller.handleMessage(join, "Lisa");

        GameBoard gameBoard = (GameBoard) controller.getModel();
        VirtualView view = controller.getVirtualView();
        assertEquals(2, view.getVirtualPlayers().size());

        //--------------PLANNING PHASE--------------------------
        //at the beginning of the game each virtual island has 1 student (except the island with mother nature and the opposite one)
        int students = 0;
        for (VirtualIsland virtualIsland : view.getVirtualWorld()){
            students = students + virtualIsland.getStudents().size();
        }
        assertEquals(students, view.getVirtualWorld().size() - 2);

        //at the beginning of the game each virtual cloud is correctly updated with 3 students
        ArrayList<ColorS> modelCoud1 = gameBoard.getCloudByIndex(0).getStudents();
        ArrayList<ColorS> modelCoud2 = gameBoard.getCloudByIndex(1).getStudents();

        ArrayList<ColorS> virtualCloud1 = view.getVirtualClouds().get(0).getStudents();
        ArrayList<ColorS> virtualCloud2 = view.getVirtualClouds().get(1).getStudents();
        assertTrue(modelCoud1.containsAll(virtualCloud1));
        assertTrue(virtualCloud1.containsAll(modelCoud1));
        assertTrue(modelCoud2.containsAll(virtualCloud2));
        assertTrue(virtualCloud2.containsAll(modelCoud2));

        controller.handleMessage(new ChooseAssistant(2), "Leo");
        controller.handleMessage(new ChooseAssistant(6), "Lisa");

        VirtualPlayer virtualLeo = view.getVirtualPlayers().get(0);
        Player modelLeo = gameBoard.getPlayers().get(0);
        Assistant card2 = modelLeo.getLastAssistant();
        assertEquals(modelLeo.getLastAssistant().getTurn(), virtualLeo.getVirtualLastAssistant().getTurn());
        assertFalse(virtualLeo.getVirtualHand().getCards().contains(card2));
        assertEquals(9, virtualLeo.getVirtualHand().getCards().size());
        assertEquals(virtualLeo.getNickname(), modelLeo.getNickname());

        VirtualPlayer virtualLisa = view.getVirtualPlayers().get(1);
        Player modelLisa = gameBoard.getPlayers().get(1);
        Assistant card6 = modelLisa.getLastAssistant();
        assertEquals(modelLisa.getLastAssistant().getTurn(), virtualLisa.getVirtualLastAssistant().getTurn());
        assertFalse(virtualLeo.getVirtualHand().getCards().contains(card6));
        assertEquals(9, virtualLeo.getVirtualHand().getCards().size());
        assertEquals(virtualLisa.getNickname(), modelLisa.getNickname());

        //the schoolBoard's entrance of each virtual player is correctly filled with 7 students
        assertEquals(7, view.getVirtualPlayers().get(0).getVirtualBoard().getEntrance().size());
        assertEquals(7, view.getVirtualPlayers().get(1).getVirtualBoard().getEntrance().size());

        assertEquals("Leo", gameBoard.getActivePlayer().getNickname());

        //--------------MOVE STUDENTS PHASE--------------------------

        //remove 7 random students from entrance
        ArrayList<ColorS> modelHallLeo = modelLeo.getMyBoard().getEntrance();
        modelLeo.getMyBoard().getEntrance().removeAll(modelHallLeo);
        assertEquals(0, virtualLeo.getVirtualBoard().getEntrance().size());
        //now Leo has: 3 red, 3 pink, 1 yellow
        for(int i = 0; i < 3; i++){
            modelLeo.getMyBoard().add(ColorS.RED);
            modelLeo.getMyBoard().add(ColorS.PINK);
        }
        modelLeo.getMyBoard().add(ColorS.YELLOW);

        int indexIsland = (view.getMnPos() + 1) % view.getVirtualWorld().size();
        //move 1 red on island, 1 red and 1 pink to hall
        int oldred = view.getVirtualWorld().get(indexIsland).getNumStudentByColor(ColorS.RED);
        MoveToIsland messageIsland = new MoveToIsland(ColorS.RED, indexIsland);
        controller.handleMessage(messageIsland, "Leo");
        int newred = view.getVirtualWorld().get(indexIsland).getNumStudentByColor(ColorS.RED);
        assertEquals(6, modelLeo.getMyBoard().getEntrance().size());
        assertEquals(modelLeo.getMyBoard().getEntrance().size(), virtualLeo.getVirtualBoard().getEntrance().size());
        assertEquals(oldred, newred - 1);

        int oldRed = virtualLeo.getVirtualBoard().getHall().get(ColorS.RED);
        int oldSizeEntrance = virtualLeo.getVirtualBoard().getEntrance().size();
        EntranceToHall messageHall = new EntranceToHall(ColorS.RED);
        controller.handleMessage(messageHall, "Leo");
        int newRed = virtualLeo.getVirtualBoard().getHall().get(ColorS.RED);
        int newSizeEntrance = virtualLeo.getVirtualBoard().getEntrance().size();
        assertEquals(oldRed + 1, newRed);
        assertEquals(oldSizeEntrance - 1, newSizeEntrance);
        gameBoard.checkProfs();
        assertEquals("Leo", gameBoard.getProfs().get(ColorS.RED).getNickname());

        EntranceToHall messageHall2 = new EntranceToHall(ColorS.PINK);
        controller.handleMessage(messageHall2, "Leo");
        assertEquals(4, virtualLeo.getVirtualBoard().getEntrance().size());

        //--------------MOVE_MN PHASE-----------------------
        int mnPos = gameBoard.getWorld().getMNPosition();
        int virtualPos = view.getMnPos();
        assertEquals(mnPos, virtualPos);

        MoveMN messageMN = new MoveMN(1);
        controller.handleMessage(messageMN, "Leo");
        int newPos = view.getMnPos();
        assertEquals((virtualPos + 1) % view.getVirtualWorld().size(), newPos);
        assertEquals("Leo", gameBoard.getProfs().get(ColorS.RED).getNickname());
        assertEquals("Leo", view.getVirtualProfs().get(ColorS.RED).getNickname());
        assertEquals(ColorT.BLACK, gameBoard.getWorld().getIslandByIndex(newPos).getTowerColor().get());

        //--------------CHOOSE_CLOUD PHASE-----------------------
        int sizeEntranceOld = virtualLeo.getVirtualBoard().getEntrance().size();
        ChooseCloud messageCloud = new ChooseCloud(1);
        controller.handleMessage(messageCloud, "Leo");
        int sizeEntranceNew = virtualLeo.getVirtualBoard().getEntrance().size();
        assertEquals(sizeEntranceNew , sizeEntranceOld + 3);


        //--------------other player's turn--------------------------

        assertEquals("Lisa", gameBoard.getActivePlayer().getNickname());

        //--------------MOVE STUDENTS PHASE--------------------------

        //remove 7 random students from entrance
        ArrayList<ColorS> modelHallLisa = modelLisa.getMyBoard().getEntrance();
        modelLisa.getMyBoard().getEntrance().removeAll(modelHallLisa);
        assertEquals(0, virtualLisa.getVirtualBoard().getEntrance().size());
        //now Lisa has: 3 blue, 3 green, 1 red
        for(int i = 0; i < 3; i++){
            modelLisa.getMyBoard().add(ColorS.BLUE);
            modelLisa.getMyBoard().add(ColorS.GREEN);
        }
        modelLisa.getMyBoard().add(ColorS.RED);
        //move 3 students on islands, so Lisa doesn't control any of the profs
        MoveToIsland message1 = new MoveToIsland(ColorS.BLUE, 1);
        controller.handleMessage(message1, "Lisa");
        MoveToIsland message2 = new MoveToIsland(ColorS.GREEN, 2);
        controller.handleMessage(message2, "Lisa");
        int index = (view.getMnPos() + 1) % view.getVirtualWorld().size();
        MoveToIsland message3 = new MoveToIsland(ColorS.RED, index);
        controller.handleMessage(message3, "Lisa");

        //--------------MOVE_MN PHASE-----------------------
        //check if two islands is correctly unified
        int numIslandsBefore = view.getVirtualWorld().size();
        MoveMN message_mn = new MoveMN(1);
        controller.handleMessage(message_mn, "Lisa");
        int pos_MN = view.getMnPos();
        assertEquals(ColorT.BLACK, gameBoard.getWorld().getIslandByIndex(pos_MN).getTowerColor().get());
        int numIslandsAfter = view.getVirtualWorld().size();
        assertEquals(numIslandsBefore - 1, numIslandsAfter);
        assertEquals(2, gameBoard.getWorld().getIslandByIndex(pos_MN).getNumSubIsland());
        assertEquals(2, view.getVirtualWorld().get(pos_MN).getNumSubIsland());
    }
}