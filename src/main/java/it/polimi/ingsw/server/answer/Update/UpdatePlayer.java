package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;

public class UpdatePlayer implements Update{
    private final VirtualPlayer player;
    private final int index;

    public UpdatePlayer(VirtualPlayer player, int index) {
        this.player = player;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public VirtualPlayer getPlayer() {
        return player;
    }

    @Override
    public void accept(CLI c){
        c.visit(this);
    }
}
