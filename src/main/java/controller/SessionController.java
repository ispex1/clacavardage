package controller;

import java.util.ArrayList;

import database.DatabaseManager;
import model.*;
import network.*;

/**
 * This class represents the session controller.
 * 
 */
public class SessionController {

    /**
     * Constructor
     */
    public SessionController(){

    }

     /**
     * This method is using the getHistory method from the DatabaseManager class.
     * It returns the history of the conversation between the two users into an ArrayList of Message.
     * 
     * @return history
     */
    public ArrayList<Message> getHistory(User otherUser){
        return DatabaseManager.getHistory(otherUser.getIP());
    }

    /**
     * This method is using the insertMessage method from the DatabaseManager class.
     * It inserts the message in the database.
     * 
     * @param msg
     */
    public static void archiveMsg(Message msg, User otherUser){
        DatabaseManager db = new DatabaseManager();
        db.insertMessage(otherUser.getIP(), msg);
    }

    /**
     * This method is using the deleteMessage method from the DatabaseManager class.
     * It deletes the message in the database.
     * 
     * @param msg
     */
    public void deleteMsg(Message msg, User otherUser){
        String ipOther = otherUser.getIP();
        int index = DatabaseManager.getIndexFromMsg(ipOther, msg);
        DatabaseManager.deleteMessage(ipOther, index);
    }

    public void sendMessage(String msg){

    }

    public void messageNotSend(){

    }

    public void receiveMessage() {

    }

    public void newSession(){

    }

    public void archiveMsg(){

    }
}