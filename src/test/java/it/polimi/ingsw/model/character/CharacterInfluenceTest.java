package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.world.World;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.world.influence.NoColorInfluence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CharacterInfluenceTest {
    World w;
    ArrayList<ColorS> students = new ArrayList<>();

    @BeforeEach
    void init(){
        for(int i=0;i<10;i++)
            students.add(ColorS.BLUE);
        w = new World(students);
    }

    /**
     * Ensures that the Character is created with the correct Strategy
     */
    @Test
    void play() {
        InfluenceStrategy i = new NoColorInfluence();
        CharacterInfluence c = new CharacterInfluence(1, "test", i, w);
        c.play();
        assertEquals(i,w.getStrategy());
    }
}