package it.polimi.ingsw.model.mnstrategy;

import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MNTwoStepsTest {

    @Test
    void mnSteps() {
        MNStrategy strategy=new MNTwoSteps();
        int result =strategy.mnSteps(new Assistant(1, 4, Mage.MAGE2));
        assertEquals(3, result);
    }
}