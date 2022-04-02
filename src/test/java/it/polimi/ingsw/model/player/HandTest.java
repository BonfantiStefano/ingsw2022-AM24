package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.InvalidIndexException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {
    Hand hand1, hand2;

    @BeforeEach
    public void initialization(){
        hand1 = new Hand(Mage.MAGE1);
        hand2 = new Hand(Mage.MAGE2);

    }

    @Test
    public void testChoosenCard() throws InvalidIndexException {
        for(int i = 1; i <=10; i++ ) {
            assertNotNull(hand1.getCard(i));
        }
    }

    @Test
    public void testSameValues() throws InvalidIndexException {
        for(int i = 1; i <=10; i++ ){
            assertEquals((hand1.getCard(i).getTurn()-hand2.getCard(i).getTurn()),0 );
            assertEquals((hand1.getCard(i).getMNsteps()-hand2.getCard(i).getMNsteps()),0);
        }
    }

    @Test
    public void testValues() throws InvalidIndexException {
        for(int i = 1; i <=10; i++){
            int mnsteps = i%2 == 0? i/2 : (Math.round(i/2) + 1);
            assertEquals(hand1.getCard(i).getTurn(), i);
            assertEquals(hand1.getCard(i).getMNsteps(), mnsteps);
        }
    }

    @Test
    public void testCompareValues() throws InvalidIndexException {
        for( int i = 1; i < 10; i++){
            assertTrue((hand1.getCard(i+1).getTurn()-hand1.getCard(i).getTurn())>0);
            assertTrue((hand1.getCard(i+1).getMNsteps()-hand1.getCard(i).getMNsteps())>=0);
        }

    }



}