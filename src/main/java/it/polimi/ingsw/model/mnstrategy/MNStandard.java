package it.polimi.ingsw.model.mnstrategy;

import it.polimi.ingsw.model.player.Assistant;

/**
 * Normal Behaviour for mnSteps method
 */
public class MNStandard implements MNStrategy {
    /**
     * Returns the number of steps the current Player can make by getting the value from the card
     * @param a Assistant played
     * @return number of steps MN can make
     */
    @Override
    public int mnSteps(Assistant a) {
        return a.getMNsteps();
    }
}
