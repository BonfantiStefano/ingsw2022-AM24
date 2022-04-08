package it.polimi.ingsw.model.profstrategy;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.player.Player;

import java.util.*;

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
        HashMap<ColorS, Player> result=new HashMap<>();
        result=profs;
        //for every ColorS put the Player with the highest number of Students of that color in the map
        for(ColorS c: ColorS.values()){
            //number of students held by the Prof's owner, if there's no owner the default value is 0
            int max=((profs.get(c)==null? 0 : profs.get(c).getMyBoard().getHall(c)));
            for(Player p: players){
                if(p.getMyBoard().getHall().get(c)>max)
                    result.put(c,p);
            }
        }
        return result;
    }
}
