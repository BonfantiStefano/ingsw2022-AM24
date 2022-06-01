package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
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
    Random r = new Random(System.currentTimeMillis());
    private HashMap<ColorS, Image> studentImages;
    private HashMap<ColorS, Image> profImages;
    private HashMap<ColorT, Image> towerImages;
    private HashMap<CharacterDescription,Image> charImages;
    private ArrayList<Image> cloudImages;
    private HashMap<Integer, Image> assistantImages;
    private Image coinImage;

    private final int radiusIslands = 200;
    private final int radiusClouds = 75;
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


    private Node from;
    private Node to;
    private ColorS selected;
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
        Image image = new Image(getClass().getResourceAsStream("/graphics/Background.jpg"));
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
            p.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("/graphics/Plancia_DEF.png")),BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
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

        updateProfs();
        updateAssistants();
        drawIslands();
        drawCharacters();
        drawClouds();
        islandsPane.setMinHeight(520);
        islandsPane.setMinWidth(520);
        from = null;
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
        HashMap<ColorS, Integer> hall = new HashMap<>();
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
        ArrayList<VirtualPlayer> vps = new ArrayList<>();
        HashMap<ColorS, VirtualPlayer> tempProfs = new HashMap<>();

            vps = new ArrayList<>(virtualView.getVirtualPlayers());
            tempProfs = virtualView.getVirtualProfs();

        HashMap<ColorS, Integer> profs = new HashMap<>();

        for(ColorS color:ColorS.values()){
            Optional<VirtualPlayer> owner = Optional.ofNullable(tempProfs.get(color));
            Optional<VirtualPlayer> vp = Optional.empty();
            if(owner.isPresent())
                vp = vps.stream().filter(p->p.getNickname().equals(owner.get().getNickname())).findAny();

            ArrayList<VirtualPlayer> finalVps = vps;
            vp.ifPresentOrElse(virtualPlayer -> profs.put(color, finalVps.indexOf(virtualPlayer)),
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
        Image image = new Image(getClass().getResourceAsStream("/graphics/island1.png"));
        for(int i =0;i<virtualView.getVirtualWorld().size();i++) {
            islands.add(new ImageView(image));
        }

        islands.forEach(i->{
            int thisAngle = -(islands.indexOf(i)+1)*(angle/islands.size());
            int index = islands.indexOf(i);
            VirtualIsland vi = virtualView.getVirtualWorld().get(index);
            ArrayList<ColorS> colors = vi.getStudents();
            ArrayList<ColorT> towers = vi.getTowers();

            StackPane p = createPane(colors, towers,virtualView.getMnPos()==index , vi.getNoEntry());
            p.setOnMouseClicked(destinationHandler);

            p.setId("island"+islands.indexOf(i));
            p.setMaxHeight(100);
            p.setMaxWidth(100);


            islandsPane.getChildren().add(p);
            p.setLayoutX(325+radiusIslands*Math.sin(thisAngle*2*Math.PI/angle)-25*Math.sqrt(2));
            p.setLayoutY(248+radiusIslands*Math.cos(thisAngle*2*Math.PI/angle)-25*Math.sqrt(2));

        });

    }


    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
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
        vBox.getChildren().add(towerBox);

        HBox mnNoEntry = new HBox();
        mnNoEntry.setSpacing(2);
        mnNoEntry.setAlignment(Pos.CENTER);

        if(mn){
            Image mnImg = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/mother_nature.png"));
            ImageView mnView = new ImageView(mnImg);
            mnView.setFitHeight(25);
            mnView.setFitWidth(25);
            mnView.setOnMouseClicked(mouseEvent -> {
                selectedMN = true;
                System.out.println("Selected MN");
                mouseEvent.consume();
            });
            mnNoEntry.getChildren().add(mnView);
        }
        if(noEntry>0){
            Image nE = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/deny_island_icon.png"));
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
            //gp.add(counter,i,2);
            box.getChildren().add(counter);
            i++;
            j=i%2==0?j:j+1;
        }

        p.getChildren().add(vBox);
        p.setAlignment(Pos.CENTER);

        Image image = new Image(getClass().getResourceAsStream("/graphics/island1.png"));

        p.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(50,50,true,true,true,true))));

        p.setPrefWidth(50);
        p.setPrefHeight(50);
        if(noEntry>=3){
            p.setMinHeight(100);
        }
        p.getStyleClass().add("islandPane");

        return p;
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
            Image img = null;
            GridPane gp = new GridPane();

            p.setId("character"+ virtualCharacters.indexOf(vc));
            p.setOnMouseClicked(destinationHandler);

            //gp.getStyleClass().add("border");
            gp.setMaxWidth((charBox.getPrefWidth()/2));
            gp.setMaxHeight(((charBox.getPrefHeight()-charBox.getSpacing()*2)/3)/3);
            gp.getColumnConstraints().add(new ColumnConstraints(20));

            Optional<CharacterDescription> charIndex = Arrays.stream(CharacterDescription.values()).filter(c->c.getDesc().equals(vc.getDescription())).findFirst();
            if(charIndex.isPresent())
                img = charImages.get(charIndex.get());

            p.setPrefWidth(charBox.getPrefWidth());
            p.setPrefHeight((charBox.getPrefHeight()-charBox.getSpacing()*2)/3);
            p.setAlignment(Pos.CENTER);

            p.setBackground(new Background(new BackgroundImage(img,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(charBox.getPrefWidth(),(charBox.getPrefHeight()-charBox.getSpacing()*2)/3,false,false,true,false))));


            if(vc instanceof VirtualCharacterWithStudents vcs){
                int i=0,j=0;
                for(ColorS c:vcs.getStudents()){
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
            else if(vc instanceof VirtualCharacterWithNoEntry vcn){
                int j=0, w=0;
                for(int i=0;i<vcn.getNoEntry();i++){
                    img = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/deny_island_icon.png"));
                    ImageView noEntry = new ImageView(img);
                    noEntry.setFitWidth(25);
                    noEntry.setFitHeight(25);

                    noEntry.setOnMouseClicked(mouseEvent -> {
                        selectedNoEntry = true;
                        System.out.println("No Entry selected");
                    });

                    gp.add(noEntry,w,j);
                    if(i==1) {
                        j++;
                        w=0;
                    }else
                        w++;
                }
            }
            else if(vc.getDescription().equals(CharacterDescription.CHAR9.getDesc())
            ||vc.getDescription().equals(CharacterDescription.CHAR12.getDesc())){
                Button b = new Button();
                b.setText("Choose Color");
                b.setMinWidth(50);
                b.setOnAction(actionEvent -> chooseColor());
                gp.getChildren().add(b);
            }

            if(vc.getCost()==charIndex.get().getCost()+1) {
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
                    gui.sendMessageToServer(new PlayCharacter(charIndex.get()));
            });
            charBox.getChildren().add(p);
        }

    }

    public void updateAssistants(){
        ArrayList<VirtualPlayer> vps = new ArrayList<>();

            vps = virtualView.getVirtualPlayers();
        for(VirtualPlayer vp : vps){
            Assistant a = vp.getVirtualLastAssistant();
            if(a!=null) {
                //int num = r.nextInt(10)+1;
                //Assistant a = new Assistant(num/2,num, Mage.MAGE1);
                Pane p = lastAssistants.get(calcIndex(virtualView.getVirtualPlayers().indexOf(vp)));
                //Pane p = lastAssistants.get(i);
                Image img = assistantImages.get(a.getTurn());
                p.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        new BackgroundSize(charBox.getPrefWidth(), (charBox.getPrefHeight() - charBox.getSpacing() * 2) / 3, false, false, true, false))));
            }
        }
    }
    public void updateSchoolBoard(int index){
        if(virtualView.getVirtualPlayers().get(index)!=null) {
            updateEntrance(index);
            updateTowers(index);
            updateHall(index);
        }
        updateProfs();
    }

    public void drawClouds(){
        List<Node> cloudsPanes = islandsPane.getChildren().stream().filter(p -> p.getId().contains("cloud")).toList();
        islandsPane.getChildren().removeAll(cloudsPanes);
        int size = virtualView.getVirtualClouds().size();
        int cloudSize = 90;
        for(int i = 0;i<size;i++){
            StackPane p = new StackPane();
            p.setAlignment(Pos.CENTER);

            p.setId("cloud"+i);

            p.setBackground(new Background(new BackgroundImage(cloudImages.get(r.nextInt(5)),BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(cloudSize,cloudSize,false,false,false,false))));

            p.setMinHeight(cloudSize);
            p.setMinWidth(cloudSize);

            createClouds(p,i);

            int thisAngle = i*angle/size;
            p.setLayoutX(325+radiusClouds*Math.sin(thisAngle*2*Math.PI/angle)-25*Math.sqrt(2));
            p.setLayoutY(248+radiusClouds*Math.cos(thisAngle*2*Math.PI/angle)-25*Math.sqrt(2));

            int finalI = i;
            p.setOnMouseClicked(mouseEvent -> gui.sendMessageToServer(new ChooseCloud(finalI)));
            islandsPane.getChildren().add(p);
        }
    }

    private void createClouds(StackPane p, int index){
        ArrayList<ColorS> students;
        students = virtualView.getVirtualClouds().get(index).getStudents();
        if(students.size()>=3) {
            int j = 0;
            ColorS currStudent;
            currStudent = students.get(j);
            p.getChildren().add(createImage(currStudent, -26.5, -7));
            j++;
            currStudent = students.get(j);
            p.getChildren().add(createImage(currStudent, 19, -20));
            j++;
            currStudent = students.get(j);
            p.getChildren().add(createImage(currStudent, 8, 25));
            if (students.size() == 4) {
                j++;
                currStudent = students.get(j);
                p.getChildren().add(createImage(currStudent, 0, -15));

            }
        }
    }

    /**
     * Creates a Student Images in the specified positions
     * @param c the Student's color
     * @param x  X coordinate
     * @param y y coordinate
     * @return an Imageview with the specified properties
     */
    private ImageView createImage(ColorS c, double x, double y){
        ImageView img = new ImageView(studentImages.get(c));
        img.setFitHeight(35);
        img.setFitWidth(35);
        img.setTranslateX(x);
        img.setTranslateY(y);
        img.setId("cloud");
        return img;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

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
        cloudImages = new ArrayList<>();

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

        Image imageC;
        for(int i = 1;i<=12;i++) {
            imageC = new Image(getClass().getResourceAsStream("/graphics/Characters/CarteTOT_front"+i+".png"));
            charImages.put(CharacterDescription.values()[i-1], imageC);
        }

        Image imageA;
        for(int i = 1;i<=10;i++){
            imageA = new Image(getClass().getResourceAsStream("/graphics/Assistants/Animali_1_"+i+"@3x.png"));
            assistantImages.put(i,imageA);
        }

        Image imageCloud;
        for(int i =1;i<=4;i++){
            imageCloud = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/cloud_card_"+i+".png"));
            cloudImages.add(imageCloud);
        }
        cloudImages.add(new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/cloud_card_5.png")));
        coinImage = new Image(getClass().getResourceAsStream("/graphics/wooden_pieces/Moneta_base.png"));
    }

    public void showHand() {
        Stage window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTROLLERS.CHOOSE_ASSISTANT.toString()));
        ChooseAssistantController controller = (ChooseAssistantController) gui.getNameMapController().get(gui.getNameMapScene().get(CONTROLLERS.CHOOSE_ASSISTANT.toString()));

        controller.setGui(gui);
        window.setScene(controller.getScene(virtualView.getVirtualPlayers().indexOf(getLocalPlayer())));
        Platform.runLater(window::showAndWait);
    }

    public void chooseColor(){
        Stage window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTROLLERS.CHOOSE_ASSISTANT.toString()));
        ChooseColorController controller = (ChooseColorController) gui.getNameMapController().get(gui.getNameMapScene().get(CONTROLLERS.CHOOSE_COLOR.toString()));

        controller.setGui(gui);
        window.setScene(controller.getScene());
        Platform.runLater(window::showAndWait);
    }

    /**
     * Handles clicks on a Student
     * @param e the MouseEvent
     */
    private void clickOnStudent(MouseEvent e){
        Node student = (Node) e.getSource(), parent = getParent((Node) e.getSource());
        if(from!=null&&((from.getId().contains("e1")&&parent.getId().contains("h1")||(from.getId().contains("h1")&&parent.getId().contains("e1")))||
                ((from.getId().contains("e1")&&to.getId().contains("character"))||(from.getId().contains("character")&&to.getId().contains("e1"))))){
            gui.sendMessageToServer(new ChooseTwoColors(selected,ColorS.valueOf(student.getId())));
        }
        selected = ColorS.valueOf(student.getId());
        if(from == null)
            from = parent;
        else {
            to = parent;
            System.out.println("from: " +from.getId()+" to: "+to.getId()+" selected: "+ selected.toString());
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
            System.out.println("from: " +from.getId()+" to: "+to.getId()+" selected: "+ selected.toString());
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
                    break;
                }
                i = (i+1)% virtualView.getVirtualWorld().size();
                steps++;
            }
            System.out.println(dest);
            selectedMN = false;
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

    public void setLastInfo(String text){
        Label l = new Label();
        l.setWrapText(true);
        l.setMaxHeight(200);
        l.setMaxWidth(213);
        l.setText("["+new SimpleDateFormat("HH.mm.ss").format(new Date())+"] " + text);
        vBoxLastInfo.getChildren().add(l);
    }

    public void setLastError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR,text, ButtonType.OK);
        alert.showAndWait();
    }

    private VirtualPlayer getLocalPlayer(){
        return virtualView.getVirtualPlayers().stream().filter(vp->vp.getNickname().equals(gui.getNickname())).findFirst().get();
    }

    private void setNames(){
        ArrayList<VirtualPlayer> vps = virtualView.getVirtualPlayers();
        name1.setText(getLocalPlayer().getNickname());
        name2.setText(vps.get((vps.indexOf(getLocalPlayer())+1)%vps.size()).getNickname());
        if(vps.size()==3)
            name3.setText(vps.get((vps.indexOf(getLocalPlayer())+2)%vps.size()).getNickname());
    }

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
    public void setBoardCoins(){
            boardCoins.setText("You can still earn: "+virtualView.getVirtualCoins()+" coins");
    }

    public void showHelp() {
        Stage window = new Stage();
        HelpController controller = (HelpController) gui.getNameMapController().get(gui.getNameMapScene().get(CONTROLLERS.HELP_CONTROLLER.toString()));

        controller.setGui(gui);
        window.setScene(controller.getScene());
        Platform.runLater(window::showAndWait);
    }

    public void clearMove(){
        from = null;
        to = null;
        selected = null;
    }

    @FXML
    public void disconnect(){
        gui.sendMessageToServer(new Disconnect());
    }

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

}

