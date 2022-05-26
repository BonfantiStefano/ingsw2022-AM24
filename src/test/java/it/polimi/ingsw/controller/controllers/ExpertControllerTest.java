package it.polimi.ingsw.controller.controllers;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.controller.PHASE;
import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.controller.controllers.ExpertController;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.*;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerInterface;
import it.polimi.ingsw.model.profstrategy.EqualProf;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.influence.AdditionalInfluence;
import it.polimi.ingsw.model.world.influence.NoColorInfluence;
import it.polimi.ingsw.model.world.influence.NoTowerInfluence;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.virtualview.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class ExpertControllerTest tests ExpertController class.
 *
 * @see ExpertController
 */
public class ExpertControllerTest {

    private ExpertController expertController, expController, c;

    private ExpertGameBoard egb;
    private int mnPos;
    private ArrayList<PlayerInterface> players;

    /**
     * Method init creates an ExpertController that is used by every test.
     */
    @BeforeEach
    public void init() throws PlaceFullException, InvalidIndexException {
        Lobby lobby = new Lobby();
        expertController = new ExpertController(lobby, new GameParams(3, true, "Bob", Mage.MAGE1, ColorT.BLACK));

        egb = (ExpertGameBoard) expertController.getModel();

        //initial position of MotherNature
        mnPos = egb.getWorld().getMNPosition();

        egb.setActivePlayer(egb.getPlayerByNickname("Bob"));

        Join join1 = new Join("Lisa", Mage.MAGE2, ColorT.WHITE,0);
        expertController.handleMessage(join1, "Lisa");
        Join join2 = new Join("Alice", Mage.MAGE3, ColorT.GREY, 1);
        expertController.handleMessage(join2, "Alice");
        //egb.addPlayer("Lisa", ColorT.WHITE, Mage.MAGE2);
        //egb.addPlayer("Alice", ColorT.GREY, Mage.MAGE3);

        // in hall: bob 3 pink, lisa 4 pink
        for (int i = 0; i < 3; i++) {
            egb.getPlayerByNickname("Lisa").getMyBoard().addToHall(ColorS.PINK);
            egb.getActivePlayer().getMyBoard().addToHall(ColorS.PINK);
        }
        egb.getPlayerByNickname("Lisa").getMyBoard().addToHall(ColorS.PINK);


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

        for (int i = 0; i < 6; i++){
            egb.getActivePlayer().getMyBoard().addToHall(ColorS.GREEN);
        }
        egb.checkProfs();
        Player p = egb.getProfs().get(ColorS.GREEN);
        assertEquals("Bob", p.getNickname());

        egb.checkProfs();
        Player player = egb.getProfs().get(ColorS.PINK);
        assertEquals("Lisa", player.getNickname());

        players = new ArrayList<>();
        players.addAll(egb.getPlayers());
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

        for (CharacterDescription c : CharacterDescription.values())
            if (!(availableChars.contains(c))) {
                notAvailableChar = c;
                break;
            }

        expertController.handleMessage(new ChooseAssistant(1), "Bob");
        expertController.handleMessage(new ChooseAssistant(2), "Lisa");
        expertController.handleMessage(new ChooseAssistant(3), "Alice");

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

        //the character in virtual view in active
        VirtualView view = expertController.getVirtualView();
        assertTrue(view.getVirtualCharacters().get(0).isActive());

        expertController.getModel().resetTurn();
        assertFalse(view.getVirtualCharacters().get(0).isActive());
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
    @Test
    public void testChar2() throws PlaceFullException {
        Character char2 = createCharacter(2);
        //lisa 4 pink, now also bob 4 pink
        egb.getPlayerByNickname("Bob").getMyBoard().addToHall(ColorS.PINK);
        egb.setActiveCharacter(char2);
        assertEquals(CharacterDescription.CHAR2.getDesc(), egb.getActiveCharacter().getDescription());
        egb.checkProfs();
        Player player = egb.getProfs().get(ColorS.PINK);
        assertEquals(egb.getActivePlayer().getNickname(), player.getNickname());

    }

    /** Method testChar3 checks if the Character's ability is actually used when the card 3 is selected */
    @Test
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
        HashMap<Player, Integer> mapInfluence = egb.getInfluence(egb.getIslandByIndex(2));
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
        ArrayList<ColorS> studentsCard = ((CharacterWithStudent) egb.getActiveCharacter()).getStudents();
        assertEquals(6, studentsCard.size());

        ColorS studentCard = studentsCard.get(0);
        int numStudentsLisa = 0;
        for(ColorS c : lisa.getMyBoard().getEntrance()){
            if(c.equals(studentCard))
                numStudentsLisa++;
        }
        ChooseTwoColors message_7 = new ChooseTwoColors(ColorS.RED, studentCard);
        expertController.handleCharacter(message_7, "Lisa");
        ArrayList<ColorS> studentsOnCard = ((CharacterWithStudent) char7).getStudents();
        assertEquals(6, studentsOnCard.size());

        int numStudLisa = 0;
        for(ColorS c : lisa.getMyBoard().getEntrance()){
            if(c.equals(studentCard))
                numStudLisa++;
        }
        if(studentCard.equals(ColorS.RED)) assertEquals(numStudentsLisa, numStudLisa);
        else assertEquals(numStudentsLisa + 1, numStudLisa);

    }

    /** Method testChar8 checks if the Character's ability is actually used when the card 8 is selected */
    @Test
    public void testChar8() throws PlaceFullException {
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
        HashMap<Player, Integer> mapInfluence = egb.getInfluence(egb.getIslandByIndex(2));
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
        egb.setActivePlayer(egb.getPlayerByNickname("Alice"));
        Character char9 = createCharacter(9);
        egb.setActiveCharacter(char9);
        ChooseColor message_9 = new ChooseColor(ColorS.YELLOW);
        expertController.handleCharacter(message_9, "Alice");
        HashMap<Player, Integer> mapInfluence = egb.getInfluence(egb.getIslandByIndex(2));
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


    /**
     * Method testListeners checks if view update itself each time an update event is emitted.
     */
    @Test
    public void testListeners(){
        Lobby lobby = new Lobby();
        expController = new ExpertController(lobby, new GameParams(3, true, "Leo", Mage.MAGE1, ColorT.BLACK));
        Join join = new Join("Lisa", Mage.MAGE2, ColorT.WHITE, 1);
        expController.handleMessage(join, "Lisa");
        Join join_message = new Join("Alice", Mage.MAGE3, ColorT.GREY, 2);
        expController.handleMessage(join_message, "Alice");
        ExpertGameBoard gameBoard = (ExpertGameBoard) expController.getModel();
        VirtualView view = expController.getVirtualView();

        assertEquals(3, view.getVirtualCharacters().size());
        assertEquals(12, view.getVirtualWorld().size());
        assertEquals(3, view.getVirtualPlayers().size());
        assertEquals(20 - gameBoard.getNumPlayers(), view.getVirtualCoins());

        expController.handleMessage(new ChooseAssistant(2), "Leo");
        expController.handleMessage(new ChooseAssistant(6), "Lisa");
        expController.handleMessage(new ChooseAssistant(10), "Alice");

        assertEquals(1, view.getVirtualPlayers().get(0).getVirtualCoins());
        assertEquals(1, view.getVirtualPlayers().get(1).getVirtualCoins());
        assertEquals(1, view.getVirtualPlayers().get(2).getVirtualCoins());

        Player modelLeo = gameBoard.getActivePlayer();
        VirtualPlayer virtualLeo = view.getVirtualPlayers().get(0);
        modelLeo.getMyBoard().getEntrance().removeAll(modelLeo.getMyBoard().getEntrance());
        assertEquals(0, virtualLeo.getVirtualBoard().getEntrance().size());
        //now Leo has: 7 blue
        for(int i = 0; i < 7; i++){
            modelLeo.getMyBoard().add(ColorS.BLUE);
        }
        assertEquals(7, virtualLeo.getVirtualBoard().getEntrance().size());
        EntranceToHall messageHall = new EntranceToHall(ColorS.BLUE);

        //invalid message
        SpecialMoveIsland invalidMessage = new SpecialMoveIsland(ColorS.GREEN, 17);
        expController.handleMessage(invalidMessage, "Leo");
        //valid messages
        for(int i = 0; i < 3; i++){
            expController.handleMessage(messageHall, "Leo");
        }
        //now Leo has 2 coins, 3 blue in hall
        assertEquals(2, view.getVirtualPlayers().get(0).getVirtualCoins());
        assertEquals(3, virtualLeo.getVirtualBoard().getHall().get(ColorS.BLUE));
        //now Leo has 4 coins
        modelLeo.setCoins(1);
        modelLeo.setCoins(1);
        assertEquals(4, view.getVirtualPlayers().get(0).getVirtualCoins());

        //---------CHARACTER_ACTION--------------------------
        int initCoins = gameBoard.getPlayerByNickname("Leo").getCoins();
        Character characterplayed = gameBoard.getCharacters().get(0);
        int cost = characterplayed.getCost();
        String desc = characterplayed.getDescription();
        PlayCharacter messageCharacter = new PlayCharacter(Arrays.stream(CharacterDescription.values()).
                filter(c->c.getDesc().equals(desc)).findFirst().get());
        expController.handleCharacter(messageCharacter, "Leo");
        int finalCoins = gameBoard.getPlayerByNickname("Leo").getCoins();
        assertEquals(initCoins - cost, finalCoins);
        assertEquals(finalCoins, view.getVirtualPlayers().get(0).getVirtualCoins());
        assertEquals(view.getVirtualCharacters().get(0).getDescription(), gameBoard.getActiveCharacter().getDescription());
        assertTrue(view.getVirtualCharacters().get(0).isAlreadyPlayed());

        //---------CHARACTER WITH NO ENTRY--------------------------
        //active character gameBoard: char 5
        //view: virtual characters[0] = char 5
        Character char5 = createCharacter(5);
        gameBoard.set(0, char5);
        view.getVirtualCharacters().set(0, new VirtualCharacterWithNoEntry(char5));
        int initialNE = ((CharacterWithNoEntry) gameBoard.getCharacters().get(0)).getNumNoEntry();

        int posMN = gameBoard.getWorld().getMNPosition();
        int virtualPosMN = view.getMnPos();
        assertEquals(posMN, virtualPosMN);
        int indexIsland = (mnPos + 1) % view.getVirtualWorld().size();
        assertEquals(0, view.getVirtualWorld().get(indexIsland).getNoEntry());
        gameBoard.setActiveCharacter(char5);

        ChooseIsland messageIsland = new ChooseIsland(indexIsland);
        expController.handleCharacter(messageIsland, "Leo");

        int finalNE = ((CharacterWithNoEntry) gameBoard.getCharacters().get(0)).getNumNoEntry();
        assertEquals(finalNE, initialNE - 1);
        assertEquals(1, view.getVirtualWorld().get(indexIsland).getNoEntry());
        int virtualNE = ((VirtualCharacterWithNoEntry) view.getVirtualCharacters().get(0)).getNoEntry();
        assertEquals(finalNE, virtualNE);
        //move MN on island with no entry
        int blu = gameBoard.getIslandByIndex(indexIsland).getNumStudentByColor(ColorS.BLUE);
        gameBoard.getWorld().getIslandByIndex(indexIsland).add(ColorS.BLUE);
        assertEquals(blu + 1, view.getVirtualWorld().get(indexIsland).getNumStudentByColor(ColorS.BLUE));
        assertEquals(view.getVirtualProfs().get(ColorS.BLUE).getNickname(), view.getVirtualPlayers().get(0).getNickname());
        MoveMN messageMN = new MoveMN(1);
        expController.handleMessage(messageMN, "Leo");
        assertEquals(0, view.getVirtualWorld().get(indexIsland).getTowers().size());

        //---------CHARACTER WITH STUDENTS--------------------------
        //active character gameBoard: char 11
        //view: virtual characters[0] = char 11
        Character char11 = createCharacter(11);
        gameBoard.set(0, char11);
        view.getVirtualCharacters().set(0, new VirtualCharacterWithStudents(char11));
        ArrayList<ColorS> studentsOnTheCard = new ArrayList<>();
        studentsOnTheCard.addAll(((CharacterWithStudent) char11).getStudents());
        ArrayList<ColorS> virtualStudentsOnTheCard = ((VirtualCharacterWithStudents) view.getVirtualCharacters().get(0)).getStudents();
        assertTrue(studentsOnTheCard.containsAll(virtualStudentsOnTheCard));
        int numVirtualStudents = view.getVirtualPlayers().get(0).getVirtualBoard().getHall().get(studentsOnTheCard.get(0));
        int numStudents = gameBoard.getActivePlayer().getMyBoard().getHall(studentsOnTheCard.get(0));
        gameBoard.setActiveCharacter(char11);
        assertEquals(CharacterDescription.CHAR11.getDesc(), gameBoard.getActiveCharacter().getDescription());
        ChooseColor message= new ChooseColor(studentsOnTheCard.get(0));
        expController.handleCharacter(message, "Leo");
        assertEquals(numStudents + 1, gameBoard.getActivePlayer().getMyBoard().getHall(studentsOnTheCard.get(0)));
        assertEquals(numVirtualStudents + 1, view.getVirtualPlayers().get(0).getVirtualBoard().getHall().get(studentsOnTheCard.get(0)));

    }

    /**
     * Method testListeners checks if view update itself each time an update event is emitted.
     */
    @Test
    public void test_Listeners(){
        Lobby lobby = new Lobby();
        c = new ExpertController(lobby, new GameParams(2, true, "Leo", Mage.MAGE1, ColorT.BLACK));
        Join join = new Join("Lisa", Mage.MAGE2, ColorT.WHITE, 1);
        c.handleMessage(join, "Lisa");

        ExpertGameBoard gameBoard = (ExpertGameBoard) c.getModel();
        VirtualView view = c.getVirtualView();

        c.handleMessage(new ChooseAssistant(2), "Leo");
        c.handleMessage(new ChooseAssistant(6), "Lisa");

        gameBoard.getPlayerByNickname("Leo").setCoins(-1);
        assertEquals(0, view.getVirtualPlayers().get(0).getVirtualCoins());

        Character characterplayed = gameBoard.getCharacters().get(0);
        String desc = characterplayed.getDescription();
        PlayCharacter messageCharacter = new PlayCharacter(Arrays.stream(CharacterDescription.values()).
                filter(c->c.getDesc().equals(desc)).findFirst().get());
        c.handleCharacter(messageCharacter, "Leo");

        assertNull(gameBoard.getActiveCharacter());

        gameBoard.getPlayerByNickname("Leo").setCoins(4);
        assertEquals(4, view.getVirtualPlayers().get(0).getVirtualCoins());

        gameBoard.getPlayerByNickname("Leo").getMyBoard().getEntrance().removeAll(gameBoard.getPlayerByNickname("Leo").getMyBoard().getEntrance());
        for(int i = 0; i < 7; i++){
            gameBoard.getPlayerByNickname("Leo").getMyBoard().add(ColorS.BLUE);
        }
        assertEquals(7, view.getVirtualPlayers().get(0).getVirtualBoard().getEntrance().size());
        EntranceToHall messageHall = new EntranceToHall(ColorS.BLUE);
        for(int i = 0; i < 3; i++){
            c.handleMessage(messageHall, "Leo");
        }

        //---------CHARACTER WITHOUT NE / STUDENTS--------------------------
        //char 3
        Character char3 = createCharacter(3);
        gameBoard.set(0, char3);
        view.getVirtualCharacters().set(0, new VirtualCharacter(char3));
        assertEquals("Leo", view.getVirtualProfs().get(ColorS.BLUE).getNickname());
        gameBoard.getIslandByIndex(5).add(ColorS.BLUE);
        gameBoard.getIslandByIndex(5).add(ColorS.BLUE);
        gameBoard.getIslandByIndex(5).add(ColorS.BLUE);
        assertEquals(0, view.getVirtualWorld().get(5).getTowers().size());

        gameBoard.setActiveCharacter(char3);

        ChooseIsland messageChooseIsland = new ChooseIsland(5);
        c.handleCharacter(messageChooseIsland, "Leo");

        assertEquals(1, view.getVirtualWorld().get(5).getTowers().size());
        assertEquals(view.getVirtualWorld().get(5).getTowerColor().get(), gameBoard.getPlayerByNickname("Leo").getColorTower());

    }


    /**
     * Method test_Game checks the whole match
     */
    @Test
    public void test_Game(){
        Lobby lobby = new Lobby();
        expController = new ExpertController(lobby, new GameParams(3, true, "Leo", Mage.MAGE1, ColorT.BLACK));
        Join join = new Join("Lisa", Mage.MAGE2, ColorT.WHITE, 1);
        expController.handleMessage(join, "Lisa");
        Join join_message = new Join("Alice", Mage.MAGE3, ColorT.GREY, 2);
        expController.handleMessage(join_message, "Alice");
        ExpertGameBoard gameBoard = (ExpertGameBoard) expController.getModel();
        VirtualView view = expController.getVirtualView();

        expController.handleMessage(new ChooseAssistant(2), "Leo");
        expController.handleMessage(new ChooseAssistant(6), "Lisa");
        expController.handleMessage(new ChooseAssistant(9), "Alice");

        // remove students from players' entrance
        ArrayList<ColorS> entranceLeo = gameBoard.getPlayerByNickname("Leo").getMyBoard().getEntrance();
        gameBoard.getPlayerByNickname("Leo").getMyBoard().getEntrance().removeAll(entranceLeo);

        ArrayList<ColorS> entranceLisa = gameBoard.getPlayerByNickname("Lisa").getMyBoard().getEntrance();
        gameBoard.getPlayerByNickname("Lisa").getMyBoard().getEntrance().removeAll(entranceLisa);

        ArrayList<ColorS> entranceAlice = gameBoard.getPlayerByNickname("Alice").getMyBoard().getEntrance();
        gameBoard.getPlayerByNickname("Alice").getMyBoard().getEntrance().removeAll(entranceAlice);

        for(VirtualPlayer p : view.getVirtualPlayers()){
            assertEquals(0, p.getVirtualBoard().getEntrance().size());
        }

        int mnPos = gameBoard.getWorld().getMNPosition();

        // 6 consecutive islands have at least 2 red students
        for(int i = 0; i < 6; i++){
            int indexIsland = (mnPos + i) % gameBoard.getWorld().getSize();
            gameBoard.getIslandByIndex(indexIsland).add(ColorS.RED);
            gameBoard.getIslandByIndex(indexIsland).add(ColorS.RED);
        }

        //entrance:
        // Leo 7 red, Lisa 7 yellow, Alice 7 blue
        for(int i = 0; i < 7; i++){
            gameBoard.getPlayerByNickname("Leo").getMyBoard().add(ColorS.RED);
            gameBoard.getPlayerByNickname("Lisa").getMyBoard().add(ColorS.YELLOW);
            gameBoard.getPlayerByNickname("Alice").getMyBoard().add(ColorS.BLUE);
        }

        for(VirtualPlayer p : view.getVirtualPlayers()){
            assertEquals(7, p.getVirtualBoard().getEntrance().size());
        }

        //----------------Leo's turn----------------------------

        //MOVE STUDENTS PHASE =>  move 4 red in hall
        for (int i = 0; i < 4; i++){
            EntranceToHall messageHall = new EntranceToHall(ColorS.RED);
            expController.handleMessage(messageHall, "Leo");
        }
        assertEquals("Leo", view.getVirtualProfs().get(ColorS.RED).getNickname());

        //MOVE_MN PHASE
        assertEquals(view.getMnPos(), gameBoard.getWorld().getMNPosition());
        MoveMN messageMN = new MoveMN(1);
        expController.handleMessage(messageMN, "Leo");
        assertEquals(view.getMnPos(), gameBoard.getWorld().getMNPosition());

        //assertEquals(ColorT.BLACK, view.getVirtualWorld().get(view.getMnPos()).getTowerColor().get());

        //CHARACTER: char 3
        Character char3 = createCharacter(3);
        gameBoard.set(0, char3);
        view.getVirtualCharacters().set(0, new VirtualCharacter(char3));
        int index = view.getMnPos() == 0 ? view.getVirtualWorld().size() -1 : view.getMnPos() - 1;
        assertEquals(0, view.getVirtualWorld().get(index).getTowers().size());

        gameBoard.setActiveCharacter(char3);

        ChooseIsland messageChooseIsland = new ChooseIsland(index);
        expController.handleCharacter(messageChooseIsland, "Leo");

        assertEquals(2, view.getVirtualWorld().get(view.getMnPos()).getTowers().size());
        assertEquals(view.getVirtualWorld().get(view.getMnPos()).getTowerColor().get(), gameBoard.getPlayerByNickname("Leo").getColorTower());

        assertEquals(11, gameBoard.getWorld().getSize());
        assertEquals(11, view.getVirtualWorld().size());

        assertEquals(view.getMnPos(), gameBoard.getWorld().getMNPosition());

        //CHOOSE_CLOUD
        int sizeEntranceOld = view.getVirtualPlayers().get(0).getVirtualBoard().getEntrance().size();
        ChooseCloud messageCloud = new ChooseCloud(1);
        expController.handleMessage(messageCloud, "Leo");
        int sizeEntranceNew = view.getVirtualPlayers().get(0).getVirtualBoard().getEntrance().size();
        assertEquals(sizeEntranceNew , sizeEntranceOld + 4);


        //----------------Lisa's turn------------------------------------------------
        //MOVE STUDENTS PHASE =>  move 4 yellow in hall
        for (int i = 0; i < 4; i++){
            EntranceToHall messageHall = new EntranceToHall(ColorS.YELLOW);
            expController.handleMessage(messageHall, "Lisa");
        }
        assertEquals("Lisa", view.getVirtualProfs().get(ColorS.YELLOW).getNickname());

        //MOVE_MN PHASE
        assertEquals(view.getMnPos(), gameBoard.getWorld().getMNPosition());
        MoveMN message_mn = new MoveMN(1);
        expController.handleMessage(message_mn, "Lisa");
        assertEquals(view.getMnPos(), gameBoard.getWorld().getMNPosition());
        assertEquals(ColorT.BLACK, view.getVirtualWorld().get(view.getMnPos()).getTowerColor().get());

        assertEquals(10, gameBoard.getWorld().getSize());
        assertEquals(10, view.getVirtualWorld().size());
        assertEquals(3, view.getVirtualWorld().get(view.getMnPos()).getTowers().size());

        //CHOOSE_CLOUD PHASE
        int sizeOld = view.getVirtualPlayers().get(1).getVirtualBoard().getEntrance().size();
        ChooseCloud message_cloud = new ChooseCloud(0);
        expController.handleMessage(message_cloud, "Lisa");
        int sizeNew = view.getVirtualPlayers().get(1).getVirtualBoard().getEntrance().size();
        assertEquals(sizeNew , sizeOld + 4);

        //char 3
        Character char_3 = createCharacter(3);
        gameBoard.set(0, char_3);
        view.getVirtualCharacters().set(0, new VirtualCharacter(char_3));
        int index_island = (view.getMnPos() + 1)%gameBoard.getWorld().getSize();
        assertEquals(0, view.getVirtualWorld().get(index_island).getTowers().size());
        gameBoard.getPlayerByNickname("Lisa").setCoins(2);
        assertEquals(4, view.getVirtualPlayers().get(1).getVirtualCoins());

        gameBoard.setActiveCharacter(char_3);

        ChooseIsland messageChoose_Island = new ChooseIsland(index_island);
        expController.handleCharacter(messageChoose_Island, "Lisa");

        assertEquals(4, view.getVirtualWorld().get(view.getMnPos()).getTowers().size());
        assertEquals(9, view.getVirtualWorld().size());

        //--------Alice's turn---------------------
        //MOVE STUDENTS PHASE =>  move 4 blue in hall
        for (int i = 0; i < 4; i++){
            EntranceToHall messageHall = new EntranceToHall(ColorS.BLUE);
            expController.handleMessage(messageHall, "Alice");
        }

        //MOVE_MN PHASE
        assertEquals(view.getMnPos(), gameBoard.getWorld().getMNPosition());
        MoveMN message_MN = new MoveMN(1);
        expController.handleMessage(message_MN, "Alice");
        assertEquals(view.getMnPos(), gameBoard.getWorld().getMNPosition());
        assertEquals(ColorT.BLACK, view.getVirtualWorld().get(view.getMnPos()).getTowerColor().get());
        assertEquals(8, view.getVirtualWorld().size());

        //char 3
        Character charThree = createCharacter(3);
        gameBoard.set(0, charThree);
        view.getVirtualCharacters().set(0, new VirtualCharacter(charThree));
        int index_= (view.getMnPos() + 1)%gameBoard.getWorld().getSize();
        assertEquals(0, view.getVirtualWorld().get(index_).getTowers().size());
        gameBoard.getPlayerByNickname("Alice").setCoins(4);

        gameBoard.setActiveCharacter(char_3);

        ChooseIsland messageChoose_island = new ChooseIsland(index_);
        expController.handleCharacter(messageChoose_island, "Alice");

        assertEquals(0, view.getVirtualPlayers().get(0).getVirtualBoard().getTowers().size());

        //CHOOSE_CLOUD PHASE
        int old_ = view.getVirtualPlayers().get(2).getVirtualBoard().getEntrance().size();
        ChooseCloud message_Cloud = new ChooseCloud(2);
        expController.handleMessage(message_Cloud, "Alice");
        int new_ = view.getVirtualPlayers().get(2).getVirtualBoard().getEntrance().size();
        assertEquals(new_ , old_ + 4);

        assertEquals(PHASE.GAME_WON, expController.getPhase());
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