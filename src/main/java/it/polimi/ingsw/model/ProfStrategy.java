package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Map;


/**
 *  Defines the signature of the method used to calculate who controls Profs
 * @Param players the List of all Players in game
 * @return a Map that contains the Prof's color and the corresponding Player in control of that Prof
 */
public interface ProfStrategy {
    Map<ColorS, Player> checkProfs(ArrayList<Player> players);
}
