package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NoSuchStudentException;

/**
 * Objects that implement this interface can reduce the number of Students they contain
 */

public interface CanRemoveStudent {
    /**
     * This method will handle the removal of a Student
     * @param s Color of the Student being removed
     */
    void remove(ColorS s) throws NoSuchStudentException;

}
