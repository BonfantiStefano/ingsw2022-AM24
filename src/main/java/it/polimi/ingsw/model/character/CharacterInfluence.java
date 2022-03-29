package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.world.influence.InfluenceStrategy;

/**
 * These characters will change how the influence on an Island is calculated
 */
public class CharacterInfluence extends Character {
    private InfluenceStrategy strategy;
    private HasStrategy<InfluenceStrategy> observer;

    public CharacterInfluence(int cost, String description,  InfluenceStrategy strategy, HasStrategy observer) {
        super(cost, description);
        this.strategy=strategy;
        this.observer=observer;
    }

    @Override
    public void play() {
        super.play();
        observer.setStrategy(strategy);
    }
}
