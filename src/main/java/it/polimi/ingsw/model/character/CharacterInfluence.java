package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;

/**
 * These characters will change how the influence on an Island is calculated
 */
public class CharacterInfluence extends Character {
    private final InfluenceStrategy strategy;
    private final HasStrategy<InfluenceStrategy> observer;

    /**
     * Creates a new CharacterInfluence that contains a determined Strategy and the World observer
     */
    public CharacterInfluence(int cost, String description,  InfluenceStrategy strategy, HasStrategy<InfluenceStrategy> observer) {
        super(cost, description);
        this.strategy=strategy;
        this.observer=observer;
    }

    /**
     * Calls the superclass method and sets the observer's strategy
     */
    @Override
    public void play() {
        super.play();
        observer.setStrategy(strategy);
    }
}
