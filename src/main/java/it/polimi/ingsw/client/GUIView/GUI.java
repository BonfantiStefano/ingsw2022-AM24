package it.polimi.ingsw.client.GUIView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUIView.controllers.*;
import it.polimi.ingsw.client.UserInterface;
import it.polimi.ingsw.client.request.Disconnect;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.InformationConnection;
import it.polimi.ingsw.server.answer.InformationGame;
import it.polimi.ingsw.server.answer.Welcome;
import it.polimi.ingsw.server.virtualview.VirtualView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class GUI represents the graphical user interface.
 *
 * @author Baratto Marco, Bonfanti Stefano.
 */
public class GUI extends Application implements UserInterface {
    private Scene currentScene;
    private Stage window;
    private Client client;
    private VirtualView virtualView;
    private  String nickname;
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();
    private final HashMap<Scene, GUIController> nameMapController = new HashMap<>();

    /**
     * Method main starts the java fx application thread.
     * @param args of type String[].
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Method start setups all the scene and then shows the initial scene.
     * @param stage the Stage class.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.window = stage;
        window.setMinWidth(600);
        window.setMinHeight(600);
        window.setResizable(true);
        run();
    }

    /**
     * Method setup creates all the stage phases which will be updated in other methods.
     */
    private void setup() {
        try {
            for (CONTROLLERS sceneName : CONTROLLERS.values()) {
                FXMLLoader loader = new FXMLLoader(GUI.class.getResource(sceneName.toString()));
                //Creates actual scene for this scene name
                Scene scene = new Scene(loader.load());
                nameMapScene.put(sceneName.toString(), scene);
                GUIController controller = loader.getController();
                //Sets the scene's controller
                controller.setGui(this);
                nameMapController.put(scene, controller);
            }
        } catch (IOException e) {
            System.out.println("Warning: scenes setup failed");
            System.exit(0);
        }
        currentScene = nameMapScene.get(CONTROLLERS.SETUP.toString());
    }

    /**
     * Method run sets the title of the main stage and launches the window.
     */
    private void run() {
        window.setWidth(747);
        window.setHeight(748);
        window.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/EriantysLogo.jpg")));
        window.setTitle("Eriantys!");
        window.setScene(currentScene);
        window.show();
    }

    /**
     * Method setupConnection opens a Socket on the given IP and port.
     * @param serverAddress String - the Server IP.
     * @param port int - the port of the Server.
     */
    @Override
    public void setupConnection(String serverAddress, int port) {
        client = new Client(this);
        client.addListener(this);
        new Thread(()-> client.startClient(serverAddress, port)).start();
    }

    /**
     * Method changeScene changes the current scene to the scene given by parameter.
     * @param newSceneName String - the scene that will be shown.
     * @throws IOException if an I/O error occurs.
     */
    public void changeScene(String newSceneName) throws IOException {
        currentScene = nameMapScene.get(newSceneName);
        window.setScene(currentScene);
        window.show();
        if(newSceneName.equals(CONTROLLERS.WELCOME.toString()) ) {
            window.setWidth(608);
            window.setHeight(650);
            LobbyController lobbyController =(LobbyController) nameMapController.get(nameMapScene.get(newSceneName));
            lobbyController.init();
            window.setResizable(false);
        }
        else if(newSceneName.equals(CONTROLLERS.MAIN.toString())){
            window.setX(50);
            window.setY(0);
        }
        CONTROLLERS c = Arrays.stream(CONTROLLERS.values()).filter(co->co.toString().equals(newSceneName)).findFirst().get();
        window.setWidth(c.getX());
        window.setHeight(c.getY());
        window.setOnCloseRequest(windowEvent -> sendMessageToServer(new Disconnect()));
    }

    /**
     * Method sendMessageToServer sends a message to the server.
     * @param message Object - the message that will be sent to the server.
     */
    public void sendMessageToServer(Object message) {
        if(client!=null)
            client.sendMessage(toJson(message));
        System.out.println(toJson(message));
    }

    /**
     * Method setVirtualView sets the virtual view.
     * @param virtualView VirtualView - the virtual view.
     */
    @Override
    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Method getVirtualView gets the virtual view.
     * @return the virtual view.
     */
    public VirtualView getVirtualView() {
        return virtualView;
    }

    /**
     * Method propertyChange handle all the possible events that the client can do.
     * @param evt PropertyChangeEvent - the event containing all the necessary information.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameController c = (GameController) nameMapController.get(nameMapScene.get(CONTROLLERS.MAIN.toString()));
        LobbyController lb = (LobbyController) nameMapController.get(nameMapScene.get(CONTROLLERS.WELCOME.toString()));
        String text;
        switch (evt.getPropertyName()) {
            case "WELCOME" -> {
                if (!currentScene.equals(nameMapScene.get(CONTROLLERS.WELCOME.toString())))
                    Platform.runLater(() -> {
                        try {
                            changeScene(CONTROLLERS.WELCOME.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                Welcome w = (Welcome) evt.getNewValue();
                lb.setWelcome(w);
                Platform.runLater(lb::init);
            }
            case "INFORMATIONGAME" -> {
                text = ((InformationGame) evt.getNewValue()).getString();
                switch (text) {
                    case "You Lose" -> Platform.runLater(() -> {
                        try {
                            changeScene(CONTROLLERS.YOULOSE.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    case "You won" -> Platform.runLater(() -> {
                        try {
                            changeScene(CONTROLLERS.YOUWIN.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    default -> Platform.runLater(() -> c.setLastInfo(text));
                }
            }
            case "INFORMATIONCONNECTION" -> {
                text = ((InformationConnection) evt.getNewValue()).getString();
                switch (text) {
                    case "Game continue", "Game Started!", "Welcome back!" -> Platform.runLater(() -> {
                        try {
                            changeScene(CONTROLLERS.MAIN.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        c.setLastInfo(text);
                    });
                    case "You are the only connected player, you won!" -> Platform.runLater(() -> {
                        try {
                            changeScene(CONTROLLERS.YOUWIN.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    case "The lobby has been created", "You have joined the game" -> Platform.runLater(() -> lb.setLastInfo(text));
                    default -> Platform.runLater(() -> c.setLastInfo(text));
                }
            }
            case "UPDATE_ALL" -> Platform.runLater(() -> {
                c.setVirtualView((VirtualView) evt.getNewValue());
                c.init();
                c.setCoins();
            });
            case "REPLACE_CHARACTER", "REPLACE_CHARACTER_S", "REPLACE_CHARACTER_NE", "ACTIVE_CHARACTER" -> Platform.runLater(c::drawCharacters);
            case "CREATE_CLOUDS", "REPLACE_CLOUD" -> Platform.runLater(c::drawClouds);
            case "BOARD_COINS" -> Platform.runLater(c::setBoardCoins);
            case "CREATE_WORLD", "REPLACE_ISLAND", "CHANGE_MN_POS" -> Platform.runLater(c::drawIslands);
            case "REPLACE_PLAYER" -> {
                int index = (int) evt.getNewValue();
                Platform.runLater(() -> {
                    c.updateSchoolBoard(index);
                    c.updateAssistants();
                    c.setCoins();
                });
            }
            case "REPLACE_PROFS" -> Platform.runLater(c::updateProfs);
            case "ERROR" -> {
                text = ((Error) evt.getNewValue()).getString();
                Platform.runLater(() -> c.setLastError(text));
            }
            default -> {
            }
        }
    }

    /**
     * Converts an Object to Json format.
     * @param r the Client's request.
     * @return the json of the Message.
     */
    @Override
    public String toJson(Object r){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }

    /**
     * Method getNickname returns the client nickname.
     * @return a String.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Method setNickname sets the client nickname.
     * @param nickname String - the client nickname.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method getNameMapScene returns the map containing all the scene with its name.
     * @return an HashMap.
     */
    public HashMap<String, Scene> getNameMapScene() {
        return nameMapScene;
    }

    /**
     * Method getNameMapController returns the map containing all the scene with its controller.
     * @return an HashMap.
     */
    public HashMap<Scene, GUIController> getNameMapController() {
        return nameMapController;
    }
}