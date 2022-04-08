package it.polimi.ingsw.model.mnstrategy;

import it.polimi.ingsw.model.player.Assistant;

/**
 * Alternate behaviour for mnSteps method
 */
public class MNTwoSteps implements MNStrategy {

    /**
     * Changes the number of steps because the Player has activated a specific Character
     * @param a lastAssist
     * @return the number of steps made available by the assistant card + 2
     */
    @Override
    public int mnSteps(Assistant a) {
        return a.getMNsteps()+2;
    }
}
