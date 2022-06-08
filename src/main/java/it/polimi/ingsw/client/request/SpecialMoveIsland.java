package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.ColorS;

/**
 * SpecialMoveIsland class is a Request used for requesting to move a student of the chosen color on the selected island.
 * @see Request
 */
public class SpecialMoveIsland implements Request {
    private final ColorS student;
    private final int islandIndex;

    /**
     * Constructor SpecialMoveIsland creates a new SpecialMoveIsland instance.
     * @param student - the student that is shifted
     * @param islandIndex - the index of the island where the student is shifted
     */
    public SpecialMoveIsland(ColorS student, int islandIndex) {
        this.student = student;
        this.islandIndex = islandIndex;
    }

    /**
     * Method getStudent returns the student that is shifted
     * @return student - the shifted student
     */
    public ColorS getStudent() {
        return student;
    }

    /**
     * Method getIslandIndex returns the island where the student is shifted
     * @return islandIndex - the index of the island
     */
    public int getIslandIndex(){
        return islandIndex;
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
