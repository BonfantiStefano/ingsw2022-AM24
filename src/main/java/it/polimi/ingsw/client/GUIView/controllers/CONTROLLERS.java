package it.polimi.ingsw.client.GUIView.controllers;

public enum CONTROLLERS {
    SETUP("/setUpScene.fxml", 747,748),
    WELCOME("/Welcome.fxml",608,650),
    MAIN("/MainScene.fxml",1400,800),
    CHOOSE_ASSISTANT("/ChooseAssistant.fxml", 500,500),
    CHOOSE_COLOR("/ChooseColor.fxml", 500,500),
    HELP_CONTROLLER("/helpScene.fxml", 300,300),
    YOUWIN("/winScene.fxml",400,400),
    YOULOSE("/winScene.fxml",400,400);

    private final String path;
    private float x,y;

    CONTROLLERS(String path, int x, int y){
        this.path = path;
        this.x = x;
        this.y = x;
    }

    @Override
    public String toString() {
        return path;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
