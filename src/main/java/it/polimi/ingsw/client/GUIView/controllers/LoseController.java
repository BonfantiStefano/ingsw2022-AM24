package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;

public class LoseController implements GUIController {

    private GUI gui;

    public void quitGame() {
        System.exit(0);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
