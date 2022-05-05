package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;

public class UpdateProfs implements Update{
    private final HashMap<ColorS, Player> profs;

    public UpdateProfs(HashMap<ColorS, Player> profs) {
        this.profs = profs;
    }

    public HashMap<ColorS, Player> getProfs() {
        return profs;
    }
}
