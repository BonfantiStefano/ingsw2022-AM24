package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCloud;

import java.util.ArrayList;

public class CreateClouds implements Update{
    ArrayList<VirtualCloud> clouds;

    public CreateClouds(ArrayList<VirtualCloud> clouds) {
        this.clouds = clouds;
    }

    public ArrayList<VirtualCloud> getClouds() {
        return clouds;
    }

    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
