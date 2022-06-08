package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.ColorS;

/**
 * MoveToIsland class is a Request used for requesting a student's move from entrance to an island.
 * @see Request
 */
public class MoveToIsland implements Request {
    private final ColorS colorS;
    private final int index;

    /**
     * Constructor MoveToIsland creates a new MoveToIsland instance.
     * @param colorS ColorS - the color of the student.
     * @param index int - the island's index.
     */
    public MoveToIsland(ColorS colorS, int index) {
        this.colorS = colorS;
        this.index = index;
    }

    /**
     * Method getColorS returns the color of the chosen student.
     * @return colorS - student's color.
     */
    public ColorS getColorS() {
        return colorS;
    }

    /**
     * Method getIndex returns the index of the chosen island.
     * @return int - the island's index.
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
