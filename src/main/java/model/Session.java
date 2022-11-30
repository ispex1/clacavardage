package model;

import java.util.ArrayList;

import database.DatabaseManager;
import model.user.*;

public class Session {
    private static String socket;
    private static User receiver;
    private static int idSession;

    public Session(String socket, User receiver, int idSession){
        this.setSocket(socket);
        this.setReceiver(receiver);
        this.setIdSession(idSession);
    }

    // use to show the history in the frame session
    // TO DO
    /*
    public ArrayList<Message> getHistory(){
        ArrayList<Message> history;
        return history;
    }
    */

    // save message in the bdd
    public void archiveMsg(Message msg){
        DatabaseManager.archiveMessage(this.idSession, msg);
    }


    // GETTER & SETTER

    public void setSocket(String socket) {
        Session.socket = socket;
    }
    public static String getSocket() {
        return socket;
    }

    public void setIdSession(int idSession) {
        Session.idSession = idSession;
    }
    public static int getIdSession() {
        return idSession;
    }

    public void setReceiver(User receiver) {
        Session.receiver = receiver;
    }
    public static User getReceiver() {
        return receiver;
    }
}
