package it.polimi.ingsw.model.profstrategy;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Alternate behaviour for checkProfs method
 */
public class EqualProf implements ProfStrategy {
    /**
     *  A different implementation of the CheckProf algorithm (for every ColorS the
     *  ActivePlayer can gain control of a Prof even if he has the same number of Students  as another Player)
     * @param players the List of all Players in game
     * @return a Map that contains the Prof's color and the corresponding Player in control of that Prof
     */
    @Override
    public HashMap<ColorS, Player> checkProfs(ArrayList<Player> players, HashMap<ColorS, Player> profs) {
        int max;
        List<Player> sameNumber;
        HashMap<ColorS,Player> result = new HashMap<>(profs);
        for(ColorS c: ColorS.values()){
            result.put(c, null);
            //find the max number of Students of this Color in a Hall
            max = Collections.max(players.stream().map(p -> p.getHall(c)).toList());
            int finalMax = max;
            //find all Players that have the same number of Students in their Hall
            sameNumber =  players.stream().filter(p->p.getHall(c) == finalMax).collect(Collectors.toList());
            if(sameNumber.size()>1)
                //if there are tied Players and the ActivePlayer is one of them he gets the Prof, otherwise the Prof doesn't change owner
                result.put(c,sameNumber.stream().filter(Player::isPlaying).findFirst().orElse(profs.get(c)));
            else
                //if no Players are tied assign the Prof to the one with the highest number of Students in his Hall
                result.put(c,players.stream().reduce((p1,p2) -> p1.getHall(c)>p2.getHall(c)?p1:p2).orElse(profs.get(c)));
        }
        return result;
    }
}
