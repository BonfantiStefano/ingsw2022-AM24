package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;

/**
 * Class WinController handles the FXML scene that appears when the player wins
 */
public class WinController implements GUIController{

    private GUI gui;

    /**
     * Closes the game
     */
    public void quitGame() {
        System.exit(0);
    }

    /**
     * Sets the GUI object for the controller
     *
     * @param gui of type GUI
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
