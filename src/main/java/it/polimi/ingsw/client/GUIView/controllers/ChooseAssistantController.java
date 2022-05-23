package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.client.request.ChooseAssistant;
import it.polimi.ingsw.server.virtualview.VirtualView;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ChooseAssistantController implements GUIController {
    private GUI gui;
    private VirtualView view;
    private int posCard = -1;

    public void showScene(){

        BorderPane root = new BorderPane();
        view = new VirtualView();

        //TODO activePlayer
        int numCards = view.getVirtualPlayers().get(0).getVirtualHand().numCards();

        ArrayList<Image> assistantImages = new ArrayList<>();
        for(int i = 1; i <= numCards; i++){
            //TODO activePlayer
            int temp = view.getVirtualPlayers().get(0).getVirtualHand().getCards().get(i).getTurn();
            Image imageA = new Image(getClass().getResourceAsStream("/graphics/Assistants/Animali_1_"+temp+"@3x.png"));
            assistantImages.add(imageA);
        }

        root.setBackground(Background.fill(Color.LIGHTBLUE));
        Button b1 = new Button("<");
        Button b2 = new Button(">");
        b1.setPrefSize(50, 50);
        b2.setPrefSize(50, 50);
        root.setRight(b2);
        root.setLeft(b1);
        b1.setTranslateY(140);
        b1.setTranslateX(30);
        b2.setTranslateY(140);
        b2.setTranslateX(-30);
        b1.setStyle("-fx-base: snow");
        b2.setStyle("-fx-base: snow");

        Rectangle rect = new Rectangle(200, 280);
        rect.setFill(Color.TRANSPARENT);
        rect.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/graphics/Assistants/choose.png"))));
        //frame
        rect.setStroke((Color.WHITE));
        rect.setStrokeWidth(2);
        root.setCenter(rect);

        b1.setOnAction(
                event -> {
                    int temp = posCard - 1;
                    if(temp >= 0){
                        posCard--;
                        rect.setFill(new ImagePattern(assistantImages.get(posCard)));
                    }
                }
        );

        b2.setOnAction(
                event -> {
                    int temp = posCard + 1;
                    if(temp < numCards){
                        posCard++;
                        rect.setFill(new ImagePattern(assistantImages.get(posCard)));
                    }
                }
        );

        CheckBox cb = new CheckBox("Done");
        cb.selectedProperty().addListener( event ->{
            if(cb.isSelected() && posCard!= -1){
                //send message to server
                int index = posCard + 1;
                gui.sendMessageToServer(new ChooseAssistant(index));
                System.exit(0);
            }
            else{
                cb.setSelected(false);
            }
        });

        cb.setTranslateX(15);
        cb.setTranslateY(-15);
        root.setBottom(cb);

        //Scene scene = new Scene(root, 400, 400);
        //stage.setTitle("Choose Assistant");
        //stage.setScene(scene);
        //stage.show();

    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
