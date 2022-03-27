package it.polimi.ingsw.model;

/**
 * This character changes how many steps MN can move
 */
public class CharacterMN extends Character{
    private HasStrategy<MNStrategy> observer;
    private MNStrategy strategy;

    public CharacterMN(int cost, String description) {
        super(cost, description);
        strategy=new MNTwoSteps();
    }

    @Override
    public void play() {
        observer.setStrategy(strategy);
    }
}
