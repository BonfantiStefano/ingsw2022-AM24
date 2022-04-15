package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyPlaceException;
import it.polimi.ingsw.exceptions.InvalidMNStepsException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.PlaceFullException;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.world.Island;

public interface ExpertModel extends Model{

    void addPlayer(String nickname, ColorT color, Mage mage);

    void entranceToHall(ColorS s) throws EmptyPlaceException, PlaceFullException;

    void hallToEntrance(ColorS s) throws EmptyPlaceException;

    void addToHall(ColorS s) throws PlaceFullException;

    void switchStudents(ColorS hallS, ColorS entranceS) throws EmptyPlaceException, PlaceFullException;

    void removeHall(ColorS s);

    void playCharacter(Character c) throws NotEnoughCoinsException;

    void checkIsland(Island island);

    Character getActiveCharacter();

    void setActiveCharacter(Character activeCharacter);

    void moveMN(int numMNSteps) throws InvalidMNStepsException;

    void resetCharacterStudent() throws ClassCastException;

    void resetNoEntryCharacter();

}
