package it.polimi.ingsw.model.world.influence;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.Island;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class NoColorInfluenceTest tests NoColorInfluence class.
 *
 * @author Bonfanti Stefano
 * @see NoColorInfluence
 */
class NoColorInfluenceTest {

    /** Method getInfluence tests the calculation of the influence with the strategy NoColorInfluence.*/
    @Test
    @DisplayName("NoColorInfluence's getInfluence method test")
    void getInfluence() {
        InfluenceStrategy noColorInfluence  = new NoColorInfluence();
        Island island = new Island();
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9);
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9);
        HashMap<ColorS, Player> profs = new HashMap<>();
        profs.put(ColorS.GREEN, lisa);
        profs.put(ColorS.PINK, lisa);
        profs.put(ColorS.BLUE, bob);
        profs.put(ColorS.RED, alice);
        lisa.setPlaying(true);
        island.add(ColorS.GREEN);
        island.add(ColorS.GREEN);
        island.add(ColorS.PINK);
        island.add(ColorS.BLUE);
        island.add(ColorT.BLACK);
        assertEquals(2, noColorInfluence.getInfluence(island, lisa, Optional.of(ColorS.GREEN), profs));
    }
}