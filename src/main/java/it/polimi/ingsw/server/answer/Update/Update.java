package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.answer.Answer;

public interface Update extends Answer {
    void accept(Client c);
}
