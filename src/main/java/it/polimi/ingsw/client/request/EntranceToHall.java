package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.ColorS;

/**
 * EntranceToHall class is a Request used for requesting a student's move from entrance to hall.
 * @see Request
 */
public class EntranceToHall implements Request{
    private final ColorS colorS;

    /**
     * Constructor EntranceToHall creates a new EntranceToHall instance.
     * @param colorS - the color of the student.
     */
    public EntranceToHall(ColorS colorS) {
        this.colorS = colorS;
    }

    /**
     * Method getColorS returns the color of the chosen student.
     * @return colorS - student's color.
     */
    public ColorS getColorS() {
        return colorS;
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
