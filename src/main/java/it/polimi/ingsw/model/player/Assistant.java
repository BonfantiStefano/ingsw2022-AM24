package it.polimi.ingsw.model.player;

public class Assistant {
    private int MNsteps;
    private int turn;
    private Mage mage;

    public Assistant(int MNsteps, int turn, Mage mage){
        this.MNsteps=MNsteps;
        this.turn=turn;
        this.mage=mage;
    }

    public int getMNsteps(){
        return MNsteps;
    }

    public int getTurn() {
        return turn;
    }

    /*
    public int compare(Assistant a){
        if(this.turn<a.getTurn())
            return 1;
        else if(this.turn==a.getTurn())
            return 0;
        else
            return -1;
    } */


}
