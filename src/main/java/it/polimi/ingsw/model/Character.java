package it.polimi.ingsw.model;

/**
 * Defines the properties of every Character: cost in coins and a description that summarises its power
 */
public abstract class Character {
    private int cost;
    private String description;

    public Character(int cost, String description){
        this.cost=cost;
        this.description=description;
    }

    public int getCost(){
        return cost;
    }

    public String getDescription(){
        return description;
    }

    /**
     * Subclassess will override this method to apply their effect
     */
    public abstract void play();

}
