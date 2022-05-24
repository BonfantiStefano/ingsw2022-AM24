package it.polimi.ingsw.client.GUIView.controllers;


import it.polimi.ingsw.client.GUIView.GUI;
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
    public ChoiceBox<String> modeField;
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
    protected void showList(){
        createTable();
    }

    public void setWelcome(Welcome welcome) {
        this.welcome = welcome;
    }

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

    public void createModeField(){
        String expert = "expert", normal = "normal";
        ArrayList<String> modes = new ArrayList<>();
        modes.add(expert);
        modes.add(normal);
        modeField.setItems(FXCollections.observableList(modes));
    }

    public void createColorField(){
        colorField.setItems(FXCollections.observableList(Arrays.stream(ColorT.values()).map(ColorT::toString).collect(Collectors.toList())));
    }

    public void createMageField(){
        mageField.setItems(FXCollections.observableList(Arrays.stream(Mage.values()).map(Mage::toString).collect(Collectors.toList())));
    }

    public void createNumberField(){
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(2);
        nums.add(3);
        numberField.setItems(FXCollections.observableList(nums));
    }

    public void init(){
        nickname = null;
        mage = null;
        color = null;
        selected = null;
        createTable();
        createColorField();
        createModeField();
        createMageField();
        createNumberField();
    }

    @FXML
    public void sendJoin(){
        error.setText("");
        Join msg;
        getCredentials();
        if(selected==null){
            error.setText("Select a lobby first");
            return;
        }
        if((selected.getNumPlayers()==2 && color.equals(ColorT.GREY)) || (selected.getTowers().stream().anyMatch(c->c.equals(color))))
            error.setText(color + "color isn't available");
        else if((selected.getNicknames().stream().anyMatch(n->n.equals(nickname))))
            error.setText("Nickname isn't available");
        if(error.getText().isEmpty()&& checkCredentials()) {
            msg = new Join(nickname, mage, color, selected.getLobbyIndex());
            gui.sendMessageToServer(msg);
            gui.setNickname(nickname);
        }
    }

    @FXML
    public void sendParams(){
        GameParams msg;
        getCredentials();
        int num = numberField.getValue();
        boolean exp = modeField.getValue().equals("expert");
        if(checkCredentials()) {
            msg = new GameParams(num, exp, nickname, mage, color);
            gui.sendMessageToServer(msg);
            gui.setNickname(nickname);
        }
    }

    private void getCredentials(){
        nickname = nicknameField.getText();
        mage = Arrays.stream(Mage.values()).filter(m -> m.toString().equals(mageField.getValue())).findFirst().get();
        color = Arrays.stream(ColorT.values()).filter(c -> c.toString().equals(colorField.getValue())).findFirst().get();
    }

    public void quickCreate(){
        gui.sendMessageToServer(new GameParams(2,true,"marco",Mage.MAGE1,ColorT.BLACK));
        gui.setNickname("marco");
    }
    public void quickJoin(){
        gui.sendMessageToServer(new Join("pippo",Mage.MAGE2,ColorT.WHITE,0));
        gui.setNickname("pippo");
    }

    public void setLastInfo(String text){
        lastInfo.setText(text);
    }

    private boolean checkCredentials(){
        return nickname != null && color != null && mage != null;
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Closes the game
     */
    public void quitGame() {
        System.exit(0);
        //gui.stop();
    }
}

