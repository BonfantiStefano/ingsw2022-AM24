package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;

/**
 * Disconnect class is a Request used for requesting to quit the game.
 * @see Request
 */
public class Disconnect implements Request{

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Controller - the object that will handle the message.
     */
    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
