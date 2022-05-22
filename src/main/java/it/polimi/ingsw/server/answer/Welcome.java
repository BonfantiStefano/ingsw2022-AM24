package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualLobby;

import java.util.ArrayList;

public class Welcome implements Answer{
    private ArrayList<VirtualLobby> lobbies;

    public Welcome(ArrayList<VirtualLobby> lobbies) {
        this.lobbies = new ArrayList<>(lobbies);
    }

    public ArrayList<VirtualLobby> getLobbies() {
        return lobbies;
    }

    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
