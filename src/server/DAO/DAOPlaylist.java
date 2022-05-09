package server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import server.db.DBConnection;
import server.models.Playlist;

public class DAOPlaylist {
    public static Playlist add(Playlist playlist) {
        Connection conn = DBConnection.getConnection();
        String req = "Insert into playlist (name) values (?);";
        try {
            PreparedStatement ps = conn.prepareStatement(req, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, playlist.name);
            ps.executeUpdate();
            ResultSet res = ps.getGeneratedKeys();
            if (res.next()) {
                playlist._id = res.getInt(1);
            }
            return playlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Playlist> find() {
        Connection conn = DBConnection.getConnection();
        String req = "select * from playlist";
        ArrayList<Playlist> result = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(req);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                result.add(new Playlist(res.getInt(1), res.getString(2)));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Playlist find(int _id) {
        Connection conn = DBConnection.getConnection();
        String req = "select * from playlist where _id = ?;";
        Playlist playlist;
        try {
            PreparedStatement ps = conn.prepareStatement(req, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(0, _id);
            ps.executeQuery();
            ResultSet res = ps.getGeneratedKeys();
            if (res.next()) {
                playlist = new Playlist(res.getInt(1), res.getString(2));
                req = "select song.name from song s, playlist p where p._id = s.playlist_id and p._id = ?";
                ps = conn.prepareStatement(req, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(0, _id);
                ps.executeQuery();
                res = ps.getGeneratedKeys();
                if (res.next()) {
                    playlist.songs.add(res.getString(1));
                }
                return playlist;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
