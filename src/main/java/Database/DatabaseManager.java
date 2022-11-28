package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.Message;
import model.user.User;

public class DatabaseManager {
    // JDBC driver name and database URL
    String url = "jdbc:sqlite:./sqlite/clac.db";  

    public DatabaseManager(){
        String db = "DatabaseManager created";
        Message msg = new Message(db);
        System.out.println(msg.getData());
    }

    // connect to the database
    private Connection connect() {
		Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
 	}	 

    

    public static void archiveMessage(int idSession, Message msg){
        //BDD(idSession).add(msg); //requête JDBC
    }

}
