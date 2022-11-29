package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
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
        //createTableConvo("user");
        createNewTable("user");
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

    public static void createNewTable(String ipOther) {
        // SQL statement for creating a new table
        String sql= "CREATE TABLE IF NOT EXISTS " + ipOther +"(\n"
                + "	indexMsg integer PRIMARY KEY,\n" // index du message
                + " sender text NOT NULL, \n" // ip de l'envoyeur
                + " message text NOT NULL, \n" // message
                + "	time text NOT NULL"// date et heure
                + ");"; 

        // SQL statement for creating a new table
        try (Connection conn = DriverManager.getConnection(url); 
             Statement  stmt = conn.createStatement()) {
                // create a new table
                stmt.execute(sql);
                System.out.println("A new table has been created with the name : " + ipOther);
        } catch (SQLException e) {
            System.out.println("Error creating table");
            System.out.println(e.getMessage());
        }
    }

    public static void archiveMessage(int idSession, Message msg){
        //BDD(idSession).add(msg); //requête JDBC
    }

}
