package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.Client;

import java.io.Serializable;

/**
 * Interface Answer is implemented by all the messages that will be sent to the client.
 */
public interface Answer extends Serializable{
    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    void accept(Client c);
}
