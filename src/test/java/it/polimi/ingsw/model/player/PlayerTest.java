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
        Player bob = new Player("Bob", ColorT.BLACK, Mage.MAGE1, 9, 6 );
        int numCards = bob.getNumCards();
        bob.chooseAssistant(7);
        assertEquals(bob.getLastAssistant().getTurn(), 7);
        assertTrue(bob.getMyCards().numCards()==numCards-1);

    }

    @Test
    public void testPlayerInfo(){
        Player bob = new Player("Bob", ColorT.BLACK, Mage.MAGE1, 9, 6 );
        assertEquals(bob.getNickname(), "Bob");
        assertEquals(bob.getMage(), Mage.MAGE1);
        assertTrue(bob.getNumCards()==10);
        assertEquals(bob.getColorTower(), ColorT.BLACK);
        assertEquals(bob.getCoins(),6);
        bob.setCoins(5);
        assertEquals(bob.getCoins(),11);
        bob.setPlaying(true);
        assertEquals(bob.isPlaying(), true);

    }


}
