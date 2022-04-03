package it.polimi.ingsw.model.pawn;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.pawn.GenericColored;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tower class contains the method getColor to get the color of the Tower.
 *
 * @author Bonfanti Stefano
 */
public class Tower extends GenericColored<ColorT> {

    /**Constructor Tower creates a new Tower instance, with the given ColorT.*/
    public Tower(ColorT c){
        super(c);
    }


    /**
     * Method getColor returns the ColorT of the Tower.
     * @return the color of the Tower.
     */
    @Override
    public ColorT getColor() {
        return super.getColor();
    }

}
