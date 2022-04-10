package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.InvalidIndexException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class HandTest tests Hand class.
 *
 * @see Hand
 */

class HandTest {
    Hand hand1, hand2;

    /** Method init initializes values. */
    @BeforeEach
    public void init(){
        hand1 = new Hand(Mage.MAGE1);
        hand2 = new Hand(Mage.MAGE2);
    }

    /**
     * Method testChosenCard checks if each Assistant card in the player's hand is not null
     * @throws InvalidIndexException if the index position of the card doesn't exist
     */
    @Test
    public void testChoosenCard() throws InvalidIndexException {
        for(int i = 1; i <=10; i++ ) {
            assertNotNull(hand1.getCard(i));
        }
    }

    /** Method testExceptionGetCard checks if method getCard is capable of throwing InvalidIndexException */
    @Test
    public void testExceptionGetCard(){
        int index = 11;
        assertThrows(InvalidIndexException.class,
                ()->hand1.getCard(index));
    }

    /**
     * Method testInitValues tests that all the Assistant cards of the different players are initialized
     * with the same values regardless of the chosen type of mage
     *  @throws InvalidIndexException if the index position of the card doesn't exist
     */
    @Test
    public void testInitSValues() throws InvalidIndexException {
        for(int i = 1; i <=10; i++ ){
            assertEquals((hand1.getCard(i).getTurn()-hand2.getCard(i).getTurn()),0 );
            assertEquals((hand1.getCard(i).getMNsteps()-hand2.getCard(i).getMNsteps()),0);
        }
    }

    /**
     * Method testValues tests that the two values with which each card is initialized are actually correct
     * @throws InvalidIndexException if the index position of the card doesn't exist
     */
    @Test
    public void testValues() throws InvalidIndexException {
        for(int i = 1; i <=10; i++){
            int mnsteps = i%2 == 0? i/2 : (Math.round(i/2) + 1);
            assertEquals(hand1.getCard(i).getTurn(), i);
            assertEquals(hand1.getCard(i).getMNsteps(), mnsteps);
        }
    }

    /**
     * Method testCompareValues tests that the two values of each card are in ascending order
     * @throws InvalidIndexException if the index position of the card doesn't exist
     */
    @Test
    public void testCompareValues() throws InvalidIndexException {
        for( int i = 1; i < 10; i++){
            assertTrue((hand1.getCard(i+1).getTurn()-hand1.getCard(i).getTurn())>0);
            assertTrue((hand1.getCard(i+1).getMNsteps()-hand1.getCard(i).getMNsteps())>=0);
        }

    }

    /**
     * Method testGetMage tests that the back of the cards actually depicts the image of the mage chosen by the player
     */
    @Test
    public void testGetMage(){
        assertEquals(Mage.MAGE1, hand1.getMage());
    }


}