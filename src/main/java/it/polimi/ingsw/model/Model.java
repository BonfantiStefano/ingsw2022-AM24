package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SchoolBoard;

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

    boolean checkGameMustEnd();

    Optional<Player> checkWin();

    public SchoolBoard getSchoolBoard();

    void moveStudent(ColorS s, CanRemoveStudent from, CanAcceptStudent to) throws NoSuchStudentException;
}
