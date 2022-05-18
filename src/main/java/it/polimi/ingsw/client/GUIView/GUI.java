package it.polimi.ingsw.client.GUIView;

import it.polimi.ingsw.client.GUIView.controllers.CONTROLLERS;
import it.polimi.ingsw.client.GUIView.controllers.GUIController;
import it.polimi.ingsw.client.GUIView.controllers.LobbyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUI extends Application {
    private Scene currentScene;
    private Stage window;
    private Socket clientSocket;
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();
    private final HashMap<Scene, GUIController> nameMapController = new HashMap<>();
    private ObjectOutputStream os;
    private ObjectInputStream is;

    public static void main(String[] args) {
        launch();
    }

    /*
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("/setUpScene.fxml"));

        GUIController c = fxmlLoader.getController();

        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene( scene );
        primaryStage.show();
    }
     */


    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.window = stage;
        window.setMinWidth(400);
        window.setMinHeight(400);
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
    }

    private void run() {
        window.setWidth(747);
        window.setHeight(748);
        window.setTitle("Eriantys!");
        window.setScene(currentScene);
        window.show();
    }

    public void setupConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            os = new ObjectOutputStream(clientSocket.getOutputStream());
            os.flush();
            is = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error during initialization of the client!");
            System.exit(0);
        }
        System.out.println("Stream created");
        new Thread(() -> {
            while (true) {
                try {
                    String s = (String) is.readObject();
                    System.out.println(s);
                    /*
                    if(s.contains("{\"lobbies\":")) {

                    }
                     */
                    //sendMessageToServer(s);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                } catch (Exception e) {
                    sendMessageToServer("Error exception");
                }
            }
        }).start();
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
        try {
            os.reset();
            os.writeObject(string);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}