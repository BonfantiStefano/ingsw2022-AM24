package it.polimi.ingsw.model.world.influence;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.Island;

import java.util.HashMap;
import java.util.Optional;

public class AdditionalInfluence implements InfluenceStrategy {
    public int getInfluence(Island i, Player p, Optional<ColorS> color, HashMap<ColorS, Player> profs) {
        int influence = 0;
        for(ColorS c : ColorS.values()) {
            if (profs.get(c).equals(p)) {
                influence = influence + i.getNumStudentByColor(c);
            }
        }
        if (p.getColorTower().equals(i.getTowerColor())) {
            influence = influence + i.getNumSubIsland();
        }
        if(p.isPlaying()) {
            influence = influence + 2;
        }
        return influence;
    }

}
