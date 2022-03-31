package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.mnstrategy.MNStrategy;
import it.polimi.ingsw.model.mnstrategy.MNTwoSteps;

/**
 * This character changes how many steps MN can move
 */
public class CharacterMN extends Character {
    private HasStrategy<MNStrategy> observer;
    private MNStrategy strategy;

    public CharacterMN(int cost, String description, HasStrategy<MNStrategy> observer) {
        super(cost, description);
        strategy=new MNTwoSteps();
        this.observer=observer;
    }

    @Override
    public void play() {
        super.play();
        observer.setStrategy(strategy);
    }
}
