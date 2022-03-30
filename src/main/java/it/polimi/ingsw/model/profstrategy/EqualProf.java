package it.polimi.ingsw.model.profstrategy;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.profstrategy.ProfStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EqualProf implements ProfStrategy {
    /**
     *  A different implementation of the CheckProf algorithm (for every ColorS the
     *  ActivePlayer can gain control of a Prof even if he has the same number of Students  as another Player)
     * @param players the List of all Players in game
     * @return a Map that contains the Prof's color and the corresponding Player in control of that Prof
     */
    @Override
    public HashMap<ColorS, Player> checkProfs(ArrayList<Player> players, HashMap<ColorS, Player> profs) {
        HashMap<ColorS, Player> result=profs;

        for(ColorS c: ColorS.values()){
            int max=((profs.get(c)==null? 0 : profs.get(c).getMyBoard().getHall().get(c))); //number of students of the Prof's owner
            for(Player p: players){
                if(p.getMyBoard().getHall().get(c)>max)
                    result.put(c,p);
                else if(p.isPlaying()&&p.getMyBoard().getHall().get(c)>=max)
                    result.put(c,p);
            }
        }
        return result;
    }
}
