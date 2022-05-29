package it.polimi.ingsw.client;

import it.polimi.ingsw.server.virtualview.VirtualView;

import java.beans.PropertyChangeListener;

/**
 * Interface UserInterface contains all the methods used from all the UI.
 *
 * @author Bonfanti Stefano
 */
public interface UserInterface extends PropertyChangeListener {
    /**
     * Method setupConnection opens a Socket on the given IP and port.
     * @param ip String - the Server IP.
     * @param port int - the port of the Server.
     */
    void setupConnection(String ip, int port);

    /**
     * Method setVirtualView sets the virtual view.
     * @param virtualView VirtualView - the virtual view.
     */
    void setVirtualView(VirtualView virtualView);

    /**
     * Converts an Object to Json format.
     * @param object the Client's request.
     * @return the json of the Message.
     */
    String toJson(Object object);
}
