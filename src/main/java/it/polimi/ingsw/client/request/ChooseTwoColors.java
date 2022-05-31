package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.ColorS;

/**
 * ChooseTwoColors class is a Request used for requesting to switch two students.
 * @see Request
 */
public class ChooseTwoColors implements Request {
    private final ColorS firstColor;
    private final ColorS secondColor;

    /**
     * Constructor ChooseTwoColors creates a new ChooseTwoColors instance.
     * @param firstColor - the color of the student that is switched from hall to entrance (in case of Character 10) /
     *                     from entrance to card (in case of Character 7)
     * @param secondColor - the color of the student that is switched from entrance to hall (in case of Character 10) /
     *                      from card to entrance (in case of Character 7)
     */
    public ChooseTwoColors(ColorS firstColor, ColorS secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }

    /**
     * Method getFirstColor returns the color of the first student that is shifted
     * @return firstColor - the color of the first student
     */
    public ColorS getFirstColor() {
        return firstColor;
    }

    /**
     * Method getsecondColor returns the color of the second student that is shifted
     * @return secondColor - the color of the second student
     */
    public ColorS getSecondColor() {
        return secondColor;
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
