package it.polimi.ingsw.exceptions;

/**
 * NoSuchStudentException is thrown after an attempt to remove a Student not present in the location
 */
public class NoSuchStudentException extends Throwable{
    /**
     * Method getMessage returns the message of NoSuchStudentException object.
     *
     * @return the message (type String) of NoSuchStudentException object.
     */
    @Override
    public String getMessage() {
        return ("Error: No such Student present");
    }
}
