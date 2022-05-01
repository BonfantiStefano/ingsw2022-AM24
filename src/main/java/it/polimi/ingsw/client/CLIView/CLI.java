package it.polimi.ingsw.client.CLIView;


import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterWithNoEntry;
import it.polimi.ingsw.model.character.CharacterWithStudent;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.world.Island;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class CLI{
    private final String appendix = "'s SchoolBoard";

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
        ArrayList<ColorS> entrance = new ArrayList<>();
        for(int i =0; i<5;i++)
            entrance.add(ColorS.BLUE);
        HashMap<ColorS,Integer> hall = new HashMap<>();
        ArrayList<ColorS> profs = new ArrayList<>();
        for(ColorS c:ColorS.values()) {
            hall.put(c, 8);
            profs.add(c);
        }
        ArrayList<ColorT> tow = new ArrayList<>();
        for(int i=0;i<5;i++)
            tow.add(ColorT.WHITE);
        printSchoolBoard("test", entrance,hall,profs, tow);
        //clearScreen();
    }

    public void printSchoolBoard(String nickname, ArrayList<ColorS> entrance, HashMap<ColorS, Integer> hall, ArrayList<ColorS> profs, ArrayList<ColorT> towers){
        final int xSize=40, ySize=7;
        String[][] draw = new String[ySize][xSize];
        for(int j=0;j < 7;j++)
            for(int i =0;i<40;i++)
                draw[j][i]=" ";

        int entrIndex = 0, towIndex = 0;
        int y, x;
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
            if(profs.contains(c))
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

    public void drawChar(Character c, int ind, boolean active){
        final int xSize=25, ySize=4;
        String[][] draw = new String[ySize][xSize];


        firstLine(draw, xSize, "", "Character no."+ind);
        int y=1, x, index=0;
        String status = active? "  Active": "  Not Active";
        lineText(draw,y,xSize, " Cost: "+c.getCost()+status);
        y=2;
        x = 0;
        draw[y][x] = BOX.VERT.toString();
        x++;
        draw[y][x] = " ";

        for(x++;x<xSize-1;x++) {
            if (c instanceof CharacterWithStudent cha) {
                ArrayList<ColorS> s = cha.getStudents();
                if (index < s.size()) {
                    draw[y][x] = getChar(s.get(index));
                    index++;
                } else
                    draw[y][x] = " ";
                x++;
                draw[y][x]= " ";
            }
            else if(c instanceof CharacterWithNoEntry cha){
                int noEntry = cha.getNumNoEntry();
                if (index < noEntry) {
                    draw[y][x] = " "+ BOX.FORBIDDEN;
                    index++;
                } else
                    draw[y][x] = " ";
                x++;
                draw[y][x]= " ";

            }
            else
                draw[y][x]=" ";
        }

        draw[y][x] = BOX.VERT.toString();

        lastLine(draw,xSize,ySize);

        print(draw, xSize, ySize);
    }

    public void drawCharacters(ArrayList<Character> characters){
        int xSize=25, ySize=4;
        ArrayList<StringBuilder> lines = new ArrayList<>();
        lines.add(new StringBuilder());
        lines.add(new StringBuilder());
        lines.add(new StringBuilder());
        lines.add(new StringBuilder());


        for(int i=1;i<=3;i++)
            firstLineChar(lines.get(0),i,9);

        StringBuilder secondLine = lines.get(1);
        for(int j=0;j<3;j++){
            //TODO insert bool isActive in character
            boolean active = false;
            secondLine.append(BOX.VERT);
            secondLine.append(" ");

            secondLine.append("Cost: ").append(characters.get(j).getCost());
            if (active) {
                secondLine.append(Color.ANSI_GREEN).append(" Active");
                secondLine.append(" ".repeat(xSize - 1 - Color.ANSI_GREEN.toString().length()- " Active".length() -3));
            }
            else {
                secondLine.append(Color.ANSI_BLACK).append(" Not Active");
                secondLine.append(" ".repeat(xSize - 1 - Color.ANSI_BLACK.toString().length()- " Not Active".length() -3));
            }
            secondLine.append(Color.RESET);


            secondLine.append(BOX.VERT);

        }

        StringBuilder thirdLine = lines.get(2);
        for(int j=0;j<3;j++) {
            thirdLine.append(BOX.VERT);
            thirdLine.append(" ");
            int index =0;
            Character c = characters.get(j);
            for (int i = 0; i < xSize-2; i++) {
                if (c instanceof CharacterWithStudent cs){
                    if(index<cs.getStudents().size()&&i%2==0) {
                        thirdLine.append(getChar(cs.getStudents().get(index)));
                        index++;
                    }
                    else
                        thirdLine.append(" ");
                }
                else if(c instanceof CharacterWithNoEntry cn){
                    if(index<cn.getNumNoEntry()&&i%2==0) {
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

        StringBuilder last = lines.get(3);
        for(int j =0;j<3;j++){
            last.append(BOX.BOT_LEFT);
            last.append(BOX.HORIZ.toString().repeat(xSize-1));
            last.append(BOX.BOT_RIGHT);
        }


        lines.forEach(System.out::println);

    }

    public void drawIslands(ArrayList<Island> islands){
        int ySize = 5, xSize = 17;
        int numIslands = islands.size();
        ArrayList<StringBuilder> lines = new ArrayList<>();

        //TODO add MN position
        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        for(int i=0;i<numIslands;i++){
            currLine.append(BOX.TOP_LEFT);
            currLine.append("Island no.").append(i+1);
            currLine.append(BOX.HORIZ.toString().repeat(xSize-"Island no.".length()-((i+1)>=10?2:1)));
            currLine.append(BOX.TOP_RIGHT);
        }
        currLine = new StringBuilder();
        lines.add(currLine);
        for(int i=0;i<numIslands;i++){
            currLine.append(BOX.VERT);
            currLine.append(" Towers: ");
            Island is = islands.get(i);
            if(is.getTowerColor().isPresent()) {
                currLine.append(getChar(is.getTowerColor().get())).append(is.getTowerColor().get());
                currLine.append(Color.RESET);
                currLine.append("x").append(is.getNumSubIsland());
                currLine.append(" ".repeat(is.getTowerColor().get().equals(ColorT.GREY)? 2: 1));
            }
            else {
                currLine.append("None");
                currLine.append(" ".repeat(xSize-"None".length()-" Towers: ".length()));
            }
            currLine.append(BOX.VERT);
        }


        for (ColorS c : ColorS.values()) {
            currLine = new StringBuilder();
            lines.add(currLine);
             for(int i=0;i<numIslands;i++){
                currLine.append(BOX.VERT).append(" ");
                currLine.append(getChar(c));
                currLine.append(c.toString()).append(":");
                currLine.append(islands.get(i).getNumStudentByColor(c));
                currLine.append(" ".repeat(xSize-3-c.toString().length()-1));
                currLine.append(BOX.VERT);
            }
        }

        currLine=new StringBuilder();
        lines.add(currLine);
        for(int i=0;i<numIslands;i++) {
            currLine.append(BOX.BOT_LEFT);
            currLine.append(BOX.HORIZ.toString().repeat(xSize));
            currLine.append(BOX.BOT_RIGHT);
        }


        lines.forEach(System.out::println);
    }

    public void drawClouds(ArrayList<Cloud> clouds){
        ArrayList<StringBuilder> lines = new ArrayList<>();
        int numClouds = clouds.size();
        int xSize = 20;

        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        for(int i=0;i<numClouds;i++){
            currLine.append(BOX.TOP_LEFT);
            currLine.append("Cloud no.").append(i+1);
            currLine.append(BOX.HORIZ.toString().repeat(xSize-"Cloud no.".length()-((i+1)>=10?2:1)));
            currLine.append(BOX.TOP_RIGHT);
        }

        currLine = new StringBuilder();
        lines.add(currLine);

        for(int i=0;i<numClouds;i++){
            currLine.append(BOX.VERT).append(" ");
            int index = 0;
            for(int j=0;j<xSize-1;j++) {
                ArrayList<ColorS> students = clouds.get(i).getStudents();
                if (index < students.size()&&j%2==0) {
                    currLine.append(getChar(students.get(index)));
                    index++;
                } else
                    currLine.append(" ");
                //currLine.append(" ");
            }
            currLine.append(BOX.VERT);
        }

        currLine = new StringBuilder();
        lines.add(currLine);

        for(int i = 0;i<clouds.size();i++){
            currLine.append(BOX.BOT_LEFT);
            currLine.append(BOX.HORIZ.toString().repeat(xSize));
            currLine.append(BOX.BOT_RIGHT);
            }

        lines.forEach(System.out::println);
    }

    public void printAssistants(ArrayList<Assistant> assistants){
        ArrayList<StringBuilder> lines = new ArrayList<>();
        int numAssistants = assistants.size();
        int xSize = 15;

        StringBuilder currLine = new StringBuilder();
        lines.add(currLine);
        for(int i=0;i<numAssistants;i++){
            currLine.append(BOX.TOP_LEFT);
            currLine.append("Assistant no.").append(i+1);
            currLine.append(BOX.HORIZ.toString().repeat(xSize-"Assistant no.".length()-((i+1)>=10?2:1)));
            currLine.append(BOX.TOP_RIGHT);
        }

        currLine = new StringBuilder();
        lines.add(currLine);

        for(int i=0;i<numAssistants;i++){
            currLine.append(BOX.VERT).append(" ");
            currLine.append(assistants.get(i).getTurn());
            currLine.append(" ".repeat(assistants.get(i).getTurn()==10?10:12));
            currLine.append(assistants.get(i).getMNsteps());
            currLine.append(BOX.VERT);
        }

        currLine = new StringBuilder();
        lines.add(currLine);
        for(int i = 0;i<numAssistants;i++){
            currLine.append(BOX.BOT_LEFT);
            currLine.append(BOX.HORIZ.toString().repeat(xSize));
            currLine.append(BOX.BOT_RIGHT);
        }

        lines.forEach(System.out::println);
    }



    private void firstLineChar(StringBuilder topLine, int i, int diff){

        topLine.append(BOX.TOP_LEFT);
        topLine.append("Character no. ").append(i);
        topLine.append(BOX.HORIZ.toString().repeat(diff));
        topLine.append(BOX.TOP_RIGHT);

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
