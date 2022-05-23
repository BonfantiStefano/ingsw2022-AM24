package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.client.request.ChooseColor;
import it.polimi.ingsw.model.ColorS;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Locale;

public class ChooseColorController implements GUIController{
    private GUI gui;
    private ColorS colorStudent;

    public void showScene(){
        HashMap<ColorS, Image> studentImages = new HashMap<>();
        BorderPane root = new BorderPane();
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        root.setBackground(Background.fill(Color.LIGHTBLUE));

        for(ColorS c : ColorS.values()){
            Image imageS = new Image(getClass().getResourceAsStream("/graphics/Students/"+c.toString().toLowerCase(Locale.ROOT)+".png"));
            studentImages.put(c,imageS);
        }

        ToggleGroup group = new ToggleGroup();

        ToggleButton tbl1 = new ToggleButton("Green");
        ToggleButton tbl2 = new ToggleButton("Red");
        ToggleButton tbl3 = new ToggleButton("Yellow");
        ToggleButton tbl4 = new ToggleButton("Pink");
        ToggleButton tbl5 = new ToggleButton("Blue");

        tbl1.setToggleGroup(group);
        tbl2.setToggleGroup(group);
        tbl3.setToggleGroup(group);
        tbl4.setToggleGroup(group);
        tbl5.setToggleGroup(group);

        tbl1.setUserData(ColorS.GREEN);
        tbl2.setUserData(ColorS.RED);
        tbl3.setUserData(ColorS.YELLOW);
        tbl4.setUserData(ColorS.PINK);
        tbl5.setUserData(ColorS.BLUE);

        buttons.getChildren().addAll(tbl1, tbl2, tbl3, tbl4, tbl5);
        root.setTop(buttons);

        CheckBox cb = new CheckBox("Done");
        cb.selectedProperty().addListener( event ->{
            if(cb.isSelected() && colorStudent!= null){
                //send message to server
                gui.sendMessageToServer(new ChooseColor(colorStudent));
                System.exit(0);
            }
            else{
                cb.setSelected(false);
            }
        });
        cb.setTranslateX(15);
        cb.setTranslateY(-15);
        root.setBottom(cb);

        Rectangle rect = new Rectangle(300, 300);
        rect.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/graphics/Students/choose.png"))));
        //frame
        rect.setStroke((Color.WHITE));
        rect.setStrokeWidth(2);
        rect.setArcHeight(50);
        rect.setArcWidth(50);

        group.selectedToggleProperty().addListener(
                event -> {
                    if(group.getSelectedToggle() != null){
                        colorStudent = (ColorS) group.getSelectedToggle().getUserData();
                        rect.setFill(new ImagePattern(studentImages.get(colorStudent)));
                    }
                    else{
                        rect.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/graphics/Students/choose.png"))));
                        colorStudent = null;
                    }
                }
        );

        tbl1.setStyle("-fx-base: lightgreen");
        tbl2.setStyle("-fx-base: coral");
        tbl3.setStyle("-fx-base: yellow");
        tbl4.setStyle("-fx-base: pink");
        tbl5.setStyle("-fx-base: lightskyblue");

        root.setCenter(rect);
        //Scene scene = new Scene(root, 400, 400);
        //stage.setTitle("Choose color!");
        //stage.setScene(scene);
        //stage.show();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
