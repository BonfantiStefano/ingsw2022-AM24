package it.polimi.ingsw.model.profstrategy;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Standard behaviour for checkProfs method
 */
public class StandardProf implements ProfStrategy {

    /**
     *  Implements the standard implementation of the CheckProf algorithm (for every ColorS the Player
     *  with the highest number of Students of that Color in his hall gains control of the corresponding Prof)
     * @param players the List of all Players in game
     * @return a Map that contains the Prof's color and the corresponding Player in control of that Prof
     */
    @Override
    public HashMap<ColorS, Player> checkProfs(ArrayList<Player> players, HashMap<ColorS, Player> profs) {
        HashMap<ColorS, Player> result = new HashMap<>();
        //for every ColorS put the Player with the highest number of Students of that color in the map
        for(ColorS c: ColorS.values()){
            final int max = players.stream().max(Comparator.comparingInt(p1->p1.getHall(c))).get().getHall(c);
            //get all Players with the max number of Students of this Color in their Hall
            List<Player> tied = players.stream().filter(p -> (p.getHall(c) == max)).toList();
            //if there is a tie the Prof doesn't change owner
            if(tied.size()>1||max==0)
                result.put(c,profs.get(c));
            //otherwise, the Player with the highest number gets the Profs
            else
                result.put(c, tied.get(0));
            //other verbose version
        }
        return result;
    }
}
