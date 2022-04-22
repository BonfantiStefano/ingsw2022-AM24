package it.polimi.ingsw.model.profstrategy;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;

import java.util.*;

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
        for(ColorS c: ColorS.values()){
            profs.put(c, players.stream().reduce((p1, p2) -> (p1.getMyBoard().getHall().get(c)>p2.getMyBoard().getHall().get(c))
                             || (p1.isPlaying() && p1.getMyBoard().getHall().get(c).equals(p2.getMyBoard().getHall().get(c)))? p1 : p2)
                    .orElse(profs.get(c)));
        }
        return profs;
    }
}
