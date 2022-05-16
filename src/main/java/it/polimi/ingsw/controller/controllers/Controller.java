package it.polimi.ingsw.controller.controllers;


import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.controller.ERRORS;
import it.polimi.ingsw.controller.PHASE;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.EVENT;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Information;
import it.polimi.ingsw.server.answer.Update.*;
import it.polimi.ingsw.server.virtualview.VirtualCloud;
import it.polimi.ingsw.server.virtualview.VirtualIsland;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;
import it.polimi.ingsw.server.virtualview.VirtualView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * The Controller will handle messages from all clients by checking their contents and modifying the model's state
 * according to the commands received
 */
public class Controller implements PropertyChangeListener {

    protected Model model;
    protected ActionController actionController;
    protected TurnController turnController;
    protected Lobby lobby;
    protected PHASE phase;
    protected int numPlayers;
    protected int havePlayed;
    protected int haveChosenAssistant;
    protected String activePlayer;
    protected String messageSender;
    protected boolean gameStarted;
    protected VirtualView virtualView;

    public Controller(Lobby lobby, GameParams m){
        virtualView = new VirtualView();
        this.lobby = lobby;
        createModel(m);
        phase=PHASE.SETUP;
        turnController = new TurnController();
        actionController= new ActionController(model, lobby, turnController);
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
        model.addListener(this);
        numPlayers = m.getNumPlayers();
        haveChosenAssistant = 0;
        havePlayed = 0;
    }

    /**
     * Receives a Request and visits the message
     * @param r the Request submitted
     * @param nickname nickname belonging to the Player submitting the Request
     */
    public void handleMessage(Request r, String nickname){
        messageSender = nickname;
        r.accept(this);
        messageSender = null;
    }

    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param msg a Client's Request
     */
    public void visit(ChooseAssistant msg){
        if(phase.equals(PHASE.PLANNING) && model.getSortedPlayers().get(haveChosenAssistant).getNickname().equals(messageSender))
            try {
                model.chooseAssistants(model.getPlayerByNickname(messageSender), msg.getIndex());
                increaseHaveChosenAssistant();
            } catch (InvalidIndexException e) {
                lobby.sendMessage(messageSender, new Error(ERRORS.INVALID_INDEX.toString()));
            }
        nextPhase();
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param msg a Client's Request
     */
    public void visit(Disconnect msg){
        //if a Player disconnects during the setup phase the game is canceled
        //can be changed later
        //Stefano: Ho commentato questo metodo perchè in teoria non ci serve

        model.setConnected(messageSender, false);
        nextPhase();
    }
    /**
     * Handles all Join messages received by checking if all conditions to join the Game are met
     * @param msg the Join message
     */
    public void visit(Join msg){
        String message = "";
        boolean availableNickname = model.getPlayerByNickname(msg.getNickname())==null;
        boolean availableMage = model.getPlayers().stream().noneMatch(p->p.getMage().equals(msg.getMage()));

        //check if another player can join and his nickname is available
        if(availableNickname && availableMage && model.getPlayers().size()<numPlayers && phase.equals(PHASE.SETUP)) {
            model.addPlayer(msg.getNickname(), msg.getColorT(), msg.getMage());
            if(model.getPlayers().size() == numPlayers) {
                //all players have connected
                turnController.setGameStarted(true);
                gameStarted = true;
                lobby.sendMessageToAll(new Information("Game Started!"));
            }
            lobby.sendMessageToAll(new FullView(virtualView));
        }
        //if the Player had disconnected update his status as connected
        else if(!availableNickname && !model.getPlayerByNickname(messageSender).isConnected()) {
            model.setConnected(messageSender, true);
            lobby.sendMessageToAll(new FullView(virtualView));
        }
        else if(!availableNickname)
            message+=ERRORS.NICKNAME_TAKEN;
        else if(!availableMage)
            message+=ERRORS.MAGE_TAKEN;


        if(!message.isEmpty())
            lobby.sendMessage(messageSender, new Error(message));
        nextPhase();
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param msg a Client's Request
     */
    public void visit(MoveToIsland msg){
        if(phase.equals(PHASE.MOVE_STUDENTS)&&verifyActive(messageSender))
            actionController.handleAction(msg);
        nextPhase();
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param msg a Client's Request
     */
    public void visit(MoveMN msg){
        if(phase.equals(PHASE.MOVE_MN)&&verifyActive(messageSender))
            actionController.handleAction(msg);
        nextPhase();
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param msg a Client's Request
     */
    public void visit(ChooseCloud msg){
        if(phase.equals(PHASE.CHOOSE_CLOUD)&&verifyActive(messageSender))
            actionController.handleAction(msg);
        nextPhase();
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param msg a Client's Request
     */
    public void visit(EntranceToHall msg){
        if(phase.equals(PHASE.MOVE_STUDENTS)&&verifyActive(messageSender))
            actionController.handleAction(msg);
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
            case PLANNING -> {
                ArrayList<Player> sortedPlayers = model.getSortedPlayers();
                //in this phase the next Player in order must choose his assistant
                //if he's connected send him a message
                if (sortedPlayers.get(haveChosenAssistant).isConnected())
                    lobby.sendMessage(sortedPlayers.get(haveChosenAssistant).getNickname(), new Information("Choose your Assistant!"));
                else
                    increaseHaveChosenAssistant();
            }
            case MOVE_STUDENTS -> {
                //this phase is the first in a turn so the controller sets the next ActivePlayer
                //and asks him to move Students
                model.nextPlayer();
                //if he's connected send him a message
                if (model.getActivePlayer().isConnected()) {
                    activePlayer = model.getActivePlayer().getNickname();
                    lobby.sendMessage(activePlayer, new Information("Select a Student and choose a destination three times!"));
                }
                //if the Player isn't connected notify the TurnController and ask the next phase
                else {
                    turnController.setPlayerConnected(false);
                    nextPhase();
                }
            }
            case MOVE_MN ->
                    //in this phase the Player must move MN
                    lobby.sendMessage(activePlayer, new Information("Choose where you want to move MN!"));
            case CHOOSE_CLOUD ->
                    //in this phase the Player must choose a Cloud
                    lobby.sendMessage(activePlayer, new Information("Choose a Cloud!"));
            case RESET_TURN -> {
                //if all Players have played their turn notify the TurnController
                havePlayed++;
                if(havePlayed==numPlayers)
                    turnController.setAllPlayedCheck(true);
                //the turn has ended so the next Player by default is regarded as connected
                turnController.setPlayerConnected(true);
                nextPhase();
            }
            case RESET_ROUND -> {
                //in this phase the controller must check if the game must end and in that case look for a winner
                //otherwise it must prepare the Model for the next round
                if (model.getGameMustEnd()) {
                    Optional<Player> winner = model.checkWin();
                    winner.ifPresentOrElse(w -> {
                                lobby.sendMessage(w.getNickname(), new Information("You won"));
                                lobby.sendMessageToOthers(w.getNickname(), new Information("You Lose"));
                            },
                            () -> lobby.sendMessageToAll(new Information("The game ends in a draw"))
                    );
                    //notify the TurnController
                    turnController.setGameEnded(true);
                } else {
                    model.resetRound();
                    havePlayed = 0;
                    haveChosenAssistant = 0;
                }
                nextPhase();
            }
            case CHARACTER_ACTION ->
                    //in this phase the Player can't end his turn unless he performs a Character move
                    lobby.sendMessage(activePlayer, new Information("You need to perform a Character move!"));
            case GAME_WON ->
                    //notify the lobby that the game has ended
                    lobby.gameEnded();
        }
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param m a Client's Request regarding a Character
     */
    public void visit(PlayCharacter m){
        handleCharacter(m, messageSender);
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param m a Client's Request regarding a Character
     */
    public void visit(ChooseIsland m){
        handleCharacter(m, messageSender);
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param m a Client's Request regarding a Character
     */
    public void visit(ChooseColor m){
        handleCharacter(m, messageSender);
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param m a Client's Request regarding a Character
     */
    public void visit(ChooseTwoColors m){
        handleCharacter(m, messageSender);
    }
    /**
     * If the game Phase is right perform the correct actions for this kind of Message
     * @param m a Client's Request regarding a Character
     */
    public void visit(SpecialMoveIsland m){
        handleCharacter(m, messageSender);
    }


    /**
     * Increase the number of Players that have chosen their Assistant, if everyone has done so notifies the TurnController
     * and resets the counter
     */
    public void increaseHaveChosenAssistant() {
        haveChosenAssistant++;
        if (haveChosenAssistant == numPlayers)
            turnController.setChooseAssistantsCheck(true);
    }
    /**
     * Handles all Requests regarding Characters
     * @param m Request message sent by a Client
     * @param nickname the nickname of the Player associated with the Client
     */
    public void handleCharacter(Request m, String nickname){
        lobby.sendMessage(nickname, new Error("You're not playing in expert mode!"));
    }

    private boolean verifyActive(String nickname){
        if(model!=null&&model.getActivePlayer()!=null) {
            activePlayer=model.getActivePlayer().getNickname();
            return activePlayer.equals(nickname);
        }
        return false;
    }

    /**
     * Gets the Model
     * @return the Game's Model
     */
    public Model getModel(){
        return this.model;
    }

    /**
     * Gets the lobby
     * @return the Game's lobby
     */
    public Lobby getLobby(){
        return this.lobby;
    }


    /**
     * Sets the Model
     * @param model the other model
     */
    public void setModel(Model model) {
        this.model = model;
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

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    /**
     * Method propertyChange updates the virtual view according to the events received by the model
     * @param evt - received event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        EVENT event = EVENT.valueOf(evt.getPropertyName());
        switch(event){
            case ADD_PLAYER:
                Player modelPlayer = (Player) evt.getNewValue();
                VirtualPlayer virtualPlayer = new VirtualPlayer(modelPlayer);
                virtualView.addVirtualPlayer(virtualPlayer);
                lobby.sendMessageToAll(new AddPlayer(virtualPlayer));
                break;
            case REPLACE_ISLAND:
                int indexIsland = (int) evt.getOldValue();
                VirtualIsland island = (VirtualIsland) evt.getNewValue();
                virtualView.setVirtualWorld(indexIsland, island);
                lobby.sendMessageToAll(new UpdateIsland(island, indexIsland));
                break;
            case CREATE_WORLD:
                ArrayList<VirtualIsland> virtualWorld = (ArrayList<VirtualIsland>) evt.getNewValue();
                virtualView.setVirtualWorld(virtualWorld);
                lobby.sendMessageToAll(new UpdateWorld(virtualWorld));
                break;
            case REPLACE_PLAYER:
                int indexPlayer = (int) evt.getOldValue();
                VirtualPlayer player = (VirtualPlayer) evt.getNewValue();
                virtualView.setVirtualPlayers(indexPlayer, player);
                lobby.sendMessageToAll(new UpdatePlayer(player, indexPlayer));
                break;
            case CREATE_CLOUDS:
                ArrayList<VirtualCloud> virtualClouds = (ArrayList<VirtualCloud>) evt.getNewValue();
                virtualView.setVirtualClouds(virtualClouds);
                lobby.sendMessageToAll(new CreateClouds(virtualClouds));
                break;
            case CREATE_PLAYERS:
                ArrayList<VirtualPlayer> virtualPlayers = (ArrayList<VirtualPlayer>) evt.getNewValue();
                virtualView.setVirtualPlayers(virtualPlayers);
                break;
            case REPLACE_CLOUD:
                int indexCloud = (int) evt.getOldValue();
                VirtualCloud cloud = (VirtualCloud) evt.getNewValue();
                virtualView.setVirtualClouds(indexCloud, cloud);
                lobby.sendMessageToAll(new ReplaceCloud(cloud, indexCloud));
                break;
            case REPLACE_PROFS:
                HashMap<ColorS, Player> modelProfs = (HashMap<ColorS, Player>) evt.getNewValue();
                HashMap<ColorS, VirtualPlayer> virtualProfs = new HashMap<>();
                for(ColorS c : ColorS.values()){
                    if(modelProfs.get(c) != null)
                        virtualProfs.put(c, new VirtualPlayer(modelProfs.get(c)));
                    else
                        virtualProfs.put(c,null);
                }
                virtualView.setVirtualProfs(virtualProfs);
                lobby.sendMessageToAll(new UpdateProfs(virtualProfs));
                break;
            case MN_POS:
                int mnPos = (int) evt.getNewValue();
                virtualView.setMnPos(mnPos);
                lobby.sendMessageToAll(new UpdateMN(mnPos));
                break;
            default:
                //in case an undefined event is thrown the whole view will be sent
                lobby.sendMessageToAll(new FullView(virtualView));
        }
    }


    public VirtualView getVirtualView() {
        return virtualView;
    }
}