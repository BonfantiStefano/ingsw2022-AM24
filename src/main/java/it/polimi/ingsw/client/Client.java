package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.client.request.GameParams;
import it.polimi.ingsw.client.request.Join;
import it.polimi.ingsw.client.request.MoveMN;
import it.polimi.ingsw.client.request.Request;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.answer.Ping;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

//Not final, work in progress
public class Client {
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private Thread ponger;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter IP");
        String ip = scanner.nextLine();
        System.out.println("IP is: " + ip);
        System.out.println("Enter port");
        int port = scanner.nextInt();
        System.out.println("Port is: "+ port);
        Client c = new Client();
        c.startClient(ip, port);
    }

    public void startClient(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            System.out.println("Connection established");
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.flush();
                is = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("Error during initialization of the client!");
                System.exit(0);
            }
            System.out.println("Stream created");
            ponger = new Thread(() -> {
                while (true){
                    try{
                        String s = (String) is.readObject();
                        System.out.println(s);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            ponger.start();
            Scanner scanner = new Scanner(System.in);
            try {
                while (true) {
                    String input = scanner.nextLine();
                    String jsonInput = jsonFromInput(input);
                    os.reset();
                    os.writeObject(jsonInput);
                    os.flush();
                    System.out.println(input);
                    System.out.println(jsonInput);
                }
            } catch (NoSuchElementException e) {
                System.out.println("Connection closed");
            } finally {
                is.close();
                os.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String jsonFromInput(String s){
        return switch (s){
            case "Join" -> toJson(new Join("test", Mage.MAGE2, ColorT.WHITE, 0));
            case "MoveMN" -> toJson(new MoveMN(5));
            default -> throw new IllegalStateException("Unexpected value: " + s);
            //non possibile perché non è una request
            //case "GameParams" -> toJson(new GameParams(3, true, "Carlo",Mage.MAGE1, ColorT.BLACK))
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