import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import database.DatabaseManager;
import model.Message;

import model.User;
import network.UDPListener;
import network.UDPSender;



/**
 * This class is the main class of the project.
 * 
 */
public class Main{

    // ip 12:48:42 -> 192.168.56.1
    public static void main (String[] args){
        testUDP();
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
        Message msg = new Message("petit caca");
        Message msg2 = new Message();
        User este = new User("este");
        User gaboche = new User("gaboche");
        msg.setSender(este);
        msg.setReceiver(gaboche);
        System.out.println(msg.toString());

        DatabaseManager db = new DatabaseManager();
        db.insertMessage(gaboche.getIP(), msg);
        ArrayList<Integer> list = db.findListOfIndex(gaboche.getIP(), "caca");
        //printList(list);
        for (int i = 0; i < list.size(); i++){
            msg2 = db.getMsgFromIndex("gaboche", list.get(i));
            System.out.println(msg2.toString());
            //db.deleteMessage(gaboche.getIP(), list.get(i));
        }
    }
    //print every element of the arrayList
    public static void printList(ArrayList<Integer> list){
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
    }
}
