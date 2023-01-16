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
import java.util.ArrayList;

/**
 * This class is the main class of the project.
 * 
 */
public class Main{

    public static void main (String[] args){

        testTCP();

    }

    //test UserController 
    private static void testUserController(){
        UserController userController = new UserController("iSpeX");
        System.out.println("My user : " + userController.getMyUser().getPseudo());
        UserController.askPseudo(UserController.getMyUser().getPseudo());

        while(true){
            try {
                Thread.sleep(2000);
                System.out.println("User list : " + userController.getListOnline());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //test TCP
    private static void testTCP() {
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


    }

    //test UDP Sender and Listener
    private static void testUDP(){
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
