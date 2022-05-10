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

    /**
     * Method getStudents returns all the students placed on the virtual island
     * @return students - students placed on the virtual island
     */
    public ArrayList<ColorS> getStudents() {
        return students;
    }

    /**
     * Method getTowers returns all the towers placed on the virtual island
     * @return towers - towers placed on the virtual island
     */
    public ArrayList<ColorT> getTowers() {
        return towers;
    }

    /**
     * Method getNoEntry returns all the no entry tiles placed on the virtual island
     * @return noEntry - no entry tiles placed on the virtual island
     */
    public int getNoEntry() {
        return noEntry;
    }

    /**
     * Method getTowerColor returns the color of the towers placed on the virtual island
     * @return the tower's color
     */
    public Optional<ColorT> getTowerColor(){
        if(towers.size() > 0) {
            return Optional.of(towers.get(0));
        }
        return Optional.empty();
    }

    /**
     * Method getNumSubIsland returns the number of the virtual islands that are contained in the archipelago
     * @return the number of subIsland.
     */
    public int getNumSubIsland() {
        return numSubIsland;
    }

    /**
     * Method getNumStudentByColor returns the number of the students of the chosen color placed on the virtual island
     * @param color - the color of the students
     * @return - the number of the students of the chosen color
     */
    public int getNumStudentByColor(ColorS color){
        return (int) students.stream().filter(s->s.equals(color)).count();
    }
}
