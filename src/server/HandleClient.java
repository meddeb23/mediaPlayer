package server;

import java.io.*;
import java.net.*;

import MAMP.MAMP;
import server.routes.HandleMessages;

public class HandleClient extends Thread {
    private Socket socket = null;

    public HandleClient(Socket socket) {
        super("ThreadClient");
        this.socket = socket;
    }

    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Reading incomming request
            MAMP req = (MAMP) inputStream.readObject();

            // handle Routing
            switch (req.getResource()) {
                case "/message":
                    HandleMessages.run(req, outputStream);
                    break;
                default:
                    break;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur : " + e);
        } finally {
            fermerSocket();
        }
    }

    private void fermerSocket() {
        try {
            this.socket.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}