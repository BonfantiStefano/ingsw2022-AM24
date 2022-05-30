package it.polimi.ingsw.client.GUIView.controllers;

/**
 * Enum CONTROLLERS enumerates all the possible scene with the corresponding path, width and height.
 */
public enum CONTROLLERS {
    SETUP("/setUpScene.fxml", 747,748),
    WELCOME("/Welcome.fxml",608,650),
    MAIN("/MainScene.fxml",1400,900),
    CHOOSE_ASSISTANT("/ChooseAssistant.fxml", 500,500),
    CHOOSE_COLOR("/ChooseColor.fxml", 500,500),
    HELP_CONTROLLER("/helpScene.fxml", 300,300),
    YOUWIN("/winScene.fxml",400,400),
    YOULOSE("/loseScene.fxml",400,400);

    private final String path;
    private final float x,y;

    /**
     * Constructor CONTROLLERS creates a new scene with its information
     * @param path String - the path to the location.
     * @param x int - the width of the scene.
     * @param y int - the height of the scene.
     */
    CONTROLLERS(String path, int x, int y){
        this.path = path;
        this.x = x;
        this.y = y;
    }

    /**
     * Method toString returns the path of the scene.
     * @return String - the path.
     */
    @Override
    public String toString() {
        return path;
    }

    /**
     * Method getX returns the width of the scene.
     * @return float - the width of the scene.
     */
    public float getX() {
        return x;
    }

    /**
     * Method getY returns the height of the scene.
     * @return float - the height of the scene.
     */
    public float getY() {
        return y;
    }
}
