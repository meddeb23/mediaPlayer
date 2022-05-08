package server.models;

import java.io.Serializable;

public class Msg implements Serializable {
    private int id;
    private String msg;
    private String sender;

    public Msg(String msg, String sender) {
        this.msg = msg;
        this.sender = sender;
    }

    public Msg(int id, String msg, String sender) {
        this.setId(id);
        this.setMsg(msg);
        this.setSender(sender);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Msg [msg=" + msg + ", sender=" + sender + "]";
    }

}
