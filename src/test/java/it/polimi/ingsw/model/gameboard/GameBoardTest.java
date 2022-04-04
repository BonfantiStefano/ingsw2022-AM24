package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameBoardTest {

    GameBoard gb;

    @BeforeEach
    public void initialization(){
        gb = new GameBoard(3);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9, 6 );
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9,6);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9,6);

        lisa.chooseAssistant(4);

        bob.chooseAssistant(1);
        bob.chooseAssistant(4);

        alice.chooseAssistant(7);
        alice.chooseAssistant(8);

        gb.addPlayer(lisa);
        gb.addPlayer(bob);
        gb.addPlayer(alice);

    }

    @Test
    public void testSortedPlayers(){
        gb.sortPlayers();
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Lisa");
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Bob");
        gb.nextPlayer();
        assertEquals(gb.getActivePlayer().getNickname(), "Alice");

    }

    @Test
    public void testSetChoosenAssistant(){
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9,6);
        bob.chooseAssistant(3);
        assertEquals(bob.getLastAssistant().getTurn(), 3);
        gb.setChoosenAssistant(bob,9);
        assertEquals(bob.getLastAssistant().getTurn(), 9);
    }

    @Test
    public void setLastAssistants(){
        gb = new GameBoard(3);
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9, 6 );
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9,6);
        Player alice = new Player("Alice", ColorT.WHITE, Mage.MAGE2, 9,6);

        lisa.chooseAssistant(4);
        bob.chooseAssistant(4);
        gb.setLastAssistants(lisa);
        gb.setLastAssistants(bob);
        assertEquals(gb.getSizeList(), 1);
        alice.chooseAssistant(5);
        gb.setLastAssistants(alice);
        assertEquals(gb.getSizeList(), 2);

    }


}
