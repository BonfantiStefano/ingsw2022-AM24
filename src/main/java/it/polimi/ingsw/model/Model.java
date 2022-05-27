package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SchoolBoard;
import it.polimi.ingsw.model.world.Island;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Optional;

public interface Model {

    int getNumPlayers();

    boolean chooseAssistants(Player player, int index) throws InvalidIndexException;

    Player getPlayerByNickname(String nickname);

    int getFirstPlayer();

    void nextPlayer();

    void addPlayer(String nickname, ColorT color, Mage mage);

    ArrayList<Player> getPlayers();

    void moveMN(int numMNSteps) throws InvalidMNStepsException;

    void newClouds();

    void setConnected(String nickname, boolean status);

    boolean checkGameMustEnd();

    Player getActivePlayer();

    Optional<Player> checkWin();

    SchoolBoard getSchoolBoard();

    ArrayList<Player> getSortedPlayers();

    void resetRound();

    void resetTurn();

    void moveStudent(ColorS s, CanRemoveStudent from, CanAcceptStudent to) throws NoSuchStudentException;

    int getSizeWorld();

    boolean getGameMustEnd();

    void entranceToHall(ColorS s) throws PlaceFullException, NoSuchStudentException;

    Island getIslandByIndex(int index);

    void addListener(PropertyChangeListener controller);

    Cloud getCloudByIndex(int index);

    public void setActivePlayerNull();
}
