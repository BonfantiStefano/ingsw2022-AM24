package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.HasStrategy;
import it.polimi.ingsw.model.mnstrategy.MNStrategy;

public interface PlayerInterface extends HasStrategy<MNStrategy> {
    public boolean isPlaying();
}
