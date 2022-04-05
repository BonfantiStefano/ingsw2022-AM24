package it.polimi.ingsw.model.character;

/**
 * Defines the properties of every Character: cost in coins and a description that summarises its power
 */
public class Character {
    private int cost;
    private String description;
    private boolean alreadyPlayed;

    /**
     * Creates a Character with given cost and Description
     */
    public Character(int cost, String description){
        this.cost=cost;
        this.description=description;
        this.alreadyPlayed = false;
    }

    /**
     * Getter for the cost attribute
     * @return cost the Character's cost
     */
    public int getCost(){
        return cost;
    }

    /**
     * Getter for the description attribute
     * @return description the Character's description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Subclasses will override this method to apply their effect
     */
    public void play(){
        if(!alreadyPlayed) {
            this.cost++;
            this.alreadyPlayed=true;
        }
    }

}
