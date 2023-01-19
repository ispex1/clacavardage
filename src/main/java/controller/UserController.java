package controller;

import model.User;
import network.TCPSession;
import network.UDPListener;
import network.UDPSender;
import view.OpenedChatFrame;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;


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
        ASK_PSEUDO, PSEUDO_OK, PSEUDO, CONNECT, DISCONNECT,ASK_USER_LIST, USER_LIST, TEST
    }
    //TODO AJOUTER UN TYPE DE MESSAGE USER_LIST, ELLE SERA ENVOYE EN REPONSE DE ASKPSEUDO SI LE PSEUDO EST OK, A LACCEPTATION DUN DE CES MESSAGE, SI LA LISTONLINE CONTINENT PLUS D'UN ELEMENT (MYUSER), REJET DU MESSAGE CAR LA LISTE A DEJA ETE INITIALISE


    /**
     * Constructor
     */
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
     *//*
    public static void askPseudo(String pseudo){
        setMyUser(pseudo);
        sendPseudo(pseudo);
    }

    *//**
     * Send a broadcast message to ask if the pseudo is available
     * @param pseudo
     * @throws SocketException
     */
    public static void sendPseudo(String pseudo){

        // Generate a String with the type of message and user informations
        String msg = TypeMsg.PSEUDO+"|IP:" + myUser.getIP() + "|Pseudo:" + pseudo;

        UDPSender.sendBroadcast(msg,myUser.getPort());

    }
    /*
    private static void sendPseudoResponse(String pseudo, String ip, Boolean isValid){
        String msg;
        if (isValid){
            msg = TypeMsg.PSEUDO_OK+"|IP:" + myUser.getIP() +"|MyPseudo:"+getMyUser().getPseudo()+"|Pseudo:" + pseudo;
        }else{
            msg = TypeMsg.PSEUDO_NOT_OK+"|IP:" + myUser.getIP() +"|MyPseudo:" +getMyUser().getPseudo()+"|Pseudo:" + pseudo;

        }
        System.out.println("Envoi de la reponse au pseudo : "+pseudo+" avec le type est valide : "+isValid);
        UDPSender.sendUDP(msg,myUser.getPort(),ip);

    }*/

    public static void sendConnect(){
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
        SessionController.close();
        }

    public static void askUserList() {
        // Generate a String with the type of message and the user informations
        String msg = TypeMsg.ASK_USER_LIST+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        System.out.println(msg);
        UDPSender.sendBroadcast(msg, myUser.getPort());
    }

    public static void sendUserList(String ip){
        String msg = TypeMsg.USER_LIST+"|IP:" + myUser.getIP() + "|Pseudo:" + myUser.getPseudo();
        for (User user : listOnline){
            msg += "|" + user.getIP() + ":" + user.getPseudo();
        }
        System.out.println("Envoi de la Userlist : " + msg);
        UDPSender.sendUDP(msg, myUser.getPort(),ip);
    }

    // RECEIVE INFORMATIONS

    //TODO : A tester
    public static void informationTreatment(String msg){
        String[] splitedMsg = msg.split("\\|");

        // Filtrage des messages recu et ejection des messages non conformes
        String[] typeMsgString = {"ASK_PSEUDO","PSEUDO_OK","PSEUDO_NOT_OK","PSEUDO","CONNECT","DISCONNECT","ASK_USER_LIST","USER_LIST","TEST"};
        if (!Arrays.asList(typeMsgString).contains(splitedMsg[0])){
            System.out.println("Message non conforme" );
            System.out.println("Message recu : " + msg);
            return;
        }
        TypeMsg type = TypeMsg.valueOf(splitedMsg[0]);

        String fullIP = splitedMsg[1];
        String IP = fullIP.split(":")[1];
        String fullPseudo = splitedMsg[2];
        String pseudo = fullPseudo.split(":")[1];

        //Filtrage des messages recu et ejection des messages envoyes a soi meme
        //TODO : ce filtre doit etre commenté lors des phases de test local et enlevé lors du fonctionnement de l'application
        if(IP.equals(myUser.getIP())){
           return;
        }

        switch(type){
            /*case ASK_PSEUDO:

                if (pseudoNotPresent(pseudo)){
                    sendPseudoResponse(pseudo, IP, true);
                    sendUserList(IP);
                }else{
                    sendPseudoResponse(pseudo, IP, false);
                }

                break;

            case PSEUDO_OK:
                listOnline.add(0,myUser);
                System.out.println("Pseudo ajouté : " + pseudo);
                System.out.println("IP ajouté : " + IP);

                sendConnect();
                System.out.println();
                System.out.println("Connecté");
                System.out.println();

                SessionController.initialize();
                //TODO : change frame to chat

                System.out.println(listOnline);
                UserController.getListOnline().forEach((user) -> {
                    System.out.println("User : " + user);
                    System.out.println("Pseudo : " + user.getPseudo());
                    System.out.println("IP : " + user.getIP());
                });

                break;

            case PSEUDO_NOT_OK:
                System.out.println("PSEUDO_NOT_OK");
                //TODO : afficher un message d'erreur via le FrameController, pseudo deja pris, recommencer

                break;*/

            //TODO : A tester
            case PSEUDO:
                updateUser(IP, pseudo);

            case CONNECT:

                if (pseudoNotPresent(pseudo)){
                    listOnline.add(new User(IP,pseudo));
                    //TODO : update the list of online users via the FrameController
                }else{
                    System.out.println("Pseudo " + pseudo + " non ajouté car déjà présent");
                }


                break;
                //TODO:change
            case DISCONNECT:
                System.out.println("DISCONNECT");
                pseudo = fullPseudo.split(":")[1];
                listOnline.remove(getUserByPseudo(pseudo));

                //TODO : update the list of online users via the FrameController
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

    private static boolean pseudoNotPresent(String pseudo){
        for (User user : listOnline){
            if (user.getPseudo().equals(pseudo)){
                return false;
            }
        }
        return true;
    }

    /**
     * Find the user in the listOnline with the pseudo
     * @param pseudo
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
        System.out.println("Liste des utilisateurs connectés : ");
        for (User userInList : listOnline) {
            System.out.println(userInList.getPseudo());
        }
        for (User userInList : listOnline) {
            if (Objects.equals(userInList.getIP(), IP)) {
                userInList.setPseudo(pseudo);
                System.out.println("Pseudo updated : " + pseudo);
            }
        }
        //print la liste
        System.out.println("Liste des utilisateurs connectés : ");
        for (User userInList : listOnline) {
            System.out.println(userInList.getPseudo());
        }
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
     * @param ip
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
        listOnline.add(new User(getLocalIP(), "me(test)"));
    }

    //TODO : Just for test, to delete
    public static void showListOnline(){
        for (User user : listOnline){
            System.out.println(user.getPseudo() + " " + user.getIP());
        }
    }

}
