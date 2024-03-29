package test;

import controller.SessionController;
import controller.UserController;
import database.DatabaseManager;
import database.DatabaseManager.*;
import model.Message;
import model.User;
import network.TCPListener;
import network.TCPSession;
import network.UDPListener;
import network.UDPSender;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static database.DatabaseManager.*;


public class testMain{

    public static void main (String[] args){

        //testSessionController();

    }

    // test Network
    private static void testNetwork(){
        //test UDP
        UserController.initialize();
        UDPSender.sendBroadcast("TEST",UserController.getMyUser().getPort());

        SessionController.initialize();

    }

    // test SessionController
    private static void testSessionController() {
        SessionController.initialize();
        UserController.initialize();

        UserController.getListOnline().add(new User(UserController.getMyUser().getIP(), "bacchus"));

        SessionController.createSession(UserController.getListOnline().get(0));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        SessionController.sendMessage(new Message(UserController.getMyUser(), UserController.getListOnline().get(0), "testouille"), UserController.getListOnline().get(0) );
        DatabaseManager.getHistory(UserController.getListOnline().get(0).getIP());
    }

    private static void testUDPSender(int type){
        // type : 0 = broadcast, 1 = unicast
        int port=1234;

        //Creating a UDP Listener, should open a new thread
        UDPListener listener = new UDPListener(port);
        System.out.println("UDP Listener created");
        listener.start();
        int i = 0;
        //Sending messages
        while(true){
            if(type == 0){
                System.out.println("Broadcast sent");
                UDPSender.sendBroadcast("TEST|Hello Broadcast "+i, port);

            }
            else if(type == 1){    
                try {
                    UDPSender.sendUDP("TEST|Hello UDP"+i, port, InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Wrong type");
            }
            i++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //test UserController
    public static void testUserController(){
        UserController.initialize();
        UserController.getListOnline().add(new User("10.10.10.10","gaboche"));
        UserController.getListOnline().add(new User(UserController.getMyUser().getIP(),"bacchus"));
        UserController.getListOnline().add(new User(UserController.getMyUser().getIP(), "esteban"));
        UserController.getListOnline().add(new User(UserController.getMyUser().getIP(), "paul"));

        System.out.println();
        System.out.println("Liste des utilisateurs connectés :");
        UserController.getListOnline().forEach((user) -> {
            System.out.println("User : " + user);
            System.out.println("Pseudo : " + user.getPseudo());
            System.out.println("IP : " + user.getIP());
        });
        System.out.println();

        //UserController.askPseudo("gabocheur");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Liste des utilisateurs connectés :");
        UserController.getListOnline().forEach((user) -> {
            System.out.println("User : " + user);
            System.out.println("Pseudo : " + user.getPseudo());
            System.out.println("IP : " + user.getIP());
        });
        System.out.println();
    }

    private static void testDBManager() {
        Message msg = new Message("testouille");
        Message msg2 = new Message();
        User este = new User("IP_Perso", "iSpeX");
        User gaboche = new User("IP_gaboche", "bacchus");
        msg.setSender(este);
        msg.setReceiver(gaboche);
        System.out.println(msg.toString());

        initialize();
        createNewConvo("IP_gaboche");

        insertMessage(gaboche.getIP(), msg);
    }
}
