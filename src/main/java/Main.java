import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import controller.*;
import database.DatabaseManager;
import model.*;
import network.*;



/**
 * This class is the main class of the project.
 * 
 */
public class Main{

    public static void main (String[] args){
        testUserController();
        //TODO: Finish the UserController
    }

    //test UserController 
    private static void testUserController(){
        UserController userController = new UserController("iSpeX");
        userController.connect();
        userController.getLocalIP();
        userController.getMacAddress();
    }

    //test UDP Sender and Listener
    private static void testUDP(){
        int port =1234;

        //Creating a UDP Listener, should open a new thread
        UDPListener listener = new UDPListener(1, port);

        // Creating a UDP Sender
        UDPSender sender = new UDPSender();

        // Sending messages
        try {
            while (true) {
                //sender.sendBroadcast("Hello Broadcast", port);
                sender.sendUDP("Hello UDP", port, InetAddress.getLocalHost());
                Thread.sleep(1000);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    //test DB Manager function
    private static void testDBManager(){
        Message msg = new Message("testouille");
        Message msg2 = new Message();
        User este = new User("MAC_Perso", "IP_Perso", "iSpeX");
        User gaboche = new User("MAC_gaboche", "IP_gaboche", "bacchus");
        msg.setSender(este);
        msg.setReceiver(gaboche);
        System.out.println(msg.toString());

        DatabaseManager db = new DatabaseManager();
        db.createPersonalInfo("MAC_Perso");
        db.createNewConvo("MAC_gaboche");

        db.insertMessage(gaboche.getID(), msg);
        ArrayList<Integer> list = db.findListOfIndex(gaboche.getID(), "test");
        //printList(list);
        for (int i = 0; i < list.size(); i++){
            msg2 = db.getMsgFromIndex(gaboche.getID(), list.get(i));
            System.out.println(msg2.toString());
            //db.deleteMessage(gaboche.getIP(), list.get(i));
        }
        db.updatePersonalInfo(este.getID(), este.getIP(), este.getPseudo());
    }
}
