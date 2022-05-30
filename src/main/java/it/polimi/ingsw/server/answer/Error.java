package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.Client;

/**
 * Class Error is used from the server to notify the client that an error occurred.
 */
public class Error implements AnswerWithString{
    private final String string;

    /**
     * Constructor Error creates a new error message with the string given by parameter.
     * @param string String - the string of the error.
     */
    public Error(String string) {
        this.string = string;
    }

    /**
     * Method getString returns the string of the message.
     * @return String - the error.
     */
    @Override
    public String getString() {
        return string;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
