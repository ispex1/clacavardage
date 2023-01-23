package controller;

import model.User;
import network.UDPListener;
import network.UDPSender;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * This class represents the user controller.
 * It manages the send and receive of UDP messages.
 * It allow to have a static attribute of the user using the application.
 */
public class UserController {
    private static final User myUser = new User(getLocalIP()); //Personal user
    private static ArrayList<User> listOnline = new ArrayList<>(); //List of all users online
    public static UDPListener udpListener; //UDPListener

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
     */
    public static void initialize(){
        myUser.setPort(1234); //unique port for the user
        udpListener = new UDPListener(myUser.getPort());
        udpListener.start();
    }

    /**
     * Send a broadcast message to ask if the pseudo is available
     *
     * @param pseudo , pseudo to check
     */
    public static void sendPseudo(String pseudo){
        // Generate a String with the type of message and user information
        String msg = TypeMsg.PSEUDO+"|IP:" + myUser.getIP() + "|Pseudo:" + pseudo;
        UDPSender.sendBroadcast(msg,myUser.getPort());
    }

    public static void sendConnect(){
        // Generate a String with the type of message and the user information
        String msg = TypeMsg.CONNECT+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        UDPSender.sendBroadcast(msg, myUser.getPort());   
    }

    public static void sendDisconnect(){
        // Generate a String with the type of message and the user information
        String msg = TypeMsg.DISCONNECT+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        UDPSender.sendBroadcast(msg, myUser.getPort());
        SessionController.closeAllSessions();
        listOnline.clear();
        setMyUser(null);
        }

    public static void askUserList() {
        // Generate a String with the type of message and the user information
        String msg = TypeMsg.ASK_USER_LIST+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        UDPSender.sendBroadcast(msg, myUser.getPort());
    }

    public static void sendUserList(String ip){
        StringBuilder msg = new StringBuilder(TypeMsg.USER_LIST + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo());
        for (User user : listOnline){
            msg.append("|").append(user.getIP()).append(":").append(user.getPseudo());
        }
        System.out.println("Send of the Userlist : " + msg);
        UDPSender.sendUDP(msg.toString(), myUser.getPort(),ip);
    }

    // RECEIVE INFORMATIONS
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

        if(IP.equals(myUser.getIP())){
           return;
        }

        switch(type){
            case PSEUDO:
                updateUser(IP, pseudo);

            case CONNECT:
                listOnline.add(new User(IP,pseudo));
                break;

            case DISCONNECT:
                pseudo = fullPseudo.split(":")[1];
                listOnline.remove(getUserByPseudo(pseudo));
                break;

            case ASK_USER_LIST:
                if(myUser.getPseudo() != null) {
                    sendUserList(IP);
                }
                break;

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

    public static boolean pseudoNotPresent(String pseudo){
        for (User user : listOnline){
            if (user.getPseudo().equals(pseudo)){
                return false;
            }
        }
        return true;
    }

    /**
     * Find the user in the listOnline with the pseudo
     * @param pseudo , the pseudo of the user searched
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
     * @param IP, pseudo
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
     */
    public static User getUserByIP(String ip){
        for (User user : listOnline){
            if (user.getIP().equals(ip)){
                return user;
            }
        }
        return null;
    }

    public static void addMyUser(){
        listOnline.add(0,myUser);
    }

    public static void updateMyUser() {
        listOnline.set(0,myUser);
    }

    public static void close(){
        udpListener.closeSocket();
    }

    // ===========================
    // GETTERS AND SETTERS
    // ===========================

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

    //TODO : Just for test, to delete
    public static void testListOnline() {
        listOnline.add(new User(getLocalIP(), "Victoria"));
        for(int i = 0; i < 10; i++){
            listOnline.add(new User("192.168.56." + i, "pseudo" + i));
        }
    }
}
