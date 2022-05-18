package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;

public class GameController implements GUIController{
    private GUI gui;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

