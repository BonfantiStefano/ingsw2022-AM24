package it.polimi.ingsw.controller.controllers;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.controller.ERRORS;
import it.polimi.ingsw.controller.PHASE;
import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.EVENT;
import it.polimi.ingsw.model.ExpertModel;
import it.polimi.ingsw.model.character.*;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Information;
import it.polimi.ingsw.server.answer.Update.*;
import it.polimi.ingsw.server.virtualview.*;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The ExpertController handles messages which represent commands received from all the clients and changes
 * the model's state when the game is played with expert rules
 */
public class ExpertController extends Controller {

    private int numSwitchMoves;
    private int numStudMoves;
    private Character activeCharacter;

    /**
     * Constructor ExpertController creates a new empty ExpertController instance.
     * @param lobby - lobby
     * @param m - number of the players
     */
    public ExpertController(Lobby lobby, GameParams m) {
        super(lobby,m);
        createModel(m);
        actionController = new ActionController(model, lobby, turnController);
        numSwitchMoves = 0;
        numStudMoves = 0;
    }

    /**
     * Method createExpertModel receives in input the number of players participating in the game and creates a
     * new ExpertGameBoard instance.
     * @param m - the number of players and first player's info
     */
    @Override
    public void createModel(GameParams m){
        numPlayers = m.getNumPlayers();
        model = new ExpertGameBoard(m.getNumPlayers());
        model.addPlayer(m.getNickname(), m.getColorT(), m.getMage());
        model.newClouds();
        model.addListener(this);
    }

    /**
     * Method handleCharacter handles the choice of a Character card :
     * it triggers the correct method relying on the description of the received card.
     * If the command sent by the client is valid the model's state can be changed,
     * otherwise an error message is sent to the client identified by his nickname
     * @param m Request message sent by a Client
     * @param nickname the nickname of the Player associated with the Client
     */
    @Override
    public void handleCharacter(Request m, String nickname){
        setMessageSender(nickname);
        activeCharacter = getModel().getActiveCharacter();
        m.accept(this);
    }

    /* Method visit activates a Character card's ability. The Character can't be played at the beginning of
    the player's turn before the Planning phase has been completed. it also can't be played if the player doesn't
    have enough coins to activate the chosen card.
    @param msg - Character which ability will be activated
     */
    @Override
    public void visit(PlayCharacter msg){
        if(activeCharacter == null && !phase.equals(PHASE.PLANNING)){
            try {
                Character c = getModel().getCharacters().stream().
                        filter(character -> msg.getC().getDesc().equals(character.getDescription())).findAny().orElse(null);
                if(c!= null) getModel().playCharacter(c);
                else lobby.sendMessage(getMessageSender(), new Error(ERRORS.CHARACTER_NOT_AVAILABLE.toString()));
            } catch (NotEnoughCoinsException e) {
                lobby.sendMessage(getMessageSender(), new Error(ERRORS.NOT_ENOUGH_COINS.toString()));
            }
        }
        else if(phase.equals(PHASE.PLANNING)){
            lobby.sendMessage(messageSender, new Error("You can't play a Character right now"));
        }
    }

    /* Method visit is used to apply the Character effect that allows to move a student from the card to an island
     @param m - request which contains the color of the student and the index of the island where he is moved
     */
    @Override
    public void visit(SpecialMoveIsland m){
        activeCharacter = getModel().getActiveCharacter();
        if(filter() && activeCharacter.getDescription().equals(CharacterDescription.CHAR1.getDesc())) {
            if (m.getIslandIndex() < 0 || m.getIslandIndex() >= getModel().getSizeWorld()) {
                lobby.sendMessage(getModel().getActivePlayer().getNickname(), new Error(ERRORS.INVALID_INDEX.toString()));
            } else {
                try {
                    getModel().moveStudent(m.getStudent(), ((CharacterWithStudent) activeCharacter), getModel().getIslandByIndex(m.getIslandIndex()));
                } catch (NoSuchStudentException e) {
                    lobby.sendMessage(getMessageSender(), new Error("there is no " + m.getStudent().toString().toLowerCase() + " student on the card"));
                }
                getModel().resetCharacterStudent();
                turnController.setCharacterActionCheck(true);
            }
        }
    }

    /* Method visit is used to apply the Character effect that allows to calculate the influence on the chosen
       island (in case the activate Character is the third one) or to place a No Entry tile on the chosen island
       (in case the activate Character is the 5th one)
       @param m - request which contains the index of the island
     */
    @Override
    public void visit(ChooseIsland m) {
        activeCharacter = getModel().getActiveCharacter();
        if (filter() && activeCharacter.getDescription().equals(CharacterDescription.CHAR3.getDesc())) {
            if (m.getIslandIndex() >= 0 && m.getIslandIndex() < getModel().getSizeWorld()) {
                getModel().checkIsland(getModel().getIslandByIndex(m.getIslandIndex()));
                turnController.setCharacterActionCheck(true);
                Optional<Player> winner = getModel().checkWin();
                winner.ifPresentOrElse(w -> {
                            lobby.sendMessage(w.getNickname(), new Information("You won"));
                            lobby.sendMessageToOthers(w.getNickname(), new Information("You Lose"));
                            getTurnController().setGameEnded(true);
                        },
                        () -> {
                            if (getModel().getSizeWorld() == 3) {
                                lobby.sendMessageToAll(new Information("The game ends in a draw"));
                                getTurnController().setGameEnded(true);
                            }
                        }
                );
            }
        }
        else if (filter() && activeCharacter.getDescription().equals(CharacterDescription.CHAR5.getDesc())) {
            int noEntry = ((CharacterWithNoEntry) activeCharacter).getNumNoEntry();
            if (noEntry != 0) {
                getModel().getIslandByIndex(m.getIslandIndex()).setNumNoEntry(1);
                ((CharacterWithNoEntry) getModel().getActiveCharacter()).removeNoEntry();
                turnController.setCharacterActionCheck(true);
            }
        }
    }

    /* Method visit is used to apply the Character effect that allows to choose the color that adds no influence
    during the influence calculation (in case the active Character is the ninth one) or the color of three students that
    the must be removed from the player's hall (in case the active character is 12th one) or the color of the
    student that must be moved from the card to the player's hall (in case the active Character is the 11th one)
    @param m - request which contains the chosen color
     */
    @Override
    public void visit(ChooseColor m) {
        activeCharacter = getModel().getActiveCharacter();
        if(filter()) {
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR9.getDesc())){
                getModel().setBannedColor(m.getColor());
                turnController.setCharacterActionCheck(true);
            }
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR12.getDesc())) {
                getModel().removeHall(m.getColor());
                turnController.setCharacterActionCheck(true);
            }
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR11.getDesc())) {
                try {
                    ((CharacterWithStudent) activeCharacter).remove(m.getColor());
                } catch (NoSuchStudentException e) {
                    lobby.sendMessage(getMessageSender(), new Error("there is no " + m.getColor().toString().toLowerCase() + " students on the card"));
                }
                try {
                    getModel().addToHall(m.getColor());
                } catch (PlaceFullException e) {
                    lobby.sendMessage(getMessageSender(), new Error("There is no space for another " + m.getColor().toString().toLowerCase() + " student in the hall"));
                }
                getModel().resetCharacterStudent();
                turnController.setCharacterActionCheck(true);
            }
        }
    }

    /*  Method visit is used to apply the Character effect that allows to switch up to 2 students between player's
    hall and entrance (in case the active Character is the 10th one) or to switch up to 3 students between
    player's entrance and the card (in case the active Character is the 7th one)
    @param m - request which contains the two chosen colors
     */
    @Override
    public void visit(ChooseTwoColors m) {
        activeCharacter = getModel().getActiveCharacter();
        if(filter()) {
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR10.getDesc())) {
                if (numSwitchMoves < 2) {
                    // first color hall, second color entrance
                    try {
                        getModel().switchStudents(m.getFirstColor(), m.getSecondColor());
                    } catch (NoSuchStudentException | PlaceFullException e) {
                        lobby.sendMessage(getMessageSender(), new Error(ERRORS.NO_SUCH_STUDENT.toString()));
                    }
                    turnController.setCharacterActionCheck(true);
                    numSwitchMoves++;
                } else {
                    lobby.sendMessage(getMessageSender(), new Error(ERRORS.NO_MOVES_REMAINING.toString()));
                }
            }
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR7.getDesc())) {
                if (numStudMoves < 3) {
                    //first color entrance, second color card
                    try {
                        getModel().moveStudent(m.getFirstColor(), getModel().getActivePlayer().getMyBoard(), ((CharacterWithStudent) activeCharacter));
                    } catch (NoSuchStudentException e) {
                        lobby.sendMessage(getMessageSender(), new Error("There is no " + m.getFirstColor().toString().toLowerCase() + " students in the entrance"));
                    }
                    try {
                        getModel().moveStudent(m.getSecondColor(), ((CharacterWithStudent) activeCharacter), getModel().getActivePlayer().getMyBoard());
                    } catch (NoSuchStudentException e) {
                        lobby.sendMessage(getMessageSender(), new Error("There is no " + m.getSecondColor().toString().toLowerCase() + " students on the card"));
                    }
                    turnController.setCharacterActionCheck(true);
                    numStudMoves++;
                } else {
                    lobby.sendMessage(getMessageSender(), new Error(ERRORS.NO_MOVES_REMAINING.toString()));
                }
            }
        }
    }

    /**
     * Checks if any Character is active
     * @return true if there's an Active Character
     */
    private boolean filter(){
        if(activeCharacter==null){
            lobby.sendMessage(getMessageSender(), new Error(ERRORS.NO_ACTIVE_CHARACTER.toString()));
            return false;
        }
        return true;
    }

    /**
     * Method getModel returns the expert gameBoard
     * @return expert gameBoard
     */
    @Override
    public ExpertModel getModel() {
        return ((ExpertModel) super.getModel());
    }

    /**
     * Method propertyChange updates the virtual view according to the events received by the model
     * @param evt - received event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt){
        super.propertyChange(evt);
        EVENT event = EVENT.valueOf(evt.getPropertyName());
        switch (event) {
            case REPLACE_CHARACTER -> {
                int indexChar = (int) evt.getOldValue();
                Character modelChar = (Character) evt.getNewValue();
                VirtualCharacter vc;
                if(modelChar instanceof CharacterWithNoEntry cha){
                    vc = new VirtualCharacterWithNoEntry(cha);
                }
                else if(modelChar instanceof CharacterWithStudent cha)
                    vc = new VirtualCharacterWithStudents(cha);
                else
                    vc = new VirtualCharacter(modelChar);
                virtualView.setVirtualCharacters(indexChar, vc);
                sendChar(indexChar);
            }
            case REPLACE_CHARACTER_S -> {
                int indexCharacter = (int) evt.getOldValue();
                VirtualCharacterWithStudents character = (VirtualCharacterWithStudents) evt.getNewValue();
                virtualView.setVirtualCharacters(indexCharacter, character);
                lobby.sendMessageToAll(new ReplaceCharacterStudents(character, indexCharacter));
            }
            case REPLACE_CHARACTER_NE -> {
                int indexC = (int) evt.getOldValue();
                VirtualCharacterWithNoEntry VirtualC = (VirtualCharacterWithNoEntry) evt.getNewValue();
                virtualView.setVirtualCharacters(indexC, VirtualC);
                lobby.sendMessageToAll(new ReplaceCharacterWithNoEntry(VirtualC, indexC));
            }
            case CREATE_CHARACTERS -> {
                ArrayList<VirtualCharacter> virtualCharacters = (ArrayList<VirtualCharacter>) evt.getNewValue();
                virtualView.setVirtualCharacters(virtualCharacters);
                sendFullView();
            }
            case BOARD_COINS -> {
                int coins = (int) evt.getNewValue();
                virtualView.setVirtualCoins(coins);
                lobby.sendMessageToAll(new UpdateCoins(coins));
            }
            case MN_POS -> {
                int pos = (int) evt.getNewValue();
                virtualView.setMnPos(pos);
                lobby.sendMessageToAll(new UpdateMN(pos));
            }
            case CREATE_WORLD -> {
                ArrayList<VirtualIsland> virtualWorld = (ArrayList<VirtualIsland>) evt.getNewValue();
                virtualView.setVirtualWorld(virtualWorld);
                lobby.sendMessageToAll(new UpdateWorld(virtualWorld));
            }
            case ACTIVE_CHARACTER -> {
                int activeVirtualCharacter = (int) evt.getNewValue();
                virtualView.getVirtualCharacters().get(activeVirtualCharacter).setActive(true);
                lobby.sendMessageToAll(new UpdateActiveCharacter(activeVirtualCharacter, true));
            }
            case NO_ACTIVE_CHARACTER -> {
                int activeVirtualChar = (int) evt.getNewValue();
                virtualView.getVirtualCharacters().get(activeVirtualChar).setActive(false);
                lobby.sendMessageToAll(new UpdateActiveCharacter(activeVirtualChar, false));
            }
        }
    }

    /* Method sendFullView is used to send to all the active clients the three Character cards
       that can be played during the game.
     */
    @Override
    public void sendFullView(){
        super.sendFullView();
        ArrayList<VirtualCharacter> virtualCharacters = virtualView.getVirtualCharacters();
        virtualCharacters.forEach(c-> sendChar(virtualCharacters.indexOf(c)));
    }

    private void sendChar(int index){
        ArrayList<VirtualCharacter> virtualCharacters = virtualView.getVirtualCharacters();
        VirtualCharacter c = virtualCharacters.get(index);
        if(c instanceof VirtualCharacterWithStudents cha)
            lobby.sendMessageToAll(new ReplaceCharacterStudents(cha, virtualCharacters.indexOf(cha)));
        else if(c instanceof VirtualCharacterWithNoEntry cha)
            lobby.sendMessageToAll(new ReplaceCharacterWithNoEntry(cha, virtualCharacters.indexOf(cha)));
        else
            lobby.sendMessageToAll(new ReplaceCharacter(c, virtualCharacters.indexOf(c)));
    }
}

