package it.polimi.ingsw.model.world.influence;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.world.Island;

import java.util.HashMap;
import java.util.Optional;

/**
 * AdditionalInfluence class contains the method getInfluence utilized to calculate the influence of an island for a
 * single player, if that player is the activePlayer he has 2 additional points.
 *
 * @author Bonfanti Stefano
 */
public class AdditionalInfluence implements InfluenceStrategy {

    /**
     * Method getInfluence calculates the influence on the island i, for the player p (given by parameter), if the player
     * is playing he has 2 additional points.
     * @param i Island - the Island on which the influence has to be calculated.
     * @param p Player - the player for whom the influence is calculated.
     * @param color Optional<ColorS> - the banned color used by the class NoColorInfluence.
     * @param profs HashMap<ColorS, Player> - the Map contains the prof (memorized as a ColorS) and the owner.
     * @return the value of the influence.
     */
    @Override
    public int getInfluence(Island i, Player p, Optional<ColorS> color, HashMap<ColorS, Player> profs) {
        int influence = 0;
        for(ColorS c : ColorS.values()) {
            if (Optional.ofNullable(profs.get(c)).equals(Optional.of(p))) {
                influence = influence + i.getNumStudentByColor(c);
            }
        }
        if (Optional.of(p.getColorTower()).equals(i.getTowerColor())) {
            influence = influence + i.getNumSubIsland();
        }
        if(p.isPlaying()) {
            influence = influence + 2;
        }
        return influence;
    }

}

