package it.polimi.ingsw.exceptions;

/**
 * Class PlaceFullException is thrown when there is no more available space in the hall of the SchoolBoard
 * for the students of one of the five colors
 */
public class PlaceFullException extends Exception {
    @Override
    public String getMessage() {
        return ("Error: The hall is full");
    }
}
