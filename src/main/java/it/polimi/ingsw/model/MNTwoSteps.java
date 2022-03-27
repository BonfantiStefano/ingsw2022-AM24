package it.polimi.ingsw.model;

public class MNTwoSteps implements MNStrategy{

    /**
     * Changes the number of steps
     * @param a lastAssist
     * @return the number of steps made available by the assistant card + 2
     */
    @Override
    public int mnSteps(Assistant a) {
        return a.getMNsteps()+2;
    }
}
