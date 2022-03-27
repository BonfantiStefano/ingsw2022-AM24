package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Optional;

public class NoTowerInfluence implements InfluenceStrategy{
    public int getInfluence(Island i, Player p, Optional<ColorS> color, HashMap<ColorS, Player> profs) {
        int influence = 0;
        for(ColorS c : ColorS.values()) {
            if (profs.get(c).equals(p)) {
                influence = influence + i.getNumStudentByColor(c);
            }
        }
        return influence;
    }

}
