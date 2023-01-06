package controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;

import model.User;
import network.UDPListener;
import network.UDPSender;

/**
 * This class represents the user controller.
 * 
 */
public class UserController {
    private static User myUser; //Personal user
    public ArrayList<User> listOnline = new ArrayList<User>(); //List of all users online
    public static final UDPListener udpListener = new UDPListener(1,1111); //UDPListener
    public static final UDPSender udpSender = new UDPSender() ; //UDPSender
    
    public enum TypeMsg {
        ASK_PSEUDO, CONNECT, DISCONNECT, PSEUDO_OK, PSEUDO_NOK
    }

    /**
     * Constructor
     */
    public UserController(String pseudo){
        setMyUser(pseudo);
        //setListOnline(listOnline);
    }

    // SEND INFORMATIONS

    public void pseudo(String pseudo){

        // Generate a String with the type of message and user informations
        String msgToSend = TypeMsg.ASK_PSEUDO+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + pseudo;
        System.out.println(msgToSend);

        try {
            udpSender.sendBroadcast(msgToSend,udpListener.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        // Generate a String with the type of message and the user informations
        String msgToSend = TypeMsg.CONNECT+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msgToSend);

        try {
            udpSender.sendBroadcast(msgToSend,udpListener.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        // Generate a String with the type of message and the user informations
        String msgToSend = TypeMsg.DISCONNECT+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msgToSend);

        try {
            udpSender.sendBroadcast(msgToSend,udpListener.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // RECEIVE INFORMATIONS
    
    //TODO: Create only one function to receive all the informations with some cases

    //TODO: update listOnline when a user connect or disconnect

    /**
     * This function is used to receive a message from the network
     * It split the message and do the right action depending on the type of message
     * (PSEUDO, CONNECT, DISCONNECT, MESSAGE)
     * @param msg
     */
    public void receiveMsg(String msgReceive){
        String[] msgSplit = msgReceive.split("\\|");
        TypeMsg typeMsg = TypeMsg.valueOf(msgSplit[0]);
        User otherUser = new User(msgSplit[1].split(":")[1], 
                                msgSplit[2].split(":")[1], 
                                msgSplit[3].split(":")[1]);

        switch (typeMsg) {

            case PSEUDO_OK:
                System.out.println("PSEUDO_OK");
                break;

            case ASK_PSEUDO:
                System.out.println("PSEUDO");
            
                if(otherUser.getID().equals(myUser.getID())){
                    System.out.println("I'm the sender");
                }
                else {
                    // I already use this pseudo
                    if(otherUser.getPseudo().equals(myUser.getPseudo())){
                        try{
                            System.out.println("PSEUDO already used");
                            String msgToSend = TypeMsg.PSEUDO_NOK+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
                            udpSender.sendUDP(msgToSend, udpListener.getPort(), otherUser.getIP());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    // I don't use this pseudo
                    else {
                        System.out.println("PSEUDO not used yet");
                    }
                }
                break;
            
            case CONNECT:
                System.out.println("CONNECT");
                break;

            case DISCONNECT:
                System.out.println("DISCONNECT");
                break;
            
            default :
                break;
        }
    }


    /**
     * This method is used to get the local IP address of the computer
     * @return String localIP
     */
    public static String getLocalIP(){
        String localIP = null;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            localIP = localHost.getHostAddress();
            System.out.println("IP of my system is "+localIP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localIP;
    }

    /**
     * This method is used to get the MAC address of the computer
     * @return String macAddress
     */
    public static String getMacAddress(){
        String macAddress = null;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            byte[] mac = networkInterface.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macAddress = sb.toString();
            System.out.println("MAC address of my system is "+macAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    /** 
     * Setter for myUser
     * @param pseudo
     */
    public static void setMyUser(String pseudo){
        myUser = new User(getMacAddress(), getLocalIP(), pseudo);
    }
    
    /**
     * Getter for myUser
     * @return myUser
     */
    public static User getMyUser(){
        return myUser;
    }

    /**
     * Setter for listOnline
     * @param listOnline
     */
    public void setListOnline(ArrayList<User> listOnline){
        this.listOnline = listOnline;
    }

    /**
     * Getter for listOnline
     * @return listOnline
     */
    public ArrayList<User> getListOnline(){
        return this.listOnline;
    }

}
