package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.exceptions.InvalidIndexException;

/**
 * Class Hand contains all the Player's available Assistant cards
 */
public class Hand{
    public final static int [] MNSTEPS ={1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
    public final static int [] TURN = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private Mage mage;

    private List<Assistant> cards;

    /**Constructor Hand creates a new empty hand of cards instance.*/
    public Hand(Mage mage){
        this.mage = mage;
        cards = new ArrayList<Assistant>();
        for(int i = 0; i<10; i++){
            cards.add(new Assistant(MNSTEPS[i], TURN[i], mage));
        }
    }

    /**
     * Method getCard obtains the card that is at the specified location in the hand
     * @param  index of type int - index position of the card
     * @return  Assistant card
     */
    public Assistant getCard(int index) throws InvalidIndexException{
        if (index >= 1 && index <= 10){
            return cards.get(index-1);
        }
        else throw new InvalidIndexException();
    }

    /**
     * Method removeCard removes the specified card from the current hand.
     * @param a of type Assistant - the card to be removed
     */
    public void removeCard(Assistant a){
        cards.remove(a);
    }

    /**
     * Method numCards returns the number of cards held in the hand.
     * @return int - the size of the hand of cards
     */
    public int numCards(){
        return cards.size();
    }

    /**
     * Method getMage returns the type of Mage in the hand.
     * @return mage - the back image of this hand of card.
     */
    public Mage getMage(){
        return mage;
    }

}

