package it.polimi.ingsw.model;

import it.polimi.ingsw.model.world.Island;

public class MotherNature{
    private Island location;

    public MotherNature () {
        location=null;
    }

    public Island getLocation() {
        return location;
    }

    public void setLocation(Island location) {
        this.location = location;
    }
}
