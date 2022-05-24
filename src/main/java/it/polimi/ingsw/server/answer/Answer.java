package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.Client;

import java.io.Serializable;

public interface Answer extends Serializable{
    void accept(Client c);
}
