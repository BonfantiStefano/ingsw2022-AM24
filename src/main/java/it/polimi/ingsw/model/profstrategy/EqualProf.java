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
        HashMap<ColorS,Player> result = new HashMap<>(profs);
        for(ColorS c: ColorS.values()){
            //get all Players with the max number of Students of this Color in their Hall
            List<Player> tied = players.stream().filter(p -> (p.getHall(c) == players.stream().max(Comparator.comparingInt(p1->p1.getHall(c))).get().getHall(c))).toList();
            //if the ActivePlayer is tied with other Players he gets the Prof, otherwise it doesn't change owner
            if(tied.size()>1)
                result.put(c, tied.stream().filter(Player::isPlaying).findAny().orElse(profs.get(c)));
            else //if there's no tie the Player with the highest number gets the Profs
                result.put(c, tied.get(0));
        }
        return result;
    }
}
