package it.polimi.ingsw.model.mnstrategy;


import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MNStandardTest {

    @Test
    void mnSteps() {
        MNStandard strategy=new MNStandard();
        int result =strategy.mnSteps(new Assistant(1, 4,Mage.MAGE2));
        assertEquals(1, result);
    }
}