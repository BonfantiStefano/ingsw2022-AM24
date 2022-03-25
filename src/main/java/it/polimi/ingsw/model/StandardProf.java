package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Map;

public class StandardProf implements ProfStrategy{

    /**
     *  Implements the standard implementation of the CheckProf algorithm (for every ColorS the Player
     *  with the highest number of Students of that Color in his hall gains control of the corresponding Prof)
     * @param players the List of all Players in game
     * @return a Map that contains the Prof's color and the corresponding Player in control of that Prof
     */
    @Override
    public Map<ColorS, Player> checkProfs(ArrayList<Player> players) {
        return null; //placeholder
    }
}
