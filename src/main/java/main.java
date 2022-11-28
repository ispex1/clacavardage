import database.DatabaseManager;
import model.Message;
import model.user.*;

public class Main{

    public static void main (String[] args){
        Message msg = new Message("gros caca");
        User este = new User("este");
        User gaboche = new User("gaboche");
        msg.setSender(este);
        msg.setReceiver(gaboche);
        System.out.println(msg.toString());


        DatabasemManager db = new DatabaseManager();
    }

}
