package controller;

import model.User;
import network.UDPListener;
import network.UDPSender;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * This class represents the user controller.
 * Controle la reception et l'envoi de message udp  en fonction du besoin
 * permet egalement d'avoir un attribut static de l'utilisateur utilisant l'application
 */
public class UserController {
    private static User myUser = new User(getLocalIP()); //Personal user
    private static ArrayList<User> listOnline = new ArrayList<User>(); //List of all users online
    public static UDPListener udpListener; //UDPListener
    /**
     * ASK_PSEUDO : "ASK_PSEUDO|ID:id|IP:ip|Pseudo:pseudo"
     * PSEUDO_OK :  "PSEUDO_OK|ID:id|IP:ip|MyPseudo:pseudo|Pseudo:pseudoVenantDEtreVerifie"
     * PSEUDO_OK :  "PSEUDO_OK|ID:id|IP:ip|MyPseudo:pseudo|Pseudo:pseudoVenantDEtreVerifie"
     * CONNECT :    "CONNECT|ID:id|IP:ip|Pseudo:pseudo"
     * DISCONNECT : "DISCONNECT|ID:id|IP:ip|Pseudo:pseudo"
     */
    public enum TypeMsg {
        ASK_PSEUDO, PSEUDO_OK, PSEUDO_NOT_OK, CONNECT, DISCONNECT, TEST
    }


    /**
     * Constructor
     */
    //TODO : passer en statique avec un initialize
    private UserController(){
    }

    /**
     * Initialize the user controller
     */
    public static void initialize(){
        myUser.setPort(1234);//UNIQUE INITIALISATION DU PORT LOCAL
        udpListener = new UDPListener(myUser.getPort());
        udpListener.start();
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
        setMyUser(pseudo);
        sendPseudo(pseudo);
    }

    /**
     * Send a broadcast message to ask if the pseudo is available
     * @param pseudo
     * @throws SocketException
     */
    private static void sendPseudo(String pseudo){

        // Generate a String with the type of message and user informations
        String msg = TypeMsg.ASK_PSEUDO+"|IP:" + myUser.getIP() + "|Pseudo:" + pseudo;
        //System.out.println(msg);
        
        UDPSender.sendBroadcast(msg,myUser.getPort());
        
    }

    //TODO : Observer la coherence des public private static etc sur l'ensemble du code
    private static void sendPseudoResponse(String pseudo, String ip, Boolean isValid){
        String msg;
        if (isValid){
            msg = TypeMsg.PSEUDO_OK+"|IP:" + myUser.getIP() +"|MyPseudo:"+getMyUser().getPseudo()+"|Pseudo:" + pseudo;
        }else{
            msg = TypeMsg.PSEUDO_NOT_OK+"|IP:" + myUser.getIP() +"|MyPseudo:" +getMyUser().getPseudo()+"|Pseudo:" + pseudo;

        }
        System.out.println(msg);
        UDPSender.sendUDP(msg,myUser.getPort(),ip);
        

    }

    private static void sendConnect(){
        // Generate a String with the type of message and the user informations
        String msg = TypeMsg.CONNECT+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msg);
        UDPSender.sendBroadcast(msg, myUser.getPort());   
    }

    public static void sendDisconnect(){
        // Generate a String with the type of message and the user informations
        String msg = TypeMsg.DISCONNECT+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msg);
        UDPSender.sendBroadcast(msg, myUser.getPort());
        }



    // RECEIVE INFORMATIONS

    //TODO : A tester
    public static void informationTreatment(String msg){
        String[] splitedMsg = msg.split("\\|");
        TypeMsg type = TypeMsg.valueOf(splitedMsg[0]);
        String fullIP;
        String IP;
        String fullPseudo;
        String pseudo;

        switch(type){
            case ASK_PSEUDO:
                fullIP = splitedMsg[1];
                IP = fullIP.split(":")[1];
                fullPseudo = splitedMsg[2];
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

            case PSEUDO_OK:
                fullIP = splitedMsg[1];
                IP = fullIP.split(":")[1];
                fullPseudo = splitedMsg[2];
                pseudo = fullPseudo.split(":")[1];
                System.out.println("PSEUDO_OK");
                if (listOnline.isEmpty()){
                    listOnline.add(0,new User(IP,getMyUser().getPseudo()));//On s'ajoute en tête de liste
                    sendConnect();
                    System.out.println("Connecté");
                    //TODO : ouvrir la fenetre de discussion via le FrameController
                }
                listOnline.add(new User(IP,pseudo));
                System.out.println(listOnline);

                break;

            case PSEUDO_NOT_OK:
                System.out.println("PSEUDO_NOT_OK");
                //TODO : afficher un message d'erreur via le FrameController, pseudo deja pris, recommencer

                break;

            case CONNECT:
                fullIP = splitedMsg[1];
                IP = fullIP.split(":")[1];
                fullPseudo = splitedMsg[2];
                pseudo = fullPseudo.split(":")[1];
                if (!listOnline.contains(new User(IP,pseudo))){
                    listOnline.add(new User(IP,pseudo));
                }
                //TODO : update the list of online users via the FrameController
                break;
            case DISCONNECT:
                fullIP = splitedMsg[1];
                IP = fullIP.split(":")[1];
                fullPseudo = splitedMsg[2];
                pseudo = fullPseudo.split(":")[1];
                if (listOnline.contains(new User(IP,pseudo))){
                    listOnline.remove(new User(IP,pseudo));
                }
                //TODO : update the list of online users via the FrameController
                break;

            case TEST:
                System.out.println("TEST Message received");
                System.out.println(msg);
                break;
            default:
                System.out.println("Type de message non reconnu");
                System.out.println(msg);
                break;
        }

    }

    /**
     * Find the user in the listOnline with the pseudo
     * @param pseudo
     */
    public static User findUser(String pseudo) {
        for (User user : listOnline) {
            if (user.getPseudo().equals(pseudo)) {
                return user;
            }
        }
        return null;
    }

    /**
     * This method is used to get the local IP address of the computer
     * @return String localIP
     */
    public static String getLocalIP(){
        String localIP = null;
        try {
            //I want the ip adress of the computer on the network so the others can connect to me
            //I don't want the loopback address or the virtual address
            //I don't want the address of the virtualbox
            //I don't want the address of the network card

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
                    System.out.println(localIP);
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
     * @param String ip
     */
    public static User getUserByIP(String ip){
        for (User user : listOnline){
            if (user.getIP().equals(ip)){
                return user;
            }
        }    
        return null;
    }

    public static void close(){
        sendDisconnect();
        udpListener.closeSocket();
        //pas besoin de fermer le socket udpSender, il est fermé automatiquement apres un envoi de message
    }

    // ===========================
    // GETTERS AND SETTERS
    // ===========================

    /**
     * Setter for myUser
     * @param pseudo
     */
    public static void setMyUser(String pseudo){
        myUser = new User(getLocalIP(), pseudo);
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
    public void setListOnline(ArrayList<User> list){
        listOnline = list;
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
        listOnline.add(new User(getLocalIP(), "ISPEX"));
        for (int i = 0; i < 15; i++) {
            listOnline.add(new User("192.168.1." + i, "PSEUDO-" + i));
        }
    }

    //TODO : Just for test, to delete
    public static void showListOnline(){
        for (User user : listOnline){
            System.out.println(user.getPseudo() + " " + user.getIP());
        }
    }

}
