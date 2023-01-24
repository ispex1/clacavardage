package controller;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import database.DatabaseManager;
import model.Message;
import model.User;
import network.TCPListener;
import network.TCPSession;

/**
 * This class represents the session controller.
 * It manages the sending and receive of TCP messages.
 * It allows to have a static attribute of the session using the application.
 * It also manages the list of all the sessions.
 */
public class SessionController {

    public static final int PORT = 6789; // Port for the TCP connection
    public static TCPListener tcpListener; // TCPListener
    public static ArrayList<TCPSession> sessionsList; // List of all the active sessions


    /**
     * Constructor
     */
    public SessionController(){

        
    }
    
    /**
     * This method initializes the session controller.
     * It creates a new TCP listener.
     * It creates a new sessions list.
     * It starts the TCP listener.
     */
    public static void initialize() {
        //start TCP listener
        tcpListener = new TCPListener(PORT);
        sessionsList = new ArrayList<>();

    }

    /**
     * Create a new TCP session with the other user.
     * It adds the session to the sessions list.
     * Called by the main frame controller when a user is selected.
     * @param userDist , the other user
     */
    public static void createSession(User userDist){
        
        if(UserController.getListOnline().contains(userDist)){
            TCPSession session = new TCPSession(userDist);
            sessionsList.add(session);
            System.out.println("< SESSION CONTROLLER > : SESSION CREATED WITH " + userDist.getPseudo());
        }
        else{
            System.out.println("< SESSION CONTROLLER > : USER NOT ONLINE");
        }
    }

    /**
     * This method is called when a new session is created with us by another user.
     * Called by the TCPListener class.
     * @param link , the socket of the new session
     */
    public static void sessionCreated(Socket link){
        TCPSession session = new TCPSession(link);
        sessionsList.add(session);
    }

    /**
     * This method is called when a message is received.
     * It calls the method to add the message to the database.
     * It calls the method to update the chat pane.
     * @param message , the message received
     * @param userDist , the user who will receive the message
     */
    public static void sendMessage(Message message,User userDist){
        for(TCPSession session : sessionsList){
            if(session.getUserDist().equals(userDist)){
                session.sendMessage(message.toString());
                DatabaseManager.insertMessage(userDist.getIP(), message);
            }
        }
    }

    /**
     * This method is used to get the Session object corresponding to the user.
     * @param userDist , the user
     * @return the session object
     */
    public static TCPSession getSessionWithUser(User userDist){
        for(TCPSession session : sessionsList){
            if(session.getUserDist().equals(userDist)){
                return session;
            }
        }
        return null;
    }

    /**
     * This method is used to get the Session object corresponding to the IP address of a user.
     * @param ipString , the IP address of the user
     * @return the session object
     */
    public static TCPSession getSessionWithAddress(String ipString){
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
     * This method is used to get the Session object corresponding to the pseudo of a user.
     * @param pseudo , the pseudo of the user
     * @return the session object
     */
    public static TCPSession getSessionWithPseudo(String pseudo){
        for(TCPSession session : sessionsList){
            if(session.getUserDist().getPseudo().equals(pseudo)){
                return session;
            }
        }
        return null;
    }

    /**
     * This method check if a session is already created with the user.
     * @param userDist , the user
     * @return true if the session is already created, false otherwise
     */
    public static boolean isSessionWith(User userDist){
        return getSessionWithUser(userDist) != null;
    }

    /**
     * This method is used to close a session.
     * It calls the method to close the socket.
     * It removes the session from the sessions list.
     * @param session , the session to close
     */
    public static void closeSession(TCPSession session){
        session.closeSession();
        sessionsList.remove(session);
    }

    /**
     * This method is used to close a session.
     * It get the session corresponding to the user.
     * It calls the method to close the socket.
     * It removes the session from the sessions list.
     * @param userDist , the user
     */
    public static void closeSession(User userDist){
        TCPSession session = getSessionWithPseudo(userDist.getPseudo());
        assert session != null;
        session.closeSession();
        sessionsList.remove(session);
    }

    /**
     * This method is used to close all the sessions.
     * It loops on the sessions list and calls the method to close the socket.
     * It clears the sessions list.
     */
    public static void closeAllSessions(){
        for(TCPSession session : sessionsList){
            session.closeSession();
        }
        sessionsList.clear();
    }

    /**
     * This method is used to close the TCP listener.
     * It calls the method to close all the sessions
     * It calls the method to close the TCP listener.
     */
    public static void close(){
        closeAllSessions();
        tcpListener.closeListener();
    }
}