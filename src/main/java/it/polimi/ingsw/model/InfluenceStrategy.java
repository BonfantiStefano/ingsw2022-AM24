package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Optional;

public interface InfluenceStrategy {
    public int getInfluence(Island i, Player p, Optional<ColorS> color, HashMap<ColorS, Player> profs);
}
