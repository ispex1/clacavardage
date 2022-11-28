package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.*;

public class DatabaseManager {

    public DatabaseManager(){
        connect();
    }

    public void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:sqlite/clac.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    

    public static void archiveMessage(int idSession, Message msg){
        //BDD(idSession).add(msg); //requête JDBC
    }

}
