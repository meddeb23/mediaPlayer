package server.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {
    public String name;
    public int _id;
    public ArrayList<String> songs;

    public Playlist(int _id, String name, ArrayList<String> songs) {
        this.name = name;
        this._id = _id;
        this.songs = songs;
    }

    public Playlist(int _id, String name) {
        this.name = name;
        this._id = _id;
    }

    public Playlist(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Playlist [_id=" + _id + ", name=" + name + "]";
    }

}
