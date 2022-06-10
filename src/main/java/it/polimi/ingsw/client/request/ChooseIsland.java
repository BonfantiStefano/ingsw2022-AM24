package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;

/**
 * ChooseIsland class is a Request used for requesting to choose a cloud.
 * @see Request
 */
public class ChooseIsland implements Request {
    private final int islandIndex;

    /**
     * Constructor ChooseIsland creates a new ChooseIsland instance.
     * @param islandIndex - the index of the selected island.
     */
    public ChooseIsland(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    /**
     * Method getIslandIndex returns the index of the chosen island.
     * @return index - island's index.
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
