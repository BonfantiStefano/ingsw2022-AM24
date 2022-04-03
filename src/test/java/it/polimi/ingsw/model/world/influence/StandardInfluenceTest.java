package it.polimi.ingsw.model.world.influence;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.pawn.Student;
import it.polimi.ingsw.model.pawn.Tower;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.Island;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class StandardInfluenceTest tests StandardInfluence class.
 *
 * @author Bonfanti Stefano
 * @see StandardInfluence
 */
class StandardInfluenceTest {

    /** Method getInfluence tests the calculation of the influence with the strategy StandardInfluence.*/
    @Test
    @DisplayName("StandardInfluence's getInfluence method test")
    void getInfluence() {
        InfluenceStrategy standardInfluence  = new StandardInfluence();
        Island island = new Island();
        Player lisa = new Player("Lisa", ColorT.BLACK, Mage.MAGE1, 9, 6 );
        Player bob = new Player("Bob", ColorT.WHITE, Mage.MAGE2, 9,6);
        Player alice = new Player("Alice", ColorT.GREY, Mage.MAGE3, 9,6);
        HashMap<ColorS, Player> profs = new HashMap<>();
        profs.put(ColorS.GREEN, lisa);
        profs.put(ColorS.BLUE, bob);
        profs.put(ColorS.RED, alice);
        lisa.setPlaying(true);
        island.add(new Student(ColorS.GREEN));
        island.add(new Student(ColorS.BLUE));
        island.add(new Tower(ColorT.BLACK));
        assertEquals(2, standardInfluence.getInfluence(island, lisa, Optional.empty(), profs));
    }
}