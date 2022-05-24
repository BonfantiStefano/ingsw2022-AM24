package it.polimi.ingsw.client;

import it.polimi.ingsw.server.virtualview.VirtualView;

import java.beans.PropertyChangeListener;

public interface UserInterface extends PropertyChangeListener {
    void setupConnection(String ip, int port);

    void setVirtualView(VirtualView virtualView);
}
