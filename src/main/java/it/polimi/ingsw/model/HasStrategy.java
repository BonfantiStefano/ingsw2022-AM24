package it.polimi.ingsw.model;

/**
 * This interface is implemented by classes affected by a character's power
 * @param <T> every implementation contains a different Strategy Type
 */
public interface HasStrategy<T> {
    public void setStrategy(T strategy);
    public void resetStrategy();
    public T getStrategy();
}
