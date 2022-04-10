package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.profstrategy.EqualProf;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;
import it.polimi.ingsw.model.world.World;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.world.influence.NoColorInfluence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class CharacterProfTest tests the correct behaviour of CharacterProf
 */
class CharacterProfTest {
    /**
     * Ensures that the Character is created with the correct Strategy
     */
    @Test
    void play() {
        GameBoard g = new GameBoard(3);
        ProfStrategy p = new EqualProf();
        CharacterProf c = new CharacterProf(1, "test", p, g);
        c.play();
        assertEquals(p,g.getStrategy());
    }
}