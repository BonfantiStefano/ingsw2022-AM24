package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.character.CharacterDescription;
import it.polimi.ingsw.model.gameboard.ExpertGameBoard;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.answer.Answer;
import it.polimi.ingsw.server.answer.Update.UpdateProfs;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class ServerTest {
    /*
    private Lobby lobby;
    private Server server;
    private ServerMain serverMain;
    private Client client;

    public String toJson(Object r){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }

    @Test
    void test(){
        HashMap<ColorS, VirtualPlayer> profs = new HashMap<>();
        String s = toJson(new UpdateProfs(profs));
        System.out.println(s);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
        System.out.println(gson.fromJson((s), UpdateProfs.class));

    }

     */
}
