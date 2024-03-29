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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class CharacterMNTest tests the correct behaviour of CharacterMN
 */
class CharacterMNTest {
    Player p;
    ArrayList<PlayerInterface> players;
    CharacterMN c;

    @BeforeEach
    void init(){
        p = new Player("1", ColorT.WHITE, Mage.MAGE2, 1);
        players = new ArrayList<>();
        c=new CharacterMN(1, "test", players);
    }

    /**
     * Ensures that the Character is created with the correct Strategy
     */
    @Test
    void playNormal() {
        p.setPlaying(true);
        players.add(p);
        c.play();
        assertTrue(p.getStrategy() instanceof MNTwoSteps);
    }

    /**
     * Ensures that the Player's strategy doesn't change if he isn't playing
     */
    @Test
    void playNoOneIsActive() {
        players.add(p);
        c.play();
        assertTrue(p.getStrategy() instanceof MNStandard);
    }
}