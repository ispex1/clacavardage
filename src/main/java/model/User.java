package model;

/**
 * This class represents a user.
 * It contains the user's IP, pseudo, id, port and state.
 *
 */

public class User {
    private String ip; // IP address of the user
    private String pseudo; // Pseudo of the user
    private int port; // Port of the user

    /**
     * Constructor
     * @param ip
     */
    public User (String ip){
        this.setIP(ip);
    }

    /**
     * Constructor
     * @param ip
     * @param pseudo
     */
    public User (String ip, String pseudo){
        this.setIP(ip);
        this.setPseudo(pseudo);
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
