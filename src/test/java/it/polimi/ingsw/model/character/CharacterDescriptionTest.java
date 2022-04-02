package it.polimi.ingsw.model.character;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterDescriptionTest {

    /**
     * The Characters' cost is either 1,2 or 3
     */
    @Test
    void getCost() {
        int i=1;
        for(CharacterDescription c : CharacterDescription.values()){
            assertEquals(i, c.getCost());
            i= i%3==0? 1 : i+1;
        }

    }
}