package it.polimi.ingsw.model.profstrategy;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class EqualProfTest tests the correct behaviour of EqualProf
 */
class EqualProfTest {

    /**
     *  Same test as StandardProf
     */
    @Test
    void NormalSituation() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("1", ColorT.WHITE, Mage.MAGE1, 7));
        players.add(new Player("2", ColorT.BLACK, Mage.MAGE2, 7));
        players.add(new Player("3", ColorT.GREY, Mage.MAGE3, 7));
        players.add(new Player("4", ColorT.BLACK, Mage.MAGE4, 7));

        int i=1;
        for(Player p : players){
            for(ColorS c: ColorS.values()) {
                p.getMyBoard().getHall().put(c, i);
            }
            i++;
        }
        HashMap<ColorS, Player> profs=new HashMap<>();
        for(ColorS c: ColorS.values()){
            profs.put(c,null);
        }

        ProfStrategy strategy=new StandardProf();
        HashMap<ColorS, Player> result=strategy.checkProfs(players, profs);
        for(ColorS c: ColorS.values()) {
            assertEquals("4", result.get(c).getNickname());
        }
    }

    /**
     *  The activePlayer should get the Prof if he has the same number of Students as the current owner
     */
    @Test
    void SameNumberAndActive(){
        ArrayList<Player> players = new ArrayList<>();
        Player testSubject = new Player("1", ColorT.WHITE, Mage.MAGE1, 7);
        players.add(testSubject);

        Player profOwner = new Player("ProfOwner", ColorT.WHITE, Mage.MAGE2, 7);
        HashMap<ColorS, Player> profs=new HashMap<>();
        for(ColorS c: ColorS.values()){
            profOwner.getMyBoard().getHall().put(c, 10);
            profs.put(c,profOwner);
            testSubject.getMyBoard().getHall().put(c,10);
        }
        testSubject.getMyBoard().getHall().put(ColorS.BLUE, 15);

        testSubject.setPlaying(true);
        ProfStrategy strategy=new EqualProf();
        HashMap<ColorS, Player> result=strategy.checkProfs(players, profs);

        for(ColorS c: ColorS.values()) {
            assertEquals("1", result.get(c).getNickname());
        }
    }

    /**
     * If the strategy is active, but it's not the Player's turn he should not get the Prof
     */
    @Test
    void SameNumberAndNOTActive(){
        ArrayList<Player> players = new ArrayList<>();
        Player testSubject = new Player("1", ColorT.WHITE, Mage.MAGE1, 7);
        players.add(testSubject);

        Player profOwner = new Player("ProfOwner", ColorT.WHITE, Mage.MAGE2, 7);
        HashMap<ColorS, Player> profs=new HashMap<>();
        for(ColorS c: ColorS.values()){
            profOwner.getMyBoard().getHall().put(c, 10);
            profs.put(c,profOwner);
            testSubject.getMyBoard().getHall().put(c,10);
        }


        testSubject.setPlaying(false);
        ProfStrategy strategy=new EqualProf();
        HashMap<ColorS, Player> result=strategy.checkProfs(players, profs);

        for(ColorS c: ColorS.values()) {
            assertNotEquals("1", result.get(c).getNickname());
        }
    }
}