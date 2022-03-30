package it.polimi.ingsw.model.profstrategy;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StandardProfTest {
    /**
     *  Checks with only one Player playing while all Profs are held by another Player with fewer Students for all ColorS
     */
    @Test
    void OnePlayer(){
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("1", ColorT.WHITE, Mage.MAGE1, 7, 8);
        Player p2 = new Player("2", ColorT.WHITE, Mage.MAGE1, 7, 8);
        for(ColorS c: ColorS.values()) {
            p1.getMyBoard().getHall().put(c, 10);
            p2.getMyBoard().getHall().put(c, 9);
        }
        players.add(p1);

        HashMap<ColorS, Player> expected = new HashMap<>();
        HashMap<ColorS, Player> profs = new HashMap<>();
        for(ColorS c: ColorS.values()){
            expected.put(c, p1);
            profs.put(c, p2);
        }
        StandardProf strategy=new StandardProf();
        HashMap<ColorS, Player> result=strategy.checkProfs(players, profs);
        assertEquals(expected.get(ColorS.BLUE).getNickname(), result.get(ColorS.BLUE).getNickname());
    }

    /**
     *  Ensures that the Player with the highest number of students for a ColorS gets the Prof
     */
    @Test
    void NormalSituation() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("1", ColorT.WHITE, Mage.MAGE1, 7, 8));
        players.add(new Player("2", ColorT.BLACK, Mage.MAGE2, 7, 8));
        players.add(new Player("3", ColorT.GREY, Mage.MAGE3, 7, 8));
        players.add(new Player("4", ColorT.BLACK, Mage.MAGE4, 7, 8));

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

}