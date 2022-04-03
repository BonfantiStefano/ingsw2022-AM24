package it.polimi.ingsw.model.pawn;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.world.Island;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class TowerTest tests Tower class.
 *
 * @author Bonfanti Stefano
 * @see Tower
 */
class TowerTest {

    /** Method getColor tests the color of the Tower's getter.*/
    @Test
    @DisplayName("Tower's color getter test")
    void getColor() {
        Tower t1 = new Tower(ColorT.BLACK);
        assertEquals(ColorT.BLACK, t1.getColor());
    }
}