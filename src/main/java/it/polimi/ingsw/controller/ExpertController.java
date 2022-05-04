package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
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
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.virtualview.VirtualCharacter;
import it.polimi.ingsw.server.virtualview.VirtualCharacterWithNoEntry;
import it.polimi.ingsw.server.virtualview.VirtualCharacterWithStudents;

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
        super(lobby, m);
        createModel(m);
        numSwitchMoves = 0;
        numStudMoves = 0;
    }

    /**
     * Method createExpertModel receives in input the number of players participating in the game and creates a
     * new ExpertGameBoard instance.
     * @param m - the number of players and first player's info
     */
    public void createModel(GameParams m){
        setNumPlayers(m.getNumPlayers());
        setModel(new ExpertGameBoard(m.getNumPlayers()));
        getModel().addPlayer(m.getNickname(), m.getColorT(), m.getMage());
        getModel().newClouds();
    }

    /**
     * Method handleCharacter handles the choice of a Character card :
     * it triggers the correct method relying on the description of the received card.
     * If the command sent by the client is valid the model's state can be changed,
     * otherwise an error message is sent to the client identified by his nickname
     * @param m Request message sent by a Client
     * @param nickname the nickname of the Player associated with the Client
     */
    public void handleCharacter(Request m, String nickname){
        setMessageSender(nickname);
        activeCharacter = getModel().getActiveCharacter();
        m.accept(this);
    }

    public void visit(PlayCharacter msg){
        if(activeCharacter == null){
            try {
                Character c = getModel().getCharacters().stream().
                        filter(character -> msg.getC().getDesc().equals(character.getDescription())).findAny().orElse(null);
                if(c!= null) getModel().playCharacter(c);
                else getLobby().sendMessage(getMessageSender(), ERRORS.CHARACTER_NOT_AVAILABLE.toString());
            } catch (NotEnoughCoinsException e) {
                getLobby().sendMessage(getMessageSender(), ERRORS.NOT_ENOUGH_COINS.toString());
            }
        }
    }

    public void visit(SpecialMoveIsland m){
        if(filter() && activeCharacter.getDescription().equals(CharacterDescription.CHAR1.getDesc())) {
            if (m.getIslandIndex() < 0 || m.getIslandIndex() >= getModel().getSizeWorld()) {
                getLobby().sendMessage(getModel().getActivePlayer().getNickname(), ERRORS.INVALID_INDEX.toString());
            } else {
                try {
                    getModel().moveStudent(m.getStudent(), ((CharacterWithStudent) activeCharacter), getModel().getIslandByIndex(m.getIslandIndex()));
                } catch (NoSuchStudentException e) {
                    getLobby().sendMessage(getMessageSender(), "there is no " + m.getStudent().toString().toLowerCase() + " student on the card");
                }
                getModel().resetCharacterStudent();
            }
        }
    }

    public void visit(ChooseIsland m) {
        if (filter() && activeCharacter.getDescription().equals(CharacterDescription.CHAR3.getDesc())) {
            if (m.getIslandIndex() < 0 || m.getIslandIndex() >= getModel().getSizeWorld()) {
                getModel().checkIsland(getModel().getIslandByIndex(m.getIslandIndex()));
                Optional<Player> winner = getModel().checkWin();
                winner.ifPresentOrElse(w -> {
                            getLobby().sendMessage(w.getNickname(), "You won");
                            getLobby().sendMessageToOthers(w.getNickname(), "You Lose");
                            getTurnController().setGameEnded(true);
                        },
                        () -> {
                            if (getModel().getSizeWorld() == 3) {
                                getLobby().sendMessageToAll("The game ends in a draw");
                                getTurnController().setGameEnded(true);
                            }
                        }
                );
                int noEntry = ((CharacterWithNoEntry) activeCharacter).getNumNoEntry();
                if (noEntry != 0) {
                    getModel().getIslandByIndex(m.getIslandIndex()).setNumNoEntry(1);
                    getModel().removeNoEntry();
                }
            }
        }
        else if (filter() && activeCharacter.getDescription().equals(CharacterDescription.CHAR5.getDesc())) {
            int noEntry = ((CharacterWithNoEntry) activeCharacter).getNumNoEntry();
            if (noEntry != 0) {
                getModel().getIslandByIndex(m.getIslandIndex()).setNumNoEntry(1);
                getModel().removeNoEntry();
            }
        }
    }

    public void visit(ChooseColor m) {
        if(filter()) {
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR9.getDesc()))
                getModel().setBannedColor(m.getColor());
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR12.getDesc())) {
                getModel().removeHall(m.getColor());
            }
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR11.getDesc())) {
                try {
                    ((CharacterWithStudent) activeCharacter).remove(m.getColor());
                } catch (NoSuchStudentException e) {
                    getLobby().sendMessage(getMessageSender(), "there is no " + m.getColor().toString().toLowerCase() + " students on the card");
                }
                try {
                    getModel().addToHall(m.getColor());
                } catch (PlaceFullException e) {
                    getLobby().sendMessage(getMessageSender(), "There is no space for another " + m.getColor().toString().toLowerCase() + " student in the hall");
                }
                getModel().resetCharacterStudent();
            }
        }
    }

    public void visit(ChooseTwoColors m) {
        if(filter()) {
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR10.getDesc())) {
                if (numSwitchMoves < 2) {
                    // first color hall, second color entrance
                    try {
                        getModel().switchStudents(m.getFirstColor(), m.getSecondColor());
                    } catch (NoSuchStudentException | PlaceFullException e) {
                        getLobby().sendMessage(getMessageSender(), ERRORS.NO_SUCH_STUDENT.toString());
                    }
                    numSwitchMoves++;
                } else {
                    getLobby().sendMessage(getMessageSender(), ERRORS.NO_MOVES_REMAINING.toString());
                }
            }
            if (activeCharacter.getDescription().equals(CharacterDescription.CHAR7.getDesc())) {
                if (numStudMoves < 3) {
                    //first color entrance, second color card
                    try {
                        getModel().moveStudent(m.getFirstColor(), getModel().getActivePlayer().getMyBoard(), ((CharacterWithStudent) activeCharacter));
                    } catch (NoSuchStudentException e) {
                        getLobby().sendMessage(getMessageSender(), "There is no " + m.getFirstColor().toString().toLowerCase() + " students in the entrance");
                    }
                    try {
                        getModel().moveStudent(m.getSecondColor(), ((CharacterWithStudent) activeCharacter), getModel().getActivePlayer().getMyBoard());
                    } catch (NoSuchStudentException e) {
                        getLobby().sendMessage(getMessageSender(), "There is no " + m.getSecondColor().toString().toLowerCase() + " students on the card");
                    }
                    numStudMoves++;
                } else {
                    getLobby().sendMessage(getMessageSender(), ERRORS.NO_MOVES_REMAINING.toString());
                }
            }
        }
    }

    /**
     * Checks if a Character is active
     * @return true if there's an Active Character
     */
    private boolean filter(){
        if(activeCharacter==null){
            getLobby().sendMessage(getMessageSender(), ERRORS.NO_ACTIVE_CHARACTER.toString());
            return false;
        }
        return true;
    }

    public ExpertModel getModel() {
        return ((ExpertModel) super.getModel());
    }

    /**
     * Method propertyChange updates the virtual view according to the events received by the model
     * @param evt - received event
     */
    public void propertyChange(PropertyChangeEvent evt){
        super.propertyChange(evt);
        EVENT event = EVENT.valueOf(evt.getPropertyName());
        switch (event){
            case REPLACE_CHARACTER:
                int indexChar = (int) evt.getOldValue();
                Character modelChar = (Character) evt.getNewValue();
                VirtualCharacter virtualChar = new VirtualCharacter(modelChar);
                getVirtualView().setVirtualCharacters(indexChar, virtualChar);
                break;
            case REPLACE_CHARACTER_S:
                int indexCharacter = (int) evt.getNewValue();
                VirtualCharacterWithStudents character = (VirtualCharacterWithStudents) evt.getNewValue();
                getVirtualView().setVirtualCharacters(indexCharacter, character);
                break;
            case REPLACE_CHARACTER_NE:
                int indexC = (int) evt.getOldValue();
                VirtualCharacterWithNoEntry VirtualC = (VirtualCharacterWithNoEntry) evt.getNewValue();
                getVirtualView().setVirtualCharacters(indexC, VirtualC);
                break;
            case CREATE_CHARACTERS:
                ArrayList<VirtualCharacter> virtualCharacters = (ArrayList<VirtualCharacter>) evt.getNewValue();
                getVirtualView().setVirtualCharacters(virtualCharacters);
                break;
            case BOARD_COINS:
                int coins = (int) evt.getNewValue();
                getVirtualView().setVirtualCoins(coins);
                break;
        }
    }
}

