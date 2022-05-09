package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;

public class Disconnect implements Request{

    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
