package it.polimi.ingsw.exceptions;
/**
 * Class PlaceEmptyException is thrown when there is no pawns in the selected place
 */
public class EmptyPlaceException extends Throwable{
    /**
     * Method getMessage returns the message of PlaceEmptyException object.
     *
     * @return the message (type String) of PlaceEmptyException object.
     */
    @Override
    public String getMessage() {
            return ("Error: There is no pawns here");
        }
}

