package it.polimi.ingsw.client.GUIView.controllers;

import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterDescription;
import it.polimi.ingsw.model.character.CharacterWithNoEntry;
import it.polimi.ingsw.model.character.CharacterWithStudent;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.server.virtualview.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
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

import java.util.*;
import java.util.List;

public class GameController implements GUIController{
    Random r = new Random(System.currentTimeMillis());
    private String nickname;
    private int numIslands = 12;
    private HashMap<ColorS, Image> studentImages;
    private HashMap<ColorS, Image> profImages;
    private HashMap<ColorT, Image> towerImages;
    private HashMap<CharacterDescription,Image> charImages;
    private ArrayList<Image> cloudImages;
    private HashMap<Integer, Image> assistantImages;
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

    private final EventHandler<MouseEvent> studentHandler = this::clickOnStudent;
    private final EventHandler<MouseEvent> destinationHandler = this::studentDestination;


    private Node from;
    private Node to;
    private ColorS selected;
    private boolean selectedMN = false;
    private boolean selectedNoEntry = false;
    private int startMN=-1;
    private int endMN=-1;

    @FXML
    private Pane sc1, sc2, sc3, islandsPane, last1, last2, last3;
    @FXML
    private VBox charBox;



    public void init() {
        createImages();
        boards.add(sc1);
        boards.add(sc2);
        if(virtualView.getVirtualPlayers().size()==3){
            boards.add(sc3);
            lastAssistants.add(last3);
        }

        lastAssistants.add(last1);
        lastAssistants.add(last2);


        for (Pane p : boards) {
            p.getChildren().forEach(this::addTo);
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
    }

    public int calcIndex(int index) {
        int i = 0, localPlayerIndex = virtualView.getVirtualPlayers().indexOf(getLocalPlayer());
        int diff = Math.abs(index-localPlayerIndex);
        if (index == localPlayerIndex)
            i = 0;
        else {
            if (diff % virtualView.getVirtualPlayers().size() % virtualView.getVirtualPlayers().size() == 1)
                i = 1;
            if (diff - localPlayerIndex % virtualView.getVirtualPlayers().size() % virtualView.getVirtualPlayers().size() == 2)
                i = 2;
        }
        return i;
    }

    public void updateEntrance(int index){
        //ArrayList<ColorS> entrance = new ArrayList<>();
        //TODO remove fillRandom and get correct entrance from virtualView
        ArrayList<ColorS> entrance = virtualView.getVirtualPlayers().get(index).getVirtualBoard().getEntrance();
        entrancesGrids.get(calcIndex(index)).getChildren().clear();
        fillRandom(entrance, 7);
        int entrIndex=0;
        for(int i=0;i<4;i++)
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

    public void updateHall(int index){
        //HashMap<ColorS, Integer> hall = new HashMap<>();
        hallGrids.get(calcIndex(index)).getChildren().clear();
        //TODO same as updateEntrance
        HashMap<ColorS, Integer> hall = (HashMap<ColorS, Integer>) virtualView.getVirtualPlayers().get(index).getVirtualBoard().getHall();

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

    public void updateProfs(){
        //HashMap<ColorS, Integer> profs = new HashMap<>();
        HashMap<ColorS, VirtualPlayer> tempProfs = virtualView.getVirtualProfs();
        HashMap<ColorS, Integer> profs = new HashMap<>();
        for(ColorS color:ColorS.values()){
            profs.put(color,virtualView.getVirtualPlayers().indexOf(tempProfs.get(color)));
        }
        for(GridPane g:profsGrids)
            g.getChildren().clear();

        //TODO insert correct hashmap from virtualView
        for(ColorS c:ColorS.values()){
            //TODO remove the following line

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

    public void updateTowers(int index){
        //ArrayList<ColorT> towers = new ArrayList<>();
        ArrayList<ColorT> towers = virtualView.getVirtualPlayers().get(index).getVirtualBoard().getTowers();
        towersGrids.get(calcIndex(index)).getChildren().clear();
        for(int k =0;k<5;k++)
            towers.add(ColorT.BLACK);
        int towerIndex=0;
        for(int i=0;i<4;i++)
            for(int j=0;j<2;j++){
                if(towerIndex<towers.size()) {
                    Circle c = new Circle(10);
                    c.setFill(new ImagePattern(towerImages.get(towers.get(0))));
                    towersGrids.get(calcIndex(index)).add(c, j, i);
                    towerIndex++;
                }
            }
    }

    public void drawIslands(){
        List<Node> islandsPanes = islandsPane.getChildren().stream().filter(p -> p.getStyleClass().contains("islandPane")).toList();
        islandsPane.getChildren().removeAll(islandsPanes);


        ArrayList<ImageView> islands = new ArrayList<>();
        Image image = new Image(getClass().getResourceAsStream("/graphics/island1.png"));
        for(int i =0;i<virtualView.getVirtualWorld().size();i++) {
            islands.add(new ImageView(image));
        }

        islands.forEach(i->{
            int thisAngle = (islands.indexOf(i)+1)*(angle/islands.size());
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

    private void drawCharacters(){
        //charBox.setAlignment(Pos.CENTER);
        charBox.setSpacing(10);

        charBox.getChildren().clear();

        //ArrayList<VirtualCharacter> virtualCharacters = createChars();
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
                p.getChildren().add(gp);
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
                p.getChildren().add(gp);
            }

            p.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount()==2)
                    gui.sendMessageToServer(new PlayCharacter(charIndex.get()));
            });
            charBox.getChildren().add(p);
        }

    }

    private void updateAssistants(){
        //for(int i = 0;i<3;i++){
            //TODO uncomment next lines and remove the preceding one
            for(VirtualPlayer vp : virtualView.getVirtualPlayers()){
            Assistant a = vp.getVirtualLastAssistant();
            if(a!=null) {
                //int num = r.nextInt(10)+1;
                //Assistant a = new Assistant(num/2,num, Mage.MAGE1);
                Pane p = lastAssistants.get(virtualView.getVirtualPlayers().indexOf(vp));
                //Pane p = lastAssistants.get(i);
                Image img = assistantImages.get(a.getTurn());
                p.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        new BackgroundSize(charBox.getPrefWidth(), (charBox.getPrefHeight() - charBox.getSpacing() * 2) / 3, false, false, true, false))));
            }
        }
    }

    private void drawClouds(){
        List<Node> cloudsPanes = islandsPane.getChildren().stream().filter(p -> p.getStyleClass().contains("cloud")).toList();
        islandsPane.getChildren().removeAll(cloudsPanes);
        int size = virtualView.getVirtualClouds().size();
        int cloudSize = 90;
        for(int i = 0;i<size;i++){
            StackPane p = new StackPane();
            p.setAlignment(Pos.CENTER);

            p.setId("cloud"+i);
            p.setBackground(new Background(new BackgroundImage(cloudImages.get(r.nextInt(4)),BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
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

    //TODO remove
    private void createClouds(Pane p, int index){
        //ArrayList<ColorS> students = new ArrayList<>();
        ArrayList<ColorS> students = virtualView.getVirtualClouds().get(index).getStudents();
        //fillRandom(students, 3);

        int j=0;
        ColorS currStudent = students.get(j);
        p.getChildren().add(createImage(currStudent, -26.5,-7));
        j++;
        currStudent = students.get(j);
        p.getChildren().add(createImage(currStudent, 19,-20));
        j++;
        currStudent = students.get(j);
        p.getChildren().add(createImage(currStudent, 8,25));
    }

    private ImageView createImage(ColorS c, double x, double y){
        ImageView img = new ImageView(studentImages.get(c));
        img.setFitHeight(35);
        img.setFitWidth(35);
        img.setTranslateX(x);
        img.setTranslateY(y);
        return img;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    private void fillRandom(ArrayList<ColorS> list, int num){
        for(int i=0;i<num;i++){
            list.add(ColorS.values()[r.nextInt(5)]);
        }
    }

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
    }


    public void again(){
        numIslands = r.nextInt(12)+1;
        drawIslands();
    }

    private void clickOnStudent(MouseEvent e){
        Node student = (Node) e.getSource(), parent = getParent((Node) e.getSource());
        if(from!=null&&(from.getId().contains("e1")&&parent.getId().contains("h1")||(from.getId().contains("h1")&&parent.getId().contains("e1")))){
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

    private void studentDestination(MouseEvent e){
        if(from!=null&&selected!=null){
            to = (Node) e.getSource();
            System.out.println("from: " +from.getId()+" to: "+to.getId()+" selected: "+ selected.toString());
            createMessage();
            from = null;
            to = null;
            selected = null;
        }
        else if(selectedMN){
            String destinationId = ((Node) e.getSource()).getId();
            int dest = Integer.parseInt(destinationId.replace("island",""));
            //TODO set currMnPos to value from virtualView
            int currMnPos = 1;
            for(int i=currMnPos;i<numIslands;){
                if(i==dest){
                    gui.sendMessageToServer(new MoveMN(i-currMnPos));
                    break;
                }
                i = (i+1)% numIslands;
            }
            System.out.println(dest);
            selectedMN = false;
        }
        else if(selectedNoEntry){
            String destinationId = ((Node) e.getSource()).getId();
            int dest = Integer.parseInt(destinationId.replace("island",""));
            gui.sendMessageToServer(new ChooseIsland(dest));
            selectedNoEntry = false;
        }
    }

    private Node getParent(Node n){
        if(n.getParent().getId()!=null)
            return n.getParent();
        return getParent(n.getParent());
    }

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




    //TODO remove, characters are taken from virtualView
    private ArrayList<VirtualCharacter> createChars(){
        ArrayList<VirtualCharacter> virtualCharacters = new ArrayList<>();
        CharacterWithStudent c1 = new CharacterWithStudent(1, "Take one Student from this card and place it on an Island of your choice. Then draw a new Student" +
                "from the bag and place it on this card.", 5);
        c1.add(ColorS.BLUE);
        c1.add(ColorS.GREEN);
        c1.add(ColorS.RED);
        c1.add(ColorS.YELLOW);
        VirtualCharacterWithStudents vc1 = new VirtualCharacterWithStudents(c1);
        virtualCharacters.add(vc1);

        CharacterWithNoEntry c2 = new CharacterWithNoEntry(1, "Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there, put the No Entry" +
                "tile back onto this card DO NOT calculate influence on that Island, or place any Towers.");
        VirtualCharacterWithNoEntry vc2 = new VirtualCharacterWithNoEntry(c2);
        virtualCharacters.add(vc2);

        Character c3 = new Character(1,"During this turn, you take control of any number of Professors even if you have the same number of Students" +
                "as the Player who currently controls them.");
        VirtualCharacter vc3 = new VirtualCharacter(c3);
        virtualCharacters.add(vc3);

        return virtualCharacters;
    }

    private VirtualPlayer getLocalPlayer(){
        return virtualView.getVirtualPlayers().stream().filter(vp->vp.getNickname().equals(gui.getNickname())).findFirst().get();
    }

}

