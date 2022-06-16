package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.client.request.ChooseColor;
import it.polimi.ingsw.model.ColorS;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import java.util.HashMap;
import java.util.Locale;

/**
 * ChooseColorController class is a controller, it is used to display students of five different colors
 * and to handle the choice of the desired color
 */
public class ChooseColorController implements GUIController{
    private GUI gui;
    private BorderPane root;
    private ColorS colorStudent;
    private ToggleGroup group;
    private HashMap<ColorS, Image> studentImages;
    private Rectangle rect;
    private HBox buttons;
    private ToggleButton tbl1, tbl2, tbl3, tbl4, tbl5;

    /**
     * Method getScene returns the scene representing the choice of one of the five colors
     * @return scene with the choice of the color
     */
    public Scene getScene(){
        studentImages = new HashMap<>();
        root = new BorderPane();
        buttons = new HBox();
        group = new ToggleGroup();

        uploadImages();
        createColorfulButtons();

        tbl1.setToggleGroup(group);
        tbl2.setToggleGroup(group);
        tbl3.setToggleGroup(group);
        tbl4.setToggleGroup(group);
        tbl5.setToggleGroup(group);
        buttons.getChildren().addAll(tbl1, tbl2, tbl3, tbl4, tbl5);

        rect = createStudentSpace();
        rect.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, this::chooseStudent);

        group.selectedToggleProperty().addListener(
                event -> selectButton()
        );

        buttons.setAlignment(Pos.CENTER);
        root.setBackground(Background.fill(Color.LIGHTBLUE));
        root.setTop(buttons);
        root.setCenter(rect);

        return new Scene(root,400,400);
    }


    /**
     * Method chooseStudent handles the double click event that allows to choose the color of the student
     * which is also sent to the server.
     */
    public void chooseStudent(MouseEvent mouseEvent){
        if(mouseEvent.getClickCount()==2&&colorStudent!=null)
            gui.sendMessageToServer(new ChooseColor(colorStudent));
    }

    /**
     * Method selectButton allows to change the color of the depicted student by pressing the button of the
     * desired color
     */
    public void selectButton(){
        if(group.getSelectedToggle() != null){
            colorStudent = (ColorS) group.getSelectedToggle().getUserData();
            rect.setFill(new ImagePattern(studentImages.get(colorStudent)));
        }
        else{
            rect.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/graphics/Students/choose.png"))));
            colorStudent = null;
        }
    }

    /**
     * Method uploadImages is used to load images representing students of different colors from resources folder
     */
    public void uploadImages(){
        for(ColorS c : ColorS.values()){
            Image imageS = new Image(getClass().getResourceAsStream("/graphics/Students/"+c.toString().toLowerCase(Locale.ROOT)+".png"));
            studentImages.put(c,imageS);
        }
    }

    /**
     * Method createColorfulButtons creates five different buttons of the same colors of the students
     */
    public void createColorfulButtons(){
        tbl1 = new ToggleButton("Green");
        tbl2 = new ToggleButton("Red");
        tbl3 = new ToggleButton("Yellow");
        tbl4 = new ToggleButton("Pink");
        tbl5 = new ToggleButton("Blue");

        tbl1.setUserData(ColorS.GREEN);
        tbl2.setUserData(ColorS.RED);
        tbl3.setUserData(ColorS.YELLOW);
        tbl4.setUserData(ColorS.PINK);
        tbl5.setUserData(ColorS.BLUE);

        tbl1.setStyle("-fx-base: lightgreen");
        tbl2.setStyle("-fx-base: coral");
        tbl3.setStyle("-fx-base: yellow");
        tbl4.setStyle("-fx-base: pink");
        tbl5.setStyle("-fx-base: lightskyblue");
    }

    /**
     * Method createStudentSpace creates a space where a student of one of the five different colors
     * is individually displayed
     * @return a rectangular space dedicated to a student
     */
    public Rectangle createStudentSpace(){
        Rectangle r = new Rectangle(300, 300);
        r.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/graphics/Students/choose.png"))));
        //frame
        r.setStroke((Color.WHITE));
        r.setStrokeWidth(2);
        r.setArcHeight(50);
        r.setArcWidth(50);
        return r;
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
