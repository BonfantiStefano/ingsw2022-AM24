package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Server;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The Controller will handle messages from all clients by checking their contents and modifying the model's state
 * according to the commands received
 */
public class Controller {

    private Model model;
    private ActionController actionController;
    private TurnController turnController;
    private Server server;
    private ArrayList<Player> sortedPlayers;
    private PHASE phase;
    private int numPlayers;
    private int havePlayed;
    private int haveChosenAssistant;
    private String activePlayer;
    private boolean gameStarted;

    public Controller(Server server , GameParams m){
        this.server = server;
        createModel(m);
        phase=PHASE.SETUP;
        turnController = new TurnController();
        actionController= new ActionController(model, server, turnController);
        gameStarted = false;
    }

    /**
     * Creates a new Model instance
     * @param m message containing the Game Parameters
     */
    public void createModel(GameParams m){
        numPlayers=m.getNumPlayers();
        model = new GameBoard(m.getNumPlayers());
        model.addPlayer(m.getNickname(), m.getColorT(), m.getMage());
        model.newClouds();
        haveChosenAssistant = 0;
        havePlayed = 0;
    }

    /**
     * Handles the message received by performing the correct action based on the type of Request
     * @param m Request message sent by a Client
     * @param nickname the nickname of the Player associated with the Client
     *                 NOT FINAL
     */
    public void handleMessage(Request m, String nickname){
        if(model.getActivePlayer()!=null)
            activePlayer = model.getActivePlayer().getNickname();
        //message needed to start the game
        if(m instanceof Join msg){
            //check if another player can join and his nickname is available
            if(model.getPlayerByNickname(msg.getNickname())==null && model.getPlayers().size()<numPlayers && phase.equals(PHASE.SETUP)) {
                model.addPlayer(msg.getNickname(), msg.getColorT(), msg.getMage());
                server.sendMessage(nickname, "Accepted!");
                if(model.getPlayers().size() == numPlayers) {
                    //all players have connected
                    turnController.setGameStarted(true);
                    gameStarted = true;
                }

            }
            //if the Player had disconnected update his status as connected
            else if(model.getPlayerByNickname(msg.getNickname())!=null)
                model.setConnected(nickname, true);
            else{
                server.sendMessage(nickname, "Nickname already in use!");
            }
        }
        else if (m instanceof ChooseAssistant msg && phase.equals(PHASE.PLANNING))
            try {
                model.chooseAssistants(model.getPlayerByNickname(nickname), msg.getIndex());
                increaseHaveChosenAssistant();
            } catch (InvalidIndexException e) {
                server.sendMessage(nickname, "Invalid index!");
            }
        else if (m instanceof Disconnect){
            //if a Player disconnects during the setup phase the game is canceled
            //can be changed later
            if (phase.equals(PHASE.SETUP))
                server.gameEnded();
            else if(gameStarted)
                model.setConnected(nickname, false);
        }
        //accept the following messages only if they come from the ActivePlayer
        else if (nickname.equals(activePlayer)) {
            if (m instanceof MoveToIsland msg && phase.equals(PHASE.MOVE_STUDENTS))
                actionController.handleAction(msg);
            else if (m instanceof EntranceToHall msg && phase.equals(PHASE.MOVE_STUDENTS))
                actionController.handleAction(msg);
            else if (m instanceof MoveMN msg && phase.equals(PHASE.MOVE_MN))
                actionController.handleAction(msg);
            else if (isMessageCharacter(m))
                handleCharacter(m, nickname);
            else if (m instanceof ChooseCloud msg && phase.equals(PHASE.CHOOSE_CLOUD))
                actionController.handleAction(msg);
        } else {
            server.sendMessage(nickname, "Invalid message!");
        }
        //after a message has been received ask the turnController for the next phase
        nextPhase();
    }

    /**
     * Invoke the TurnController to ask for the next phase.
     */
    public void nextPhase(){
        PHASE oldPhase = phase;
        //the TurnController will evaluate all conditions and return the next phase
        phase = turnController.nextPhase(phase);
        //do the actions corresponding to the phase only if starting a new one or if Players still need to choose an Assistant
        if(!oldPhase.equals(phase) || phase.equals(PHASE.PLANNING))
            doPhase();
    }

    /**
     * Perform the actions corresponding to the current phase
     */
    public void doPhase(){
        //get information about Players and the ActivePlayer

        if(!phase.equals(PHASE.PLANNING) && !phase.equals(PHASE.SETUP) && !phase.equals(PHASE.MOVE_STUDENTS))
            activePlayer = model.getActivePlayer().getNickname();

        switch (phase) {
            case PLANNING:
                sortedPlayers = model.getSortedPlayers();
                //in this phase the next Player in order must choose his assistant
                //if he's connected send him a message
                if(sortedPlayers.get(haveChosenAssistant).isConnected())
                    server.sendMessage(sortedPlayers.get(haveChosenAssistant).getNickname(), "Choose your Assistant!");
                else
                    increaseHaveChosenAssistant();
                break;
            case MOVE_STUDENTS:
                //this phase is the first in a turn so the controller sets the next ActivePlayer
                //and asks him to move Students
                model.nextPlayer();
                //if he's connected send him a message
                if(model.getActivePlayer().isConnected()) {
                    activePlayer = model.getActivePlayer().getNickname();
                    server.sendMessage(activePlayer, "Select a Student and choose a destination three times!");
                }
                //if the Player isn't connected notify the TurnController and ask the next phase
                else {
                    turnController.setPlayerConnected(false);
                    nextPhase();
                }
                break;
            case MOVE_MN:
                //in this phase the Player must move MN
                server.sendMessage(activePlayer, "Choose where you want to move MN!");
                break;
            case CHOOSE_CLOUD:
                //in this phase the Player must choose a Cloud
                server.sendMessage(activePlayer, "Choose a Cloud!");
                break;
            case RESET_TURN:
                //if all Players have played their turn notify the TurnController
                if(havePlayed % numPlayers == 1)
                    turnController.setAllPlayedCheck(true);
                //if there's still Players that must do their turn
                else
                    havePlayed++;
                //the turn has ended so the next Player by default is regarded as connected
                turnController.setPlayerConnected(true);
                nextPhase();
                break;
            case RESET_ROUND:
                //in this phase the controller must check if the game must end and in that case look for a winner
                //otherwise it must prepare the Model for the next round
                if(model.getGameMustEnd()){
                    Optional<Player> winner = model.checkWin();
                    winner.ifPresentOrElse(w -> {server.sendMessage(w.getNickname(), "You won");
                                    server.sendMessageToOthers(w.getNickname(), "You Lose");},
                                () -> {server.sendMessageToAll("The game ends in a draw");}
                        );
                    //notify the TurnController
                    turnController.setGameEnded(true);
                }
                else {
                    model.resetRound();
                    havePlayed = 0;
                }
                nextPhase();
                break;
            case CHARACTER_ACTION:
                //in this phase the Player can't end his turn unless he performs a Character move
                server.sendMessage(activePlayer, "You need to perform a Character move!");
                break;
            case GAME_WON:
                //notify the server that the game has ended
                server.gameEnded();
                break;
        }
    }

    /**
     * Verify if a Request is relative to Characters
     * @param m the Request to analyze
     * @return true if the Request is a message relative to Characters
     */
    private boolean isMessageCharacter(Request m){
        return (m instanceof PlayCharacter || m instanceof ChooseIsland || m instanceof ChooseColor
                || m instanceof ChooseTwoColors || m instanceof SpecialMoveIsland);
    }

    /**
     * Increase the number of Players that have chosen their Assistant, if everyone has done so notify the TurnController
     */
    public void increaseHaveChosenAssistant(){
        if(haveChosenAssistant % numPlayers == 1) {
            turnController.setChooseAssistantsCheck(true);
            haveChosenAssistant = 0;
        }
        else
            haveChosenAssistant++;
    }

    /**
     * Handles all Requests regarding Characters
     * @param m Request message sent by a Client
     * @param nickname the nickname of the Player associated with the Client
     */
    private void handleCharacter(Request m, String nickname){
        server.sendMessage(nickname, "You're not playing in expert mode!");
    }

    /**
     * Gets the Model
     * @return the Game's Model
     */
    public Model getModel(){
        return this.model;
    }

    /**
     * Gets the Server
     * @return the Game's server
     */
    public Server getServer(){
        return this.server;
    }

    /**
     * Gets the current Phase
     * @return the current Phase
     */
    public PHASE getPhase(){
        return phase;
    }
    /**
     * Gets the TurnController
     * @return the TurnController
     */
    public TurnController getTurnController(){
        return this.turnController;
    }
}
