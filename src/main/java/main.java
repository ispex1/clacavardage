import database.DatabaseManager;
import model.Message;

public class main {

    public static void main (String[] args){
        System.out.println("hello world");
        Message msg = new Message("gros caca");
        System.out.println(msg.getData());
        DatabaseManager db = new DatabaseManager();
    }

}

