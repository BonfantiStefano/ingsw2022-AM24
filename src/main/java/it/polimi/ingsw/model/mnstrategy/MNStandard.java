package it.polimi.ingsw.model.mnstrategy;

import it.polimi.ingsw.model.player.Assistant;

public class MNStandard implements MNStrategy {
    @Override
    public int mnSteps(Assistant a) {
        return a.getMNsteps();
    }
}
