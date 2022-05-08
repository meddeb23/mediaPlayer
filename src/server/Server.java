package server;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket SocketServeur = null;
        Socket socket = null;
        int PORT = 5000;
        try {
            SocketServeur = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Error creating ServerSocket");
            return;
        }
        System.out.println("server is listing on port " + PORT);
        try {
            while (true) {
                socket = SocketServeur.accept();
                HandleClient thClient = new HandleClient(socket);
                thClient.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur : " + e);
        } finally {
            try {
                SocketServeur.close();
            } catch (IOException e) {
            }
        }
    }
}