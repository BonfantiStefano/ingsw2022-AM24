package it.polimi.ingsw.client.CLIView;


import it.polimi.ingsw.client.request.GameParams;
import it.polimi.ingsw.client.request.Join;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.answer.Update.*;
import it.polimi.ingsw.server.answer.Welcome;
import it.polimi.ingsw.server.virtualview.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class CLI {
    private boolean gameOn = true;
    private long start;
    private VirtualView virtualView;

    public static void main(String[] args) {
        CLI c = new CLI();
    }

    private final PrintStream output;
    private final Scanner input;

    public CLI() {
        output = new PrintStream(System.out);
        input = new Scanner(System.in);
        virtualView = new VirtualView();
    }

    private void printView() {
        ArrayList<VirtualIsland> virtualWorld = virtualView.getVirtualWorld();
        drawIslands((ArrayList<VirtualIsland>) virtualWorld.subList(0, virtualWorld.size() / 2));
        drawIslands((ArrayList<VirtualIsland>) virtualWorld.subList(virtualWorld.size() / 2 + 1, virtualWorld.size() - 1));
        drawCharacters(virtualView.getVirtualCharacters());
        for (VirtualPlayer vp : virtualView.getVirtualPlayers())
            drawSchoolBoard(vp.getVirtualBoard(), vp.getNickname(), virtualView.getVirtualProfs());
    }

    /**
     * Ask the Player whether they want to create a Lobby or Join one
     * @param w Welcome message containing all available Lobbies
     */
    private void getInfo(Welcome w) {
        //TODO set lobbies to w.getLobbies()
        ArrayList<Lobby> lobbies = new ArrayList<>();
        System.out.println("Here's the list of available lobbies:");
        for (Lobby l : lobbies) {
            System.out.println(lobbies.indexOf(l) + ":");
            System.out.println(l.isMode() ? "Expert Mode" : "Normal Mode");
            System.out.println("Num Players: " + l.getNumPlayers());
        }

        String answer;
        do {
            System.out.println("Do you want to Join a Lobby? (y/n)");
            answer = input.nextLine();
        } while (!answer.equals("y") && !answer.equals("n"));

        if (answer.equals("y")) {
            int index;
            System.out.println("Insert the Lobby's number: ");
            index = input.nextInt();


            String nickname;
            do {
                System.out.println("Choose your nickname: ");
                nickname = input.nextLine();
                if (lobbies.get(index).getNicknames().contains(nickname)) {
                    System.out.println("Nickname already in use!");
                    nickname = null;
                }
            } while (nickname == null);

            int mageIndex;
            do {
                System.out.println("Choose your Mage (1,2,3,4):");
                mageIndex = input.nextInt();
                if (lobbies.get(index).getMages().contains(Mage.values()[mageIndex])) {
                    System.out.println("Mage already in use!");
                    mageIndex = -1;
                }
            } while (mageIndex < 0 || mageIndex > 4);

            int towerIndex;
            do {
                System.out.println("Choose your TowerColor (1,2,3):");
                towerIndex = input.nextInt();
                if (lobbies.get(index).getColorTowers().contains(ColorT.values()[towerIndex])) {
                    System.out.println("Tower Color already in use!");
                    towerIndex = -1;
                }
            } while (towerIndex < 0 || towerIndex > 4);

            Join msg = new Join(nickname, Mage.values()[mageIndex], ColorT.values()[towerIndex], index);
        }
        else{
            int numPlayers;
            do {
                System.out.println("How many other Players do you want to play with? (2/3)");
                numPlayers = input.nextInt();
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
                mageIndex = input.nextInt();
            } while (mageIndex < 0 || mageIndex > 4);

            int towerIndex;
            do {
                System.out.println("Choose your TowerColor (1,2,3):");
                towerIndex = input.nextInt();
            } while (towerIndex < 0 || towerIndex > 4);

            GameParams msg = new GameParams(numPlayers, expert.equals("y"), nickname,Mage.values()[mageIndex], ColorT.values()[towerIndex]);
        }
        //client.sendMessage(msg);
    }

    public void update(Update u){
        u.accept(this);
        printView();
    }

    /**
     * Prints a SchoolBoard
     * @param schoolBoard the SchoolBoard to print
     * @param profs the HashMap containing all Profs
     */
    public void drawSchoolBoard(VirtualSchoolBoard schoolBoard,String nickname, HashMap<ColorS, Player> profs){
        //TODO add nickname parameter
        String appendix = "'s SchoolBoard";
        ArrayList<ColorS> entrance = schoolBoard.getEntrance();
        HashMap<ColorS, Integer> hall = (HashMap<ColorS, Integer>) schoolBoard.getHall();
        ArrayList<ColorT> towers = schoolBoard.getTowers();
        ArrayList<StringBuilder> lines = new ArrayList<>();
        final int xSize=40, ySize=7;

        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        firstLine(currLine, nickname+appendix, xSize);
        int entrIndex = 0, towIndex = 0;

        currLine = new StringBuilder();
        lines.add(currLine);

        for(ColorS c: ColorS.values()) {
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

            currLine.append(" ".repeat(Math.max(0, 10 - hall.get(c))));

            currLine.append(BOX.VERT);

            //TODO change parameter
            if(profs.get(c).getNickname().equals("test"))
                currLine.append(getChar(c));
            else
                currLine.append(" ");

            currLine.append(" ").append(BOX.VERT).append(" ");

            for(int w = 0; w<2;w++){
                if(towIndex<towers.size()) {
                    currLine.append(getChar(towers.get(towIndex))).append(BOX.CIRCLE).append(" ").append(Color.RESET);
                    towIndex++;
                }
                else
                    currLine.append("  ");
            }

            currLine.append(" ").append(BOX.VERT);

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
        int ySize = 5, xSize = 17;
        int numIslands = islands.size();
        ArrayList<StringBuilder> lines = new ArrayList<>();

        //TODO add MN position
        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        for(VirtualIsland i:islands)
            firstLine(currLine,"Island no. "+ (islands.indexOf(i)+1), xSize);

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
     * @param lines ArrayList containing all other lines
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


        lines.forEach(System.out::println);
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
    public void visit(UpdateCoins u){
        virtualView.setVirtualCoins(u.getCoins());
    }
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

}
