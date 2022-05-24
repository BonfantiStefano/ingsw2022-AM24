package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;

import java.util.HashMap;

public class UpdateProfs implements Update{
    private final HashMap<ColorS, VirtualPlayer> profs;

    public UpdateProfs(HashMap<ColorS, VirtualPlayer> profs) {
        this.profs = profs;
    }

    public HashMap<ColorS, VirtualPlayer> getProfs() {
        return profs;
    }

    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
