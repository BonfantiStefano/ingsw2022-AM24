package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.mnstrategy.MNStrategy;
import it.polimi.ingsw.model.mnstrategy.MNTwoSteps;
import it.polimi.ingsw.model.player.PlayerInterface;

import java.util.ArrayList;

/**
 * This character changes how many steps MN can move
 */
public class CharacterMN extends Character {
    private ArrayList<PlayerInterface> players;
    private MNStrategy strategy;

    public CharacterMN(int cost, String description, ArrayList<PlayerInterface> players) {
        super(cost, description);
        strategy=new MNTwoSteps();
        this.players=players;
    }

    @Override
    public void play() {
        super.play();
        players.stream().filter(PlayerInterface::isPlaying).findFirst().ifPresent(p -> p.setStrategy(strategy));

    }
}
