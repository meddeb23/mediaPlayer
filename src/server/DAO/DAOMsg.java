package server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import server.db.DBConnection;
import server.models.Msg;

public class DAOMsg {
    public static Msg add(Msg msg) {
        Connection conn = DBConnection.getConnection();
        String req = "Insert into msg (msg, sender) values (?,?);";
        try {
            PreparedStatement ps = conn.prepareStatement(req, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, msg.getMsg());
            ps.setString(2, msg.getSender());
            ps.executeUpdate();
            ResultSet res = ps.getGeneratedKeys();
            if (res.next()) {
                msg.setId(res.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Msg> find() {
        Connection conn = DBConnection.getConnection();
        String req = "select * from msg;";
        ArrayList<Msg> result = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(req, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.executeQuery();
            ResultSet res = ps.getGeneratedKeys();
            if (res.next()) {
                result.add(new Msg(res.getInt(1), res.getString(2), res.getString(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
