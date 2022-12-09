
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

// ADD NEW DB PERSONAL INFORMATIONS (ID, IP, PSEUDO)

//  https://www.sqlitetutorial.net/sqlite-java/
// amélioration à faire : demander à user de choisir un id unique à la premiere
// connexion, puis le sauvegarder dans la bdd locale -> changement d'ip, même user

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
     * The constructor creates the database if it does not exist and connects to it.
     * 
     */
    public DatabaseManager(){
        createNewDatabase();
        connect();
        createPersonalInfo("este");
        createNewConvo("gaboche");
    }

    /**
     * This method creates a new database if it does not exist.
     *  
     */
    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData(); // Get the metadata of the database
                System.out.println("The driver name is " + meta.getDriverName()); 
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method connects to the database previously created.
     * 
     */
    public void connect() {
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
     * This method create the table for the personal informations
     * of the user in the database.
     * The name of the table is the id of the user. (@MAC)
     * It will contain the id, the ip and the pseudo of the user.
     * 
     * @param idPerso
     */
    public static void createPersonalInfo(String idPerso) {
        // SQL statement for creating a new table
        String sql= "CREATE TABLE IF NOT EXISTS " + idPerso +"(\n"
                + " index integer PRIMARY KEY,\n" // Index of the row
                + "	id text NOT NULL,\n" // MAC of the user
                + " ip text NOT NULL, \n" // IP address of the user
                + " pseudo text NOT NULL" // Pseudo of the user
                + ");"; 

        try (Connection conn = DriverManager.getConnection(url); 
             Statement  stmt = conn.createStatement()) {
                stmt.execute(sql); // Create a new table
                System.out.println("The personal table has been created with the name : " + idPerso);
        } catch (SQLException e) {
            System.out.println("Error creating table");
            System.out.println(e.getMessage());
        }
    }

    /** 
     * This method update the personal informations of the user in the database.
     * 
     * @param idPerso
     * @param ip
     * @param pseudo
     */
    public static void updatePersonalInfo(String idPerso, String ip, String pseudo) {
        String sql = "DELETE FROM " + idPerso + " WHERE indexMsg = ?";
        String sql2 = "INSERT INTO " + idPerso + " (index, id, ip, pseudo) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                //Deleting the previous personal informations
                pstmt.setInt(1, 1);
                pstmt.executeUpdate(); // Delete the message

                //Inserting the new personal informations
                pstmt2.setInt(1, 1);
                pstmt2.setString(2, ip);
                pstmt2.setString(3, pseudo);
                pstmt2.setString(4, idPerso);
                pstmt2.executeUpdate(); // Update the personal informations

                System.out.println("The personal informations have been updated.");

        } catch (SQLException e) {
            System.out.println("Error updating personal informations");
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method create a new table (conversation) in the database.
     * The name of the table is the id of the other user.
     * 
     * @param idOther
     */
    public static void createNewConvo(String idOther) {
        // SQL statement for creating a new table
        String sql= "CREATE TABLE IF NOT EXISTS " + idOther +"(\n"
                + "	indexMsg integer PRIMARY KEY,\n" // Index of the message
                + " sender text NOT NULL, \n" // IP of the sender
                + " receiver text NOT NULL, \n" // IP of the receiver
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
     * This method deletes a table (conversation) in the database if it exists.
     * The name of the table deleted is the id of the other user.
     * 
     * @param idOther
     */
    public static void deleteConvo(String idOther) {
        // SQL statement for deleting a table
        String sql= "DROP TABLE IF EXISTS " + idOther; 

        try (Connection conn = DriverManager.getConnection(url); 
             Statement  stmt = conn.createStatement()) {
                stmt.executeUpdate(sql); // Delete the table
                System.out.println("The " + idOther + " table has been successfully deleted");
        } catch (SQLException e) {
            System.out.println("Error deleting table");
            System.out.println(e.getMessage());
        }
    }


    /**
     * This method inserts a message in a conversation.
     * The message is inserted in the table corresponding to the ip of the other user.
     * 
     * @param idOther
     * @param msg
     */
    public void insertMessage(String idOther, Message msg) {
        // SQL statement for inserting a new row (message)
        String sql = "INSERT INTO " + idOther + "(indexMsg,sender,receiver,message,time) VALUES(?,?,?,?,?)";

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
     * This method deletes a message in a conversation.
     * The message is deleted in the table corresponding to the id of the other user.
     * 
     * @param idOther
     * @param index
     */
    public static void deleteMessage(String idOther, int index) {
        // SQL statement for deleting a message
        String sql = "DELETE FROM " + idOther + " WHERE indexMsg = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, index);
                pstmt.executeUpdate(); // Delete the message
                System.out.println("The message with the index " + index + " has been successfully deleted");
        } catch (SQLException e) {
            System.out.println("Error deleting a message");
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * This method returns an ArrayList of all the messages's index
     * where a specific word appears in a choosen conversation.
     * 
     * @param idOther
     * @param data
     * @return indexList
     */
    public ArrayList<Integer> findListOfIndex(String idOther, String data){
        // SQL statement for selecting data
        String sql = "SELECT indexMsg, message FROM " + idOther; 
        ArrayList<Integer> indexList = new ArrayList<Integer>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery(); // Select the index and the text of the message
                while (rs.next()) {
                    if(rs.getString("message").indexOf(data) != -1){ // If the word appears in the message
                        indexList.add(rs.getInt("indexMsg")); // Add the index of the message to the list
                    }
                }
        } catch (SQLException e) { 
            System.out.println("Error building the indexList");
            System.out.println(e.getMessage());
        }
        return indexList;
    }

    /**
     * This method returns an ArrayList of Message
     * where a specific word appears in a conversation.
     * 
     * @param idOther
     * @param data
     * @return msgList
     */
    public ArrayList<Message> findListOfMessage(String idOther, String data){
        String sql = "SELECT indexMsg, sender, receiver, message, time FROM " + idOther;
        ArrayList<Message> msgList = new ArrayList<Message>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    if(rs.getString("message").indexOf(data) != -1){
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
     * This method returns the index of a Message in a conversation.
     * Two (or more) messages can't be the same thanks to the precise time,
     * so the index is unique.
     * 
     * @param ipOther
     * @param msg
     * @return index
     */
    public static int getIndexFromMsg(String ipOther, Message msg){
        // SQL statement for selecting everything from the table
        String sql = "SELECT * FROM " + ipOther;
        int index = -1;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery(); // Select everything from the table
                while (rs.next()) { // For each row
                    if( rs.getString("message").equals(msg.getData()) &&
                        rs.getString("sender").equals(msg.getSender().getIP()) &&
                        rs.getString("receiver").equals(msg.getReceiver().getIP()) &&
                        rs.getString("time").equals(msg.getTime())){ // If the exact message is find in the table
                        index = rs.getInt("indexMsg"); // Get the index of the message
                    }
                }
        } catch (SQLException e) { 
            System.out.println("Error finding a message");
            System.out.println(e.getMessage());
        }
        return index;
    }

    /**
     * This method returns a Message from a conversation with a specific index.
     * 
     * @param ipOther
     * @param index
     * @return msg
     */
    public Message getMsgFromIndex(String ipOther, int index) {
        // SQL statement for selecting everything from the table where 
        // the index of the message is equal to the index given in parameter
        String sql = "SELECT * FROM " + ipOther + " WHERE indexMsg = ?";
        Message msg = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, index);
                ResultSet rs = pstmt.executeQuery(); // Select everything from the table
                // Create a message with the data from the table
                msg = new Message(new User(rs.getString("sender")), 
                                  new User(rs.getString("receiver")), 
                                  rs.getString("message"), 
                                  rs.getString("time")
                                  );
        } catch (SQLException e) {
            System.out.println("Error getting a message");
            System.out.println(e.getMessage());
        }
        return msg;
    }

    /**
     * This method returns the history of a conversation into an ArrayList of Message.
     * 
     * @param idOther
     * @return history
     */
    public static ArrayList<Message> getHistory(String idOther) {
        String sql = "SELECT * FROM " + idOther;
        ArrayList<Message> history = new ArrayList<Message>();

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
