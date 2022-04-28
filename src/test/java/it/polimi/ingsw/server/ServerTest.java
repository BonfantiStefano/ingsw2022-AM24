package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.CharacterDescription;
import it.polimi.ingsw.model.player.Mage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ServerTest {
    private final Server s = new Server();


    void toJson(Request r) {
        System.out.println(s.toJson(r));
    }

    @Test
    void testAllMessages() throws Exception {
        Request msg = new ChooseColor(ColorS.BLUE);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());

        msg = new ChooseAssistant(1);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());

        msg = new ChooseCloud(1);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());

        msg = new ChooseIsland(1);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        msg = new ChooseTwoColors(ColorS.BLUE, ColorS.RED);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        msg = new Disconnect();
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        msg = new EntranceToHall(ColorS.BLUE);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        //msg = new GameParams(2, true, "pippo", Mage.MAGE2, ColorT.WHITE);
        //assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        msg = new Join("ciao", Mage.MAGE2, ColorT.WHITE);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        msg = new MoveMN(1);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        msg = new MoveToIsland(ColorS.BLUE, 2);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        msg = new PlayCharacter(CharacterDescription.CHAR5);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
        msg = new SpecialMoveIsland(ColorS.YELLOW, 2);
        assertEquals(msg.getClass(), s.parseMessage(s.toJson(msg)).getClass());
    }
    @Test
    void prova() {
        Request msg = new ChooseColor(ColorS.BLUE);
        String toJson = s.toJson(msg);
        System.out.println(toJson);
        ChooseColor c = new Gson().fromJson(toJson, ChooseColor.class);
        System.out.println(c.getColor());
    }
}
