package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.GameParams;
import it.polimi.ingsw.client.request.Join;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller c;
    Server s = new Server();

    @BeforeEach
    void init(){
        c = new Controller(s);
        c.handleMessage(new GameParams(2, false, "test", Mage.MAGE2, ColorT.WHITE), "test");
    }

    @Test
    void join(){
        Join join1 = new Join("player2", Mage.MAGE1, ColorT.BLACK);
        c.handleMessage(join1, "player2");
        assertEquals(2, c.getModel().getPlayers().size());
    }

    @Test
    void allMessages(){

    }

}