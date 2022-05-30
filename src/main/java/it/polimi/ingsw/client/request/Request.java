package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;

/**
 * Interface Request is implemented by all the messages that will be sent to the server.
 */
public interface Request{
    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Controller - the object that will handle the message.
     */
    void accept(Controller c);
}
