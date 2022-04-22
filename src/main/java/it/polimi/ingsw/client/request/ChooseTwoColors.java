package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorS;

public class ChooseTwoColors implements Request {
    private ColorS firstColor;
    private ColorS secondColor;

    public ChooseTwoColors(ColorS firstColor, ColorS secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }

    public ColorS getFirstColor() {
        return firstColor;
    }
    public ColorS getSecondColor() {
        return secondColor;
    }
}
