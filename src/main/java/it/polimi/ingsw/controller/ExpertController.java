package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.ExpertModel;
import it.polimi.ingsw.model.character.*;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.server.Server;

/**
 * The ExpertController handles messages which represent commands received from all the clients and changes
 * the model's state when the game is played with expert rules
 */
public class ExpertController extends Controller {

    private ExpertModel expertModel;
    private int numPlayers;
    private int numSwitchMoves;
    private int numStudMoves;


    /**
     * Constructor ExpertController creates a new empty ExpertController instance.
     * @param server - server
     */
    public ExpertController(Server server) {
        super(server);
        numSwitchMoves = 0;
        numStudMoves = 0;
    }

    /**
     * Method createExpertModel receives in input the number of players participating in the game and creates a
     * new ExpertGameBoard instance.
     * @param m - number of the players
     */
    public void createExpertModel(GameParams m){
        numPlayers=m.getNumPlayers();
        expertModel = new ExpertGameBoard(m.getNumPlayers());
        expertModel.addPlayer(m.getNickname(), m.getColorT(), m.getMage());
        expertModel.newClouds();
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
        if (m instanceof PlayCharacter msg) {
            if(expertModel.getActiveCharacter() == null){
                try {
                    Character c = expertModel.getCharacters().stream().
                            filter(character -> msg.getC().equals(character.getDescription())).findAny().orElse(null);
                    if(c!= null) expertModel.playCharacter(c);
                    else getServer().sendMessage(nickname, "this card is not available");
                } catch (NotEnoughCoinsException e) {
                    getServer().sendMessage(nickname, "you can't afford to play this card");
                }
            }
        }

        if (expertModel.getActiveCharacter() != null) {
            Character activeCharacter = expertModel.getActiveCharacter();

            if (m instanceof SpecialMoveIsland mess) {
                if(activeCharacter.getDescription().equals(CharacterDescription.CHAR1)) {
                    try {
                        expertModel.moveStudent(mess.getStudent(), ((CharacterWithStudent) activeCharacter), expertModel.getIslandByIndex(mess.getIslandIndex()));
                    } catch (NoSuchStudentException e) {
                        getServer().sendMessage(nickname,"there is no " + mess.getStudent().toString().toLowerCase() + " student on the card" );
                    }
                        expertModel.resetCharacterStudent();
                    }
                }
            if (m instanceof ChooseIsland mess) {
                if(activeCharacter.getDescription().equals(CharacterDescription.CHAR3)){
                    if(mess.getIslandIndex() < 0 || mess.getIslandIndex() >= expertModel.getSizeWorld()) {
                        expertModel.checkIsland(expertModel.getIslandByIndex(mess.getIslandIndex()));
                        expertModel.checkWin();
                    }
                }

                if (activeCharacter.getDescription().equals(CharacterDescription.CHAR5)) {
                    int noEntry = ((CharacterWithNoEntry) activeCharacter).getNumNoEntry();
                    if (noEntry != 0) {
                        expertModel.getIslandByIndex(mess.getIslandIndex()).setNumNoEntry(1);
                        expertModel.removeNoEntry();
                    }
                }
            }
            if (m instanceof ChooseColor mess){
                if(activeCharacter.getDescription().equals(CharacterDescription.CHAR9))
                    expertModel.setBannedColor(mess.getColor());
                if(activeCharacter.getDescription().equals(CharacterDescription.CHAR12)){
                    try {
                        ((CharacterWithStudent) activeCharacter).remove(mess.getColor());
                    } catch (NoSuchStudentException e) {
                        getServer().sendMessage(nickname,"there is no " + mess.getColor().toString().toLowerCase() + " students on the card" );
                    }
                    expertModel.removeHall(mess.getColor());
                    expertModel.resetCharacterStudent();
                    }
                if(activeCharacter.getDescription().equals(CharacterDescription.CHAR11)){
                    try {
                        ((CharacterWithStudent) activeCharacter).remove(mess.getColor());
                    } catch (NoSuchStudentException e) {
                        getServer().sendMessage(nickname,"there is no " + mess.getColor().toString().toLowerCase() + " students on the card" );
                    }
                    try {
                        expertModel.addToHall(mess.getColor());
                    } catch (PlaceFullException e) {
                        getServer().sendMessage(nickname,"There is no space for another " + mess.getColor().toString().toLowerCase() + " student in the hall");
                    }
                    expertModel.resetCharacterStudent();
                }
            }

            if(m instanceof ChooseTwoColors mess){
                if(activeCharacter.getDescription().equals(CharacterDescription.CHAR10)){
                    if(numSwitchMoves < 2){
                        // first color hall, second color entrance
                        try {
                            expertModel.switchStudents(mess.getFirstColor(), mess.getSecondColor());
                        } catch (NoSuchStudentException e) {
                            getServer().sendMessage(nickname,"The two chosen students can't be switched");
                        } catch (PlaceFullException e) {
                            getServer().sendMessage(nickname,"The two chosen students can't be switched");
                        }
                        numSwitchMoves++;
                    }
                }
                if(activeCharacter.getDescription().equals(CharacterDescription.CHAR7)){
                    if(numStudMoves < 3){
                        //first color entrance, second color card
                        try {
                            expertModel.moveStudent(mess.getFirstColor(), expertModel.getSchoolBoard(), ((CharacterWithStudent) activeCharacter));
                        } catch (NoSuchStudentException e) {
                            getServer().sendMessage(nickname,"There is no " + mess.getFirstColor().toString().toLowerCase()+ " students in the entrance");
                        }
                        try {
                            expertModel.moveStudent(mess.getSecondColor(), ((CharacterWithStudent) activeCharacter), expertModel.getSchoolBoard());
                        } catch (NoSuchStudentException e) {
                            getServer().sendMessage(nickname,"There is no " + mess.getSecondColor().toString().toLowerCase()+ " students on the card");
                        }
                        numStudMoves++;
                    }
                }
            }
        }
    }
}

