package it.polimi.ingsw.model.mnstrategy;

import it.polimi.ingsw.model.player.Assistant;

/**
 * Defines the method used to obtain the number of steps from an Assistant
 */
public interface MNStrategy {
    /**
     * Returns the number of steps the current Player can make
     * @param a Assistant played
     * @return number of steps MN can make
     */
    int mnSteps(Assistant a);
}
