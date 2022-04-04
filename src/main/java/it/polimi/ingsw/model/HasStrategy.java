package it.polimi.ingsw.model;

/**
 * This interface is implemented by classes affected by a character's power
 * @param <T> every implementation contains a different Strategy Type
 */
public interface HasStrategy<T> {
    /**
     * Sets the active Strategy
     * @param strategy the Strategy that will be activated
     */
    void setStrategy(T strategy);

    /**
     * Will set the current Strategy to the default one
     */
    void resetStrategy();

    /**
     * Gets the active Strategy
     * @return the active Strategy
     */
    T getStrategy();
}
