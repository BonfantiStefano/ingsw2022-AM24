package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.StudentContainer;
import it.polimi.ingsw.model.character.*;
import it.polimi.ingsw.model.character.Character;

import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SchoolBoard;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.server.virtualview.*;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

class CLITest {
    CLI c = new CLI(new Client());
    CharacterFactory factory = new CharacterFactory(null,null,new StudentContainer(),null);
/*
    @Test
    void test(){
        ArrayList<Character> characters = new ArrayList<>();
        while(characters.size()<3) {
            Character c = factory.createCharacter();
            if (c instanceof CharacterWithStudent)
                characters.add(c);
            else if (c instanceof CharacterWithNoEntry)
                characters.add(c);
        }
        ArrayList<VirtualCharacter> vchars = new ArrayList<>();
        characters.forEach(c->{
            if(c instanceof CharacterWithStudent)
                vchars.add(new VirtualCharacterWithStudents(c));
            else if(c instanceof CharacterWithNoEntry)
                vchars.add(new VirtualCharacterWithNoEntry(c));
        });
        c.drawCharacters(vchars);
        characters.forEach(c->System.out.println(c.getDescription()));
    }
/*
    @Test
    void schoolBoard(){
        SchoolBoard s = new SchoolBoard(ColorT.WHITE,5);

        ArrayList<ColorS> entrance = new ArrayList<>();
        for(int i =0; i<5;i++)
            entrance.add(ColorS.BLUE);
        s.getEntrance().addAll(entrance);
        HashMap<ColorS,Integer> hall = new HashMap<>();
        HashMap<ColorS, VirtualPlayer> profs = new HashMap<>();
        for(ColorS c:ColorS.values()) {
            hall.put(c, 8);
            profs.put(c, new VirtualPlayer(new Player("test",ColorT.WHITE,Mage.MAGE1,5)));
        }
        s.getHall().putAll(hall);
        ArrayList<ColorT> tow = new ArrayList<>();
        for(int i=0;i<5;i++)
            tow.add(ColorT.WHITE);

        c.drawSchoolBoard(new VirtualSchoolBoard(s), "test", profs);

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

        ArrayList<VirtualCharacter> chars = new ArrayList<>();
        chars.add(new VirtualCharacterWithNoEntry(character2));
        chars.add(new VirtualCharacterWithStudents(character1));
        chars.add(new VirtualCharacter(character3));
        c.drawCharacters(chars);
    }

    @Test
    void printIslands(){
        ArrayList<Island> islands = new ArrayList<>();
        Island i1 = new Island();
        addIslands(islands);

        c.drawIslands(new ArrayList<>(islands.stream().map(VirtualIsland::new).collect(Collectors.toList())));
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
        c.drawClouds(new ArrayList<>(clouds.stream().map(VirtualCloud::new).collect(Collectors.toList())));
    }
    @Test
    void printAssistant(){
        ArrayList<Assistant> assistants = new ArrayList<>();
        for(int i=1;i<=10;i++){
            assistants.add(new Assistant(i,i, Mage.MAGE2));
        }
        c.printAssistants(assistants);
    }


 */

}

