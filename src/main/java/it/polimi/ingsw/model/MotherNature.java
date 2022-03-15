package it.polimi.ingsw.model;

public class MotherNature{
    private static MotherNature instance=null;
    private Island location;

    public static MotherNature getInstance() {
        if (instance == null)
            instance = new MotherNature();
        return instance;
    }

    public Island getLocation() {
        return location;
    }

    public void setLocation(Island location) {
        this.location = location;
    }
}
