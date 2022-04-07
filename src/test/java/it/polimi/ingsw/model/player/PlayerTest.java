package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.mnstrategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

    Player player;

    @BeforeEach
    public void init(){
        player = new Player("Bob", ColorT.BLACK, Mage.MAGE1, 9);
    }

    @Test
    public void testChooseAssistant_testNumCards(){
        int numCards = player.getNumCards();
        Assistant a = player.chooseAssistant(7);
        player.setLastAssist(a);
        assertEquals(player.getLastAssistant().getTurn(), 7);
    }

    @Test
    public void testPlayerInfo(){
        assertEquals(player.getNickname(), "Bob");
        assertEquals(player.getMage(), Mage.MAGE1);
        assertTrue(player.getNumCards()==10);
        assertEquals(player.getColorTower(), ColorT.BLACK);
        player.setPlaying(true);
        assertEquals(player.isPlaying(), true);

    }

    @Test
    @DisplayName("Set influence strategy test")
    void influenceStrategy() {
        assertTrue(player.getStrategy() instanceof MNStandard);
        player.setStrategy(new MNTwoSteps());
        assertTrue(player.getStrategy() instanceof MNTwoSteps);
        player.resetStrategy();
        assertTrue(player.getStrategy() instanceof MNStandard);

    }


}


