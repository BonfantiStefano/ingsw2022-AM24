package it.polimi.ingsw.exceptions;

public class InvalidIndexException extends Throwable {
    /**
     * Class InvalidIndexException is thrown when the index position on the card doesn't exist in the player's hand
     */
    @Override
    public String getMessage() {
        return ("Error: Choose a card between 1 and 10");
    }
}

