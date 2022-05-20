package it.polimi.ingsw.client;

import it.polimi.ingsw.client.request.Pong;
import it.polimi.ingsw.server.answer.Answer;

public interface UserInterface {
    void begin(String ip, int port);

    void addMessage(Answer a);
}
