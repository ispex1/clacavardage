package controller;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import database.DatabaseManager;
import model.*;
import network.*;
import view.MainFrame;

/**
 * This class represents the session controller.
 *
 */
public class SessionController {

    public static final int PORT = 6789;
    public static TCPListener tcpListener;
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
    public static void initialize() {
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
            System.out.println("< SESSION CONTROLLER > : SESSION CREATED WITH " + userDist.getPseudo());
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

    public static void sendMessage(Message message,User user){
        for(TCPSession session : sessionsList){
            if(session.getUserDist().equals(user)){
                session.sendMessage(message.toString());
                DatabaseManager.insertMessage(user.getIP(), message);
            }
        }
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

    public static TCPSession getSessionWithUser(User userDist){
        for(TCPSession session : sessionsList){
            if(session.getUserDist().equals(userDist)){
                return session;
            }
        }
        return null;
    }

    /**
     * This method is used to get a session from the Session list with the IP address of the other user.
     * @param ipString
     */
    public static TCPSession getSessionWithAdress(String ipString){
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

    /**
     * This method is used to get a session from the Session list with the pseudo of the other user.
     * @param pseudo
     */
    public static TCPSession getSessionWithPseudo(String pseudo){
        for(TCPSession session : sessionsList){
            if(session.getUserDist().getPseudo().equals(pseudo)){
                return session;
            }
        }
        return null;
    }

    public static boolean isSessionWith(User userDist){
        if (getSessionWithUser(userDist) != null){
            return true;
        }
        return false;
    }

    public static void closeSession(TCPSession session){
        session.closeSession();
        sessionsList.remove(session);
    }

    //TODO:change
    public static void closeSession(User user){
        System.out.println("close session with " + user.getPseudo());
        TCPSession session = getSessionWithPseudo(user.getPseudo());
        session.closeSession();
        sessionsList.remove(session);
    }

    public static void closeAllSessions(){
        for(TCPSession session : sessionsList){
            session.closeSession();
        }
        sessionsList.clear();
    }


    public static void close(){
        closeAllSessions();
        tcpListener.closeListener();
    }
}