import java.net.InetAddress;
import java.net.UnknownHostException;

import database.DatabaseManager;
import model.Message;
import model.user.*;

public class Main{

    // ip 12:48:42 -> 192.168.56.1

    public static void main (String[] args){
        Message msg = new Message("testouille");
        User este = new User("este");
        User gaboche = new User("gaboche");
        msg.setSender(este);
        msg.setReceiver(gaboche);
        System.out.println(msg.toString());

        DatabaseManager db = new DatabaseManager();   
        db.insertMessage(gaboche.getIP(), msg);
        int index = db.findMessage(gaboche.getIP(), "tout doux");
        System.out.println(index);
    }
    
}
