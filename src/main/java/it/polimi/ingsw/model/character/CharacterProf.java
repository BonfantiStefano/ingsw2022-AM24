package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;

/**
 * These Characters will change how Players gain or lose Profs
 */
public class CharacterProf extends Character {
    private ProfStrategy strategy;
    private HasStrategy<ProfStrategy> observer;

    public CharacterProf(int cost, String description,  ProfStrategy strategy, HasStrategy<ProfStrategy> observer) {
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
