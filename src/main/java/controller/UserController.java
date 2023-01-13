package controller;

import model.User;
import network.UDPListener;
import network.UDPSender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the user controller.
 * Controle la reception et l'envoi de message udp  en fonction du besoin
 * permet egalement d'avoir un attribut static de l'utilisateur utilisant l'application
 */
public class UserController {
    private static User myUser; //Personal user
    private static List<User> listOnline = Collections.synchronizedList(new ArrayList<User>()); //List of all users online
    public static UDPListener udpListener; //UDPListener
    public static final UDPSender udpSender = new UDPSender() ; //UDPSender
    
    /**
     * ASK_PSEUDO : "ASK_PSEUDO|ID:id|IP:ip|Pseudo:pseudo"
     * PSEUDO_OK :  "PSEUDO_OK|ID:id|IP:ip|MyPseudo:pseudo|Pseudo:pseudoVenantDEtreVerifie"
     * PSEUDO_OK :  "PSEUDO_OK|ID:id|IP:ip|MyPseudo:pseudo|Pseudo:pseudoVenantDEtreVerifie"
     * CONNECT :    "CONNECT|ID:id|IP:ip|Pseudo:pseudo"
     * DISCONNECT : "DISCONNECT|ID:id|IP:ip|Pseudo:pseudo"
     */
    public enum TypeMsg {
        ASK_PSEUDO, PSEUDO_OK, PSEUDO_NOT_OK, CONNECT, DISCONNECT
    }


    /**
     * Constructor
     */
    public UserController(String Pseudo){
        setMyUser(Pseudo);
        myUser.setPort(1234);//UNIQUE INITIALISATION DU PORT LOCAL
        myUser.setIP(getLocalIP());
        myUser.setID(getMacAddress());//TODO : A changer pour avoir l'ID de l'utilisateur, est qu'on garde l'adresse mac ?
        udpListener = new UDPListener(myUser.getPort());
        //setListOnline(listOnline);
    }

    // SEND INFORMATIONS

    //pour verifier si tout les pseudos sont dispo: 
    // 1) on envoie un message en broadcast avec le pseudo + info
    // 2) on attend jusqu'a recevoir un reponse pseudo
    // 3) les gens recevant le ask pseudo regarde si le pseudo est dans la liste
    //      -> si oui reponse reponse NOT OK (tout le monde est sensé renvoyer not ok)
    //      -> si non reponse OK + son pseudo 
    // 4) sur reception d'un PSEUDO_OK
    //      -> cela signifie que le pseudo n'est pas sur le reseau
    //      -> on ajoute son pseudo en tete de liste(on verifie avant si la liste est vide, pour ne le faire qu'une seule fois)
    //      -> on ajoute le pseudo recu a la liste (elle va se construire au fur et a mesure de la reception des OK_PSEUDO)
    //      -> on envoie un message en broadcast pour dire qu'on est connecte
    // 5) sur reception d'un PSEUDO_NOT_OK
    //      -> on affiche un message d'erreur, pseudo deja pris, recommencer


    /**
     * Public function to use to start to ask if the pseudo is available
     * @param pseudo
     */
    public static void askPseudo(String pseudo){
        udpListener.run();
        sendPseudo(pseudo);
    }

    /**
     * Send a broadcast message to ask if the pseudo is available
     * @param pseudo
     * @throws SocketException
     */
    private static void sendPseudo(String pseudo){

        // Generate a String with the type of message and user informations
        String msg = TypeMsg.ASK_PSEUDO+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + pseudo;
        //System.out.println(msg);
        try {
            udpSender.sendBroadcast(msg,myUser.getPort());
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //TODO : Observer la coherence des public private static etc sur l'ensemble du code
    private static void sendPseudoResponse(String pseudo, String ip, Boolean isValid){
        String msg;
        if (isValid){
            msg = TypeMsg.PSEUDO_OK+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() +"|MyPseudo:"+getMyUser().getPseudo()+"|Pseudo:" + pseudo;
        }else{
            msg = TypeMsg.PSEUDO_NOT_OK+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() +"|MyPseudo:" +getMyUser().getPseudo()+"|Pseudo:" + pseudo;
            
        }
        System.out.println(msg);

    
        try {
            udpSender.sendUDP(msg,myUser.getPort(),ip);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private static void sendConnect(){
        // Generate a String with the type of message and the user informations
        String msg = TypeMsg.CONNECT+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msg);

        try {
            udpSender.sendBroadcast(msg, myUser.getPort());
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void sendDisconnect(){
        // Generate a String with the type of message and the user informations
        String msg = TypeMsg.DISCONNECT+"|ID:" + myUser.getID() + "|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msg);

        try {
            udpSender.sendBroadcast(msg, myUser.getPort());
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    

    // RECEIVE INFORMATIONS
    
    //TODO: Create only one function to receive all the informations with some cases

    public static void informationTreatment(String msg){
        String[] splitedMsg = msg.split("\\|");
        String type = splitedMsg[0];
        String fullID;
        String ID;
        String fullIP;
        String IP;
        String fullPseudo;
        String pseudo;


        switch(type){
            case "ASK_PSEUDO":
                fullIP = splitedMsg[2];
                IP = fullIP.split(":")[1];
                fullPseudo = splitedMsg[3];
                pseudo = fullPseudo.split(":")[1];

                boolean isPseudoValid=true;
                for (User user : listOnline){
                    if (user.getPseudo().equals(pseudo)){
                        isPseudoValid=false;
                        sendPseudoResponse(pseudo, IP, false);
                        break;
                    }
                }
                if (isPseudoValid){    
                    sendPseudoResponse(pseudo,IP, true);
                }           

                break;
            
            case "PSEUDO_OK":
                fullID = splitedMsg[1];
                ID = fullID.split(":")[1];
                fullIP = splitedMsg[2];
                IP = fullIP.split(":")[1];
                fullPseudo = splitedMsg[3];
                pseudo = fullPseudo.split(":")[1];
                System.out.println("PSEUDO_OK");
                if (listOnline.isEmpty()){
                    listOnline.add(0,new User(ID,IP,getMyUser().getPseudo()));//On s'ajoute en tête de liste
                    sendConnect();
                    System.out.println("Connecté");
                    //TODO : ouvrir la fenetre de discussion via le FrameController
                }
                listOnline.add(new User(ID,IP,pseudo));
                System.out.println(listOnline);

                break;

            case "PSEUDO_NOT_OK":
                System.out.println("PSEUDO_NOT_OK");
                udpListener.closeSocket();
                //TODO: afficher un message d'erreur via le FrameController, pseudo deja pris, recommencer

                break;

            //TODO: update listOnline when a user connect or disconnect
            case "CONNECT":
                fullID = splitedMsg[1];
                ID = fullID.split(":")[1];
                fullIP = splitedMsg[2];
                IP = fullIP.split(":")[1];
                fullPseudo = splitedMsg[3];
                pseudo = fullPseudo.split(":")[1];
                if (!listOnline.contains(new User(ID,IP,pseudo))){
                    listOnline.add(new User(ID,IP,pseudo));
                }
                //TODO: update the list of online users via the FrameController
                break;
            case "DISCONNECT":
                fullID = splitedMsg[1];
                ID = fullID.split(":")[1];
                fullIP = splitedMsg[2];
                IP = fullIP.split(":")[1];
                fullPseudo = splitedMsg[3];
                pseudo = fullPseudo.split(":")[1];
                if (listOnline.contains(new User(ID,IP,pseudo))){
                    listOnline.remove(new User(ID,IP,pseudo));
                }
                //TODO: update the list of online users via the FrameController
                break;
            default:
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
    //TODO :Remove MAC adress and change DB
    public static String getMacAddress(){
        String macAddress = null;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            byte[] mac = networkInterface.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            if (mac != null) {
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                macAddress = sb.toString();
            }
            else{
                macAddress = "00-00-00-00-00-00";
            }

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
    public List<User> getListOnline(){
        return this.listOnline;
    }

}
