package it.polimi.ingsw.client.CLIView;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.CharacterDescription;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Update.*;
import it.polimi.ingsw.server.virtualview.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class CLI represents the terminal UI
 */
public class CLI implements Runnable{
    //TODO add handle disconnection
    private VirtualView virtualView;
    private final Client client;
    private Welcome welcome;
    private boolean gameActive;
    private ArrayList<String> lastInfo;
    private boolean inLobby;

    private final Scanner input;

    /**
     * Creates a new CLI object
     * @param client the Client creating the UI
     */
    public CLI(Client client) {
        input = new Scanner(System.in);
        virtualView = new VirtualView();
        this.client = client;
        welcome = null;
        //TODO implement set gameActive based on messages from Server
        gameActive = true;
        lastInfo= new ArrayList<>();
        inLobby=false;
    }

    /**
     * Handles the continuous loop
     */
    public void run() {
        getInfo();

        while(client.isActive()) {
            String s = input.nextLine();
            parseInput(s);
        }
    }

    /**
     * Handles all Updates calling the respective visitor method
     * @param a the Update received
     */
    public void handleMessage(Answer a){
        a.accept(this);
        if(gameActive&&(a instanceof Update))
            printView();
    }

    /**
     * Parses the user's input and tries to match it to all possible commands
     * @param s the user's input
     */
    public void parseInput(String s){
        Pattern pattern;
        Matcher matcher;
        for(REGEX r:REGEX.values()){
            pattern = Pattern.compile(r.toString(), Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(s);

            if(matcher.find()) {
                Request msg = createMessage(r, s);
                System.out.println(toJson(msg));
                client.sendMessage(toJson(msg));
                return;
            }
        }
        System.out.println("Invalid command!");

    }

    /**
     * Converts an Object to Json format
     * @param r the Client's request
     * @return Request Object containing the Message
     */
    public String toJson(Object r){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }

    /**
     * Based on the Pattern matched parse the user's input
     * @param r the Pattern matched
     * @param s the user's input
     * @return the Request corresponding to the user's input
     */
    private Request createMessage(REGEX r, String s){
        //TODO check all substring indexes
        switch (r){
            case DISCONNECT: return new Disconnect();
            case MOVE_MN: return new MoveMN(Integer.parseInt(s.substring(8)));
            case ENTR_HALL: return new EntranceToHall(colorByString(s.substring(5)));
            case ONE_COLOR: return new ChooseColor(colorByString(s));
            case PLAY_CHAR:
                int index = Integer.parseInt(s.substring(5))-1;
                //get the corresponding enum value
                CharacterDescription c = Arrays.stream(CharacterDescription.values()).filter(c1 -> c1.getDesc().equals(virtualView.getVirtualCharacters().get(index).getDescription())).findFirst().get();
                return new PlayCharacter(c);
            case CHOOSE_ASSISTANT: return new ChooseAssistant(Integer.parseInt(s.substring(10))-1);
            case TO_ISLAND:
                ColorS colorS = colorByString(s.substring(5));
                int in = Integer.parseInt(s.split(" ")[3])-1;
                //TODO check if input.nextLine() is needed here
                return new MoveToIsland(colorS, in);
            case CHOOSE_CLOUD: return new ChooseCloud(Integer.parseInt(s.substring(6))-1);
            case TWO_COLORS:
                ColorS first, second;
                first = colorByString(s.split(" ")[0]);
                second =  colorByString(s.split(" ")[1]);
                return new ChooseTwoColors(first,second);
            case SPECIAL_MOVE:
                ColorS color = colorByString(s.split(" ")[1]);
                int i = Integer.parseInt(s.split(" ")[3]);
                return new SpecialMoveIsland(color, i);
            }

        return null;
    }

    /**
     * Prints elements contained in the VirtualView
     */
    private synchronized void printView() {
        if(gameActive) {
            if(System.getProperty("os.name").contains("Windows")){
                try {
                    new ProcessBuilder("cmd.exe", "/c", "chcp 65001").inheritIO().start().waitFor();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
            clearScreen();
            ArrayList<VirtualIsland> virtualWorld = virtualView.getVirtualWorld();

            ArrayList<VirtualIsland> firstHalf = new ArrayList<>(virtualWorld.subList(0, virtualWorld.size() / 2));
            ArrayList<VirtualIsland> secondHalf = new ArrayList<>(virtualWorld.subList(virtualWorld.size() / 2, virtualWorld.size()));
            drawIslands(firstHalf);
            drawIslands(secondHalf);

            //drawIslands(virtualWorld);
            drawClouds(virtualView.getVirtualClouds());
            if (virtualView.getVirtualCharacters().size() > 0)
                drawCharacters(virtualView.getVirtualCharacters());
            for (VirtualPlayer vp : virtualView.getVirtualPlayers()) {
                drawSchoolBoard(vp.getVirtualBoard(), vp.getNickname(), virtualView.getVirtualProfs());

                if (vp.getVirtualLastAssistant() != null) {
                    System.out.print(vp.getNickname() + "'s last assistant played: ");
                    System.out.println("Turn: " + vp.getVirtualLastAssistant().getTurn() + " Steps: " + vp.getVirtualLastAssistant().getMNsteps());
                }

                if (virtualView.getVirtualCharacters().size() != 0) {
                    System.out.println("Player: " + vp.getNickname() + " has " + vp.getVirtualCoins() + " coins.");
                }
            }
            lastInfo.forEach(System.out::println);
        }
    }

    /**
     * Ask the Player whether they want to create a Lobby or Join one
     */
    private void getInfo() {
        clearScreen();
        ArrayList<VirtualLobby> lobbies = new ArrayList<>();
        if(welcome!=null) {
            printLobbies(welcome);
            lobbies=welcome.getLobbies();
        }
        String answer;
        do {
            System.out.println("Do you want to Join a Lobby? (y/n)");
            answer = input.nextLine();
        } while ((!answer.equals("y") && !answer.equals("n"))||(answer.equals("y")&&lobbies.size()==0));

        if (answer.equals("y")) {
            //TODO check the value of the lobby's index
            int index;
            do {
                System.out.println("Insert the Lobby's number: ");
                index = Integer.parseInt(input.nextLine());
            }while(index<0||index> lobbies.size()-1);

            String nickname;
            do {
                System.out.println("Choose your nickname: ");
                nickname = input.nextLine();
                if(getLobbyByIndex(lobbies, index) == null) {
                    System.out.println("Invalid lobby index!");
                } else if (getLobbyByIndex(lobbies, index).getNicknames().contains(nickname)) {
                    System.out.println("Nickname already in use!");
                    nickname = null;
                }
            } while (nickname == null);

            int mageIndex;
            do {
                System.out.println("Choose your Mage (1,2,3,4):");
                mageIndex = Integer.parseInt(input.nextLine());
                if (getLobbyByIndex(lobbies, index).getMages().contains(Mage.values()[mageIndex-1])) {
                    System.out.println("Mage already in use!");
                    mageIndex = -1;
                }
            } while (mageIndex < 0 || mageIndex > 4);
            //TODO print also the color of the tower
            int towerIndex;
            do {
                System.out.println("Choose your TowerColor (1,2"+ (lobbies.get(index).getNumPlayers()==2 ? ")":",3)")+":");
                towerIndex = Integer.parseInt(input.nextLine());
                if (getLobbyByIndex(lobbies, index).getTowers().contains(ColorT.values()[towerIndex-1])) {
                    System.out.println("Tower Color already in use!");
                    towerIndex = -1;
                }
            } while ((towerIndex < 0 || towerIndex > 4)||(towerIndex==3&&lobbies.get(index).getNumPlayers()==2));

            Join msg = new Join(nickname, Mage.values()[mageIndex-1], ColorT.values()[towerIndex-1], index);
            client.sendMessage(toJson(msg));
        }
        else{
            System.out.println("Creating a lobby!");
            int numPlayers;
            do {
                System.out.println("How many Players will the Game have? (2/3)");
                numPlayers = Integer.parseInt(input.nextLine());
            }while(numPlayers<2 || numPlayers>3);
            String expert;
            do{
                System.out.println("Do you want to create an Expert Game? (y/n)");
                expert = input.nextLine();
            }while(!expert.equals("y")&&!expert.equals("n"));

            String nickname;
            do {
                System.out.println("Choose your nickname: ");
                nickname = input.nextLine();
            } while (nickname == null);

            int mageIndex;
            do {
                System.out.println("Choose your Mage (1,2,3,4):");
                mageIndex = Integer.parseInt(input.nextLine());
            } while (mageIndex < 0 || mageIndex > 4);

            int towerIndex;
            do {
                System.out.println("Choose your TowerColor (1,2"+ (numPlayers==2 ? ")":",3)")+":");
                towerIndex = Integer.parseInt(input.nextLine());
            } while (towerIndex < 0 || towerIndex > 4);

            GameParams msg = new GameParams(numPlayers, expert.equals("y"), nickname,Mage.values()[mageIndex-1], ColorT.values()[towerIndex-1]);
            client.sendMessage(toJson(msg));
        }

    }

    /**
     * Receives an update and calls the visitor's method
     * @param u the Update received
     */
    public void update(Update u){
        u.accept(this);
        printView();
    }

    /**
     * Prints a SchoolBoard
     * @param schoolBoard the SchoolBoard to print
     * @param profs the HashMap containing all Profs
     */
    public void drawSchoolBoard(VirtualSchoolBoard schoolBoard, String nickname, HashMap<ColorS, VirtualPlayer> profs){
        String appendix = "'s SchoolBoard";
        ArrayList<ColorS> entrance = schoolBoard.getEntrance();
        HashMap<ColorS, Integer> hall = (HashMap<ColorS, Integer>) schoolBoard.getHall();
        ArrayList<ColorT> towers = schoolBoard.getTowers();
        ArrayList<StringBuilder> lines = new ArrayList<>();
        final int xSize=37;

        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        firstLine(currLine, nickname+appendix, xSize);
        int entrIndex = 0, towIndex = 0;



        for(ColorS c: ColorS.values()) {
            currLine = new StringBuilder();
            lines.add(currLine);
            //for every row
            currLine.append(BOX.VERT).append(" ");
            for (int i = 1; i < 3; i++) {
                if (entrIndex < entrance.size()) {
                    currLine.append(getChar(entrance.get(entrIndex))).append(" ");
                    entrIndex++;
                } else
                    currLine.append("  ");
            }

            currLine.append(BOX.VERT).append(" ");
            for (int w =0; w < hall.get(c); w++){
                currLine.append(getChar(c)).append(" ");
            }

            currLine.append("  ".repeat(Math.max(0, 10 - hall.get(c))));

            currLine.append(BOX.VERT).append(" ");

            if(profs!=null&&profs.get(c)!=null) {
                if (profs.get(c).getNickname().equals(nickname))
                    currLine.append(getChar(c)).append(" ");
                else
                    currLine.append("  ");
            }
            else
                currLine.append("  ");

            //currLine.append(" ").append(BOX.VERT).append(" ");
            currLine.append(BOX.VERT).append(" ");

            for(int w = 0; w<2;w++){
                if(towIndex<towers.size()) {
                    currLine.append(getChar(towers.get(towIndex))).append(BOX.CIRCLE).append(" ").append(Color.RESET);
                    towIndex++;
                }
                else
                    currLine.append("  ");
            }

            currLine.append(BOX.VERT);

       }
        lastLine(xSize,1,lines);

        lines.forEach(System.out::println);
    }

    /**
     * Gets the special Char corresponding to the parameter's ColorS
     * @param c the ColorS being searched
     * @return the Symbol and Color corresponding to the parameter
     */
    private String getChar(ColorS c) {
        final String circle = BOX.CIRCLE.toString();
        return switch (c) {
            case BLUE -> Color.ANSI_BLUE + circle + Color.RESET;
            case GREEN -> Color.ANSI_GREEN + circle + Color.RESET;
            case YELLOW -> Color.ANSI_YELLOW + circle + Color.RESET;
            case RED -> Color.ANSI_RED + circle + Color.RESET;
            case PINK -> Color.ANSI_PURPLE + circle + Color.RESET;
        };
    }

    /**
     * Gets the special Char corresponding to the parameter's ColorS
     * @param c the ColorS being searched
     * @return the Symbol and Color corresponding to the parameter
     */
    private String getChar(ColorT c) {
        return switch (c) {
            case BLACK -> Color.ANSI_BLACK.toString();
            case WHITE -> Color.RESET;
            case GREY -> Color.ANSI_GREY.toString();

        };
    }

    /**
     * Draws all Characters
     * @param characters ArrayList containing the VirtualCharacters
     */
    public void drawCharacters(ArrayList<VirtualCharacter> characters){
        int xSize=25;
        ArrayList<StringBuilder> lines = new ArrayList<>();
        lines.add(new StringBuilder());
        lines.add(new StringBuilder());
        lines.add(new StringBuilder());


        for(VirtualCharacter c:characters)
            firstLine(lines.get(0),"Character no. "+(characters.indexOf(c)+1),xSize);

        StringBuilder secondLine = lines.get(1);
        for(int j=0;j<3;j++){
            //TODO insert bool isActive in character
            boolean active = false;
            secondLine.append(BOX.VERT);
            secondLine.append(" ");

            secondLine.append("Cost: ").append(characters.get(j).getCost());
            if (active) {
                secondLine.append(Color.ANSI_GREEN).append(" Active");
                secondLine.append(" ".repeat(xSize - Color.ANSI_GREEN.toString().length()- " Active".length() -3));
            }
            else {
                secondLine.append(Color.ANSI_BLACK).append(" Not Active");
                secondLine.append(" ".repeat(xSize - Color.ANSI_BLACK.toString().length()- " Not Active".length() -3));
            }
            secondLine.append(Color.RESET);


            secondLine.append(BOX.VERT);

        }

        StringBuilder thirdLine = lines.get(2);
        for(int j=0;j<3;j++) {
            thirdLine.append(BOX.VERT);
            thirdLine.append(" ");
            int index =0;
            VirtualCharacter c = characters.get(j);
            for (int i = 0; i < xSize-1; i++) {
                if (c instanceof VirtualCharacterWithStudents cs){
                    if(index<cs.getStudents().size()&&i%2==0) {
                        thirdLine.append(getChar(cs.getStudents().get(index)));
                        index++;
                    }
                    else
                        thirdLine.append(" ");
                }
                else if(c instanceof VirtualCharacterWithNoEntry cn){
                    if(index<cn.getNoEntry()&&i%2==0) {
                        thirdLine.append(" ").append(BOX.FORBIDDEN);
                        index++;
                    }
                    else
                        thirdLine.append(" ");
                }
                else
                    thirdLine.append(" ");

            }
            thirdLine.append(BOX.VERT);
        }

        lastLine(xSize,characters.size(),lines);

        lines.forEach(System.out::println);
    }

    /**
     * Draws all Islands, one next to the other
     * @param islands ArrayList containing all the VirtualIslands
     */
    public void drawIslands(ArrayList<VirtualIsland> islands){
        ArrayList<VirtualIsland> allIslands = virtualView.getVirtualWorld();
        int xSize = 17;
        int numIslands = islands.size();
        ArrayList<StringBuilder> lines = new ArrayList<>();

        //TODO add MN position
        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        for(VirtualIsland i:islands)
            firstLine(currLine,"Island no. "+ (allIslands.indexOf(i)+1) + (allIslands.indexOf(i)==virtualView.getMnPos()?BOX.HORIZ+"MN":""), xSize);

        currLine = new StringBuilder();
        lines.add(currLine);
        for (VirtualIsland island : islands) {
            currLine.append(BOX.VERT);
            currLine.append(" Towers: ");
            if (island.getTowerColor().isPresent()) {
                currLine.append(getChar(island.getTowerColor().get())).append(island.getTowerColor().get());
                currLine.append(Color.RESET);
                currLine.append("x").append(island.getNumSubIsland());
                currLine.append(" ".repeat(island.getTowerColor().get().equals(ColorT.GREY) ? 2 : 1));
            } else {
                currLine.append("None");
                currLine.append(" ".repeat(xSize - "None".length() - " Towers: ".length()));
            }
            currLine.append(BOX.VERT);
        }


        for (ColorS c : ColorS.values()) {
            currLine = new StringBuilder();
            lines.add(currLine);
            for (VirtualIsland island : islands) {
                currLine.append(BOX.VERT).append(" ");
                currLine.append(getChar(c));
                currLine.append(c.toString()).append(":");
                currLine.append(island.getNumStudentByColor(c));
                currLine.append(" ".repeat(xSize - 3 - c.toString().length() - 1));
                currLine.append(BOX.VERT);
            }
        }

        lastLine(xSize, numIslands, lines);
        lines.forEach(System.out::println);
    }

    /**
     * Builds the drawing's first line
     * @param currLine StringBuilder to use
     * @param text String containing the drawing's label
     * @param xSize the drawing's X dimension
     */
    public void firstLine(StringBuilder currLine, String text, int xSize){
        currLine.append(BOX.TOP_LEFT);
        currLine.append(text);
        currLine.append(BOX.HORIZ.toString().repeat(xSize+1-text.length()-(text.length()>=10?1:0)));
        currLine.append(BOX.TOP_RIGHT);
    }

    /**
     * Builds the drawing's last line
     * @param lines ArrayList containing all lines
     * @param repeat how many boxes are in this line
     * @param xSize the drawing's X dimension
     */
    private void lastLine(int xSize, int repeat, ArrayList<StringBuilder> lines) {
        StringBuilder currLine;
        currLine=new StringBuilder();
        lines.add(currLine);
        for(int i = 0; i< repeat; i++) {
            currLine.append(BOX.BOT_LEFT);
            currLine.append(BOX.HORIZ.toString().repeat(xSize));
            currLine.append(BOX.BOT_RIGHT);
        }
    }

    /**
     * Draws all Clouds
     * @param clouds ArrayList containing all VirtualClouds
     */
    public void drawClouds(ArrayList<VirtualCloud> clouds){
        ArrayList<StringBuilder> lines = new ArrayList<>();
        int xSize = 20;

        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        for(VirtualCloud c: clouds)
            firstLine(currLine,"Cloud no."+ (clouds.indexOf(c) + 1),xSize);

        currLine = new StringBuilder();


        for (VirtualCloud cloud : clouds) {
            currLine.append(BOX.VERT).append(" ");
            int index = 0;
            for (int j = 0; j < xSize - 1; j++) {
                ArrayList<ColorS> students = cloud.getStudents();
                if (index < students.size() && j % 2 == 0) {
                    currLine.append(getChar(students.get(index)));
                    index++;
                } else
                    currLine.append(" ");
            }
            currLine.append(BOX.VERT);
        }
        lines.add(currLine);
        currLine = new StringBuilder();

        for(int i = 0;i<clouds.size();i++){
            currLine.append(BOX.BOT_LEFT);
            currLine.append(BOX.HORIZ.toString().repeat(xSize));
            currLine.append(BOX.BOT_RIGHT);
            }
        lines.add(currLine);
        lines.forEach(System.out::println);
    }


    /**
     * Draws all Assistants
     * @param assistants ArrayList containing all VirtualAssistants
     */
    public void printAssistants(ArrayList<Assistant> assistants){
        ArrayList<StringBuilder> lines = new ArrayList<>();
        int numAssistants = assistants.size();
        int xSize = 15;

        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        for(Assistant a: assistants)
            firstLine(currLine, "Assistant no."+(assistants.indexOf(a)+1),xSize);

        currLine = new StringBuilder();
        lines.add(currLine);

        for (Assistant assistant : assistants) {
            currLine.append(BOX.VERT).append(" ");
            currLine.append(assistant.getTurn());
            currLine.append(" ".repeat(assistant.getTurn() == 10 ? 10 : 12));
            currLine.append(assistant.getMNsteps());
            currLine.append(BOX.VERT);
        }

        lastLine(xSize, numAssistants, lines);
    }

    public void visit(FullView u){
        virtualView = u.getVirtualView();
    }
    public void visit(AddPlayer u){
        virtualView.addVirtualPlayer(u.getPlayer());
    }
    public void visit(CreateCharacters u){
        virtualView.setVirtualCharacters(u.getCharacters());
    }
    public void visit(CreateClouds u){
        virtualView.setVirtualClouds(u.getClouds());
    }
    public void visit(ReplaceCharacter u){
        virtualView.setVirtualCharacters(u.getIndex(),u.getCharacter());
    }
    public void visit(ReplaceCloud u){
        virtualView.setVirtualClouds(u.getIndex(),u.getCloud());
    }
    public void visit(UpdateCoins u){virtualView.setVirtualCoins(u.getCoins());}
    public void visit(UpdateIsland u){
        virtualView.setVirtualWorld(u.getIndex(),u.getIsland());
    }
    public void visit(UpdateWorld u){
        virtualView.setVirtualWorld(u.getIslands());
    }
    public void visit(UpdateMN u){
        virtualView.setMnPos(u.getIndex());
    }
    public void visit(UpdatePlayer u){
        virtualView.setVirtualPlayers(u.getIndex(),u.getPlayer());
    }
    public void visit(UpdateProfs u){
        virtualView.setVirtualProfs(u.getProfs());
    }
    public void visit(Information i){ lastInfo.add(i.getString()); }
    public void visit(Error e){System.out.println(e.getString());}
    public void visit(NotifyDisconnection e){System.out.println(e.getString());}
    public void visit(Welcome w){welcome = w;}

    /**
     * Gets the color corresponding to the user's string
     * @param s the user's input
     * @return the ColorS corresponding to the input string
     */
    private ColorS colorByString(String s){
        return switch (s.charAt(0)){
            case ('p') -> ColorS.PINK;
            case ('b') -> ColorS.BLUE;
            case ('y') -> ColorS.YELLOW;
            case ('g') -> ColorS.GREEN;
            case ('r') -> ColorS.RED;
            default -> null;
        };
    }

    /**
     * Clears the screen
     */
    public static void clearScreen() {
        try{
            if(System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd.exe", "/c", "cls").inheritIO().start().waitFor();
            }
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (IOException | InterruptedException e){
            Thread.currentThread().interrupt();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void printLobbies(Welcome welcome) {
        ArrayList<VirtualLobby> lobbies = welcome.getLobbies();
        if(lobbies.isEmpty()) {
            System.out.println("There aren't lobby available, please create a new one");
        } else {
            System.out.println("Here's the list of available lobbies:");
            for (VirtualLobby lobby : lobbies) {
                System.out.println(BOX.HORIZ.toString().repeat(10));
                System.out.println("Lobby " + lobby.getLobbyIndex() + ":");
                System.out.println(lobby.isMode() ? "Expert Mode" : "Normal Mode");
                System.out.println("Num Players: " + lobby.getNumPlayers());
                System.out.println("Lobby status: " + lobby.getGameStatus());
                System.out.println("NickName taken: ");
                lobby.getNicknames().forEach(System.out::println);
                System.out.println("Mage already chosen: ");
                lobby.getMages().forEach(System.out::println);
                System.out.println("Tower's Color already chosen: ");
                lobby.getTowers().forEach(System.out::println);
                System.out.println(BOX.HORIZ.toString().repeat(10));
            }
        }
    }

    private VirtualLobby getLobbyByIndex(ArrayList<VirtualLobby> lobbies, int index) {
        for(VirtualLobby lobby : lobbies) {
            if(lobby.getLobbyIndex() == index)
                return lobby;
        }
        return null;
    }
}
