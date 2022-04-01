package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.world.World;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.world.influence.NoColorInfluence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterInfluenceTest {

    @Test
    void play() {
        World w = new World();
        InfluenceStrategy i= new NoColorInfluence();
        CharacterInfluence c=new CharacterInfluence(1, "test", i, w);
        c.play();
        assertEquals(i,w.getStrategy());
    }
}