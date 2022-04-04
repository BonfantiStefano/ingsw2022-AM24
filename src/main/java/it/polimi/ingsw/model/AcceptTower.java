package it.polimi.ingsw.model;


/**
 * Interface that defines methods to add and remove Towers
 */
public interface AcceptTower {
    /**
     * Method used to add a Tower
     * @param t the Tower being added
     */
    void add(ColorT t);

    /**
     * Method used to remove a Tower
     * @param t the Tower being removed
     */
    void remove(ColorT t);
}
