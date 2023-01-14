package model;

/**
 * This class represents a user.
 * It contains the user's IP, pseudo, id, port and state.
 *
 */

public class User {
    private String id; // MAC of the user
    private String ip; // IP address of the user
    private String pseudo; // Pseudo of the user
    private int port; // Port of the user

    /**
     * Constructor
     * @param id
     */
    public User (String id){
        this.setID(id);
    }

    /**
     * Constructor
     * @param id
     * @param ip
     * @param pseudo
     */
    public User (String id, String ip, String pseudo){
        this.setID(id);
        this.setIP(ip);
        this.setPseudo(pseudo);
    }

    /**
     * Setter for id
     * @param id
     */
    public void setID(String id){
        this.id = id;
    }

    /**
     * Getter for id
     * @return id
     */
    public String getID(){
        return this.id;
    }


    /**
     * Setter for pseudo
     * @param pseudo
     */
    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }

    /**
     * Getter for pseudo
     * @return pseudo
     */
    public String getPseudo(){
        return pseudo;
    }

    /**
     * Setter for ip
     * @param ip
     */
    public void setIP(String ip){
        this.ip = ip ;
    }

    /**
     * Getter for ip
     * @return ip
     */
    public String getIP(){
        return this.ip;
    }

    /**
     * Setter for port
     * @param port
     */
    public void setPort(int port){
        this.port = port;
    }

    /**
     * Getter for port
     * @return port
     */
    public int getPort(){
        return this.port;
    }
}
