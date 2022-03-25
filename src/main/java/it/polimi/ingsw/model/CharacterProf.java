package it.polimi.ingsw.model;

/**
 * These Characters will change how Players gain or lose Profs
 */
public class CharacterProf extends Character {
    private ProfStrategy strategy;
    private GameBoard observer;

    public CharacterProf(int cost, String description,  ProfStrategy strategy, GameBoard observer) {
        super(cost, description);
        this.strategy=strategy;
        this.observer=observer;
    }

    @Override
    public void play() {
        observer.setStrategy(strategy);
    }
}
