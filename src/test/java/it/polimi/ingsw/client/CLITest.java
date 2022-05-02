package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.*;

import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.world.Island;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

class CLITest {
    CLI c = new CLI();


    void drawCharStud() {
        CharacterWithStudent character = new CharacterWithStudent(1,"test", 5);
        character.add(ColorS.GREEN);
        character.add(ColorS.BLUE);
        character.add(ColorS.YELLOW);
        character.add(ColorS.YELLOW);
        character.add(ColorS.YELLOW);
        c.drawChar(character, 1, true);


    }

    void drawCharNoEntry() {
        CharacterWithNoEntry character = new CharacterWithNoEntry(1,"test");
        c.drawChar(character, 1, true);
    }


    void drawNormalChar(){
        c.drawChar(new Character(1, "test"), 1, true);
        c.drawChar(new Character(1, "test"), 1, false);
    }

    @Test
    void schoolBoard(){
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
        c.printSchoolBoard("test", entrance,hall,profs, tow);
    }

    @Test
    void multChar(){
        CharacterWithStudent character1 = new CharacterWithStudent(1,"test", 5);
        character1.add(ColorS.GREEN);
        character1.add(ColorS.BLUE);
        character1.add(ColorS.YELLOW);
        character1.add(ColorS.YELLOW);
        character1.add(ColorS.YELLOW);
        CharacterWithNoEntry character2 = new CharacterWithNoEntry(1,"test");
        Character character3 = new Character(1,"test");

        ArrayList<Character> chars = new ArrayList<>();
        chars.add(character2);
        chars.add(character1);
        chars.add(character3);
        c.drawCharacters(chars);
    }

    @Test
    void printIslands(){
        ArrayList<Island> islands = new ArrayList<>();
        Island i1 = new Island();
        addIslands(islands);
        c.drawIslands(islands);
    }

    public void addIslands(ArrayList<Island> is){
        Island i;
        for(int j=0;j<12;j++) {
            i=new Island();
            for (ColorS c : ColorS.values()) {
                i.add(c);
                i.add(ColorT.BLACK);
            }
            is.add(i);
        }
        i = new Island();
        i.add(ColorT.GREY);
        is.set(5,i);

    }

    @Test
    void printClouds(){
        Cloud c1 = new Cloud();
        Cloud c2 = new Cloud();
        ArrayList<Cloud> clouds = new ArrayList<>();
        clouds.add(c1);
        clouds.add(c2);
        c1.add(ColorS.GREEN);
        c2.add(ColorS.BLUE);
        c2.add(ColorS.GREEN);
        c1.add(ColorS.BLUE);
        c.drawClouds(clouds);
    }
    @Test
    void printAssistant(){
        ArrayList<Assistant> assistants = new ArrayList<>();
        for(int i=1;i<=10;i++){
            assistants.add(new Assistant(i,i, Mage.MAGE2));
        }
        c.printAssistants(assistants);
    }
}