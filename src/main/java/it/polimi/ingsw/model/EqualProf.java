package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Map;

public class EqualProf implements ProfStrategy{
    /**
     *  A different implementation of the CheckProf algorithm (for every ColorS the
     *  ActivePlayer can gain control of a Prof even if he has the same number of Students  as another Player)
     * @param players the List of all Players in game
     * @return a Map that contains the Prof's color and the corresponding Player in control of that Prof
     */
    @Override
    public Map<ColorS, Player> checkProfs(ArrayList<Player> players) {
        return null; //placeholder
    }
}
