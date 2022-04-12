package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.EmptyPlaceException;

/**
 * Interface that defines methods to add and remove Towers
 */
public interface AcceptTower {
    /**
     * Method used to add a Tower
     * @param t Color of the Tower being added
     */
    void add(ColorT t);

    /**
     * Method used to remove a Tower
     * @param t Color of the Tower being removed
     */
    void remove(ColorT t);

}
