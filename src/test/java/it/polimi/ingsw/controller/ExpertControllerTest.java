package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.*;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerInterface;
import it.polimi.ingsw.model.profstrategy.EqualProf;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.influence.AdditionalInfluence;
import it.polimi.ingsw.model.world.influence.NoColorInfluence;
import it.polimi.ingsw.model.world.influence.NoTowerInfluence;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class ExpertControllerTest tests ExpertController class.
 *
 * @see ExpertController
 */
public class ExpertControllerTest {

    private ExpertController expertController;

    private ExpertGameBoard egb;
    private int mnPos;
    private ArrayList<PlayerInterface> players;

    /**
     * Method init creates an ExpertController that is used by every test.
     */
    @BeforeEach
    public void init() throws PlaceFullException, InvalidIndexException {
        Server server = new Server();
        expertController = new ExpertController(server, new GameParams(3, true, "Bob", Mage.MAGE1, ColorT.BLACK));

        egb = (ExpertGameBoard) expertController.getExpertModel();

        players = new ArrayList<>();
        for (Player p : egb.getPlayers()) {
            players.add(p);
        }

        //initial position of MotherNature
        mnPos = egb.getWorld().getMNPosition();

        egb.setActivePlayer(egb.getPlayerByNickname("Bob"));
        egb.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        egb.addPlayer("Alice", ColorT.GREY, Mage.MAGE3);
        //lisa and Bob have the same number (4) of pink students
        for (int i = 0; i < 4; i++) {
            egb.getPlayerByNickname("Lisa").getMyBoard().addToHall(ColorS.PINK);
            egb.getActivePlayer().getMyBoard().addToHall(ColorS.PINK);
        }
        //Alice has 1 pink student and 1 yellow student
        egb.getPlayerByNickname("Alice").getMyBoard().addToHall(ColorS.PINK);
        egb.getPlayerByNickname("Alice").getMyBoard().addToHall(ColorS.YELLOW);

        //bob chooses Assistant card 8 => MNsteps = 4
        egb.chooseAssistants(egb.getActivePlayer(), 8);

        //adding green students on island 5 and 8
        Island island5 = egb.getWorld().getIslandByIndex(5);
        Island island8 = egb.getWorld().getIslandByIndex(8);
        for (int i = 0; i < 5; i++) {
            island5.add(ColorS.GREEN);
            island8.add(ColorS.GREEN);
        }

        for (int i = 0; i < 6; i++)
            egb.getActivePlayer().getMyBoard().addToHall(ColorS.GREEN);

        egb.checkProfs();
        Player p = egb.getProfs().get(ColorS.GREEN);
        assertEquals("Bob", p.getNickname());

    }

    /** Method testPlayCharacter checks if one of the three available Characters is correctly chosen */
    @Test
    public void testPlayCharacter() {
        CharacterDescription notAvailableChar = null;
        ArrayList<Character> characters = egb.getCharacters();
        ArrayList<CharacterDescription> availableChars = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String desc = characters.get(i).getDescription();
            for (CharacterDescription cd : CharacterDescription.values()) {
                if (cd.getDesc().equals(desc))
                    availableChars.add(cd);
            }
        }
        //System.out.println("available cards: ");
        //availableChars.stream().forEach(System.out :: println);

        for (CharacterDescription c : CharacterDescription.values())
            if (!(availableChars.contains(c))) {
                notAvailableChar = c;
                break;
            }
        //System.out.println("non available card: " + notAvailableChar);

        assertNull(egb.getActiveCharacter());

        //sending message with not valid card
        PlayCharacter message1 = new PlayCharacter(notAvailableChar);
        expertController.handleCharacter(message1, "Bob");
        assertNull(egb.getActiveCharacter());

        //sending message with valid card
        PlayCharacter message2 = new PlayCharacter(availableChars.get(0));
        egb.getActivePlayer().setCoins(15);
        int initCoins = egb.getActivePlayer().getCoins();
        int initCost = availableChars.get(0).getCost();
        expertController.handleCharacter(message2, egb.getActivePlayer().getNickname());
        assertEquals(egb.getActiveCharacter().getDescription(), availableChars.get(0).getDesc());
        assertEquals(egb.getActivePlayer().getCoins(), initCoins - initCost);
        //System.out.println("active Character: "+ egb.getActiveCharacter().getDescription());
    }

    /** Method testChar1 checks if the Character's ability is actually used when the card 1 is selected */
    @Test
    public void testChar1() {
        Character char1 = createCharacter(1);
        ((CharacterWithStudent) char1).add(ColorS.RED);
        egb.setActiveCharacter(char1);
        assertEquals(CharacterDescription.CHAR1.getDesc(), egb.getActiveCharacter().getDescription());
        SpecialMoveIsland message_1 = new SpecialMoveIsland(ColorS.RED, 6);
        int numRedStudentsOnIsland = egb.getIslandByIndex(6).getNumStudentByColor(ColorS.RED);
        expertController.handleCharacter(message_1, "Bob");
        assertEquals(egb.getIslandByIndex(6).getNumStudentByColor(ColorS.RED), numRedStudentsOnIsland + 1);
    }

    /** Method testChar2 checks if the Character's ability is actually used when the card 2 is selected */
    public void testChar2() {
        Character char2 = createCharacter(2);
        egb.setActiveCharacter(char2);
        assertEquals(CharacterDescription.CHAR2.getDesc(), egb.getActiveCharacter().getDescription());
        egb.checkProfs();
        //Player player = egb.getProfs().get(ColorS.PINK);
        //assertEquals(egb.getActivePlayer().getNickname(), player.getNickname());

    }

    /** Method testChar3 checks if the Character's ability is actually used when the card 3 is selected */
    public void testChar3() {
        Character char3 = createCharacter(3);
        egb.setActiveCharacter(char3);
        ChooseIsland message_4 = new ChooseIsland(5);
        expertController.handleCharacter(message_4, "Bob");
        egb.checkIsland(egb.getIslandByIndex(5));
        Optional<ColorT> colorT = egb.getWorld().getIslandByIndex(5).getTowerColor();
        assertEquals(Optional.of(egb.getPlayerByNickname("Bob").getColorTower()), colorT);
    }

    /** Method testChar4 checks if the Character's ability is actually used when the card 4 is selected */
    @Test
    public void testChar4() throws InvalidMNStepsException {
        Character char4 = createCharacter(4);
        egb.setActiveCharacter(char4);
        assertEquals(CharacterDescription.CHAR4.getDesc(), egb.getActiveCharacter().getDescription());
        int numIslands = egb.getWorld().getSize();
        egb.moveMN(6);
        int newmnPos = egb.getWorld().getMNPosition();
        if (egb.getSizeWorld() == 11) {
            assertEquals((mnPos + 5) % numIslands, newmnPos);
        } else {
            assertEquals((mnPos + 6) % numIslands, newmnPos);
        }
    }

    /** Method testChar5 checks if the Character's ability is actually used when the card 5 is selected */
    @Test
    public void testChar5() {
        Character char5 = createCharacter(5);
        egb.setActiveCharacter(char5);
        ChooseIsland message_5 = new ChooseIsland(8);
        expertController.handleCharacter(message_5, "Bob");
        assertEquals(1, egb.getWorld().getIslandByIndex(8).getNumNoEntry());
        int posMN = egb.getWorld().getMNPosition();
        int numSteps = posMN < 8 ? 8 - posMN : egb.getSizeWorld() - posMN + 8;
        int newPosMN = (posMN + numSteps) % egb.getSizeWorld();
        egb.getWorld().checkEntry(egb.getWorld().getIslandByIndex(8));
        Optional<ColorT> colorTower = egb.getWorld().getIslandByIndex(8).getTowerColor();
        assertEquals(0, egb.getWorld().getIslandByIndex(8).getNumNoEntry());
        assertEquals(Optional.empty(), colorTower);
    }

    /** Method testChar6 checks if the Character's ability is actually used when the card 6 is selected */
    @Test
    public void testChar6() throws PlaceFullException {
        for (int i = 0; i < 6; i++)
            egb.getPlayerByNickname("Alice").getMyBoard().addToHall(ColorS.YELLOW);
        Island island2 = egb.getWorld().getIslandByIndex(2);
        for (int i = 0; i < 5; i++)
            island2.add(ColorS.YELLOW);
        egb.checkProfs();
        egb.checkIsland(egb.getIslandByIndex(2));
        assertEquals(egb.getPlayerByNickname("Alice").getColorTower(), egb.getWorld().getIslandByIndex(2).getTowerColor().get());
        int yellowStudents = island2.getNumStudentByColor(ColorS.YELLOW);

        Character char6 = createCharacter(6);
        egb.setActiveCharacter(char6);
        HashMap<Player, Integer> mapInfluence = new HashMap<>();
        mapInfluence = egb.getInfluence(egb.getIslandByIndex(2));
        int influenceAlice = mapInfluence.get(egb.getPlayerByNickname("Alice"));
        assertEquals(influenceAlice, yellowStudents);
    }

    /** Method testChar7 checks if the Character's ability is actually used when the card 7 is selected */
    @Test
    public void testChar7(){
        Player lisa = egb.getPlayerByNickname("Lisa");
        egb.setActivePlayer(lisa);
        lisa.getMyBoard().add(ColorS.RED);
        Character char7 = createCharacter(7);
        egb.setActiveCharacter(char7);
        assertEquals(CharacterDescription.CHAR7.getDesc(), egb.getActiveCharacter().getDescription());
        ArrayList<ColorS> studentsCard = ((CharacterWithStudent) char7).getStudents();
        assertEquals(6, studentsCard.size());
        ColorS student = studentsCard.get(0);
        int numStudentsLisa = 0;
        for(ColorS c : lisa.getMyBoard().getEntrance()){
            if(c==student)
                numStudentsLisa++;
        }
        ChooseTwoColors message_7 = new ChooseTwoColors(ColorS.RED, student);
        expertController.handleCharacter(message_7, "Lisa");
        ArrayList<ColorS> studentsOnCard = ((CharacterWithStudent) char7).getStudents();
        assertEquals(6, studentsOnCard.size());
        int numStudLisa = 0;
        for(ColorS c : lisa.getMyBoard().getEntrance()){
            if(c==student)
                numStudLisa++;
        }
        assertEquals(numStudentsLisa + 1, numStudLisa);

    }

    /** Method testChar8 checks if the Character's ability is actually used when the card 8 is selected */
    @Test
    public void testChar8() throws PlaceFullException, InvalidMNStepsException, InvalidIndexException {
        for (int i = 0; i < 6; i++)
            egb.getPlayerByNickname("Alice").getMyBoard().addToHall(ColorS.YELLOW);
        Island island2 = egb.getWorld().getIslandByIndex(2);
        for (int i = 0; i < 5; i++)
            island2.add(ColorS.YELLOW);
        egb.checkProfs();
        egb.checkIsland(egb.getIslandByIndex(2));
        assertEquals(egb.getPlayerByNickname("Alice").getColorTower(), egb.getWorld().getIslandByIndex(2).getTowerColor().get());
        int yellowStudents = island2.getNumStudentByColor(ColorS.YELLOW);
        egb.setActivePlayer(egb.getPlayerByNickname("Alice"));
        Character char8 = createCharacter(8);
        egb.setActiveCharacter(char8);
        HashMap<Player, Integer> mapInfluence = new HashMap<>();
        mapInfluence = egb.getInfluence(egb.getIslandByIndex(2));
        int influenceAlice = mapInfluence.get(egb.getPlayerByNickname("Alice"));
        assertEquals(yellowStudents + 1 + 2, influenceAlice);
    }

    /** Method testChar9 checks if the Character's ability is actually used when the card 9 is selected */
    @Test
    public void testChar9() throws PlaceFullException {
        for (int i = 0; i < 6; i++)
            egb.getPlayerByNickname("Alice").getMyBoard().addToHall(ColorS.YELLOW);
        Island island2 = egb.getWorld().getIslandByIndex(2);
        for (int i = 0; i < 5; i++)
            island2.add(ColorS.YELLOW);
        egb.checkProfs();
        egb.checkIsland(egb.getIslandByIndex(2));
        assertEquals(egb.getPlayerByNickname("Alice").getColorTower(), egb.getWorld().getIslandByIndex(2).getTowerColor().get());
        int yellowStudents = island2.getNumStudentByColor(ColorS.YELLOW);
        egb.setActivePlayer(egb.getPlayerByNickname("Alice"));
        Character char9 = createCharacter(9);
        egb.setActiveCharacter(char9);
        ChooseColor message_9 = new ChooseColor(ColorS.YELLOW);
        expertController.handleCharacter(message_9, "Alice");
        HashMap<Player, Integer> mapInfluence = new HashMap<>();
        mapInfluence = egb.getInfluence(egb.getIslandByIndex(2));
        int influenceAlice = mapInfluence.get(egb.getPlayerByNickname("Alice"));
        assertEquals(1, influenceAlice);
    }

    /** Method testChar10 checks if the Character's ability is actually used when the card 10 is selected */
    @Test
    public void testChar10 () throws PlaceFullException {
        Character char10 = createCharacter(10);
        egb.setActiveCharacter(char10);
        egb.getActivePlayer().getMyBoard().addToHall(ColorS.YELLOW);
        egb.getActivePlayer().getMyBoard().addToHall(ColorS.RED);
        egb.getActivePlayer().getMyBoard().add(ColorS.GREEN);
        egb.getActivePlayer().getMyBoard().add(ColorS.YELLOW);
        int yellowStudents = egb.getActivePlayer().getMyBoard().getHall(ColorS.YELLOW);
        assertEquals(CharacterDescription.CHAR10.getDesc(), egb.getActiveCharacter().getDescription());
        ChooseTwoColors message_10 = new ChooseTwoColors(ColorS.RED, ColorS.YELLOW);
        expertController.handleCharacter(message_10, "Bob");
        assertEquals(egb.getPlayerByNickname("Bob").getMyBoard().getHall(ColorS.YELLOW), yellowStudents + 1);
    }

    /** Method testChar11 checks if the Character's ability is actually used when the card 11 is selected */
    @Test
    public void testChar11 () {
        Character char11 = createCharacter(11);
        ArrayList<ColorS> studentsOnTheCard = new ArrayList<>();
        studentsOnTheCard.addAll(((CharacterWithStudent) char11).getStudents());
        int numStudents = egb.getActivePlayer().getMyBoard().getHall(studentsOnTheCard.get(0));
        egb.setActiveCharacter(char11);
        assertEquals(CharacterDescription.CHAR11.getDesc(), egb.getActiveCharacter().getDescription());
        ChooseColor message_11 = new ChooseColor(studentsOnTheCard.get(0));
        expertController.handleCharacter(message_11, "Bob");
        assertEquals(numStudents + 1, egb.getActivePlayer().getMyBoard().getHall(studentsOnTheCard.get(0)));
    }

    /** Method testChar12 checks if the Character's ability is actually used when the card 12 is selected */
    @Test
    public void testChar12 () {
        Character char12 = createCharacter(12);
        int lisaPink = egb.getPlayerByNickname("Lisa").getMyBoard().getHall(ColorS.PINK);
        int bobPink = egb.getPlayerByNickname("Bob").getMyBoard().getHall(ColorS.PINK);
        egb.setActiveCharacter(char12);
        assertEquals(CharacterDescription.CHAR12.getDesc(), egb.getActiveCharacter().getDescription());
        ChooseColor message_12 = new ChooseColor(ColorS.PINK);
        expertController.handleCharacter(message_12, "Bob");
        assertEquals(bobPink - 3, egb.getPlayerByNickname("Bob").getMyBoard().getHall(ColorS.PINK));
        assertEquals(lisaPink - 3, egb.getPlayerByNickname("Lisa").getMyBoard().getHall(ColorS.PINK));

        assertEquals(egb.getPlayerByNickname("Alice").getMyBoard().getHall(ColorS.PINK), 0);
        assertEquals(egb.getPlayerByNickname("Alice").getMyBoard().getHall(ColorS.YELLOW), 1);
    }

    /** Method createCharacter for creating different Character cards used in the tests */
    public Character createCharacter ( int charNum){

        int cost = CharacterDescription.values()[charNum - 1].getCost();
        String desc = CharacterDescription.values()[charNum - 1].getDesc();

        Character c = null;
        CharacterWithStudent temp;

            switch (charNum) {
                case 1:
                case 11:
                    temp = new CharacterWithStudent(cost, desc, 4);
                    for (int j = 0; j < 4; j++) {
                        temp.add(egb.getContainer().draw());
                    }
                    c = temp;
                    break;
                case 2:
                    c = new CharacterProf(cost, desc, new EqualProf(), egb);
                    break;
                case 3:
                case 10:
                case 12:
                    c = new Character(cost, desc);
                    break;
                case 4:
                    c = new CharacterMN(cost, desc, players);
                    break;
                case 5:
                    c = new CharacterWithNoEntry(cost, desc);
                    break;
                case 6:
                    c = new CharacterInfluence(cost, desc, new NoTowerInfluence(), egb.getWorld());
                    break;
                case 7:
                    temp = new CharacterWithStudent(cost, desc, 6);
                    for (int j = 0; j < 6; j++) {
                        temp.add(egb.getContainer().draw());
                    }
                    c = temp;
                    break;
                case 8:
                    c = new CharacterInfluence(cost, desc, new AdditionalInfluence(), egb.getWorld());
                    break;
                case 9:
                    c = new CharacterInfluence(cost, desc, new NoColorInfluence(), egb.getWorld());
                    break;
            }
            return c;
    }



}