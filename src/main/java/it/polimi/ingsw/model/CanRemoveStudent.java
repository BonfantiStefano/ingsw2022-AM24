package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyPlaceException;
import it.polimi.ingsw.exceptions.PlaceFullException;

/**
 * Objects that implement this interface can reduce the number of Students they contain
 */

public interface CanRemoveStudent {
    /**
     * This method will handle the removal of a Student
     * @param s Color of the Student being removed
     */
    void remove(ColorS s) throws EmptyPlaceException;

}
