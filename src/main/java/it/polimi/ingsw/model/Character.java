package it.polimi.ingsw.model;

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

    public abstract void play();

}
