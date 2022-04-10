package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class AssistantTest tests Assistant class.
 *
 * @see Assistant
 */
class AssistantTest {

    Assistant a;
    Assistant b;

    /** Method init initializes values that will be used during the tests. */
    @BeforeEach
    public void init(){
        a = new Assistant(5,10, Mage.MAGE1);
        b= new Assistant(2,4, Mage.MAGE2);
    }

    /**
     * Method testValues tests that the two values with which each card is initialized are actually correct
     */
    @Test
    public void testValues(){
        assertEquals(a.getTurn(),10);
        assertEquals(a.getMNsteps(),5);
    }

    /**
     * Method testCompareTo checks if method compareTo retutns 1 when the card is greater than the referenced one,
     * -1 when is less and 0 when they are equals
     */
    @Test
    public void testCompareTo(){
        assertTrue(a.compareTo(b)==1);
        assertTrue(b.compareTo(a)==-1);
        assertTrue(a.compareTo(a)==0);
    }

}