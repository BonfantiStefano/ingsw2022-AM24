package it.polimi.ingsw.model;

/**
 * These characters will change how the influence on an Island is calculated
 */
public class CharacterInfluence extends Character{
    private InfluenceStrategy strategy;
    private World observer;

    public CharacterInfluence(int cost, String description,  InfluenceStrategy strategy, World observer) {
        super(cost, description);
        this.strategy=strategy;
        this.observer=observer;
    }

    @Override
    public void play() {
        observer.setStrategy(strategy);
    }
}
