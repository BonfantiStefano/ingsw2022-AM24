package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.StudentContainer;
import it.polimi.ingsw.model.character.CharacterDescription;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller c;
    Server s = new Server();

    @BeforeEach
    void init(){
        c = new Controller(s, new GameParams(2, false, "test", Mage.MAGE2, ColorT.WHITE));
    }

    void addSecondPlayer() {
        Join join1 = new Join("player2", Mage.MAGE1, ColorT.BLACK);
        c.handleMessage(join1, "player2");
    }

    void chooseAssistants( int i) throws InvalidIndexException {
        c.handleMessage(new ChooseAssistant(i), "test");
        c.handleMessage(new ChooseAssistant(i), "player2");
        /*
        c.getModel().chooseAssistants(c.getModel().getPlayerByNickname("test"), 1);
        c.increaseHaveChosenAssistant();
        c.getModel().chooseAssistants(c.getModel().getPlayerByNickname("player2"), 1);
        c.increaseHaveChosenAssistant();
         */
    }

    /**
     * Ensures that a Join messages are handled correctly
     */
    @Test
    void join(){
        //normal situation
        addSecondPlayer();
        assertEquals(2, c.getModel().getPlayers().size());
    }

    /**
     * Ensures that Disconnect message are handled correctly
     */
    @Test
    void disconnect(){
        c.handleMessage(new Join("player2", Mage.MAGE1, ColorT.BLACK), "player2");
        c.handleMessage(new Disconnect(), "test");
        assertFalse(c.getModel().getPlayers().stream().filter( p -> p.getNickname().equals("test")).findFirst().get().isConnected());
    }

    /**
     * Ensures that a Player is able to rejoin a Game
     */
    @Test
    void rejoin() throws InvalidIndexException {
        addSecondPlayer();
        //System.out.println(c.getPhase());
        chooseAssistants(1);

        c.handleMessage(new Disconnect(), "test");
        c.handleMessage(new Join("test", Mage.MAGE2, ColorT.WHITE), "test");
        assertTrue(c.getModel().getPlayers().stream().filter( p -> p.getNickname().equals("test")).findFirst().get().isConnected());
    }

    /**
     *
     * @throws InvalidIndexException
     */
    void completeTurn() throws InvalidIndexException {
        addSecondPlayer();
        chooseAssistants(1);
        assertEquals(PHASE.MOVE_STUDENTS, c.getPhase());
        ArrayList<ColorS> entrance = new ArrayList<>(c.getModel().getPlayerByNickname("test").getMyBoard().getEntrance());
        int i = 0;
        for(ColorS color : entrance){
            if(i==3)
                break;
            c.handleMessage(new EntranceToHall(color), "test");
            i++;
        }
        assertEquals(PHASE.MOVE_MN, c.getPhase());
        c.handleMessage(new MoveMN(1), "test");
        assertEquals(PHASE.CHOOSE_CLOUD, c.getPhase());
        c.handleMessage(new ChooseCloud(0), "test");
        assertEquals(PHASE.PLANNING, c.getPhase());
    }

    /**
     * Ensures that no Character messages are accepted
     */
    @Test
    void handleCharacter() throws InvalidIndexException {
        addSecondPlayer();
        chooseAssistants(1);
        GameBoard gb = (GameBoard) c.getModel();
        gb.setActivePlayer(c.getModel().getPlayerByNickname("test"));
        c.handleMessage(new PlayCharacter(CharacterDescription.CHAR5),"test" );
    }

    /**
     * Ensures that turns are completed and after a Round the game continues
     */
    @Test
    void resetTurnAndRound() throws InvalidIndexException {
        //first player plays his whole turn
        completeTurn();
        //both choose assistants
        chooseAssistants(2);
        //second Player's turn
        assertEquals(PHASE.MOVE_STUDENTS, c.getPhase());
        ArrayList<ColorS> entrance = new ArrayList<>(c.getModel().getPlayerByNickname("player2").getMyBoard().getEntrance());
        int i = 0;
        for(ColorS color : entrance){
            if(i==3)
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

}