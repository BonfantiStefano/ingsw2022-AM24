open module it.polimi.ingsw {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.controls;

    exports it.polimi.ingsw.client.GUIView;
    exports it.polimi.ingsw.client.GUIView.controllers;
    exports it.polimi.ingsw.server.virtualview;
    exports it.polimi.ingsw.server.answer;
}