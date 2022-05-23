package it.polimi.ingsw.client.GUIView.controllers;

public enum CONTROLLERS {
    SETUP("/setUpScene.fxml"),
    WELCOME("/Welcome.fxml"),
    GAME("/Game.fxml"),
    MAIN("/MainScene.fxml"),
    CHOOSE_ASSISTANT("/ChooseAssistant.fxml");

    private final String path;

    CONTROLLERS(String path){
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
