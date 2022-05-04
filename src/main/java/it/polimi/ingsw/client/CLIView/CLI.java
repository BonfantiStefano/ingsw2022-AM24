package it.polimi.ingsw.client.CLIView;


import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterWithNoEntry;
import it.polimi.ingsw.model.character.CharacterWithStudent;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.server.virtualview.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class CLI{
    private boolean gameOn = true;
    private long start;

    public static void main(String[] args) {
        CLI c = new CLI();
        c.run();
    }

    private final PrintStream output;
    private final Scanner input;

    public CLI(){
        output = new PrintStream(System.out);
        input = new Scanner(System.in);
    }


    public void run() {
        new Thread(new ReadRunnable()).start();
        new Thread(new PrintRunnable()).start();
        gameOn=true;

    }


    //TODO change to StringBuilder instead of String Array
    public void printSchoolBoard(VirtualSchoolBoard schoolBoard, HashMap<ColorS, VirtualPlayer> profs){
        //TODO add nickname parameter
        String nickname = "Test";
        ArrayList<ColorS> entrance = schoolBoard.getEntrance();
        HashMap<ColorS, Integer> hall = (HashMap<ColorS, Integer>) schoolBoard.getHall();
        ArrayList<ColorT> towers = schoolBoard.getTowers();

        final int xSize=40, ySize=7;
        String[][] draw = new String[ySize][xSize];


        int entrIndex = 0, towIndex = 0;
        int y, x;
        String appendix = "'s SchoolBoard";
        firstLine(draw, xSize, appendix, nickname);
        y=1;

        for(ColorS c: ColorS.values()) {
            x=0;
            //for every row
            draw[y][x] = BOX.VERT +" ";
            for (x = 1; x < 3; x++) {
                if (entrIndex < entrance.size()) {
                    draw[y][x] = (getChar(entrance.get(entrIndex)) + " ");
                    entrIndex++;
                } else
                    draw[y][x] = "  ";
            }

            draw[y][x] = BOX.VERT +" ";
            x++;
            for (int w =0; w < hall.get(c); w++,x++){
                draw[y][x] = getChar(c) + " ";
            }

            for (int w=0; w < 10 - hall.get(c); w++,x++){
                draw[y][x] = "  ";
            }

            draw[y][x] = BOX.VERT +" ";

            x++;
            //TODO change parameter
            if(profs.get(c).getNickname().equals("test"))
                draw[y][x]=getChar(c);
            else
                draw[y][x]=" ";

            x++;
            draw[y][x] = " "+ BOX.VERT+ " ";
            x++;
            for(int w = 0; w<2;w++,x++){
                if(towIndex<towers.size()) {
                    draw[y][x]=getChar(towers.get(towIndex)) + BOX.CIRCLE+" " + Color.RESET;
                    towIndex++;
                }
                else
                    draw[y][x] = "  ";
            }

            draw[y][x] = " "+ BOX.VERT;

            y++;
       }
        lastLine(draw, xSize, ySize);

        print(draw, xSize, ySize);

    }

    private void clearScreen(){
        for(int i=0;i<50;i++)
            System.out.println();

    }


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
    private String getChar(ColorT c) {
        return switch (c) {
            case BLACK -> Color.ANSI_BLACK.toString();
            case WHITE -> Color.RESET;
            case GREY -> Color.ANSI_GREY.toString();

        };
    }


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

    public void firstLine(StringBuilder currLine, String text, int xSize){
        currLine.append(BOX.TOP_LEFT);
        currLine.append(text);
        currLine.append(BOX.HORIZ.toString().repeat(xSize+1-text.length()-(text.length()>=10?1:0)));
        currLine.append(BOX.TOP_RIGHT);
    }


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

    public void drawClouds(ArrayList<VirtualCloud> clouds){
        ArrayList<StringBuilder> lines = new ArrayList<>();
        int numClouds = clouds.size();
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


    private void lineText(String[][] draw, int y, int xSize, String text){
        int x=0;
        draw[y][x]= BOX.VERT.toString();
        x++;
        for(int j=0;j<text.length();x++,j++)
            draw[y][x]= String.valueOf(text.charAt(j));
        for(;x<xSize-1;x++)
            draw[y][x]= " ";
        draw[y][x]= BOX.VERT.toString();
    }

    private void firstLine(String[][] draw, int xSize, String appendix, String nickname){
        int y=0, x=0;
        draw[x][y]= BOX.TOP_LEFT.toString();
        x++;
        draw[y][x]= BOX.HORIZ.toString();

        for(int w=0;w<nickname.length();w++,x++)
            draw[y][x]= String.valueOf(nickname.charAt(w));
        for(int w=0;w<appendix.length();w++,x++)
            draw[y][x]= String.valueOf(appendix.charAt(w));
        for(;x<xSize-1;x++)
            draw[y][x]= BOX.HORIZ.toString();

        draw[y][x]= BOX.TOP_RIGHT.toString();

    }

    private void lastLine(String[][] s, int xSize, int ySize){
        int x=0;
        s[ySize-1][x]= BOX.BOT_LEFT.toString();
        x++;

        for(;x<xSize-1;x++)
            s[ySize-1][x]= BOX.HORIZ.toString();

        s[ySize-1][x]= BOX.BOT_RIGHT.toString();
    }

    public void print(String[][] s, int xSize,int ySize){
        for(int i = 0; i<ySize;i++) {
            for (int j = 0; j < xSize; j++)
                System.out.print(s[i][j]);
            System.out.print("\n");
        }
    }

}

class ReadRunnable implements Runnable {

    @Override
    public void run() {
        final Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            final String line = in.nextLine();
            System.out.println("Input line: " + line);
            if ("end".equalsIgnoreCase(line)) {
                System.out.println("Ending one thread");
                break;
            }
        }
    }

}

class PrintRunnable implements Runnable {

    @Override
    public void run() {
        int i = 50;
        while(i>0) {
            System.out.println("Beep: " + i --);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        }
        System.out.println("!!!! BOOM !!!!");
    }
}
