package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.client.GUIView.IMAGE_PATHS;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.CharacterDescription;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.server.virtualview.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * GameController class handles the main Game scene
 */
public class GameController implements GUIController{
    private HashMap<ColorS, Image> studentImages;
    private HashMap<ColorS, Image> profImages;
    private HashMap<ColorT, Image> towerImages;
    private HashMap<CharacterDescription,Image> charImages;
    private Image cloudImage;
    private HashMap<Integer, Image> assistantImages;
    private Image coinImage;

    private final int angle = 360;
    private GUI gui;
    private VirtualView virtualView;
    private final ArrayList<GridPane> entrancesGrids = new ArrayList<>();
    private final ArrayList<GridPane> hallGrids = new ArrayList<>();
    private final ArrayList<GridPane> profsGrids = new ArrayList<>();
    private final ArrayList<GridPane> towersGrids = new ArrayList<>();
    private final ArrayList<Pane> boards = new ArrayList<>();
    private final ArrayList<Pane> lastAssistants = new ArrayList<>();
    private final ArrayList<HBox> pInfo = new ArrayList<>();

    private final EventHandler<MouseEvent> studentHandler = this::clickOnStudent;
    private final EventHandler<MouseEvent> destinationHandler = this::studentDestination;


    private Node from = null;
    private Node to = null;
    private ColorS selected = null;
    private boolean selectedMN = false;
    private boolean selectedNoEntry = false;

    @FXML
    private Pane sc1, sc2, sc3, islandsPane, last1, last2, last3;
    @FXML
    private Label name1,name2,name3, boardCoins;
    @FXML
    private HBox pInfo1,pInfo2,pInfo3;
    @FXML
    private VBox charBox;
    @FXML
    private AnchorPane anchor;
    @FXML
    private VBox vBoxLastInfo;
    @FXML
    private ScrollPane paneLastInfo;


    /**
     * Performs all operations needed to initialize the scene: creates all Panes representing model objects and updates them
     */
    public void init() {
        Image image = new Image(getClass().getResourceAsStream(IMAGE_PATHS.MAIN_BACKGROUND.toString()));
        anchor.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(50,50,true,true,true,true))));
        createImages();
        boards.add(sc1);
        boards.add(sc2);
        pInfo.add(pInfo1);
        pInfo.add(pInfo2);

        if(virtualView.getVirtualPlayers().size()==3){
            boards.add(sc3);
            sc3.setVisible(true);
            pInfo.add(pInfo3);
        }

        lastAssistants.add(last1);
        lastAssistants.add(last2);
        if(virtualView.getVirtualPlayers().size()==3)
            lastAssistants.add(last3);

        for (Pane p : boards) {
            p.getChildren().forEach(this::addTo);
            p.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream(IMAGE_PATHS.BOARD.toString())),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(50,50,true,true,true,true))));
        }

        for (GridPane g : entrancesGrids) {
            updateEntrance(entrancesGrids.indexOf(g));
            g.setOnMouseClicked(destinationHandler);
        }
        for (GridPane g : hallGrids) {
            updateHall(hallGrids.indexOf(g));
            g.setOnMouseClicked(destinationHandler);
        }
        for (GridPane g : towersGrids)
            updateTowers(towersGrids.indexOf(g));

        updateAll();
    }

    /**
     * Updates the whole scene by calling all methods to update single view components
     */
    private void updateAll(){
        updateProfs();
        updateAssistants();
        drawIslands();
        drawCharacters();
        drawClouds();
        islandsPane.setMinHeight(520);
        islandsPane.setMinWidth(520);
        vBoxLastInfo.setAlignment(Pos.TOP_LEFT);
        vBoxLastInfo.setSpacing(10);
        paneLastInfo.setContent(vBoxLastInfo);
        paneLastInfo.vvalueProperty().bind(vBoxLastInfo.heightProperty());
        setNames();
        setCoins();
        boardCoins.setAlignment(Pos.CENTER);
        setBoardCoins();
    }

    /**
     * Updates the selected Entrance by reading data from VirtualView
     * @param index the selected Entrance's index
     */
    public void updateEntrance(int index){
        ArrayList<ColorS> entrance = virtualView.getVirtualPlayers().get(index).getVirtualBoard().getEntrance();
        entrancesGrids.get(calcIndex(index)).getChildren().clear();
        int entrIndex=0;
        for(int i=0;i<5;i++)
            for(int j=0;j<2;j++){
                //skip first cell
                j = i==0? j+1:j;
                if(entrIndex<entrance.size()) {
                    Circle c = new Circle(10);
                    c.setFill(new ImagePattern(studentImages.get(entrance.get(entrIndex))));
                    c.setEffect(new DropShadow(5,Color.BLACK));
                    entrancesGrids.get(calcIndex(index)).add(c, j, i);
                    c.setId(entrance.get(entrIndex).toString());
                    c.setOnMouseClicked(studentHandler);
                    entrIndex++;
                }
            }
    }

    /**
     * Updates the selected Hall by reading data from VirtualView
     * @param index the selected Hall's index
     */
    public void updateHall(int index){
        HashMap<ColorS, Integer> hall;
        hallGrids.get(calcIndex(index)).getChildren().clear();
            hall = (HashMap<ColorS, Integer>) virtualView.getVirtualPlayers().get(index).getVirtualBoard().getHall();

        for(int i=0;i<5;i++) {
            ColorS color = ColorS.values()[i];
            for (int j = 0; j < 10; j++) {
                if (hall.get(color)-j>0) {
                    Circle c = new Circle(10);
                    c.setFill(new ImagePattern(studentImages.get(color)));
                    c.setEffect(new DropShadow(5,Color.BLACK));
                    c.setId(color.toString());
                    c.setOnMouseClicked(studentHandler);
                    hallGrids.get(calcIndex(index)).add(c, j, i);
                }
            }
        }
    }

    /**
     * Updates Profs by reading data from VirtualView
     */
    public void updateProfs(){
        ArrayList<VirtualPlayer> vps = new ArrayList<>(virtualView.getVirtualPlayers());
        HashMap<ColorS, VirtualPlayer> tempProfs = virtualView.getVirtualProfs();
        HashMap<ColorS, Integer> profs = new HashMap<>();

        for(ColorS color:ColorS.values()){
            Optional<VirtualPlayer> owner = Optional.ofNullable(tempProfs.get(color));
            Optional<VirtualPlayer> vp = Optional.empty();
            if(owner.isPresent())
                vp = vps.stream().filter(p->p.getNickname().equals(owner.get().getNickname())).findAny();

            vp.ifPresentOrElse(virtualPlayer -> profs.put(color, vps.indexOf(virtualPlayer)),
                    ()-> profs.put(color,-1));

        }

        for(GridPane g:profsGrids)
            g.getChildren().clear();

        for(ColorS c:ColorS.values()){
            if(profs.get(c)>=0){
                int index = profs.get(c);
                GridPane g = profsGrids.get(calcIndex(index));
                Circle circle = new Circle(10);
                circle.setFill(new ImagePattern(profImages.get(c)));
                circle.setEffect(new DropShadow(5,Color.BLACK));
                g.add(circle,0, ColorS.valueOf(c.toString()).ordinal());
            }

        }
    }
    /**
     * Updates the selected Towers GridPane by reading data from VirtualView
     * @param index the selected Tower GridPane index
     */
    public void updateTowers(int index){
        ArrayList<ColorT> towers = virtualView.getVirtualPlayers().get(index).getVirtualBoard().getTowers();
        towersGrids.get(calcIndex(index)).getChildren().clear();

        int towerIndex=0;
        for(int i=0;i<4;i++)
            for(int j=0;j<2;j++){
                if(towerIndex<towers.size()) {
                    ImageView img = new ImageView(towerImages.get(towers.get(0)));
                    img.setFitHeight(25);
                    img.setFitWidth(25);
                    towersGrids.get(calcIndex(index)).add(img, j, i);
                    towerIndex++;
                }
            }
    }

    /**
     * Draws all Islands in the central Pane
     */
    public void drawIslands(){
        List<Node> islandsPanes = islandsPane.getChildren().stream().filter(p -> p.getStyleClass().contains("islandPane")).toList();
        islandsPane.getChildren().removeAll(islandsPanes);

        ArrayList<ImageView> islands = new ArrayList<>();
        Image image = new Image(getClass().getResourceAsStream(IMAGE_PATHS.ISLAND.toString()));

        for(int i =0;i<virtualView.getVirtualWorld().size();i++) {
            islands.add(new ImageView(image));
        }

        islands.forEach(i->{
            createIsland(islands, i);
        });

    }

    /**
     * Creates a StackPane representing a single Island with all the relevant Information
     * @param islands ArrayList containing all islands
     * @param i ImageView to be used as BackGround
     */
    private void createIsland(ArrayList<ImageView> islands, ImageView i) {
        int thisAngle = -(islands.indexOf(i)+1)*(angle/ islands.size());
        int index = islands.indexOf(i);
        VirtualIsland vi = virtualView.getVirtualWorld().get(index);
        ArrayList<ColorS> colors = vi.getStudents();
        ArrayList<ColorT> towers = vi.getTowers();

        StackPane p = createPane(colors, towers,virtualView.getMnPos()==index , vi.getNoEntry());
        p.setOnMouseClicked(destinationHandler);

        p.setId("island"+ islands.indexOf(i));

        islandsPane.getChildren().add(p);
        int radiusIslands = 200;
        circleLayout(p, thisAngle, radiusIslands);
    }

    /**
     * Puts the parameter in the correct Node list
     * @param n the Node to categorise
     */
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

    /**
     * Creates an Island Pane collecting all Island information
     * @param students the list of Students on the Island
     * @param towers the list of Towers on the Island
     * @param mn true if MN is on the Island
     * @param noEntry the number of NoEntry tiles on the Island
     * @return the Pane containing all Island information
     */
    private StackPane createPane(ArrayList<ColorS> students,ArrayList<ColorT> towers, boolean mn, int noEntry){
        StackPane p = new StackPane();
        ArrayList<Group> counters = new ArrayList<>();
        HBox box = new HBox();
        VBox vBox = new VBox();
        HBox towerBox = new HBox();

        vBox.setAlignment(Pos.CENTER);

        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);
        ImageView tower;

        if(towers.size()>0) {
          tower = new ImageView(towerImages.get(towers.get(0)));
        }
        else{
            tower = new ImageView(towerImages.get(ColorT.WHITE));
            tower.setVisible(false);
        }
        tower.setFitWidth(25);
        tower.setFitHeight(25);

        towerBox.getChildren().add(tower);
        towerBox.setAlignment(Pos.CENTER);

        String numT = "x" + towers.size();
        Text numText = new Text();
        numText.setText(numT);
        numText.setEffect(new DropShadow(1, Color.GREEN));
        if(towers.size()==0){
            numText.setVisible(false);
        }
        towerBox.getChildren().add(numText);

        setCounters(students, counters);
        vBox.getChildren().add(towerBox);

        HBox mnNoEntry = new HBox();
        mnNoEntry.setSpacing(2);
        mnNoEntry.setAlignment(Pos.CENTER);

        if(mn){
            Image mnImg = new Image(getClass().getResourceAsStream(IMAGE_PATHS.MN.toString()));
            ImageView mnView = new ImageView(mnImg);
            mnView.setFitHeight(25);
            mnView.setFitWidth(25);
            mnView.setOnMouseClicked(mouseEvent -> {
                selectedMN = true;
                mouseEvent.consume();
            });
            mnNoEntry.getChildren().add(mnView);
        }
        if(noEntry>0){
            Image nE = new Image(getClass().getResourceAsStream(IMAGE_PATHS.NO_ENTRY.toString()));
            for(int i =0;i<noEntry;i++) {
                ImageView nEView = new ImageView(nE);
                nEView.setFitHeight(25);
                nEView.setFitWidth(25);
                mnNoEntry.getChildren().add(nEView);
            }

        }
        if(mnNoEntry.getChildren().isEmpty()){
            Circle c = new Circle(12.5);
            c.setVisible(false);
            mnNoEntry.getChildren().add(c);
        }

        vBox.getChildren().add(box);
        vBox.getChildren().add(mnNoEntry);
        int i=0,j=0;
        for(Group counter:counters){
            box.getChildren().add(counter);
            i++;
            j=i%2==0?j:j+1;
        }

        p.getChildren().add(vBox);
        p.setAlignment(Pos.CENTER);

        Image image = new Image(getClass().getResourceAsStream(IMAGE_PATHS.ISLAND.toString()));

        p.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(50,50,true,true,true,true))));

        p.setPrefWidth(50);
        p.setPrefHeight(50);
        p.setMaxHeight(100);
        p.setMaxWidth(100);
        if(noEntry>=3){
            p.setMinHeight(100);
        }
        p.getStyleClass().add("islandPane");

        return p;
    }

    /**
     * Adds a counter for every Color to show how many Students are on the Island
     * @param students ArrayList containing all Students on the Island
     * @param counters ArrayList containing all counters needed to show information
     */
    private void setCounters(ArrayList<ColorS> students, ArrayList<Group> counters) {
        for(ColorS c:ColorS.values()) {
            Group g = new Group();
            Text t = new Text();
            ImageView img = new ImageView(studentImages.get(c));

            img.setFitHeight(15);
            img.setFitWidth(15);
            img.setEffect(new DropShadow(1,Color.BLACK));
            int num = (int) students.stream().filter(s->s.equals(c)).count();

            t.setText(String.valueOf(num));
            t.setTextAlignment(TextAlignment.CENTER);
            t.setX(4);
            t.setY(-2);
            t.setTextAlignment(TextAlignment.CENTER);
            t.setEffect(new DropShadow(2,Color.GREEN));

            g.getChildren().add(img);
            g.getChildren().add(t);

            counters.add(g);
        }
    }

    /**
     * Draws all Characters in the Characters Pane
     */
    public void drawCharacters(){
        charBox.setSpacing(10);
        charBox.getChildren().clear();

        ArrayList<VirtualCharacter> virtualCharacters = virtualView.getVirtualCharacters();

        for(VirtualCharacter vc:virtualCharacters){
            StackPane p = new StackPane();
            Image img;
            GridPane gp = new GridPane();

            p.setId("character"+ virtualCharacters.indexOf(vc));
            p.setOnMouseClicked(destinationHandler);

            gp.setMaxWidth((charBox.getPrefWidth()/2));
            gp.setMaxHeight(((charBox.getPrefHeight()-charBox.getSpacing()*2)/3)/3);
            gp.getColumnConstraints().add(new ColumnConstraints(20));

            CharacterDescription charIndex = Arrays.stream(CharacterDescription.values()).filter(c->c.getDesc().equals(vc.getDescription())).findFirst().get();

            img = charImages.get(charIndex);

            p.setPrefWidth(charBox.getPrefWidth());
            p.setPrefHeight((charBox.getPrefHeight()-charBox.getSpacing()*2)/3);
            p.setAlignment(Pos.CENTER);

            p.setBackground(new Background(new BackgroundImage(img,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(charBox.getPrefWidth(),(charBox.getPrefHeight()-charBox.getSpacing()*2)/3,false,false,true,false))));


            if(vc instanceof VirtualCharacterWithStudents vcs){
                drawCharStudents(gp, vcs);
            }
            else if(vc instanceof VirtualCharacterWithNoEntry vcn){
                drawCharNoEntry(gp, vcn);
            }
            else if(vc.getDescription().equals(CharacterDescription.CHAR9.getDesc())
            ||vc.getDescription().equals(CharacterDescription.CHAR12.getDesc())){
                Button b = new Button();
                b.setText("Choose Color");
                b.setMinWidth(50);
                b.setOnAction(actionEvent -> chooseColor());
                gp.getChildren().add(b);
            }

            if(vc.getCost()==charIndex.getCost()+1) {
                ImageView coin = new ImageView(coinImage);
                coin.setFitWidth(50);
                coin.setFitHeight(50);
                coin.setTranslateX(25);
                coin.setTranslateY(-50);
                p.getChildren().add(coin);
            }

            if(vc.isActive())
                p.setEffect(new DropShadow(100, Color.YELLOW));

            if(!gp.getChildren().isEmpty())
                p.getChildren().add(gp);

            p.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount()==2)
                    gui.sendMessageToServer(new PlayCharacter(charIndex));
            });
            charBox.getChildren().add(p);
        }

    }

    /**
     * Draws a Character with No Entry tiles on top
     * @param gp GridPane used to contain all No Entry tiles
     * @param vcn virtualCharacter to represent
     */
    private void drawCharNoEntry(GridPane gp, VirtualCharacterWithNoEntry vcn) {
        Image img;
        int j=0, w=0;
        for(int i = 0; i< vcn.getNoEntry(); i++){
            img = new Image(getClass().getResourceAsStream(IMAGE_PATHS.NO_ENTRY.toString()));
            ImageView noEntry = new ImageView(img);
            noEntry.setFitWidth(25);
            noEntry.setFitHeight(25);

            noEntry.setOnMouseClicked(mouseEvent -> {
                selectedNoEntry = true;
            });

            gp.add(noEntry,w,j);

            if(i==1) {
                j++;
                w=0;
            }else
                w++;
        }
    }

    /**
     * Draws a Character with Students on top
     * @param gp GridPane used to contain all Students
     * @param vcs virtualCharacter to represent
     */
    private void drawCharStudents(GridPane gp, VirtualCharacterWithStudents vcs) {
        int i=0,j=0;
        for(ColorS c: vcs.getStudents()){
            ImageView imgv = new ImageView(studentImages.get(c));
            imgv.setFitWidth(20);
            imgv.setFitHeight(20);
            imgv.setEffect(new DropShadow(5,Color.BLACK));
            imgv.setOnMouseClicked(studentHandler);
            imgv.setId(c.toString());
            gp.add(imgv,i,j);
            i++;
            if(i%3==0){
                i=0;
                j++;
            }
        }
    }

    /**
     * Update the view to show each Player's last chosen Assistant
     */
    public void updateAssistants(){
        ArrayList<VirtualPlayer> vps = virtualView.getVirtualPlayers();
        for(VirtualPlayer vp : vps){
            Assistant a = vp.getVirtualLastAssistant();
            if(a!=null) {
                Pane p = lastAssistants.get(calcIndex(virtualView.getVirtualPlayers().indexOf(vp)));
                Image img = assistantImages.get(a.getTurn());
                p.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        new BackgroundSize(charBox.getPrefWidth(), (charBox.getPrefHeight() - charBox.getSpacing() * 2) / 3, false, false, true, false))));
            }
        }
    }

    /**
     * Updates a single SchoolBoard by updating the entrance, hall and towers
     * @param index the SchoolBoard's index
     */
    public void updateSchoolBoard(int index){
        if(virtualView.getVirtualPlayers().get(index)!=null) {
            updateEntrance(index);
            updateTowers(index);
            updateHall(index);
        }
        updateProfs();
    }

    /**
     * Creates all Clouds and adds them to the correct Pane
     */
    public void drawClouds(){
        List<Node> cloudsPanes = islandsPane.getChildren().stream().filter(p -> p.getId().contains("cloud")).toList();
        islandsPane.getChildren().removeAll(cloudsPanes);
        int size = virtualView.getVirtualClouds().size();
        final int cloudSize = 90;
        for(int i = 0;i<size;i++){
            StackPane p = createCloudPanel(size, cloudSize, i);
            islandsPane.getChildren().add(p);
        }
    }

    /**
     * Creates a single Pane reprensenting a Cloud
     * @param size total number of Clouds (2/3)
     * @param cloudSize Cloud's dimension
     * @param i Cloud's index
     * @return a Pane showing the Clouds and every Student on in
     */
    private StackPane createCloudPanel(int size, int cloudSize, int i) {
        StackPane p = new StackPane();
        GridPane gp = new GridPane();

        p.setId("cloud"+ i);

        p.setBackground(new Background(new BackgroundImage(cloudImage,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(cloudSize, cloudSize,false,false,false,false))));

        p.setMinHeight(cloudSize);
        p.setMinWidth(cloudSize);

        drawStudentsCloud(gp, i);

        p.getChildren().add(gp);

        int thisAngle = i *angle/ size;
        int radiusClouds = 75;
        circleLayout(p, thisAngle, radiusClouds);

        p.setOnMouseClicked(mouseEvent -> gui.sendMessageToServer(new ChooseCloud(i)));
        return p;
    }

    /**
     * Positions a Pane along a circumference in the IslandPane
     * @param p the Pane to be positioned
     * @param thisAngle the Pane's angle in the circumference
     * @param radius the circumference's radius
     */
    private void circleLayout(StackPane p, int thisAngle, int radius) {
        p.setLayoutX(325+ radius *Math.sin(thisAngle*2*Math.PI/angle)-25*Math.sqrt(2));
        p.setLayoutY(248+ radius *Math.cos(thisAngle*2*Math.PI/angle)-25*Math.sqrt(2));
    }

    /**
     * Draws all Students on a Cloud
     * @param gp the GripPane holding all Student images
     * @param index the Cloud's index
     */
    private void drawStudentsCloud(GridPane gp, int index){
        ArrayList<ColorS> students;
        students = virtualView.getVirtualClouds().get(index).getStudents();
        int x=0, y=0;
        for(ColorS c : students){
            ImageView img = new ImageView(studentImages.get(c));
            img.setFitWidth(25);
            img.setFitHeight(25);
            gp.add(img,x,y);
            x++;
            if(x%2==0){
                x=0;
                y++;
            }
            gp.setTranslateY(20);
            gp.setTranslateX(20);
        }
    }

    /**
     * Sets the VirtualView attribute
     * @param virtualView the virtualView received from the Server
     */
    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Reads from file all Images needed in the Scene and stores them in Lists to be easily accessible
     */
    private void createImages(){
        studentImages = new HashMap<>();
        profImages = new HashMap<>();
        towerImages = new HashMap<>();
        charImages = new HashMap<>();
        assistantImages = new HashMap<>();

        for(ColorS c:ColorS.values()){
            Image imageS = new Image(getClass().getResourceAsStream(IMAGE_PATHS.STUDENT+c.toString().toLowerCase()+".png"));
            studentImages.put(c,imageS);

            Image imageP = new Image(getClass().getResourceAsStream(IMAGE_PATHS.PROF+c.toString().toLowerCase()+".png"));
            profImages.put(c,imageP);
        }
        for(ColorT c : ColorT.values()){
            Image imageT = new Image(getClass().getResourceAsStream(IMAGE_PATHS.TOWER+c.toString().toLowerCase()+"_tower.png"));
            towerImages.put(c,imageT);
        }

        Image imageC;
        for(int i = 1;i<=12;i++) {
            imageC = new Image(getClass().getResourceAsStream(IMAGE_PATHS.CHARACTER.toString()+i+".png"));
            charImages.put(CharacterDescription.values()[i-1], imageC);
        }

        Image imageA;
        for(int i = 1;i<=10;i++){
            imageA = new Image(getClass().getResourceAsStream(IMAGE_PATHS.ASSISTANT.toString()+i+"@3x.png"));
            assistantImages.put(i,imageA);
        }

        cloudImage = new Image(getClass().getResourceAsStream(IMAGE_PATHS.CLOUD.toString()));

        coinImage = new Image(getClass().getResourceAsStream(IMAGE_PATHS.COIN.toString()));
    }

    /**
     * Shows the Player's hand in a new window
     */
    public void showHand() {
        Stage window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTROLLERS.CHOOSE_ASSISTANT.toString()));
        ChooseAssistantController controller = (ChooseAssistantController) gui.getNameMapController().get(gui.getNameMapScene().get(CONTROLLERS.CHOOSE_ASSISTANT.toString()));

        window.setScene(controller.getScene(virtualView.getVirtualPlayers().indexOf(getLocalPlayer())));
        Platform.runLater(window::showAndWait);
    }

    /**
     * Shows a window that allows the Player to choose a Color
     */
    public void chooseColor(){
        Stage window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTROLLERS.CHOOSE_ASSISTANT.toString()));
        ChooseColorController controller = (ChooseColorController) gui.getNameMapController().get(gui.getNameMapScene().get(CONTROLLERS.CHOOSE_COLOR.toString()));

        window.setScene(controller.getScene());
        Platform.runLater(window::showAndWait);
    }

    /**
     * Handles clicks on a Student
     * @param e the MouseEvent
     */
    private void clickOnStudent(MouseEvent e){
        Node student = (Node) e.getSource(), parent = getParent((Node) e.getSource());
        boolean switchStudents = from!=null&&(from.getId().contains("e1")&&parent.getId().contains("h1")||(from.getId().contains("h1")&&parent.getId().contains("e1")));
        boolean characterEntrance = from!=null&&((from.getId().contains("e1")&&to.getId().contains("character"))||(from.getId().contains("character")&&to.getId().contains("e1")));

        if(switchStudents|| characterEntrance){
            gui.sendMessageToServer(new ChooseTwoColors(selected,ColorS.valueOf(student.getId())));
            return;
        }
        selected = ColorS.valueOf(student.getId());
        if(from == null)
            from = parent;
        else {
            to = parent;
            createMessage();
            from = null;
            to = null;
            selected = null;
        }
        e.consume();
    }

    /**
     * Handles clicks on a Student destination, a place where a Student can move
     * @param e the MouseEvent
     */
    private void studentDestination(MouseEvent e){
        String destinationId = ((Node) e.getSource()).getId();
        if(from!=null&&selected!=null){
            to = (Node) e.getSource();
            createMessage();
            from = null;
            to = null;
            selected = null;
        }
        else if(selectedMN){
            int dest = Integer.parseInt(destinationId.replace("island",""));
            int currMnPos = virtualView.getMnPos(), steps = 0;

            for(int i=currMnPos;i<virtualView.getVirtualWorld().size();){
                if(i==dest){
                    gui.sendMessageToServer(new MoveMN(steps));
                    selectedMN = false;
                    return;
                }
                i = (i+1)% virtualView.getVirtualWorld().size();
                steps++;
            }

        }
        else if(selectedNoEntry){
            destinationId = ((Node) e.getSource()).getId();
            int dest = Integer.parseInt(destinationId.replace("island",""));
            gui.sendMessageToServer(new ChooseIsland(dest));
            selectedNoEntry = false;
        }
        else if(destinationId!=null&&destinationId.contains("island")&&e.getClickCount()==2){
            int dest = Integer.parseInt(destinationId.replace("island",""));
            gui.sendMessageToServer(new ChooseIsland(dest));
        }

    }

    /**
     * Recursively finds the Node's first parent with an ID
     * @param n the Node to search the parent for
     * @return the Node's parent
     */
    private Node getParent(Node n){
        if(n.getParent().getId()!=null)
            return n.getParent();
        return getParent(n.getParent());
    }

    /**
     * Based on the information from the last Click creates and sends a Message to the Server
     */
    private void createMessage(){
        String fromId = from.getId();
        boolean fromEntrance = fromId.charAt(0)=='e'&&fromId.charAt(1)=='1';
        String toId = to.getId();
        if(!from.equals(to)){
            if(fromEntrance&&toId.contains("h1"))
                gui.sendMessageToServer(new EntranceToHall(selected));
            else if(fromEntrance && toId.contains("island"))
                gui.sendMessageToServer(new MoveToIsland(selected, Integer.parseInt(toId.replace("island",""))));
            if(fromId.contains("character")&&toId.contains("island")){
                gui.sendMessageToServer(new SpecialMoveIsland(selected,Integer.parseInt(toId.replace("island",""))));
            }
            if(fromId.contains("character") && toId.contains("h1"))
                gui.sendMessageToServer(new ChooseColor(selected));

        }
    }

    /**
     * Shows in a scrollPane the latest Information received
     * @param text the latest Information received
     */
    public void setLastInfo(String text){
        Label l = new Label();
        l.setWrapText(true);
        l.setMaxHeight(200);
        l.setMaxWidth(213);
        l.setText("["+new SimpleDateFormat("HH.mm.ss").format(new Date())+"] " + text);
        vBoxLastInfo.getChildren().add(l);
    }

    /**
     * Shows an Alert containing the latest Error received
     * @param text the latest Error received
     */
    public void setLastError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR,text, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Finds in the VirtualView the localPlayer
     * @return VirtualPlayer - the local Player
     */
    private VirtualPlayer getLocalPlayer(){
        return virtualView.getVirtualPlayers().stream().filter(vp->vp.getNickname().equals(gui.getNickname())).findFirst().get();
    }

    /**
     * Shows all Players' names in the correct order
     */
    private void setNames(){
        ArrayList<VirtualPlayer> vps = virtualView.getVirtualPlayers();
        name1.setText(getLocalPlayer().getNickname());
        name2.setText(vps.get((vps.indexOf(getLocalPlayer())+1)%vps.size()).getNickname());
        if(vps.size()==3)
            name3.setText(vps.get((vps.indexOf(getLocalPlayer())+2)%vps.size()).getNickname());
    }

    /**
     * Shows for each Player the number of coins they have
     */
    public void setCoins(){
        pInfo.forEach(p->{
            List<Node> coins = p.getChildren().stream().filter(c -> c.getId().equals("coin")).toList();
            p.getChildren().removeAll(coins);
        });
        ArrayList<VirtualPlayer> vps = virtualView.getVirtualPlayers();

        for(VirtualPlayer vp:vps) {
            for (int i = 0; i <vp.getVirtualCoins();i++)
            {
                ImageView coin = new ImageView(coinImage);
                coin.setId("coin");
                coin.setFitWidth(15);
                coin.setFitHeight(15);
                pInfo.get(calcIndex(vps.indexOf(vp))).getChildren().add(coin);
            }
        }
    }

    /**
     * Shows how many coins can still be earned by Players
     */
    public void setBoardCoins(){
            boardCoins.setText("You can still earn: "+virtualView.getVirtualCoins()+" coins");
    }

    /**
     * Shows a window containing basic information on how to Play and the Character's description
     */
    public void showHelp() {
        Stage window = new Stage();
        HelpController controller = (HelpController) gui.getNameMapController().get(gui.getNameMapScene().get(CONTROLLERS.HELP_CONTROLLER.toString()));

        controller.setGui(gui);
        window.setScene(controller.getScene());
        Platform.runLater(window::showAndWait);
    }

    /**
     * Deletes current selection
     */
    public void clearMove(){
        from = null;
        to = null;
        selected = null;
    }

    /**
     * Sends a Disconnect message to the Server
     */
    @FXML
    public void disconnect(){
        gui.sendMessageToServer(new Disconnect());
    }

    /**
     * Given a Player's index calculates the corresponding list index
     * @param index the Player's index in the virtualView's list
     * @return the corresponding board index
     */
    public int calcIndex(int index) {
        ArrayList<VirtualPlayer> vps = virtualView.getVirtualPlayers();
        int localPlayerIndex = virtualView.getVirtualPlayers().indexOf(getLocalPlayer());
        if(index==localPlayerIndex){
            return 0;
        }
        else if(index==(localPlayerIndex+1)%vps.size()){
            return 1;
        }
        else if(index==(localPlayerIndex+2)%vps.size()){
            return 2;
        }
        return 0;
    }

    /**
     * Sets the GUI object for the controller
     * @param gui GUI - the controller's GUI.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

}

