package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.ColorS;

import java.util.ArrayList;

/** VirtualCloud class is a simplified representation of a cloud.*/
public class VirtualCloud {
    private final ArrayList<ColorS> students;

    /**Constructor VirtualCloud creates a new VirtualCloud instance.*/
    public VirtualCloud(Cloud cloud) {
        this.students = new ArrayList<>(cloud.getStudents());
    }

    /**
     * Method getStudents returns all the students placed on the virtual cloud
     * @return students - students placed on the virtual cloud
     */
    public ArrayList<ColorS> getStudents() {
        return students;
    }
}
