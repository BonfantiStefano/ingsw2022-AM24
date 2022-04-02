package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.profstrategy.EqualProf;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;
import it.polimi.ingsw.model.world.World;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.world.influence.NoColorInfluence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterProfTest {

    @Test
    void play() {
        GameBoard g = new GameBoard();
        ProfStrategy p= new EqualProf();
        CharacterProf c= new CharacterProf(1, "test", p, g);
        c.play();
        assertEquals(p,g.getStrategy());
    }
}