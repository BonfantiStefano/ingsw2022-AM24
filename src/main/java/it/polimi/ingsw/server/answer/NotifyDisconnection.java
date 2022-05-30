package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.Client;

/**
 * Class NotifyDisconnection is used from the server to notify that the client must be disconnected.
 */
public class NotifyDisconnection implements AnswerWithString{
    private String string;

    /**
     * Constructor NotifyDisconnection creates a new message with the given string.
     * @param string String - the string containing the information.
     */
    public NotifyDisconnection(String string) {
        this.string = string;
    }

    /**
     * Method getString returns the string of the message.
     * @return String - the connection's information.
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
