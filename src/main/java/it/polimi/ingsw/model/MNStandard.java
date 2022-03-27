package it.polimi.ingsw.model;

public class MNStandard implements MNStrategy{
    @Override
    public int mnSteps(Assistant a) {
        return a.getMNsteps();
    }
}
