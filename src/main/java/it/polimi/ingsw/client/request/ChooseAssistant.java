package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;

/**
 * ChooseAssistant class is a Request used for requesting to play an Assistant card.
 * @see Request
 */
public class ChooseAssistant implements Request{
    private final int index;

    /**
     * Constructor ChooseAssistant creates a new ChooseAssistant instance.
     * @param index - the index of the selected card
     */
    public ChooseAssistant(int index) {
        this.index = index;
    }

    /**
     * Method getIndex returns the index of the chosen card
     * @return index - card's index
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
