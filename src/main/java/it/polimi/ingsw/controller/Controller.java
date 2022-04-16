package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Server;

import java.util.ArrayList;

/**
 * The Controller will handle messages from all clients by checking their contents and modifying the model's state
 * according to the commands received
 */
public class Controller {

    private Model model;
    private ActionController actionController;
    private TurnController turnController;
    private Server server;
    private ArrayList<String> players;
    private PHASE phase;
    private int numPlayers;

    public Controller(Server server){
        this.server = server;
        phase=PHASE.SETUP;
        turnController = new TurnController();
        actionController= new ActionController(model);
    }

    public void createModel(GameParams m){
        numPlayers=m.getNumPlayers();
        model = new GameBoard(m.getNumPlayers());
        model.addPlayer(m.getNickname(), m.getColorT(), m.getMage());
        model.newClouds();
    }

    /**
     * Handles the message received by performing the correct action based on the type of Request
     * @param m Request message sent by a Client
     * @param nickname the nickname of the Player associated with the Client
     *                 NOT FINAL
     */
    public void handleMessage(Request m, String nickname){
        String currPlayer = model.getActivePlayer().getNickname();
        if(m instanceof GameParams msg && phase == PHASE.SETUP){
            if(msg.getNumPlayers()>1 && msg.getNumPlayers() <= 4) {
                createModel(msg);
            }
        }
        else if(m instanceof Join msg && phase == PHASE.SETUP){
            //check if another player can join and his nickname is available
            if(model.getPlayerByNickname(msg.getNickname())==null && players.size()<numPlayers) {
                model.addPlayer(msg.getNickname(), msg.getColorT(), msg.getMage());
                players.add(msg.getNickname());
            }
            if(players.size() == numPlayers)
                //all players have connected
                turnController.setGameStarted(true);
        }
        else if(m instanceof ChooseAssistant  msg && phase.equals(PHASE.PLANNING))
            try {
                model.chooseAssistants(model.getPlayerByNickname(nickname), msg.getIndex());
            }catch (InvalidIndexException e){
               server.sendMessage(nickname, "Invalid index!");
            }
        else if(m instanceof Disconnect);
            //model.setDisconnected(nickname)
        else if(m instanceof MoveToIsland msg && nickname.equals(currPlayer) && phase.equals(PHASE.MOVE_STUDENTS))
            actionController.handleAction(msg);
        else if(m instanceof EntranceToHall msg && nickname.equals(currPlayer) && phase.equals(PHASE.MOVE_STUDENTS))
            actionController.handleAction(msg);
        else if(m instanceof MoveMN msg && nickname.equals(currPlayer) && phase.equals(PHASE.MOVE_MN))
            actionController.handleAction(msg);
        else if(m instanceof PlayCharacter && nickname.equals(currPlayer))
            handleCharacter(m, nickname);



        else {
            server.sendMessage(nickname, "Invalid message!");
        }
        //after a message has been received ask the turnController for the next phase
        phase=turnController.nextPhase(phase);
    }

    public Model getModel(){
        return this.model;
    }

    public void setModel(Model model){
        this.model = model;
    }

    public void doPhase(){

    }

    /**
     * Handles all Requests regarding Characters
     * @param m Request message sent by a Client
     * @param nickname the nickname of the Player associated with the Client
     */
    public void handleCharacter(Request m, String nickname){
        server.sendMessage(nickname, "You're not playing in expert mode!");
    }

    public Server getServer(){
        return this.server;
    }

    public TurnController getTurnController(){
        return this.turnController;
    }

    public PHASE getPhase(){
        return this.phase;
    }

    public ActionController getActionController() {
        return actionController;
    }

    public void setActionController(ActionController actionController) {
        this.actionController = actionController;
    }
}
