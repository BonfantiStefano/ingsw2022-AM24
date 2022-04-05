package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.mnstrategy.MNStandard;
import it.polimi.ingsw.model.mnstrategy.MNStrategy;
import it.polimi.ingsw.model.mnstrategy.MNTwoSteps;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerInterface;
import it.polimi.ingsw.model.world.World;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;
import it.polimi.ingsw.model.world.influence.NoColorInfluence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CharacterMNTest {

    @Test
    void play() {
        Player p=new Player("1", ColorT.WHITE, Mage.MAGE2, 1);
        ArrayList<PlayerInterface> players = new ArrayList<>();
        p.setPlaying(true);
        players.add(p);
        CharacterMN c=new CharacterMN(1, "test", players);
        c.play();
        assertTrue(p.getStrategy() instanceof MNTwoSteps);
    }
}