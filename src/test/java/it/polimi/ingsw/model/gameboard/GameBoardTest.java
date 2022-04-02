package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameBoardTest {

    GameBoard gb;

    @BeforeEach
    public void initialization(){
        gb = new GameBoard();
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9, 6 );
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9,6);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9,6);
        lisa.chooseAssistant(1);
        bob.chooseAssistant(4);
        alice.chooseAssistant(8);
        gb.addPlayer(lisa);
        gb.addPlayer(bob);
        gb.addPlayer(alice);

    }

    @Test
    public void firstPlayer(){
        gb.nthPlayer(1);
        assertEquals(gb.getActivePlayer().getNickname(),"Lisa" );
    }

    @Test
    public void secondPlayer(){
        gb.nthPlayer(2);
        assertEquals(gb.getActivePlayer().getNickname(),"Bob" );
    }

    @Test
    public void thirdFirstPlayer(){
        gb.nthPlayer(3);
        assertEquals(gb.getActivePlayer().getNickname(),"Alice" );
    }
}
