package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.request.Request;
import it.polimi.ingsw.model.Model;

public class ActionController {
    private Model model;

    public ActionController(Model model){
        this.model=model;
    }

    public void handleAction(Request m){}
}
