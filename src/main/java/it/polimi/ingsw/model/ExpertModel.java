package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.exceptions.NoSuchStudentException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.world.Island;

import java.util.ArrayList;

public interface ExpertModel extends Model{

    void addPlayer(String nickname, ColorT color, Mage mage);

    void entranceToHall(ColorS s) throws PlaceFullException, NoSuchStudentException;

    void hallToEntrance(ColorS s) throws NoSuchStudentException;

    void addToHall(ColorS s) throws PlaceFullException;

    void switchStudents(ColorS hallS, ColorS entranceS) throws PlaceFullException, NoSuchStudentException;

    void removeHall(ColorS s) throws NoSuchStudentException;

    void playCharacter(Character c) throws NotEnoughCoinsException;

    void checkIsland(Island island);

    Character getActiveCharacter();

    //void setActiveCharacter(Character activeCharacter);

    void moveMN(int numMNSteps) throws InvalidMNStepsException;

    void resetCharacterStudent() throws ClassCastException;

    void resetNoEntryCharacter();

    void removeNoEntry();

    ArrayList<Character> getCharacters();

    void setBannedColor(ColorS color);

}
