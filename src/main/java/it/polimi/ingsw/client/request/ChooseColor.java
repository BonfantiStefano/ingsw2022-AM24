package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.ColorS;

/**
 * ChooseColor class is a Request used for requesting to choose a color.
 * @see Request
 */
public class ChooseColor implements Request {
    private final ColorS color;

    /**
     * Constructor ChooseColor creates a new ChooseColor instance.
     * @param color - the color chosen.
     */
    public ChooseColor(ColorS color){
       this.color = color;
    }

    /**
     * Method getColor returns the color chosen.
     * @return color - the chosen color.
     */
    public ColorS getColor() {
        return color;
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
