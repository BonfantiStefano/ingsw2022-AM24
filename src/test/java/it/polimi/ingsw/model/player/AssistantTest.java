package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantTest {

    Assistant a;
    Assistant b;

    @BeforeEach
    public void initialization(){
        a = new Assistant(5,10, Mage.MAGE1);
        b= new Assistant(2,4, Mage.MAGE2);
    }

    @Test
    public void testValues(){
        assertEquals(a.getTurn(),10);
        assertEquals(a.getMNsteps(),5);
    }

    @Test
    public void testCompareTo(){
        assertTrue(a.compareTo(b)==1);
        assertTrue(b.compareTo(a)==-1);
        assertTrue(a.compareTo(a)==0);
    }

}