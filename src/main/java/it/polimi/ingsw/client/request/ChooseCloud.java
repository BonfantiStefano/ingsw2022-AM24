package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;

public class ChooseCloud implements Request{
    private final int index;

    public ChooseCloud(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
