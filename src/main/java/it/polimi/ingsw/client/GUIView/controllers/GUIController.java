package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;

/**
 * Interface GUIController contains the method setGui that is common to all the controllers.
 */
public interface GUIController {

    /**
     * Method setGui sets the Gui.
     * @param gui GUI - the controller's GUI.
     */
    void setGui(GUI gui);
}
