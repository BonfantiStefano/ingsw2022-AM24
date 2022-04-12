package it.polimi.ingsw.exceptions;
/**
 * Class InvalidIndexException is thrown when the index position of the card doesn't exist in the player's hand
 */
public class InvalidIndexException extends Throwable {
    /**
     * Method getMessage returns the message of InvalidIndexException object.
     *
     * @return the message (type String) of InvalidIndexException object.
     */
    @Override
    public String getMessage() {
        return ("Error: Choose a card between 1 and 10");
    }
}

