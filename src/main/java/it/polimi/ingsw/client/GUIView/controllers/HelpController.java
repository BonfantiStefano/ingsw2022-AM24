package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.server.virtualview.VirtualCharacter;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class HelpController implements GUIController{
    private GUI gui;
    @FXML
    private Label studentMoveText,moveMN,chooseAssistant;
    @FXML
    private VBox vBox;

    public Scene getScene(){
        AnchorPane root = new AnchorPane();
        studentMoveText.setText("To move Students click on a Student to select it, then click on the destination (if you want to select two students" +
                "be sure to click on both of them one after the other!)");
        moveMN.setText("To move MN select the pawn by clicking on it, then click on the Island where you want to move it");
        chooseAssistant.setText("To choose an Assistant click on the \"Show Hand\" button, then select the Assistant you want to play");
        ArrayList<Label> charLabels = new ArrayList<>(vBox.getChildren().stream().filter(l->l.getId().contains("char")).map(l->(Label)l).toList());
        for(VirtualCharacter vc:gui.getVirtualView().getVirtualCharacters()){
            charLabels.get(gui.getVirtualView().getVirtualCharacters().indexOf(vc)).setText(vc.getDescription());
        }
        vBox.setSpacing(50);
        vBox.getChildren().forEach(c->{
            if(c instanceof Label l) {
                l.setWrapText(true);
                l.setMaxHeight(50);
                l.setMaxWidth(vBox.getMaxWidth());
            }
        });
        root.getChildren().add(vBox);
        return new Scene(root);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
