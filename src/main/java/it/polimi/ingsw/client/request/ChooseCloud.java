package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;

/**
 * ChooseCloud class is a Request used for requesting to choose a Cloud.
 * @see Request
 */
public class ChooseCloud implements Request{
    private final int index;

    /**
     * Constructor ChooseCloud creates a new ChooseCloud instance.
     * @param index - the index of the selected cloud.
     */
    public ChooseCloud(int index) {
        this.index = index;
    }

    /**
     * Method getIndex returns the index of the chosen cloud.
     * @return index - cloud's index.
     */
    public int getIndex() {
        return index;
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
