package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.SchoolBoard;

import java.util.ArrayList;
import java.util.Map;

/** VirtualSchoolBoard class is a simplified representation of a player's schoolBoard.*/
public class VirtualSchoolBoard {
    private ArrayList<ColorS> entrance;
    private ArrayList<ColorT> towers;
    private Map<ColorS,Integer> hall;

    /**Constructor VirtualSchoolBoard creates a new VirtualSchoolBoard instance.*/
    public VirtualSchoolBoard(SchoolBoard schoolBoard) {
        this.entrance = schoolBoard.getEntrance();
        this.towers = schoolBoard.getTowers();
        hall = schoolBoard.getHall();
    }

}
