package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.ChooseCloud;
import it.polimi.ingsw.client.request.EntranceToHall;
import it.polimi.ingsw.client.request.MoveMN;
import it.polimi.ingsw.client.request.MoveToIsland;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.Island;
import it.polimi.ingsw.model.world.World;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class ActionControllerTest tests ActionController class.
 *
 * @author Bonfanti Stefano
 * @see ActionController
 */
class ActionControllerTest {
    private GameBoard gameBoard;
    private ActionController actionController;
    private Lobby lobby;
    private TurnController turnController;

    /** Method setup creates an ActionController and a GameBoard that
     * will be used by every test.
     * */
    @BeforeEach
    void setUp() throws InvalidIndexException {
        lobby = new Lobby();
        turnController = new TurnController();
        gameBoard = new GameBoard(3);
        gameBoard.addPlayer("Lisa", ColorT.BLACK, Mage.MAGE1);
        gameBoard.addPlayer("Bob", ColorT.WHITE, Mage.MAGE2);
        gameBoard.addPlayer("Alice", ColorT.GREY, Mage.MAGE3);
        gameBoard.setActivePlayer(gameBoard.getPlayerByNickname("Alice"));
        gameBoard.chooseAssistants(gameBoard.getPlayerByNickname("Lisa"), 8);
        gameBoard.chooseAssistants(gameBoard.getPlayerByNickname("Bob"), 7);
        gameBoard.chooseAssistants(gameBoard.getPlayerByNickname("Alice"), 4);
        actionController = new ActionController(gameBoard, lobby, turnController);
    }

    /**
     * Method handleActionMoveMN tests the arrival of a MoveMN message, in particular in the case when the
     * number of steps is completely wrong, when the number of steps is bigger than the value of the assistant
     * and when there aren't mistakes, in this case also the model is checked.
     */
    @Test
    void handleActionMoveMN() {
        //Case when the number of steps is completely wrong.
        MoveMN message1 = new MoveMN(8);
        int mnPosition = gameBoard.getWorld().getMNPosition();
        actionController.handleAction(message1);
        assertEquals(mnPosition, gameBoard.getWorld().getMNPosition());
        //Case when the number of steps is bigger than the value of the assistant.
        MoveMN message2 = new MoveMN(3);
        mnPosition = gameBoard.getWorld().getMNPosition();
        actionController.handleAction(message2);
        assertEquals(mnPosition, gameBoard.getWorld().getMNPosition());
        //Case when MotherNature has to move.
        MoveMN message3 = new MoveMN(2);
        mnPosition = gameBoard.getWorld().getMNPosition();
        actionController.handleAction(message3);
        assertEquals((mnPosition + 2) % gameBoard.getSizeWorld(), gameBoard.getWorld().getMNPosition());
        //Case when the game must end
        ColorT colorT = gameBoard.getActivePlayer().getColorTower();
        for(int counter = 0; counter < 6; counter++) {
            gameBoard.getActivePlayer().getMyBoard().remove(colorT);
        }
        MoveMN message4 = new MoveMN(2);
        mnPosition = gameBoard.getWorld().getMNPosition();
        actionController.handleAction(message4);
        assertEquals((mnPosition + 2) % gameBoard.getSizeWorld(), gameBoard.getWorld().getMNPosition());
    }

    /**
     * Method handleActionMoveToIsland tests the arrival of a MoveToIsland message, in particular in the case when the
     * index of the Island is completely wrong, three times when there aren't mistakes, in this case also the model is
     * checked and also when there isn't the Student in the Entrance.
     */
    @Test
    void handleActionMoveToIsland() {
        //Case when there aren't mistakes, so the student must be moved (first time).
        ColorS colorS = gameBoard.getActivePlayer().getMyBoard().getEntrance().get(0);
        MoveToIsland message1 = new MoveToIsland(colorS, 3);
        Island island = gameBoard.getIslandByIndex(message1.getIndex());
        int numStudentIsland = island.getNumStudentByColor(colorS);
        int sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message1);
        assertEquals(numStudentIsland + 1, island.getNumStudentByColor(colorS));
        assertEquals(sizeEntrance - 1, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        //Case when the index of the Island is bigger than the world size.
        colorS = gameBoard.getActivePlayer().getMyBoard().getEntrance().get(0);
        MoveToIsland message2 = new MoveToIsland(colorS, 15);
        sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message2);
        assertEquals(sizeEntrance , gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        //Case when there aren't mistakes, so the student must be moved (second time).
        colorS = gameBoard.getActivePlayer().getMyBoard().getEntrance().get(0);
        MoveToIsland message3 = new MoveToIsland(colorS, 2);
        island = gameBoard.getIslandByIndex(message3.getIndex());
        numStudentIsland = island.getNumStudentByColor(colorS);
        sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message3);
        assertEquals(numStudentIsland + 1, island.getNumStudentByColor(colorS));
        assertEquals(sizeEntrance - 1, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        //Case when there aren't mistakes, so the student must be moved and is called the method
        // turnController.setMoveStudentsCheck().
        colorS = gameBoard.getActivePlayer().getMyBoard().getEntrance().get(0);
        MoveToIsland message4 = new MoveToIsland(colorS, 1);
        island = gameBoard.getIslandByIndex(message4.getIndex());
        numStudentIsland = island.getNumStudentByColor(colorS);
        sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message4);
        assertEquals(numStudentIsland + 1, island.getNumStudentByColor(colorS));
        assertEquals(sizeEntrance - 1, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        //Case when there isn't the student in the Entrance
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        MoveToIsland message5 = new MoveToIsland(ColorS.BLUE, 3);
        sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message5);
        assertEquals(sizeEntrance, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
    }

    /**
     * Method handleActionEntranceToHall tests the arrival of an EntranceToHall message, in particular three times when
     * there aren't mistakes, in this case also the model is checked, when there isn't the Student in the Entrance and when
     * the Hall is full.
     */
    @Test
    void handleActionEntranceToHall() {
        //Case when there aren't mistakes, so the student must be moved (first time).
        ColorS colorS = gameBoard.getActivePlayer().getMyBoard().getEntrance().get(0);
        EntranceToHall message1 = new EntranceToHall(colorS);
        int sizeHallColorS = gameBoard.getActivePlayer().getMyBoard().getHall(colorS);
        int sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message1);
        assertEquals(sizeHallColorS + 1, gameBoard.getActivePlayer().getMyBoard().getHall(colorS));
        assertEquals(sizeEntrance - 1, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        //Case when there aren't mistakes, so the student must be moved (second time).
        colorS = gameBoard.getActivePlayer().getMyBoard().getEntrance().get(0);
        EntranceToHall message2 = new EntranceToHall(colorS);
        sizeHallColorS = gameBoard.getActivePlayer().getMyBoard().getHall(colorS);
        sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message2);
        assertEquals(sizeHallColorS + 1, gameBoard.getActivePlayer().getMyBoard().getHall(colorS));
        assertEquals(sizeEntrance - 1, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        // Case when there aren't mistakes, so the student must be moved and is called the method
        // turnController.setMoveStudentsCheck().
        colorS = gameBoard.getActivePlayer().getMyBoard().getEntrance().get(0);
        EntranceToHall message3 = new EntranceToHall(colorS);
        sizeHallColorS = gameBoard.getActivePlayer().getMyBoard().getHall(colorS);
        sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message3);
        assertEquals(sizeHallColorS + 1, gameBoard.getActivePlayer().getMyBoard().getHall(colorS));
        assertEquals(sizeEntrance - 1, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        //Case when the hall is full.
        gameBoard.getActivePlayer().getMyBoard().getHall().put(ColorS.RED, 10);
        gameBoard.getActivePlayer().getMyBoard().add(ColorS.RED);
        EntranceToHall message4 = new EntranceToHall(ColorS.RED);
        sizeHallColorS = gameBoard.getActivePlayer().getMyBoard().getHall(colorS);
        sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message4);
        assertEquals(sizeHallColorS, gameBoard.getActivePlayer().getMyBoard().getHall(colorS));
        assertEquals(sizeEntrance, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        //Case when there isn't the student in the Entrance
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        EntranceToHall message5 = new EntranceToHall(ColorS.BLUE);
        sizeEntrance = gameBoard.getActivePlayer().getMyBoard().getEntrance().size();
        actionController.handleAction(message5);
        assertEquals(sizeEntrance, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
    }

    /**
     * Method handleActionChooseCloud tests the arrival of a ChooseCloud message, in particular a case where the index of
     * the Cloud is completely wrong,a case where there aren't mistakes and a case where the Cloud has already been chosen.
     */
    @Test
    void handleActionChooseCloud() {
        //Case when the index of the Cloud is completely wrong.
        ChooseCloud message1 = new ChooseCloud(6);
        gameBoard.newClouds();
        actionController.handleAction(message1);
        for(int counter = 0; counter < gameBoard.getNumPlayers(); counter++) {
            assertEquals(4, gameBoard.getCloudByIndex(counter).getStudents().size());
        }
        //Case when there aren't mistakes, so the students must be moved from the Cloud.
        ChooseCloud message2 = new ChooseCloud(1);
        Cloud cloud = gameBoard.getCloudByIndex(1);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        actionController.handleAction(message2);
        assertEquals(9, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        assertEquals(0, cloud.getStudents().size());
        //Case when the Cloud as already been chosen by another player.
        ChooseCloud message3 = new ChooseCloud(1);
        cloud = gameBoard.getCloudByIndex(1);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        gameBoard.getActivePlayer().getMyBoard().getEntrance().remove(0);
        actionController.handleAction(message3);
        assertEquals(5, gameBoard.getActivePlayer().getMyBoard().getEntrance().size());
        assertEquals(0, cloud.getStudents().size());
    }
}