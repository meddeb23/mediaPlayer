package server.routes;

import java.io.IOException;
import java.io.ObjectOutputStream;

import MAMP.MAMP;
import server.models.Msg;

public class HandleMessages {
    public static void run(MAMP req, ObjectOutputStream res) {
        Msg recMsg = (Msg) req.getBody();
        System.out.println(recMsg);
        if (recMsg.getMsg().equals("hello")) {
            Msg msg = new Msg("hello back", "server");
            try {
                res.writeObject(msg);
                res.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            res.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
