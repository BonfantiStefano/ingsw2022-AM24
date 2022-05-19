package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.server.virtualview.VirtualView;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.*;

public class GameController implements GUIController{
    Random r = new Random(System.currentTimeMillis());
    private final int numIslands = 5;
    private HashMap<ColorS, Image> studentImages;
    private HashMap<ColorS, Image> profImages;
    private HashMap<ColorT, Image> towerImages;
    private final int radius = 200;
    private final int angle = 360;
    private GUI gui;
    private VirtualView virtualView;
    private ArrayList<GridPane> entrancesGrids = new ArrayList<>();
    private ArrayList<GridPane> hallGrids = new ArrayList<>();
    private ArrayList<GridPane> profsGrids = new ArrayList<>();
    private ArrayList<GridPane> towersGrids = new ArrayList<>();
    private ArrayList<Pane> boards = new ArrayList<>();

    @FXML
    private AnchorPane anchor;
    @FXML
    private Pane sc1, sc2, sc3, islandsPane;

    @FXML
    private GridPane e1,h1,p1,t1;


    public void init() {
        createImages();
        boards.add(sc1);
        boards.add(sc2);
        boards.add(sc3);

        for (Pane p : boards) {
            p.getChildren().forEach(this::addTo);
        }
        for (GridPane g : entrancesGrids)
            updateEntrance(entrancesGrids.indexOf(g));
        for (GridPane g : hallGrids)
            updateHall(hallGrids.indexOf(g));
        updateProfs();
        for (GridPane g : towersGrids)
            updateTowers(towersGrids.indexOf(g));
        drawIslands();
        /*
        anchor.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("/graphics/EriantysLogo.jpg")),BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(10,10,false,false,true,true))));
        */
    }


    public void updateEntrance(int index){
        ArrayList<ColorS> entrance = new ArrayList<>();
        //TODO remove fillRandom and get correct entrance from virtualView
        //entrance = virtualView.getVirtualPlayers().get(index).getVirtualBoard().getEntrance();
        fillRandom(entrance, 7);
        int entrIndex=0;
        for(int i=0;i<4;i++)
            for(int j=0;j<2;j++){
                //skip first cell
                j = i==0? j+1:j;
                if(entrIndex<entrance.size()) {
                    Circle c = new Circle(10);
                    c.setFill(new ImagePattern(studentImages.get(entrance.get(entrIndex))));
                    entrancesGrids.get(index).add(c, j, i);

                    entrIndex++;
                }
            }
    }

    public void updateHall(int index){
        HashMap<ColorS, Integer> hall = new HashMap<>();
        //TODO same as updateEntrance
        for(ColorS c: ColorS.values())
            hall.put(c,5);

        for(int i=0;i<5;i++) {
            ColorS color = ColorS.values()[i];
            for (int j = 0; j < 10; j++) {
                if (hall.get(color)-j>0) {
                    Circle c = new Circle(10);
                    c.setFill(new ImagePattern(studentImages.get(color)));
                    hallGrids.get(index).add(c, j, i);
                }
            }
        }
    }

    public void updateProfs(){
        HashMap<ColorS, Integer> profs = new HashMap<>();
        //TODO insert correct hashmap from virtualView
        for(ColorS c:ColorS.values()){
            //TODO remove the following line
            profs.put(c,0);
            int index = profs.get(c);
            if(index!=-1){
                GridPane g = profsGrids.get(index);
                Circle circle = new Circle(10);
                circle.setFill(new ImagePattern(profImages.get(c)));
                g.add(circle,0, ColorS.valueOf(c.toString()).ordinal());
            }

        }
    }

    public void updateTowers(int index){
        ArrayList<ColorT> towers = new ArrayList<>();
        for(int k =0;k<5;k++)
            towers.add(ColorT.BLACK);
        int towerIndex=0;
        for(int i=0;i<4;i++)
            for(int j=0;j<2;j++){
                if(towerIndex<towers.size()) {
                    Circle c = new Circle(10);
                    c.setFill(new ImagePattern(towerImages.get(towers.get(0))));
                    towersGrids.get(index).add(c, j, i);
                    towerIndex++;
                }
            }
    }

    public void drawIslands(){
        final int offset = 200;

        ArrayList<ImageView> islands = new ArrayList<>();
        Image image = new Image(getClass().getResourceAsStream("/graphics/island1.png"));
        for(int i =0;i<numIslands;i++) {
            islands.add(new ImageView(image));
        }

        islands.forEach(i->{
            int thisAngle = (islands.indexOf(i)+1)*(angle/islands.size());
            /*
            StackPane p = new StackPane();
            Text text = new Text();

            text.setText("island: " + islands.indexOf(i));

            ArrayList<Circle> circles = new ArrayList<>();


            colors.forEach(s-> {
                Circle c = new Circle(5);
                c.setFill(new ImagePattern(studentImages.get(s)));
                circles.add(c);
            });
            HBox v = new HBox(5);

            v.getChildren().addAll(circles);

            i.setFitHeight(100);
            i.setFitWidth(100);

            p.getChildren().addAll(text,v);
            v.setLayoutX(p.getLayoutX()/2);
            v.setLayoutY(p.getLayoutY()/2);

             */
            ArrayList<ColorS> colors = new ArrayList<>();
            fillRandom(colors, 5);
            StackPane p = createPane(colors, true);
            p.getStyleClass().add("islandPanel");
            p.setMinHeight(100);
            p.setMinWidth(100);

            //p.getChildren().forEach(n->n.getStyleClass().add("InIsland"));

            p.setLayoutX(islandsPane.getHeight()/2 + radius*Math.sin(thisAngle*2*Math.PI/angle)+offset);
            p.setLayoutY(islandsPane.getWidth()/2 + radius*Math.cos(thisAngle*2*Math.PI/angle)+offset);
            /*
            p.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(50,50,true,true,true,true))));

            p.setPrefWidth(50);
            p.setPrefHeight(50);

             */
            islandsPane.getChildren().add(p);
        });

    }



    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    private void addTo(Node n){
        String id = n.getId();
        GridPane p = (GridPane) n;
        if(id.contains("e"))
            entrancesGrids.add(p);
        else if(id.contains("h"))
            hallGrids.add(p);
        else if(id.contains("p"))
            profsGrids.add(p);
        else if (id.contains("t"))
            towersGrids.add(p);

    }

    private StackPane createPane(ArrayList<ColorS> students, boolean mn){
        StackPane p = new StackPane();
        GridPane gp = new GridPane();
        ArrayList<Group> counters = new ArrayList<>();

        gp.setMaxHeight(50);
        gp.setMaxWidth(50);
        gp.setAlignment(Pos.BOTTOM_CENTER);


        for(ColorS c:ColorS.values()) {
            Group g = new Group();
            Text t = new Text();
            ImageView img = new ImageView(studentImages.get(c));
            img.setFitHeight(15);
            img.setFitWidth(15);
            img.setEffect(new DropShadow(1,Color.BLACK));
            int num = (int) students.stream().filter(s->s.equals(c)).count();

            t.setText(String.valueOf(num));
            g.getChildren().add(t);
            t.setTextAlignment(TextAlignment.CENTER);
            t.setX(4);
            t.setY(-2);
            t.setTextAlignment(TextAlignment.CENTER);
            t.setEffect(new DropShadow(2,Color.GREEN));

            g.getChildren().add(img);

            counters.add(g);
        }
        if(mn){
            Image mnImg = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/mother_nature.png"));
            ImageView mnView = new ImageView(mnImg);
            mnView.setFitHeight(25);
            mnView.setFitWidth(25);
            gp.add(mnView,2,0);
        }

        int i=0,j=0;
        for(Group counter:counters){
            gp.add(counter,i,2);
            i++;
            j=i%2==0?j:j+1;
        }
        gp.getRowConstraints().add(new RowConstraints(10));
        gp.getColumnConstraints().add(new ColumnConstraints(15));
        p.getChildren().add(gp);

        Image image = new Image(getClass().getResourceAsStream("/graphics/island1.png"));

        p.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(50,50,true,true,true,true))));

        p.setPrefWidth(50);
        p.setPrefHeight(50);
        return p;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    private void fillRandom(ArrayList<ColorS> list, int num){
        for(int i=0;i<num;i++){
            list.add(ColorS.values()[r.nextInt(4)]);
        }
    }
    private void createImages(){
        studentImages = new HashMap<>();
        profImages = new HashMap<>();
        towerImages = new HashMap<>();
        for(ColorS c:ColorS.values()){
            Image imageS = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/student_"+c.toString().toLowerCase()+".png"));
            studentImages.put(c,imageS);

            Image imageP = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/teacher_"+c.toString().toLowerCase()+".png"));
            profImages.put(c,imageP);
        }

        for(ColorT c : ColorT.values()){
            Image imageT = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/"+c.toString().toLowerCase()+"_tower.png"));
            towerImages.put(c,imageT);
        }
    }

}

