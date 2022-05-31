package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * This class is the GUIController which handles choosing which host IP and port to connect to.
 *
 * @author Bonfanti Stefano.
 */
public class SetUpController implements GUIController{

    private GUI gui;

    /**
     * The graphical elements of this controller's scene
     */
    @FXML
    private Button connectButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField serverAddressField;
    @FXML
    private TextField serverPortField;

    // GETTERS

    /**
     * Getter for the server IP written in the corresponding text box
     *
     * @return the selected server IP
     */
    public String getServer() {
        return serverAddressField.getText();
    }

    /**
     * Getter for the server port written in the corresponding text box
     *
     * @return the selected server port
     */
    public String getServerPort() {
        return serverPortField.getText();
    }

    // PUBLIC METHODS

    /**
     * Attempts to connect the client to the selected host IP and port
     */
    public void connect() {
        connectButton.setDisable(true);
        statusLabel.setText("Wait...");

        if (getServer().isBlank()) {
            statusLabel.setText("Please specify a server address");
            connectButton.setDisable(false);
        } else if (getServerPort().isBlank()) {
            statusLabel.setText("Please specify a server port");
            connectButton.setDisable(false);
        } else {
            statusLabel.setText("Checking the connection");

            try {
                //Attempts connection to server and creates the Reader
                int port = Integer.parseInt(getServerPort());
                gui.setupConnection(getServer(), port);
                /*
            } catch (IOException e) {
                statusLabel.setText("Couldn't connect to the specified server");
                connectButton.setDisable(false);
                 */
            } catch (NumberFormatException e1) {
                statusLabel.setText("The specified port is not a number");
                connectButton.setDisable(false);
            }
        }
    }

    /**
     * Closes the game
     */
    public void quitGame() {
        System.exit(0);
    }

    //SETTERS

    /**
     * Sets the GUI object for the controller
     *
     * //@param gui of type GUI - the main GUI class.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}





