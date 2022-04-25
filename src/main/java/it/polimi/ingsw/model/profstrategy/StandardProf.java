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
            //get all Players with the max number of Students of this Color in their Hall
            List<Player> tied = players.stream().filter(p -> (p.getHall(c) == players.stream().max(Comparator.comparingInt(p1->p1.getHall(c))).get().getHall(c))).toList();
            //if there is a tie the Prof doesn't change owner
            if(tied.size()>1)
                result.put(c,profs.get(c));
            //otherwise, the Player with the highest number gets the Profs
            else
                result.put(c, tied.get(0));
            //other verbose version
            /*
            //find the max number of Students of this Color in a Hall
            max = Collections.max(players.stream().map(p -> p.getHall(c)).toList());
            int finalMax = max;
            //find how many Players are tied
            //if there is a tie the Prof doesn't change owner
            if(players.stream().filter(p->p.getHall(c) == finalMax).toList().size()>1)
                result.put(c,profs.get(c));
            //if there's no tie the prof goes to the player with the highest number of Students in his Hall
            else
                result.put(c,players.stream().reduce((p1,p2) -> p1.getHall(c)>p2.getHall(c)?p1:p2).orElse(profs.get(c)));
             */
        }
        return result;
    }
}
