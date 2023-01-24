package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.*;

/**
 * This class manages the database using SQLite.
 * It contains the methods to create the database, to connect to it, to insert
 * and delete messages.
 * It also contains the methods to get the history of a conversation and return all
 * the messages containing a specific word in a conversation.
 * 
 */

public class DatabaseManager {
    public static String url = "jdbc:sqlite:sqlite/clac.db"; // Path to the database

    /**
     * Constructor
     * Static class
     */
    private DatabaseManager(){
    }
    
    /**
     * This method is used to initialize the database.
     * It creates a new database if it does not exist and connects to it.
     */
    public static void initialize(){
        createNewDatabase();
        connect();
    }

    /**
     * This method creates a new database if it does not exist.
     */
    private static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData(); // Get the metadata of the database
                System.out.println("The driver name is " + meta.getDriverName());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method connects to the database previously created.
     */
    private static void connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url); // Connect to the database
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


    /**
     * This method create a new table (conversation) in the database.
     * The name of the table is the id of the other user.
     * @param idOther , the id of the other user
     */
    public static void createNewConvo(String idOther) {
        // SQL statement for creating a new table
        String sql= "CREATE TABLE IF NOT EXISTS id_" + idOther.replace(".","_") +"(\n"
                + "	indexMsg integer PRIMARY KEY,\n" // Index of the message
                + " sender text NOT NULL, \n" // ID of the sender
                + " receiver text NOT NULL, \n" // ID of the receiver
                + " message text NOT NULL, \n" // Text of the message
                + "	time text NOT NULL"// Time of the message (dd:MM::yyyy HH:mm:ss)
                + ");"; 

        try (Connection conn = DriverManager.getConnection(url); 
             Statement  stmt = conn.createStatement()) {
                stmt.execute(sql); // Create a new table
                System.out.println("A new convo has been created with the name : " + idOther);
        } catch (SQLException e) {
            System.out.println("Error creating table");
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method inserts a message in a conversation.
     * The message is inserted in the table corresponding to the id of the other user.
     * @param idOther , the id of the other user
     * @param msg , the message to insert
     */
    public static void insertMessage(String idOther, Message msg) {
        // SQL statement for inserting a new row (message)
        String sql = "INSERT INTO id_" + idOther.replace(".","_")
                + "(indexMsg,sender,receiver,message,time) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(2, msg.getSender().getIP());
                pstmt.setString(3, msg.getReceiver().getIP());
                pstmt.setString(4, msg.getData());
                pstmt.setString(5, msg.getTime());
                pstmt.executeUpdate(); // Insert a new row (message)
                System.out.println("A new message has been add to the " + idOther + " table");
        } catch (SQLException e) {
            System.out.println("Error insert a message");
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method deletes all the messages in a conversation.
     * The messages are deleted in the table corresponding to the id of the other user.
     * @param idOther , the id of the other user
     */
    public static void deletteAllMessages(String idOther) {
        // SQL statement for deleting a message
        String sql = "DELETE FROM id_" + idOther.replace(".","_");

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate(); // Delete the message
                System.out.println("All messages have been successfully deleted");
        } catch (SQLException e) {
            System.out.println("Error deleting a message");
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method returns an ArrayList of Message
     * where a specific word appears in a conversation.
     * @param idOther , the id of the other user
     * @param data , the word to search
     * @return msgList , the ArrayList of Message
     */
    public static ArrayList<Message> findListOfMessage(String idOther, String data){
        String sql = "SELECT indexMsg, sender, receiver, message, time FROM id_" + idOther.replace(".","_");
        ArrayList<Message> msgList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    if(rs.getString("message").contains(data)){
                        msgList.add(new Message(new User(rs.getString("sender")), 
                                                new User(rs.getString("receiver")), 
                                                rs.getString("message"), 
                                                rs.getString("time")
                                                )
                                    ); 
                    }
                }
        } catch (SQLException e) { 
            System.out.println("Error building the messageList");
            System.out.println(e.getMessage());
        }
        return msgList;
    }

    /**
     * This method returns the history of a conversation into an ArrayList of Message.
     * @param idOther , the id of the other user
     * @return history , the ArrayList of Message
     */
    public static ArrayList<Message> getHistory(String idOther) {
        String sql = "SELECT * FROM id_" + idOther.replace(".","_");
        ArrayList<Message> history = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    history.add(new Message(new User(rs.getString("sender")), 
                                            new User(rs.getString("receiver")), 
                                            rs.getString("message"), 
                                            rs.getString("time")
                                            ));
                }
        } catch (SQLException e) {
            System.out.println("Error getting the history");
            System.out.println(e.getMessage());
        }
        return history;
    }
}
