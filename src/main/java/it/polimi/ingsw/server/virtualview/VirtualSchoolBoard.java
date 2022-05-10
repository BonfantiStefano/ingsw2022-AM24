package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.SchoolBoard;

import java.util.ArrayList;
import java.util.Map;

/** VirtualSchoolBoard class is a simplified representation of a player's schoolBoard.*/
public class VirtualSchoolBoard {
    private final ArrayList<ColorS> entrance;
    private final ArrayList<ColorT> towers;
    private final Map<ColorS,Integer> hall;

    /**Constructor VirtualSchoolBoard creates a new VirtualSchoolBoard instance.*/
    public VirtualSchoolBoard(SchoolBoard schoolBoard) {
        this.entrance = schoolBoard.getEntrance();
        this.towers = schoolBoard.getTowers();
        hall = schoolBoard.getHall();
    }

    /** Method getEntrance returns the students in virtual player's entrance.
     * @return entrance - students in entrance
     */
    public ArrayList<ColorS> getEntrance() {
        return entrance;
    }

    /** Method getTowers returns the towers in virtual player's gameBoard.
     * @return entrance - player's towers
     */
    public ArrayList<ColorT> getTowers() {
        return towers;
    }

    /** Method getHall returns the students in virtual player's hall.
     * @return hall - students in hall
     */
    public Map<ColorS, Integer> getHall() {
        return hall;
    }
}
