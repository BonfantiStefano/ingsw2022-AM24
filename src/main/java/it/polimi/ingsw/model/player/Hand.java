package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.exceptions.InvalidIndexException;

/**
 * Class Hand contains a set of all the Player's available Assistant cards which have a matching back
 */
public class Hand{
    private final static int [] MNSTEPS ={1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
    private final static int [] TURN = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private final Mage mage;

    private final List<Assistant> cards;

    /**Constructor Hand creates a hand of playing cards instance.
     * Each card is initialized with two values: the first one determines the turn order of the round,
     * the second one how many steps Mother Nature pawn can be shifted
     * @param mage of type Mage - the back of the cards (the image of the mage chosen by the player)
     */
    public Hand(Mage mage){
        this.mage = mage;
        cards = new ArrayList<>();
        for(int i = 0; i<10; i++){
            cards.add(new Assistant(MNSTEPS[i], TURN[i], mage));
        }
    }

    /**
     * Method getCard gets the Assistant card at the given position in the Hand.
     * @param  index of type int - index position of the card
     * @return  Assistant card
     * @throws InvalidIndexException if the index position of the card doesn't exist in the player's hand
     */
    public Assistant getCard(int index) throws InvalidIndexException{
        if (index >= 1 && index <= cards.size()){
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
     * Method numCards returns how many cards are left in the hand.
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

    /**
     * Method getCards returns all the cards of the Hand
     * @return cards of type List Assistant
     */
    public List<Assistant> getCards(){
        return cards;
    }

}

