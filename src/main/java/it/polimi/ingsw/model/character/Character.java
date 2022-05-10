package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.EVENT;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Defines the properties of every Character: cost in coins and a description that summarises its power
 */
public class Character {
    private int cost;
    private String description;
    private boolean alreadyPlayed;
    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

    /**
     * Creates a Character with given cost and Description
     */
    public Character(int cost, String description){
        this.cost=cost;
        this.description=description;
        this.alreadyPlayed = false;
    }

    /**
     * Method addListener is used in order to register an event listener
     * @param expertBoard - event listener that is used for receiving the events
     */
    public void addListener(PropertyChangeListener expertBoard){
        listener.addPropertyChangeListener(expertBoard);
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
            listener.firePropertyChange(String.valueOf(EVENT.CHARACTER_COST), null, this);
        }
    }

    /**
     * Getter for the alreadyPlayed attribute.
     * @return alredyPlayed - attribute which tells if a Character has been already played
     */
    public boolean isAlreadyPlayed() {
        return alreadyPlayed;
    }

    /**
     * Getter for the listener attribute.
     * @return listener - attribute which register rhe listener
     */
    public PropertyChangeSupport getListener() {
        return listener;
    }
}
