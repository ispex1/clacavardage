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
        PSEUDO, CONNECT, DISCONNECT, MESSAGE
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
        String msg = TypeMsg.PSEUDO+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + pseudo;
        System.out.println(msg);

        //udpSender.sendBroadcast(msg);
    }

    public void connect(){
        // Generate a String with the type of message and the user informations
        String msg = TypeMsg.CONNECT+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msg);

        //udpSender.sendBroadcast(msg);
    }

    public void disconnect(){
        // Generate a String with the type of message and the user informations
        String msg = TypeMsg.DISCONNECT+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msg);

        //udpSender.sendBroadcast(msg);
    }

    public void message(String data, User receiver){
        // Generate a String with the type of message, the user informations and the txt wrote by the user
        String msg = TypeMsg.MESSAGE+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo() + "|Message:" + data;
        System.out.println(msg);

        //TODO: send the message to the receiver
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
    public void receiveMsg(String msg){
        String[] msgSplit = msg.split("\\|");
        String typeMsg = msgSplit[0];
        User chatter = new User(msgSplit[1].split(":")[1], 
                                msgSplit[2].split(":")[1], 
                                msgSplit[3].split(":")[1]);
        //String data = msgSplit[4].split(":")[1];

        if(typeMsg.equals("OK")){
            System.out.println("OK");
        }
        else {
            switch (TypeMsg.valueOf(typeMsg)) {

                case PSEUDO:
                    System.out.println("PSEUDO");
                
                    // I already use this pseudo
                    if(chatter.getPseudo().equals(myUser.getPseudo())){
                        try{
                            System.out.println("PSEUDO already used");
                            String notOK = "notOK"+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo() + "|Message:";
                            udpSender.sendUDP(notOK, udpListener.getPort(), chatter.getIP());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    // I don't use this pseudo
                    else {
                        System.out.println("PSEUDO not used");
                    }
                
                    break;
                
                case CONNECT:
                    System.out.println("CONNECT");
                    break;

                case DISCONNECT:
                    System.out.println("DISCONNECT");
                    break;

                case MESSAGE:
                    System.out.println("MESSAGE");
                    break;
                
                default :
                    break;
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
