package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.CharacterDescription;
import it.polimi.ingsw.model.character.CharacterProf;
import it.polimi.ingsw.model.character.CharacterWithStudent;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpertControllerTest {

    private ExpertController expertController;
    private Server server;

    private ExpertGameBoard egb;
    private int mnPos;

    @BeforeEach
    public void init() throws PlaceFullException, InvalidIndexException {
        server = new Server();
        expertController = new ExpertController(server, new GameParams(3, true, "Bob", Mage.MAGE1, ColorT.BLACK));

        egb = (ExpertGameBoard)expertController.getExpertModel();

        //initial position of MotherNature
        mnPos = egb.getWorld().getMNPosition();

        egb.setActivePlayer(egb.getPlayerByNickname("Bob"));
        egb.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        egb.addPlayer("Alice", ColorT.GREY, Mage.MAGE3);
        //lisa and Bob have the same number of pink students
        for(int i = 0; i < 4; i++){
            egb.getPlayerByNickname("Lisa").getMyBoard().addToHall(ColorS.PINK);
            egb.getActivePlayer().getMyBoard().addToHall(ColorS.PINK);
        }
        //bob chooses Assistant card 8 => MNsteps = 4
        egb.chooseAssistants(egb.getActivePlayer(), 8);


    }

    @Test
    public void testPlayCharacter() throws PlaceFullException, InvalidMNStepsException {
        CharacterDescription notAvailableChar = null;
        ArrayList<Character> characters = egb.getCharacters();
        ArrayList<CharacterDescription> availableChars = new ArrayList<>();
        for(int i = 0; i<3; i++){
            String desc = characters.get(i).getDescription();
            for(CharacterDescription cd : CharacterDescription.values()){
                if (cd.getDesc().equals(desc))
                    availableChars.add(cd);
            }
        }
        System.out.println("available cards: ");
        availableChars.stream().forEach(System.out :: println);

        for(CharacterDescription c : CharacterDescription.values())
            if(!(availableChars.contains(c))){
                notAvailableChar = c;
                break;
            }
        //System.out.println("non available card: " + notAvailableChar);

        assertTrue(egb.getActiveCharacter()==null);

        //sending message with not valid card
        PlayCharacter message1 = new PlayCharacter(notAvailableChar);
        expertController.handleCharacter(message1, "Bob");
        assertTrue(egb.getActiveCharacter()==null);

        //sending message with valid card
        PlayCharacter message2 = new PlayCharacter(availableChars.get(0));
        egb.getActivePlayer().setCoins(15);
        int initCoins = egb.getActivePlayer().getCoins();
        int initCost = availableChars.get(0).getCost();
        expertController.handleCharacter(message2, egb.getActivePlayer().getNickname());
        assertTrue(egb.getActiveCharacter().getDescription().equals(availableChars.get(0).getDesc()));
        assertTrue(egb.getActivePlayer().getCoins() == initCoins-initCost);
        System.out.println("active Character: "+ egb.getActiveCharacter().getDescription());

        SpecialMoveIsland message_1 = new SpecialMoveIsland(ColorS.RED, 6);
        int numRedStudentsOnIsland =  egb.getIslandByIndex(6).getNumStudentByColor(ColorS.RED);

        ChooseColor message_2 = new ChooseColor(ColorS.GREEN);
        ChooseTwoColors message_3 = new ChooseTwoColors(ColorS.GREEN, ColorS.YELLOW);
        ChooseIsland message_4 = new ChooseIsland(5);

        expertController.handleCharacter(message_1, "Bob");
        expertController.handleCharacter(message_2, "Bob");
        expertController.handleCharacter(message_3, "Bob");
        expertController.handleCharacter(message_4, "Bob");

        /**
        if(egb.getActiveCharacter().getDescription().equals(CharacterDescription.CHAR1.getDesc())){
            if(((CharacterWithStudent) egb.getActiveCharacter()).getStudents().contains(ColorS.RED))
                assertTrue(egb.getIslandByIndex(6).getNumStudentByColor(ColorS.RED)==numRedStudentsOnIsland+1);
        }

        if(egb.getActiveCharacter().getDescription().equals(CharacterDescription.CHAR2.getDesc())){
            Player player = egb.getProfs().get(ColorS.PINK);
            assertTrue(player.getNickname().equals(egb.getActivePlayer().getNickname()));
        }
        //char3 ???
        if(egb.getActiveCharacter().getDescription().equals(CharacterDescription.CHAR4.getDesc())){
            int numIslands = egb.getWorld().getSize();
            egb.moveMN(6);
            int newmnPos = egb.getWorld().getMNPosition();
            assertTrue((mnPos + 6) % numIslands == newmnPos);
        }
         */

    }



}
