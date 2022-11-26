package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Model.Message;

public class DatabaseManager {
    // JDBC driver name and database URL
    String url = "jdbc:sqlite:./sqlite/clac.db";  

    public DatabaseManager(){

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
