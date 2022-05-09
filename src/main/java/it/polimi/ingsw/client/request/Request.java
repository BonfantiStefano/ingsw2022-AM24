package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;


public interface Request{
    void accept(Controller c);
}
