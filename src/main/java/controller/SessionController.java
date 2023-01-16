package controller;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import database.DatabaseManager;
import model.*;
import network.*;

/**
 * This class represents the session controller.
 *
 */
public class SessionController {

    public static final int PORT = 6789;
    private static TCPListener tcpListener;
    //table of all the sessions sockets
    public static ArrayList<TCPSession> sessionsList;

    /**
     * Constructor
     */
    public SessionController(){

        
    }
    
    /**
     * Initialise le controller et crée un listener TCP qui va permettre de créer des sessions TCP.
     * A appeler lorsqu'on passe a l'ecran de chat.
     */
    public static void initialize(){
        //start TCP listener
        tcpListener = new TCPListener(PORT);
        sessionsList = new ArrayList<TCPSession>();
    }
    /**
     * Create a new TCP session with the other user.
     * A appeler lorsque l'on clique sur un utilisateur de la liste.
     * Un thread va etre lancé pour ecouter les messages de l'autre utilisateur a partir de la classe TCPSession.
     * Called by FrameController.
     * @param userDist
     */
    public static void createSession(User userDist){
        
        if(UserController.getListOnline().contains(userDist)){
            TCPSession session = new TCPSession(userDist);
            sessionsList.add(session);
        }
        else{
            System.out.println("User not online");
        }
    }

    /**
     * This method is called when a new session is created with us by another user.
     * Called by the TCPListener class.
     * @param link
     */
    public static void sessionCreated(Socket link){
        TCPSession session = new TCPSession(link);
        sessionsList.add(session);
    }


    /**
     * This method is using the getHistory method from the DatabaseManager class.
     * It returns the history of the conversation between the two users into an ArrayList of Message.
     * 
     * @param otherUser
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

    public TCPSession getSessionWithAdress(String ipString){
        InetAddress ip;
        try {
            ip = InetAddress.getByName(ipString);
            for(TCPSession session : sessionsList){
                if(session.getSocket().getInetAddress().equals(ip)){
                    return session;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void sendMessage(String msg){

    }

    public void messageNotSend(){

    }

    public void receiveMessage() {

    }

    

    
}