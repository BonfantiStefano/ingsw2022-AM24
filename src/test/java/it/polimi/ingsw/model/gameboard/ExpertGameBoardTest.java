package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpertGameBoardTest {
    private ExpertGameBoard gb;

    @BeforeEach
    void init(){
        gb = new ExpertGameBoard(4);
        gb.addPlayer("1", ColorT.WHITE, Mage.MAGE2);
        gb.nextPlayer();
    }

    @Test
    void entranceToHall() {
        gb.entranceToHall(ColorS.BLUE);
        gb.entranceToHall(ColorS.BLUE);
        gb.entranceToHall(ColorS.BLUE);
        assertEquals(gb.getActivePlayer().getMyBoard().getHall().get(ColorS.BLUE), 3);
    }

    @Test
    void hallToEntrance() {
    }

    @Test
    void addToHall() {
    }


    @Test
    void removeHall() {
    }

    @Test
    void playCharacter() {
    }

    @Test
    void checkIsland() {
    }

    @Test
    void getCharacters() {
    }

    @Test
    void getActiveCharacter() {
    }

    @Test
    void setActiveCharacter() {
    }

    @Test
    void getAvailableCoins() {
    }
}