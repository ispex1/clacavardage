/**
 * =================================================================================================================
<<<<<<< HEAD
 *
 * CLASSE A SUPRIMER
 * CETTE CLASSE A FUSIONNE AVEC TCP SESSION ET EST DISPONIBLE DESORMAIS DANS LE PACKAGE network
 * TODO : Avant de supprimer la classe, verifier que toutes les methodes et attributs ont bien ete remplace dans TCPSession
 *
 *
=======
 * 
 * CLASSE A SUPRIMER
 * CETTE CLASSE A FUSIONNE AVEC TCP SESSION ET EST DISPONIBLE DESORMAIS DANS LE PACKAGE network
 * TODO : Avant de supprimer la classe, verifier que toutes les methodes et attributs ont bien ete remplace dans TCPSession
 * 
 * 
>>>>>>> 441eb797adb2cef241148bb8dd136b13dd2b261e
 * =================================================================================================================
 */

package model;

import java.util.ArrayList;

import database.DatabaseManager;

/**
 * This class represents a session.
 * A session is created for each conversation between two users connected.
 * It contains the socket, the other user and the id of the session.
 *
 */

public class Session {
    private String socket; // Socket of the session
    private User otherUser; // Other user of the session
    private int idSession; // Id of the session

    /**
     * Constructor
     * @param socket
     * @param otherUser
     * @param idSession
     */
    public Session(String socket, User otherUser, int idSession){
        this.setSocket(socket);
        this.setOtherUser(otherUser);
        this.setIdSession(idSession);
    }

    /**
     * This method is using the getHistory method from the DatabaseManager class.
     * It returns the history of the conversation between the two users into an ArrayList of Message.
     *
     * @return history
     */
    public ArrayList<Message> getHistory(){
        return DatabaseManager.getHistory(this.otherUser.getIP());
    }

    /**
     * This method is using the insertMessage method from the DatabaseManager class.
     * It inserts the message in the database.
     *
     * @param msg
     */
    public void archiveMsg(Message msg){
        DatabaseManager db = new DatabaseManager();
        db.insertMessage(this.getOtherUser().getIP(), msg);
    }

    /**
     * This method is using the deleteMessage method from the DatabaseManager class.
     * It deletes the message in the database.
     *
     * @param msg
     */
    public void deleteMsg(Message msg){
        String ipOther = this.otherUser.getIP();
        int index = DatabaseManager.getIndexFromMsg(ipOther, msg);
        DatabaseManager.deleteMessage(ipOther, index);
    }

    /**
     * Setter for socket
     * @param socket
     */
    public void setSocket(String socket) {
        this.socket = socket;
    }

    /**
     * Getter for socket
     * @return socket
     */
    public String getSocket() {
        return socket;
    }

    /**
     * Setter for idSession
     * @param idSession
     */
    public void setIdSession(int idSession) {
        this.idSession = idSession;
    }

    /**
     * Getter for idSession
     * @return idSession
     */
    public int getIdSession() {
        return idSession;
    }

    /**
     * Setter for otherUser
     * @param otherUser
     */
    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }

    /**
     * Getter for otherUser
     * @return otherUser
     */
    public User getOtherUser() {
        return otherUser;
    }
}
