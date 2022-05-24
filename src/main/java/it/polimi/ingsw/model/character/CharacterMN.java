package it.polimi.ingsw.model.character;

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

    /**
     * Creates a new CharacterMN object
     */
    public CharacterMN(int cost, String description, ArrayList<PlayerInterface> players) {
        super(cost, description);
        strategy=new MNTwoSteps();
        this.players=players;
    }

    /**
     * Calls the superclass method and sets the observer's strategy
     */
    @Override
    public void play() {
        super.play();
        players.stream().filter(PlayerInterface::isPlaying).findFirst().ifPresent(p -> p.setStrategy(strategy));
    }
}
