package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.mnstrategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class PlayerTest tests Player class.
 *
 * @see Player
 */
public class PlayerTest {

    Player player;
    /** Method init creates a Player that will be used by every test.*/
    @BeforeEach
    public void init(){
        player = new Player("Bob", ColorT.BLACK, Mage.MAGE1, 9);
    }

    /** Method testChooseAssistant tests the choice procedure when the player selects a cars and sets it
     * as the last card he played
      */
    @Test
    public void testChooseAssistant() throws InvalidIndexException {
        int numCards = player.getNumCards();
        assertEquals(numCards, 10);
        Assistant a = player.chooseAssistant(7);
        player.setLastAssist(a);
        assertEquals(player.getLastAssistant().getTurn(), 7);
    }

    /**
     * Method testPlayerInfo checks the correctness of the player information getters
     */
    @Test
    public void testPlayerInfo(){
        assertEquals(player.getNickname(), "Bob");
        assertEquals(player.getMage(), Mage.MAGE1);
        assertTrue(player.getNumCards()==10);
        assertEquals(player.getColorTower(), ColorT.BLACK);
        player.setPlaying(true);
        assertEquals(player.isPlaying(), true);

    }

    /**
     * Method influenceStrategy checks the selection of a strategy representing the effect of a Character
     */
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


