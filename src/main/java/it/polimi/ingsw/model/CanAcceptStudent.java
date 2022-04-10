package it.polimi.ingsw.model;


/**
 *  Objects that implement this interface can receive Students
 */
public interface CanAcceptStudent {
    /**
     * This method will handle how a Student is added
     * @param s Color of the Student being added
     */
    void add(ColorS s);
}
