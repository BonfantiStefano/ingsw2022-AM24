package it.polimi.ingsw.model.world.influence;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.Island;

import java.util.HashMap;
import java.util.Optional;

/**
 * InfluenceStrategy interface contains the method getInfluence utilized to calculate the influence of an island for a single player.
 *
 * @author Bonfanti Stefano
 */
public interface InfluenceStrategy {

    /**
     * Method getInfluence calculates the influence on the island i, for the player p (given by parameter).
     * @param i Island - the Island on which the influence has to be calculated.
     * @param p Player - the player for whom the influence is calculated.
     * @param color Optional ColorS - the banned color used by the class NoColorInfluence.
     * @param profs Map ColorS, Player - the Map contains the prof (memorized as a ColorS) and the owner.
     * @return the value of the influence.
     */
    int getInfluence(Island i, Player p, Optional<ColorS> color, HashMap<ColorS, Player> profs);
}
