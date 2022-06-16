package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;

/**
 * These Characters will change how Players gain or lose Profs
 */
public class CharacterProf extends Character {
    private final ProfStrategy strategy;
    private final HasStrategy<ProfStrategy> observer;

    /**
     * Creates a new CharacterProf object
     */
    public CharacterProf(int cost, String description,  ProfStrategy strategy, HasStrategy<ProfStrategy> observer) {
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
