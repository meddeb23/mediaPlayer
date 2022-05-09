package server.routes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import MAMP.MAMP;
import server.DAO.DAOPlaylist;
import server.models.Playlist;

public class HandlerPlaylist {
    public static void AddPlaylist(MAMP req, ObjectOutputStream res) throws IOException {
        Playlist playlist = DAOPlaylist.add((Playlist) req.getBody());

        System.out.println("adding playlist : " + playlist);
        res.close();
    }

    public static void GetPlaylists(MAMP req, ObjectOutputStream res) throws IOException {
        ArrayList<Playlist> playlists = DAOPlaylist.find();
        res.writeObject(playlists);
        res.flush();
        res.close();
    }

    public static void GetPlaylist(MAMP req, ObjectOutputStream res) throws IOException {
        Playlist playlist = DAOPlaylist.find((int) req.getBody());
        res.writeObject(playlist);
        res.flush();
        res.close();
    }
}
