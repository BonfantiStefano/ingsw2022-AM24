package it.polimi.ingsw.client.GUIView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUIView.controllers.CONTROLLERS;
import it.polimi.ingsw.client.GUIView.controllers.GUIController;
import it.polimi.ingsw.client.GUIView.controllers.GameController;
import it.polimi.ingsw.client.GUIView.controllers.LobbyController;
import it.polimi.ingsw.client.UserInterface;
import it.polimi.ingsw.server.virtualview.VirtualView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.HashMap;

public class GUI extends Application implements UserInterface {
    private Scene currentScene;
    private Stage window;
    private Client client;
    private VirtualView virtualView;
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();
    private final HashMap<Scene, GUIController> nameMapController = new HashMap<>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.window = stage;
        window.setMinWidth(1000);
        window.setMinHeight(800);
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
        currentScene = nameMapScene.get(CONTROLLERS.MAIN.toString());
    }

    private void run() {
        window.setWidth(1400);
        window.setHeight(800);
        window.setTitle("Eriantys!");
        window.setScene(currentScene);
        GameController g = (GameController) nameMapController.get(currentScene);
        g.init();
        window.show();
    }

    @Override
    public void setupConnection(String serverAddress, int port) {
        client = new Client(this);
        client.addListener(this);
        client.startClient(serverAddress, port);
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public String toJson(Object r){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }
}