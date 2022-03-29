package it.polimi.ingsw.model.pawn;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.pawn.GenericColored;

public class Tower extends GenericColored<ColorT> {
    public Tower(ColorT c){
        super(c);
    }

    @Override
    public ColorT getColor() {
        return super.getColor();
    }

}
