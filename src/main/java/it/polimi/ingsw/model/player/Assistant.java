package it.polimi.ingsw.model.player;

public class Assistant {
    private int MNsteps;
    private int turn;
    private Mage mage;

    /**
     * Constructor Assistant creates a new playing card.
     *
     * @param MNsteps of type int - the Mother Nature's steps value of this card.
     * @param turn of type int - the turn value of this card.
     * @param mage of type Mage - the back image of this card.
     */
    public Assistant(int MNsteps, int turn, Mage mage){
        this.MNsteps=MNsteps;
        this.turn=turn;
        this.mage=mage;
    }

    /**
     * Method getMNsteps returns the value that determines the maximum number of steps of Mother Nature
     * @return MNsteps of type int - number of steps
     */
    public int getMNsteps(){
        return MNsteps;
    }

    /**
     * Method getTurn returns the value that determines the turn order.
     * @return turn of type int - turn value
     */
    public int getTurn() {
        return turn;
    }


    /**
     * Method compareTo compares two cards for the purposes of sorting. The cards are ordered by the value that determines
     * the turn order of the round.
     *
     * @param a of type Assistant - the other card
     * @return int -  a negative integer, zero, or a positive integer if this card is less than, equal to,
     *                or greater than the referenced card.
     */
    public int compareTo(Assistant a){
        if(this.turn>a.getTurn())
            return 1;
        else if(this.turn==a.getTurn())
            return 0;
        else
            return -1;
    }


}
