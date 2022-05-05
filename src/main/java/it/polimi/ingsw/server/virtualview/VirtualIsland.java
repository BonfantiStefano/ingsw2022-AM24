package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.world.Island;

import java.util.ArrayList;
import java.util.Optional;

/** VirtualIsland class is a simplified representation of an island.*/
public class VirtualIsland {
    private final ArrayList<ColorS> students;
    private final ArrayList<ColorT> towers;
    private final int noEntry;
    private final int numSubIsland;

    /**Constructor VirtualIsland creates a new VirtualIsland instance.*/
    public VirtualIsland(Island island) {
        this.students = new ArrayList<>(island.getStudents());
        this.towers = new ArrayList<>(island.getTowers());
        this.noEntry = island.getNumNoEntry();
        this.numSubIsland = island.getNumSubIsland();
    }

    public ArrayList<ColorS> getStudents() {
        return students;
    }

    public ArrayList<ColorT> getTowers() {
        return towers;
    }

    public int getNoEntry() {
        return noEntry;
    }

    public Optional<ColorT> getTowerColor(){
        if(towers.size() > 0) {
            return Optional.of(towers.get(0));
        }
        return Optional.empty();
    }

    public int getNumSubIsland() {
        return numSubIsland;
    }

    public int getNumStudentByColor(ColorS color){
        return (int) students.stream().filter(s->s.equals(color)).count();
    }
}
