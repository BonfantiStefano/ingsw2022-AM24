package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.Server;

import java.util.Optional;

/**
 * ActionController class handle all the messages that all the players have to do during their turn.
 *
 * @author Bonfanti Stefano
 */
public class ActionController {
    private Model model;
    private TurnController turnController; //maybe is better the interface
    private int numMoveStudent;
    private Lobby lobby; //maybe is better the interface

    /**
     * Constructor ActionController creates a new empty ActionController instance.
     * @param model Model - the model associated to the controller.
     * @param lobby Lobby - the lobby where all the messages are sent.
     * @param turnController - the turnController used to updating the Phase of the game.
     */
    public ActionController(Model model, Lobby lobby, TurnController turnController){
        this.model=model;
        this.turnController = turnController;
        this.numMoveStudent = 0;
        this.lobby = lobby;
    }

    /**
     * Method handleAction(EntranceToHall) checks if the params of the message are corrected, if there are some mistakes sends a message
     * to the client, otherwise updates the model's state, in particular moves the Student, whose color is obtained by the message,
     * from the Entrance to the Hall.
     * @param m EntranceToHall - the message sent by the client to the server.
     */
    public void handleAction(EntranceToHall m){
        try {
            model.entranceToHall(m.getColorS());
            numMoveStudent++;
            if(numMoveStudent == 3) {
                turnController.setMoveStudentsCheck(true);
                numMoveStudent = 0;
            }
        } catch (PlaceFullException e) {
            lobby.sendMessage(model.getActivePlayer().getNickname(), e.getMessage());
        } catch (NoSuchStudentException e) {
            lobby.sendMessage(model.getActivePlayer().getNickname(), e.getMessage());
        }
    }

    /**
     * Method handleAction(MoveToIsland) checks if the params of the message are corrected, if there are some mistakes sends a message
     * to the client, otherwise updates the model's state, in particular moves the Student, whose color is obtained by the message,
     * from the Entrance to the Island, whose index is obtained by the message.
     * @param m MoveToIsland - the message sent by the client to the server.
     */
    public void handleAction(MoveToIsland m){
        if(m.getIndex() < 0 || m.getIndex() >= model.getSizeWorld()) {
            lobby.sendMessage(model.getActivePlayer().getNickname(), "Error: invalid Island index");
        } else {
            try {
                model.moveStudent(m.getColorS(), model.getActivePlayer().getMyBoard(), model.getIslandByIndex(m.getIndex()));
                numMoveStudent++;
                if(numMoveStudent == model.getNumPlayers()+1) {
                    turnController.setMoveStudentsCheck(true);
                    numMoveStudent = 0;
                }
            } catch (NoSuchStudentException e) {
                lobby.sendMessage(model.getActivePlayer().getNickname(), e.getMessage());
            }
        }
    }

    /**
     * Method handleAction(MoveMN) checks if the params of the message are corrected, if there are some mistakes sends a message
     * to the client, otherwise updates the model's state, in particular moves mother nature by a number of steps equal to the
     * value passed as a parameter in the message and checks if the game must end.
     * @param m MoveMN - the message sent by the client to the server.
     */
    public void handleAction(MoveMN m) {
        if(m.getIndex() > 7 || m.getIndex() < 1) {
            lobby.sendMessage(model.getActivePlayer().getNickname(), "Error: Mother Nature can't do these steps");
        } else {
            try {
                model.moveMN(m.getIndex());
                Optional<Player> winner = model.checkWin();
                winner.ifPresentOrElse(w -> {lobby.sendMessage(w.getNickname(), "You won");
                            lobby.sendMessageToOthers(w.getNickname(), "You Lose");
                            turnController.setGameEnded(true);},
                        () -> {if(model.getSizeWorld() == 3) {
                            lobby.sendMessageToAll("The game ends in a draw");
                            turnController.setGameEnded(true);
                        }
                        }
                );
                turnController.setMoveMNCheck(true);
            } catch (InvalidMNStepsException e) {
                lobby.sendMessage(model.getActivePlayer().getNickname(), e.getMessage());
            }
        }
    }


    /**
     * Method handleAction(ChooseCloud) checks if the params of the message are corrected, if there are some mistakes sends a message
     * to the client, otherwise updates the model's state, in particular moves the Students from the Cloud, whose index is obtained
     * to the Entrance.
     * @param m ChooseCloud - the message sent by the client to the server.
     */
    public void handleAction(ChooseCloud m){
        if(m.getIndex() < 0 || m.getIndex() >= model.getNumPlayers() || model.getCloudByIndex(m.getIndex()).getStudents().size() == 0) {
            lobby.sendMessage(model.getActivePlayer().getNickname(), "Error: invalid Cloud index");
        } else {
            Cloud cloud = model.getCloudByIndex(m.getIndex());
            for(int counter = 0; counter <= model.getNumPlayers(); counter++) {
                try {
                    model.moveStudent(cloud.getStudents().get(0), cloud, model.getActivePlayer().getMyBoard());
                } catch (NoSuchStudentException e) {
                    lobby.sendMessage(model.getActivePlayer().getNickname(), e.getMessage());
                }
            }
            turnController.setChooseCloudCheck(true);
        }
    }
}
