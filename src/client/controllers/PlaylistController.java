package client.controllers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import MAMP.MAMP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.models.Playlist;

public class PlaylistController {

    @FXML
    private TextField textField;

    @FXML
    private Button CreatePlaylist;

    @FXML
    void createPlaylist(ActionEvent event) {
        System.out.println("Create playlist");
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            Playlist playlist = new Playlist(textField.getText());
            MAMP req = new MAMP("/addPlaylist", "", playlist);
            outputStream.writeObject(req);

            outputStream.close();
            socket.close();
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}