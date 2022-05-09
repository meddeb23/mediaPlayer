package client.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import MAMP.MAMP;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server.models.Playlist;
import utilities.Utilities;

public class HomeControllers implements Initializable {
    @FXML
    private ImageView actionIcon;

    @FXML
    private Button addPlaylistBtn;

    @FXML
    private Label songTitle;

    @FXML
    private VBox mediaList;

    @FXML
    private Button nextBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Button prvBtn;

    @FXML
    private ImageView volumeBtn;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar songProgressBar;

    @FXML
    private VBox playlistList;

    @FXML
    private Button home;

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs;
    private ArrayList<File> arts;

    private int songNumber = 0;
    private Timer timer;
    private TimerTask task;
    private boolean isPlaying;

    private Image playBtnIcon = new Image(getClass().getResourceAsStream("/assets/icons8_play_30px_2.png"));
    private Image pauseBtnIcon = new Image(getClass().getResourceAsStream("/assets/icons8_pause_30px_1.png"));

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        songs = new ArrayList<File>();
        directory = new File("E:\\Music\\Music");

        files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String test = Utilities.getExtension(file.getName());
                if (test.equals("mp3")) {
                    songs.add(file);
                }
            }
            createMusicList(files);

        }
        System.out.println(songs.get(songNumber).getAbsolutePath());

        // songTitle.setText(Utilities.read_metadata(songs.get(songNumber)));

        loadMedia(false);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
        actionIcon.setImage(playBtnIcon);
        try {
            loadPlaylistList();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadPlaylistList() throws ClassNotFoundException {
        System.out.println("Getting playlist");
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            MAMP req = new MAMP("/getPlaylists", "", null);
            outputStream.writeObject(req);

            ArrayList<Playlist> playlists = (ArrayList<Playlist>) inputStream.readObject();
            playlistList.getChildren();
            for (Playlist p : playlists) {
                Button btn = new Button(p.name);
                btn.setStyle("-fx-text-fill: #fff; -fx-background-color: #00000000");
                btn.setFont(new Font(18));
                playlistList.getChildren().add(btn);
            }
            outputStream.close();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addPlaylist(ActionEvent event) throws IOException {
        Stage createPlaylist = new Stage();
        createPlaylist.setTitle("Create new playlist");
        Parent root = FXMLLoader.load(getClass().getResource("/client/views/PlaylistPopUP.fxml"));
        Scene scene = new Scene(root, 450, 220);
        createPlaylist.setScene(scene);
        createPlaylist.show();
    }

    private void createMusicList(File[] files) {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        int indexCounter = 0;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (Utilities.getExtension(file.getName()).equals("mp3")) {
                final int index = indexCounter++;

                Image playBtnIcon = new Image(getClass().getResourceAsStream("/assets/icons8_play_30px.png"));
                Image addBtnIcon = new Image(getClass().getResourceAsStream("/assets/icons8_plus_math_30px.png"));
                // Image pauseBtnIcon = new
                // Image(getClass().getResourceAsStream("/assets/icons8_pause_30px.png"));

                HBox songItem = new HBox();
                songItem.setPadding(new Insets(10.0));
                songItem.setAlignment(Pos.CENTER_LEFT);
                songItem.setPrefWidth(715.0);
                songItem.setPrefHeight(30.0);
                if (indexCounter % 2 == 0)
                    songItem.setStyle("-fx-background-color: #00000050;");

                Label songTitle = new Label(file.getName());
                songTitle.setStyle("-fx-text-fill: #ffffff;");
                songTitle.setPrefWidth(372.0);

                Button playBtn = new Button();
                playBtn.setStyle("-fx-background-color: #00000000;");

                Button addBtn = new Button();
                addBtn.setStyle("-fx-background-color: #00000000;");

                playBtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        songNumber = index;
                        System.out.println("song index: " + songNumber);
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                        }
                        if (timer != null) {
                            cancelTimer();
                        }
                        loadMedia(true);
                    }
                });

                addBtn.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        System.out.println("add to playlist...");
                    }
                });
                ImageView icon = new ImageView(playBtnIcon);
                playBtn.setGraphic(icon);

                ImageView iconAdd = new ImageView(addBtnIcon);
                addBtn.setGraphic(iconAdd);

                songItem.getChildren().addAll(songTitle, playBtn, iconAdd);
                mediaList.getChildren().add(songItem);
            }
        }
    }

    @FXML
    void refresh(ActionEvent event) throws ClassNotFoundException {
        System.out.println("Refresh home");
        loadPlaylistList();
    }

    @FXML
    void next(ActionEvent event) {
        if (songNumber < songs.size() - 1) {
            songNumber++;
            mediaPlayer.stop();
            if (timer != null)
                cancelTimer();
            loadMedia(true);
        }
    }

    @FXML
    void togglePlay() {
        if (isPlaying) {
            mediaPlayer.pause();
            cancelTimer();
            actionIcon.setImage(playBtnIcon);
        } else {
            mediaPlayer.play();
            beginTimer();
            actionIcon.setImage(pauseBtnIcon);
        }
    }

    @FXML
    void previous(ActionEvent event) {

        if (songNumber > 0) {
            songNumber--;
            mediaPlayer.stop();
            if (timer != null)
                cancelTimer();
            loadMedia(true);
        }
    }

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                isPlaying = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current / end);
                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void cancelTimer() {
        isPlaying = false;
        timer.cancel();
    }

    @FXML
    void updateTimer(DragEvent event) {
        System.out.println("drag over" + event.getX());
    }

    public void loadMedia(boolean autoPlay) {
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        if (autoPlay)
            togglePlay();
    }

}
