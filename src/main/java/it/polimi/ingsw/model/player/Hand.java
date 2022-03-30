package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import it.polimi.ingsw.exceptions.InvalidIndexException;

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

    public Assistant getCard(int index) throws InvalidIndexException{
        if (index >= 1 && index <= 10){
            return cards.get(index-1);
        }
        else throw new InvalidIndexException();
    }


}

