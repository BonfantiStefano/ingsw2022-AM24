package it.polimi.ingsw.exceptions;

public class InvalidIndexException extends Throwable {

    @Override
    public String getMessage() {
        return ("Error: Choose a card between 1 and 10");
    }
}

