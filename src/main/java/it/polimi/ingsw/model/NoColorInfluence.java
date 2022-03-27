package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Optional;

public class NoColorInfluence implements InfluenceStrategy{
    public int getInfluence(Island i, Player p, Optional<ColorS> color, HashMap<ColorS, Player> profs) {
        int influence = 0;
        for(ColorS c : ColorS.values()) {
            if (profs.get(c).equals(p) && color.isPresent() && c != color.orElse(ColorS.RED)) {
                influence = influence + i.getNumStudentByColor(c);
            }
        }
        if (p.getColorTower().equals(i.getTowerColor())) {
            influence = influence + i.getNumSubIsland();
        }
        return influence;
    }
}