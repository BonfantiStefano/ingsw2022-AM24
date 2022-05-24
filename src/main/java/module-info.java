open module it.polimi.ingsw {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.controls;

    //opens it.polimi.ingsw.client.GUIView to javafx.fxml;
    exports it.polimi.ingsw.client.GUIView;
    exports it.polimi.ingsw.client.GUIView.controllers;
    //opens it.polimi.ingsw.client.GUIView.controllers to javafx.fxml;
    //opens it.polimi.ingsw.server to com.google.gson;
    exports it.polimi.ingsw.server.virtualview;
    exports it.polimi.ingsw.server.answer;
}

/*
module com.example.javafxexample {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxexample to javafx.fxml;
    exports com.example.javafxexample;
    exports com.example.javafxexample.utils;
    opens com.example.javafxexample.utils to javafx.fxml;
}
 */