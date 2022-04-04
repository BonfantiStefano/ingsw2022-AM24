package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.mnstrategy.MNStrategy;

/**
 * This interface exposes the method needed to check if the Player is active
 */
public interface PlayerInterface extends HasStrategy<MNStrategy> {
    /**
     * Checks if the Player is playing
     * @return true if the player is playing
     */
    boolean isPlaying();
}
