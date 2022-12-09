package controller;

import java.util.ArrayList;

import model.User;
import network.UDPListener;
import network.UDPSender;

/**
 * This class represents the user controller.
 * 
 */
public class UserController {
    private ArrayList<User> listOnline = new ArrayList<User>(); //List of all users online
    public static final UDPListener udpListener = new UDPListener(1,1111); //UDPListener
    public static final UDPSender udpSender = new UDPSender() ; //UDPSender
    
    public enum TypeMsg {
        CONNECT, DISCONNECT, MESSAGE, PSEUDO
    }

    /**
     * Constructor
     */
    public UserController(){
        //udpListener.start();
    }

    // SEND INFORMATIONS

    public void Connect(String Pseudo){
        // Generate a String with the type of message and the pseudo
        String msg = TypeMsg.CONNECT+"|ID:" + "id" + "|Pseudo:" + Pseudo;
        System.out.println(msg);


        //udpSender.sendBroadcast();
    }





    // RECEIVE INFORMATIONS
    
    //TODO: update listOnline when a user connect or disconnect


    /**
     * Setter for listUser
     * @param listUser
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
