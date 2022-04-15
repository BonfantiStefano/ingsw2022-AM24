package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Server;

import java.util.ArrayList;

public class Controller {

    private Model model;
    private ActionController actionController;
    private TurnController turnController;
    private Server server;
    private ArrayList<Player> players;
    private PHASE phase;

    public Controller(Server server){
        this.server = server;
        phase=PHASE.SETUP;
        turnController = new TurnController();
    }

    public void handleMessage(Request m, String nickname){
        if(m instanceof GameParams msg && phase == PHASE.SETUP){
            if(msg.getNumPlayers()<2 || msg.getNumPlayers()>3) {
                turnController.setGameStarted(true);
                model = new GameBoard(msg.getNumPlayers());
                model.addPlayer(msg.getNickname(), msg.getColorT(), msg.getMage());
                actionController=new ActionController(model);
            }
        }
        if(m instanceof ChooseAssistant  msg && phase == PHASE.PLANNING)
            try {
                model.chooseAssistants(model.getPlayerByNickname(nickname), msg.getIndex());
            }catch (InvalidIndexException e){
               server.sendMessage(nickname, "Invalid index!");
            }
        if(m instanceof Disconnect);
            //model.setDisconnected(nickname)
        if(m instanceof PlayCharacter)
            handleCharacter(m, nickname);

    }

    public void handleCharacter(Request m, String nickname){
        server.sendMessage(nickname, "You're not playing in expert mode!");
    }

}
