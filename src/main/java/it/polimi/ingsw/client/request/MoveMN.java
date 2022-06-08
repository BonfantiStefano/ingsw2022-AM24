package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;

/**
 * MoveMN class is a Request used for requesting the mother nature move.
 * @see Request
 */
public class MoveMN implements Request{
    private final int steps;

    /**
     * Constructor MoveMN creates a new MoveMN instance.
     * @param steps int - the steps that mother nature want to do.
     */
    public MoveMN(int steps) {
        this.steps = steps;
    }

    /**
     * Method getSteps returns the number of steps.
     * @return int - the steps that mother nature want to do.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Controller - the object that will handle the message.
     */
    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
