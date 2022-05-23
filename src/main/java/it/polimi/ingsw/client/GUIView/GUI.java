package it.polimi.ingsw.client.GUIView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUIView.controllers.*;
import it.polimi.ingsw.client.UserInterface;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Information;
import it.polimi.ingsw.server.answer.Welcome;
import it.polimi.ingsw.server.virtualview.VirtualView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class GUI extends Application implements UserInterface {
    private Scene currentScene;
    private Stage window;
    private Client client;
    private VirtualView virtualView;
    private  String nickname;
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();
    private final HashMap<Scene, GUIController> nameMapController = new HashMap<>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.window = stage;
        window.setMinWidth(600);
        window.setMinHeight(600);
        window.setResizable(true);
        run();
    }

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
        //currentScene = nameMapScene.get(CONTROLLERS.MAIN.toString());
    }

    private void run() {
        window.setWidth(747);
        window.setHeight(748);
        window.setTitle("Eriantys!");
        window.setScene(currentScene);
        //GameController g = (GameController) nameMapController.get(currentScene);
        //g.init();
        window.show();
    }

    @Override
    public void setupConnection(String serverAddress, int port) {
        client = new Client(this);
        client.addListener(this);
        new Thread(()-> client.startClient(serverAddress, port)).start();
    }

    public void changeScene(String newSceneName) throws IOException {
        currentScene = nameMapScene.get(newSceneName);
        window.setScene(currentScene);
        //window.sizeToScene();
        window.show();
        if(newSceneName.equals(CONTROLLERS.WELCOME.toString()) ) {
            window.setWidth(608);
            window.setHeight(650);
            LobbyController lobbyController =(LobbyController) nameMapController.get(nameMapScene.get(newSceneName));
            lobbyController.init();
        }
    }

    public void sendMessageToServer(Object string) {
        //Dopo cambiare questo metodo
        if(client!=null)
            client.sendMessage(client.toJson(string));
        System.out.println(toJson(string));
    }

    @Override
    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameController c = (GameController) nameMapController.get(nameMapScene.get(CONTROLLERS.MAIN.toString()));
        LobbyController lb = (LobbyController) nameMapController.get(nameMapScene.get(CONTROLLERS.WELCOME.toString()));
        switch(evt.getPropertyName()){
            case "WELCOME":
                if(currentScene.equals(nameMapScene.get(CONTROLLERS.WELCOME.toString())))
                Platform.runLater(()-> {
                    try {
                        changeScene(CONTROLLERS.WELCOME.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                Welcome w = (Welcome) evt.getNewValue();
                lb.setWelcome(w);

                Platform.runLater(lb::init);
                break;
            case "INFORMATION":
                String text = ((Information) evt.getNewValue()).getString();
                if(text.equals("Game Started!")){
                    Platform.runLater(()-> {
                        try {
                            changeScene(CONTROLLERS.MAIN.toString());
                            c.init();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else if(text.equals("The lobby has been created")||text.equals("You have joined the game")){
                    Platform.runLater(()->lb.setLastInfo(text));
                }
                Platform.runLater(()->c.setLastInfo(text));
                break;
            case "UPDATE_ALL":
                Platform.runLater(()->{
                    c.setVirtualView((VirtualView) evt.getNewValue());
                    c.init();
                }
                );
            break;
            case "REPLACE_CHARACTER":
            case "REPLACE_CHARACTER_S":
            case "REPLACE_CHARACTER_NE":
                Platform.runLater(c::drawCharacters);
                break;
            case "REPLACE_CLOUD":
                Platform.runLater(c::drawClouds);
                break;
            case "BOARD_COINS":
                //TODO call updateCoins
                break;
            case "CREATE_WORLD":
            case "REPLACE_ISLAND":
            case "CHANGE_MN_POS":
                Platform.runLater(c::drawIslands);
                break;
            case "REPLACE_PLAYER":
                int index = (int) evt.getNewValue();
                Platform.runLater(()->{
                    c.updateSchoolBoard(index);
                    c.updateAssistants();
                });
                break;
            case "REPLACE_PROFS":
                Platform.runLater(c::updateProfs);
                break;
            case "ERROR":
                text = ((Error)evt.getNewValue()).getString();
                Platform.runLater(()->c.setLastError(text));
            default:
                break;
        }
    }

    public String toJson(Object r){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public HashMap<String, Scene> getNameMapScene() {
        return nameMapScene;
    }

    public HashMap<Scene, GUIController> getNameMapController() {
        return nameMapController;
    }
}