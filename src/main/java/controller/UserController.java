package controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import model.User;
import network.UDPListener;
import network.UDPSender;

/**
 * This class represents the user controller.
 * It manages the sending and receive of UDP messages.
 * It allows to have a static attribute of the user using the application.
 */
public class UserController {
    private static final User myUser = new User(getLocalIP()); // Personal user
    private static ArrayList<User> listOnline = new ArrayList<>(); // List of all users online
    public static UDPListener udpListener; // UDPListener

    /**
     * PSEUDO : "PSEUDO|IP:ip|Pseudo:pseudo"
     * CONNECT :    "CONNECT|IP:ip|Pseudo:pseudo"
     * DISCONNECT : "DISCONNECT|IP:ip|Pseudo:pseudo"
     * ASK_USER_LIST : "ASK_USER_LIST|IP:ip|Pseudo:pseudo"
     * USER_LIST : "USER_LIST|IP:ip|Pseudo:pseudo|IP:ip|Pseudo:pseudo|..."
     */
    public enum TypeMsg {
       PSEUDO, CONNECT, DISCONNECT,ASK_USER_LIST, USER_LIST
    }

    /**
     * Constructor
     */
    private UserController(){
    }

    /**
     * Initialize the user controller
     * It set the port of the user.
     * It creates a new UDP listener.
     * It starts the UDP listener.
     */
    public static void initialize(){
        myUser.setPort(1234); //unique port for the user
        udpListener = new UDPListener(myUser.getPort());
        udpListener.start();
    }

    /**
     * Send a broadcast message to send the pseudo of the user.
     * It sends the typeMsg PSEUDO.
     * @param pseudo , pseudo to check
     */
    public static void sendPseudo(String pseudo){
        // Generate a String with the type of message and user information
        String msg = TypeMsg.PSEUDO+"|IP:" + myUser.getIP() + "|Pseudo:" + pseudo;
        UDPSender.sendBroadcast(msg,myUser.getPort());
    }

    /**
     * Send a broadcast message to inform that the user is connected.
     * It sends the typeMsg CONNECT.
     */
    public static void sendConnect(){
        // Generate a String with the type of message and the user information
        String msg = TypeMsg.CONNECT+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        UDPSender.sendBroadcast(msg, myUser.getPort());   
    }

    /**
     * Send a broadcast message to inform that the user is disconnected.
     * It sends the typeMsg DISCONNECT.
     */
    public static void sendDisconnect(){
        // Generate a String with the type of message and the user information
        String msg = TypeMsg.DISCONNECT+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        UDPSender.sendBroadcast(msg, myUser.getPort());
        SessionController.closeAllSessions();
        listOnline.clear();
        setMyUser(null);
    }

    /**
     * Send a broadcast message to ask the list of all users online.
     * It sends the typeMsg ASK_USER_LIST.
     */
    public static void askUserList() {
        // Generate a String with the type of message and the user information
        String msg = TypeMsg.ASK_USER_LIST+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        UDPSender.sendBroadcast(msg, myUser.getPort());
    }

    /**
     * Send a broadcast message to send the list of all users online.
     * It sends the typeMsg USER_LIST.
     */
    public static void sendUserList(String ip){
        StringBuilder msg = new StringBuilder(TypeMsg.USER_LIST + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo());
        for (User user : listOnline){
            msg.append("|").append(user.getIP()).append(":").append(user.getPseudo());
        }
        System.out.println("Send of the Userlist : " + msg);
        UDPSender.sendUDP(msg.toString(), myUser.getPort(),ip);
    }

    /**
     * Treat the message received.
     * It checks the type of message and calls the corresponding method.
     * @param msg , message received
     */
    public static void informationTreatment(String msg){
        String[] splitedMsg = msg.split("\\|");

        // filtration of the message
        String[] typeMsgString = {"PSEUDO","CONNECT","DISCONNECT","ASK_USER_LIST","USER_LIST"};

        //if the type of message is not in the list, we drop the message
        if (!Arrays.asList(typeMsgString).contains(splitedMsg[0])) return;

        TypeMsg type = TypeMsg.valueOf(splitedMsg[0]);
        String fullIP = splitedMsg[1];
        String IP = fullIP.split(":")[1];
        String fullPseudo = splitedMsg[2];
        String pseudo = fullPseudo.split(":")[1];

        // if the message is from the user himself, we drop the message
        if(IP.equals(myUser.getIP())){
           return;
        }

        switch(type){

            // if the message is a pseudo changed, we check update the pseudo of the user
            case PSEUDO:
                updateUser(IP, pseudo);
                break;

            // if the message is a connection, we add the user to the list of online users
            case CONNECT:
                listOnline.add(new User(IP,pseudo));
                break;

            // if the message is a disconnection, we remove the user from the list of online users
            case DISCONNECT:
                pseudo = fullPseudo.split(":")[1];
                listOnline.remove(getUserByPseudo(pseudo));
                break;

            // if the message is a request for the list of online users, we send the list of online users
            case ASK_USER_LIST:
                if(myUser.getPseudo() != null) {
                    sendUserList(IP);
                }
                break;

            // if the message is a list of online users, we update the list of online users
            case USER_LIST:
                for (int i = 3; i < splitedMsg.length; i++){
                    String[] user = splitedMsg[i].split(":");
                    if (pseudoNotPresent(user[1])){
                        listOnline.add(new User(user[0],user[1]));
                    }
                }
                break;

            default:
                break;
        }

    }

    /**
     * Check if the pseudo is not already present in the list of online users.
     * @param pseudo , pseudo to check
     * @return true if the pseudo is not present, false otherwise
     */
    public static boolean pseudoNotPresent(String pseudo){
        for (User user : listOnline){
            if (user.getPseudo().equals(pseudo)){
                return false;
            }
        }
        return true;
    }

    /**
     * Find the user in the listOnline with the corresponding pseudo
     * @param pseudo , the pseudo of the user searched
     * @return the user with the corresponding pseudo
     */
    public static User getUserByPseudo(String pseudo) {
        for (User user : listOnline) {
            if (user.getPseudo().equals(pseudo)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update the user pseudo in the listOnline
     * @param IP , the IP of the user to update
     * @param pseudo , the new pseudo of the user
     */
    public static void updateUser(String IP , String pseudo) {
        for (User userInList : listOnline) {
            if (Objects.equals(userInList.getIP(), IP)) {
                System.out.println("Pseudo " + userInList.getPseudo() + " changed to " + pseudo);
                userInList.setPseudo(pseudo);
            }
        }
    }

    /**
     * This method is used to get the local IP address of the computer
     * @return String localIP
     */
    public static String getLocalIP(){
        String localIP = null;

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress current_addr = addresses.nextElement();
                    if (current_addr.isLoopbackAddress()) continue;
                    localIP = current_addr.getHostAddress();
                    if (localIP.contains("192.168.56.")) continue;
                    if (localIP.contains(":")) continue;
                    return localIP;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return localIP;
    }

    /**
     * Get a user from the listOnline with his IP
     * @param ip , the ip of the user searched
     * @return the user with the corresponding IP
     */
    public static User getUserByIP(String ip){
        for (User user : listOnline){
            if (user.getIP().equals(ip)){
                return user;
            }
        }
        return null;
    }

    /**
     * Add the myUser object to the index 0 of the listOnline
     */
    public static void addMyUser(){
        listOnline.add(0,myUser);
    }

    /**
     * Update the myUser object from the listOnline
     */
    public static void updateMyUser() {
        listOnline.set(0,myUser);
    }

    /**
     * Call the method to close the udpListener
     */
    public static void close(){
        udpListener.closeSocket();
    }

    /**
     * Setter for myUser
     * @param pseudo , the pseudo of myUser
     */
    public static void setMyUser(String pseudo){
        myUser.setPseudo(pseudo);
    }

    /**
     * Getter for myUser
     * @return myUser
     */
    public static User getMyUser(){
        return myUser;
    }

    /**
     * Getter for listOnline
     * @return listOnline
     */
    public static List<User> getListOnline(){
        return listOnline;
    }
}
