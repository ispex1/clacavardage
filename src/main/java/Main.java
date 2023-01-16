import controller.UserController;
import database.DatabaseManager;
import model.Message;
import model.User;
import network.TCPListener;
import network.TCPSession;
import network.UDPListener;
import network.UDPSender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This class is the main class of the project.
 *
 */
public class Main{

    public static void main (String[] args){

        testUDPSender(0);

    }

    // test UserController

    // test SessionController

    //ACNCIEN test TCP
    /* private static void testTCP() {
        int port = 6789;
        TCPListener listener = new TCPListener(port);

        try {
            Thread.sleep(1000);
            User userdist = new User("localhost");
            System.out.println("< MAIN > : START NEW SESSION");
            //on créé une demande de session a localhost, comme localhost ecoute sur ce port, cela devrait ourvrir un socket
            TCPSession session = new TCPSession(userdist, port);
            Thread.sleep(1000);
            System.out.println("< MAIN > : SENDING MESSAGE FROM SESSION CREATED BY MAIN");
            session.sendMessage("Hello, from session created by main");
            Thread.sleep(1000);
            System.out.println("< MAIN > : SENDING MESSAGE FROM SESSION CREATED BY LISTENER");
            listener.sessionsList.get(0).sendMessage("Hello, from session created by listener");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    } */

    //test UDP
    private static void testUDPSender(int type){
        // type : 0 = broadcast, 1 = unicast
        int port=1234;

        //Creating a UDP Listener, should open a new thread
        UDPListener listener = new UDPListener(port);
        
        //Sending messages
        while(true){
            if(type == 0){
                UDPSender.sendBroadcast("Hello Broadcast", port);
            }
            else if(type == 1){    
                try {
                    UDPSender.sendUDP("Hello UDP", port, InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Wrong type");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    // ANCIEN test UDP Sender and Listener
    /* private static void testUDP(){
        int port=1234;

        //Creating a UDP Listener, should open a new thread
        UDPListener listener = new UDPListener(port);

        // Creating a UDP Sender
        UDPSender sender = new UDPSender();

        // Sending messages
        try {
            while (true) {
                //sender.sendBroadcast("Hello Broadcast", port);
                sender.sendUDP("Hello UDP", port, InetAddress.getLocalHost().getHostAddress());
                Thread.sleep(1000);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    } */


    //ANCIEN test DB Manager function
    /* private static void testDBManager(){
        Message msg = new Message("testouille");
        Message msg2 = new Message();
        User este = new User("IP_Perso", "iSpeX");
        User gaboche = new User("IP_gaboche", "bacchus");
        msg.setSender(este);
        msg.setReceiver(gaboche);
        System.out.println(msg.toString());

        DatabaseManager db = new DatabaseManager();
        db.createPersonalInfo("MAC_Perso");
        db.createNewConvo("MAC_gaboche");

        db.insertMessage(gaboche.getIP(), msg);
        ArrayList<Integer> list = db.findListOfIndex(gaboche.getIP(), "test");
        //printList(list);
        for (int i = 0; i < list.size(); i++){
            msg2 = db.getMsgFromIndex(gaboche.getIP(), list.get(i));
            System.out.println(msg2.toString());
            //db.deleteMessage(gaboche.getIP(), list.get(i));
        }
    } */
}
