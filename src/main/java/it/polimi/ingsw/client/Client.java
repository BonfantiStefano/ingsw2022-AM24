package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.answer.Answer;
import it.polimi.ingsw.server.answer.Error;
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
    private boolean active;

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
        active = true;
        //TODO implementare che quando mi arriva un errore per cui sono disconnesso metto active a false in modo da non poter più
        //leggere e poi chiudere il socket
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
                while (active){
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
                while (active) {
                    String input = scanner.nextLine();
                    String jsonInput = jsonFromInput(input);
                    os.reset();
                    os.writeObject(jsonInput);
                    os.flush();
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
         switch (s){
             case "Join" :
                 return toJson(new Join("test", Mage.MAGE2, ColorT.WHITE, 0));
             case "MoveMN" :
                 return toJson(new MoveMN(5));
             default : System.out.println("Invalid String");
                 return null;
            //non possibile perché non è una request
            //case "GameParams" -> toJson(new GameParams(3, true, "Carlo",Mage.MAGE1, ColorT.BLACK))
        }
    }

    public String toJson(Request r){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }
}