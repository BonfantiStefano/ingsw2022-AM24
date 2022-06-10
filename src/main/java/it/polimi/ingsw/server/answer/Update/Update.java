package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.answer.Answer;

/**
 * Interface Update is implemented by all the answers that notify to the client a view update.
 */
public interface Update extends Answer {
    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    void accept(Client c);
}
