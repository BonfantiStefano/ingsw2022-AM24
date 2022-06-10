package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.client.request.ChooseAssistant;
import it.polimi.ingsw.server.virtualview.VirtualView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;


import java.util.ArrayList;

/**
 * ChooseAssistantController class is a controller, it is used to visually display Assistant cards
 * and to handle the choice of one of them
 */
public class ChooseAssistantController implements GUIController {
    private GUI gui;
    private VirtualView view;
    private ArrayList<Image> assistantImages;
    private int posCard = -1;
    private int numCards;

    private BorderPane root;
    private Button leftButton, rightButton;
    private Rectangle card;

    /**
     * Method getScene returns the scene representing the choice of an Assistant card
     * @param player - the player the cards belong to
     * @return scene with the choice of an Assistant card
     */
    public Scene getScene(int player){
        view = gui.getVirtualView();
        numCards = view.getVirtualPlayers().get(player).getVirtualHand().numCards();

        assistantImages = new ArrayList<>();
        uploadImages(player, numCards);

        root = new BorderPane();
        root.setBackground(Background.fill(Color.LIGHTBLUE));

        card = createCardSpace();
        card.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> chooseCard(event));

        leftButton = createLeftButton();
        rightButton = createRightButton();
        leftButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> previousCard());
        rightButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> nextCard());

        root.setRight(rightButton);
        root.setLeft(leftButton);
        root.setCenter(card);

        return new Scene(root, 400, 400);
    }

    /**
     * Method uploadImages is used to load images representing Assistant cards from resources folder
     * @param player - the player the cards belongs to
     * @param numCards - the number of cards in player's hand
     */
    public void uploadImages(int player, int numCards){
        for(int i = 0; i < numCards; i++){
            int temp = view.getVirtualPlayers().get(player).getVirtualHand().getCards().get(i).getTurn();
            Image imageA = new Image(getClass().getResourceAsStream("/graphics/Assistants/Animali_1_"+temp+"@3x.png"));
            assistantImages.add(imageA);
        }
    }

    /**
     * Method createLeftButton creates the left button
     * @return the left button
     */
    public Button createLeftButton(){
        Button b = new Button("<");
        b.setPrefSize(50, 50);
        b.setTranslateY(140);
        b.setTranslateX(30);
        b.setStyle("-fx-base: snow");
        return b;
    }

    /**
     * Method createRightButton creates the right button
     * @return the right button
     */
    public Button createRightButton(){
        Button b = new Button(">");
        b.setPrefSize(50, 50);
        b.setTranslateY(140);
        b.setTranslateX(-30);
        b.setStyle("-fx-base: snow");
        return b;
    }

    /**
     * Method previousCard handles the event of the left button that allows to return to the previous card.
     */
    public void previousCard (){
        int temp = posCard - 1;
        if(temp >= 0){
            posCard--;
            card.setFill(new ImagePattern(assistantImages.get(posCard)));
        }
    }

    /**
     * Method nextCard handles the event of the right button that allows to go to the next card.
     */
    public void nextCard(){
        int temp = posCard + 1;
        if(temp < numCards){
            posCard++;
            card.setFill(new ImagePattern(assistantImages.get(posCard)));
        }
    }

    /**
     * Method chooseCard handles the double click event that allows to choose an Assistant card which
     * index is sent to the server.
     */
    public void chooseCard(MouseEvent event){
        if(event.getClickCount()==2&&posCard!=-1) {
            int index = posCard + 1;
            gui.sendMessageToServer(new ChooseAssistant(index));
            Stage s = (Stage) card.getScene().getWindow();
            s.close();
            posCard = -1;
        }
    }

    /**
     * Method createCardSpace creates a space where each Assistant card is individually displayed
     * @return a rectangular space dedicated to an Assistant card
     */
    public Rectangle createCardSpace(){
        Rectangle rectangle = new Rectangle(200, 280);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/graphics/Assistants/choose.png"))));
        //frame
        rectangle.setStroke((Color.WHITE));
        rectangle.setStrokeWidth(2);
        return rectangle;
    }

    /**
     * Method setGui sets the GUI reference to the local controller.
     * @param gui of type GUI - the main GUI class.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

}