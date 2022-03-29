package it.polimi.ingsw.model;
import java.util.ArrayList;

public class Hand{
    public final static int [] MNSTEPS ={1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
    public final static int [] TURN = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private Mage mage;

    private ArrayList<Assistant> cards;

    public Hand(Mage mage){
        this.mage = mage;
        cards = new ArrayList<Assistant>();
        for(int i = 0; i<10; i++){
            cards.add(new Assistant(MNSTEPS[i], TURN[i], mage));
        }
    }
    public Assistant getCard(int index){
        return cards.get(index);
    }
}