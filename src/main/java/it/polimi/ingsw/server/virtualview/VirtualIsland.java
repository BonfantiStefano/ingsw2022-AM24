package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.world.Island;

import java.util.ArrayList;

/** VirtualIsland class is a simplified representation of an island.*/
public class VirtualIsland {
    private ArrayList<ColorS> students;
    private ArrayList<ColorT> towers;
    private int noEntry;

    /**Constructor VirtualIsland creates a new VirtualIsland instance.*/
    public VirtualIsland(Island island) {
        this.students = new ArrayList<>(island.getStudents());
        this.towers = new ArrayList<>(island.getTowers());
        this.noEntry = island.getNumNoEntry();
    }
}
