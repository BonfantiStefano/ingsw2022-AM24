package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ColorT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

    Player bob;

    @Test
    public void testChooseAssistant_testNumCards(){
        Player player = new Player("Bob", ColorT.BLACK, Mage.MAGE1, 9);
        int numCards = player.getNumCards();
        Assistant a = player.chooseAssistant(7);
        player.setLastAssist(a);
        assertEquals(player.getLastAssistant().getTurn(), 7);
    }

    @Test
    public void testPlayerInfo(){
        Player bob = new Player("Bob", ColorT.BLACK, Mage.MAGE1, 9 );
        assertEquals(bob.getNickname(), "Bob");
        assertEquals(bob.getMage(), Mage.MAGE1);
        assertTrue(bob.getNumCards()==10);
        assertEquals(bob.getColorTower(), ColorT.BLACK);
        bob.setPlaying(true);
        assertEquals(bob.isPlaying(), true);

    }


}
