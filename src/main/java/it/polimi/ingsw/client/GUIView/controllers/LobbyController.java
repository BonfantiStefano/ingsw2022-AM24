package it.polimi.ingsw.client.GUIView.controllers;


import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.client.GUIView.IMAGE_PATHS;
import it.polimi.ingsw.client.request.GameParams;
import it.polimi.ingsw.client.request.Join;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.GameStatus;
import it.polimi.ingsw.server.answer.Welcome;
import it.polimi.ingsw.server.virtualview.VirtualLobby;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LobbyController implements GUIController{
    private GUI gui;
    public TableView<VirtualLobby> table = new TableView<>();
    public TableColumn<VirtualLobby,Integer> IDcloumn;
    public TableColumn<VirtualLobby, ArrayList<String>> connected;
    public TableColumn<VirtualLobby, String> ExpColumn;
    public Button Join;
    public Button Params;
    public ChoiceBox<String> colorField;
    public CheckBox modeField;
    public ChoiceBox<Integer> numberField;
    public TextField nicknameField;
    public ChoiceBox<String> mageField;
    private Welcome welcome;

    private String nickname;
    private Mage mage;
    private ColorT color;
    private VirtualLobby selected;

    @FXML
    private Label error, lastInfo;
    @FXML
    private AnchorPane anchor;


    @FXML
    protected void showList(){
        createTable();
    }

    /**
     * Sets the welcome message containing all Lobbies
     * @param welcome the welcome message received from the server
     */
    public void setWelcome(Welcome welcome) {
        this.welcome = welcome;
    }

    /**
     * Creates the table containing all Lobbies and their information
     */
    public void createTable(){
        table.getItems().clear();
        table.setPlaceholder(new Label("No lobbies :("));

        ArrayList<VirtualLobby> list = welcome !=null?welcome.getLobbies():new ArrayList<>();

        table.setRowFactory(tv -> {TableRow<VirtualLobby> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount()==2 && !row.isEmpty()){
                    VirtualLobby l = row.getItem();
                    error.setText("You selected "+l.getLobbyIndex());
                    selected = l;
                }
            });
            return row;
        });

        IDcloumn.setCellValueFactory(new PropertyValueFactory<>("lobbyIndex"));
        ExpColumn.setCellValueFactory(new PropertyValueFactory<>("mode"));
        connected.setCellValueFactory(new PropertyValueFactory<>("nicknames"));

        table.getItems().addAll(list);
    }

    /**
     * Sets up the ColorField
     */
    public void createColorField(){
        colorField.setItems(FXCollections.observableList(Arrays.stream(ColorT.values()).map(ColorT::toString).collect(Collectors.toList())));
    }
    /**
     * Sets up the MageField
     */
    public void createMageField(){
        mageField.setItems(FXCollections.observableList(Arrays.stream(Mage.values()).map(Mage::toString).collect(Collectors.toList())));
    }
    /**
     * Sets up the NumberField
     */
    public void createNumberField(){
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(2);
        nums.add(3);
        numberField.setItems(FXCollections.observableList(nums));
    }

    /**
     * All operations that need to be performed on window show
     */
    public void init(){
        nickname = null;
        mage = null;
        color = null;
        selected = null;
        anchor.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream(IMAGE_PATHS.WELCOME_BACKGROUND.toString())),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(50,50,true,true,true,true))));
        createTable();
        createColorField();
        createMageField();
        createNumberField();
    }

    /**
     * Send a Join message to the Server with the Player's input
     */
    @FXML
    public void sendJoin(){
        error.setText("");
        Join msg;
        getCredentials();
        if(selected==null){
            error.setText("Select a lobby first");
            return;
        }
        if(error.getText().isEmpty() && checkCredentials()) {
            msg = new Join(nickname, mage, color, selected.getLobbyIndex());
            gui.sendMessageToServer(msg);
            gui.setNickname(nickname);
        }
    }

    /**
     * Send a GameParams (to create a Lobby) message to the Server with the Player's input
     */
    @FXML
    public void sendParams(){
        GameParams msg;
        getCredentials();
        int num = numberField.getValue();
        boolean exp = modeField.isSelected();
        if(checkCredentials()) {
            msg = new GameParams(num, exp, nickname, mage, color);
            gui.sendMessageToServer(msg);
            gui.setNickname(nickname);
        }
    }

    /**
     * Get Player's input from all fields
     */
    private void getCredentials(){
        nickname = nicknameField.getText();
        mage = Arrays.stream(Mage.values()).filter(m -> m.toString().equals(mageField.getValue())).findFirst().get();
        color = Arrays.stream(ColorT.values()).filter(c -> c.toString().equals(colorField.getValue())).findFirst().get();
    }

    public void quickCreate(){
        gui.sendMessageToServer(new GameParams(2,true,"marco",Mage.MAGE1,ColorT.BLACK));
        gui.setNickname("marco");
    }
    public void quickCreate3(){
        gui.sendMessageToServer(new GameParams(3,true,"marco",Mage.MAGE1,ColorT.BLACK));
        gui.setNickname("marco");
    }
    public void quickJoin3(){
        gui.sendMessageToServer(new Join("paolo",Mage.MAGE3,ColorT.GREY,0));
        gui.setNickname("paolo");
    }
    public void quickJoin(){
        gui.sendMessageToServer(new Join("pippo",Mage.MAGE2,ColorT.WHITE,0));
        gui.setNickname("pippo");
    }

    /**
     * Show last Info received from Server
     * @param text latest Info message received from Server
     */
    public void setLastInfo(String text){
        lastInfo.setText(text);
    }

    /**
     * Check if the Player filled al fields
     * @return true if the Player has filled all fields
     */
    private boolean checkCredentials(){
        return nicknameField.getText() != null && colorField.getValue() != null && mageField.getValue() != null;
    }

    /**
     * Sets the GUI object for the controller
     * @param gui GUI - the controller's GUI.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Closes the game
     */
    public void quitGame() {
        System.exit(0);
    }
}

