package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.request.ChooseAssistant;
import it.polimi.ingsw.client.request.*;

public class Server {

    public void sendMessage(String nickname, String content){}

    public void sendMessageToOthers(String nickname, String context) {}

    public void sendMessageToAll(String context) {}

    public void gameEnded(){}

    public Request parseMessage(String jsonString) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        return switch (jsonObject.get("type").getAsString()) {
            case "ChooseAssistant" -> gson.fromJson(jsonString, ChooseAssistant.class);
            case "ChooseCloud" -> gson.fromJson(jsonString, ChooseCloud.class);
            case "ChooseColor" -> gson.fromJson(jsonString, ChooseColor.class);
            case "ChooseIsland" -> gson.fromJson(jsonString, ChooseIsland.class);
            case "ChooseTwoColors" -> gson.fromJson(jsonString, ChooseTwoColors.class);
            case "Disconnect" -> gson.fromJson(jsonString, Disconnect.class);
            case "EntranceToHall" -> gson.fromJson(jsonString, EntranceToHall.class);
            //case "GameParams" -> gson.fromJson(jsonString, GameParams.class);
            case "Join" -> gson.fromJson(jsonString, Join.class);
            case "MoveMN" -> gson.fromJson(jsonString, MoveMN.class);
            case "MoveToIsland" -> gson.fromJson(jsonString, MoveToIsland.class);
            case "PlayCharacter" -> gson.fromJson(jsonString, PlayCharacter.class);
            case "SpecialMoveIsland" -> gson.fromJson(jsonString, SpecialMoveIsland.class);
            default -> throw new Exception("Invalid message");
        };
    }

    public String toJson(Request r){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }
}
