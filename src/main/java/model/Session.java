package model;

import java.util.ArrayList;

import database.DatabaseManager;
import model.user.*;

public class Session {
    private String socket;
    private User otherUser;
    private int idSession;

    public Session(String socket, User otherUser, int idSession){
        this.setSocket(socket);
        this.setOtherUser(otherUser);
        this.setIdSession(idSession);
    }

    public ArrayList<Message> getHistory(){
        return DatabaseManager.getHistory(this.otherUser.getIP());
    }

    public void archiveMsg(Message msg){
        DatabaseManager db = new DatabaseManager();
        db.insertMessage(this.getOtherUser().getIP(), msg);
    }

    // delete message in the bdd
    public void deleteMsg(Message msg){
        String ipOther = this.otherUser.getIP();
        int index = DatabaseManager.getIndexFromMsg(ipOther, msg);
        DatabaseManager.deleteMessage(ipOther, index);
    }


    // GETTER & SETTER

    public void setSocket(String socket) {
        this.socket = socket;
    }
    public String getSocket() {
        return socket;
    }

    public void setIdSession(int idSession) {
        this.idSession = idSession;
    }
    public int getIdSession() {
        return idSession;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }
    public User getOtherUser() {
        return otherUser;
    }
}
