package client.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import utilities.Utilities;

public class HomeControllers implements Initializable {
    @FXML
    private ImageView actionIcon;

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

        // try {
        // FileInputStream file = new FileInputStream(songs.get(songNumber));
        // int size = (int) songs.get(songNumber).length();
        // // file.skip(size - 128);
        // byte[] last128 = new byte[size];
        // file.read(last128);
        // System.out.println(last128.toString());
        // String id3 = new String(last128);
        // String tag = id3.substring(0, 3);
        // System.out.println(tag);
        // if (tag.equals("ID3")) {
        // System.out.println("Title: " + id3.substring(18, 48).replace("TPE", ""));
        // System.out.println("Artist: " + id3.substring(33, 62));
        // System.out.println("Album: " + id3.substring(63, 91));
        // System.out.println("Year: " + id3.substring(93, 97));
        // } else
        // System.out.println(" does not contain" + " ID3 information.");
        // file.close();

        // } catch (Exception e) {

        // System.out.println("Error - " + e.toString());

        // }
        loadMedia(false);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
        actionIcon.setImage(playBtnIcon);

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
                // Image pauseBtnIcon = new
                // Image(getClass().getResourceAsStream("/assets/icons8_pause_30px.png"));

                HBox songItem = new HBox();
                songItem.setAlignment(Pos.CENTER_LEFT);
                songItem.setPrefWidth(715.0);
                songItem.setPrefHeight(30.0);
                songItem.setStyle("-fx-background-color: #00000000;");

                Label songTitle = new Label(file.getName());
                songTitle.setStyle("-fx-text-fill: #ffffff;");
                songTitle.setPrefWidth(372.0);

                Button playBtn = new Button();
                playBtn.setStyle("-fx-background-color: #00000000;");

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
                ImageView icon = new ImageView(playBtnIcon);
                playBtn.setGraphic(icon);
                songItem.getChildren().addAll(songTitle, playBtn);
                mediaList.getChildren().add(songItem);
            }
        }
    }

    @FXML
    void next(ActionEvent event) {
        System.out.println(songNumber + " / " + songs.size());
        if (songNumber - 1 < songs.size()) {
            songNumber++;
            mediaPlayer.stop();
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
        System.out.println(songNumber + " / " + songs.size());

        if (songNumber > 0) {
            songNumber--;
            mediaPlayer.stop();
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
                // System.out.println(current / end);
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
