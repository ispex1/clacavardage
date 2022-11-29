
package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.*;

//  https://www.sqlitetutorial.net/sqlite-java/
// amélioration à faire : demander à user de choisir un id unique à la premiere
// connexion, puis le sauvegarder dans la bdd locale -> changement d'ip, même user

public class DatabaseManager {

    public static String url = "jdbc:sqlite:sqlite/clac.db";

    public DatabaseManager(){
        createNewDatabase();
        connect();
        createNewConvo("gaboche");
    }

    // utile ??? 
    // "When you connect to an SQLite database that does not exist, 
    // it automatically creates a new database"
    // connect crée la db ??
    public static void createNewDatabase() {
        
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void connect() {
        Connection conn = null;
        try {
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

    public static void createNewConvo(String ipOther) {
        // SQL statement for creating a new table
        String sql= "CREATE TABLE IF NOT EXISTS " + ipOther +"(\n"
                + "	indexMsg integer PRIMARY KEY,\n" // index du message
                + " sender text NOT NULL, \n" // ip de l'envoyeur
                + " message text NOT NULL, \n" // message
                + "	time text NOT NULL"// date et heure
                + ");"; 

        try (Connection conn = DriverManager.getConnection(url); 
             Statement  stmt = conn.createStatement()) {
                // create a new table
                stmt.execute(sql);
                System.out.println("A new convo has been created with the name : " + ipOther);
        } catch (SQLException e) {
            System.out.println("Error creating table");
            System.out.println(e.getMessage());
        }
    }

    public static void deleteConvo(String ipOther) {
        // SQL statement for deleting a table
        String sql= "DROP TABLE IF EXISTS " + ipOther; 

        try (Connection conn = DriverManager.getConnection(url); 
             Statement  stmt = conn.createStatement()) {
                // delete the table
                stmt.executeUpdate(sql);
                System.out.println("The " + ipOther + " table has been successfully deleted");
        } catch (SQLException e) {
            System.out.println("Error deleting table");
            System.out.println(e.getMessage());
        }
    }

    public void insertMessage(String ipOther, Message msg) {
        String sql = "INSERT INTO " + ipOther + "(indexMsg,sender,message,time) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(2, msg.getSender().getIP());
                pstmt.setString(3, msg.getData());
                pstmt.setString(4, msg.getTime());
                pstmt.executeUpdate();
                System.out.println("A new message has been add to the " + ipOther + " table");
        } catch (SQLException e) {
            System.out.println("Error insert a message");
            System.out.println(e.getMessage());
        }
    }

    // the function return the indexMsg of rows that have the data string into it
    // for the moment, it return the first indexMsg that match
    public int findMessage(String ipOther, String data){
        String sql = "SELECT indexMsg FROM " + ipOther + " WHERE message = ?";
        int index = -1;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, data);
                pstmt.executeQuery();
                System.out.println("The index of the message is : " + index);
        } catch (SQLException e) { 
            System.out.println("Error finding a message");
            System.out.println(e.getMessage());
        }
        return index;
    }    

    public static void archiveMessage(int idSession, Message msg){
        //BDD(idSession).add(msg); //requête JDBC
    }

}
