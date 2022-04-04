package it.polimi.ingsw.model.character;

/**
 * Defines the properties of every Character: cost in coins and a description that summarises its power
 */
public class Character {
    private int cost;
    private String description;
    private boolean alreadyPlayed;

    public Character(int cost, String description){
        this.cost=cost;
        this.description=description;
        this.alreadyPlayed = false;
    }


    public int getCost(){
        return cost;
    }

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
