package client;

import java.io.*;
import java.net.*;

import MAMP.MAMP;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.models.Msg;

public class Client extends Application implements EventHandler<ActionEvent>, Serializable {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/client/views/home.fxml"));

        Scene scene = new Scene(root, 1100, 650);
        stage.setScene(scene);
        stage.setTitle("Music Player");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void handle(ActionEvent e) {
        // Object source = e.getSource();
        // if (source == send_btn) {
        // String msg = userInput.getText();
        // if (msg.length() != 0) {
        // chatHistory.setText(chatHistory.getText() + "Client : " + msg + "\n");
        // try {
        // this.send_msg(msg);
        // } catch (ClassNotFoundException e1) {
        // e1.printStackTrace();
        // }
        // userInput.setText(null);
        // }
        // }
    }

    private void send_msg(String msgText) throws ClassNotFoundException {
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Msg msg = new Msg("hello", "client");
            MAMP req = new MAMP("/message", "", msg);
            outputStream.writeObject(req);

            Msg recMsg = (Msg) inputStream.readObject();
            System.out.println(recMsg);

            outputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}