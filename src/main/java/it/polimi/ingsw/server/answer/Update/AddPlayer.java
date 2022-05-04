package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.server.virtualview.VirtualPlayer;

public class AddPlayer implements Update {
    private final VirtualPlayer player;

    public AddPlayer(VirtualPlayer player) {
        this.player = player;
    }

    public VirtualPlayer getPlayer() {
        return player;
    }
}
