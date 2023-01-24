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
     * @param ip , the IP address of the user
     */
    public User (String ip){
        this.setIP(ip);
    }

    /**
     * Constructor
     * @param ip , the IP address of the user
     * @param pseudo , the pseudo of the user
     */
    public User (String ip, String pseudo){
        this.setIP(ip);
        this.setPseudo(pseudo);
    }

    /**
     * Setter for ip
     * @param ip , the IP address of the user
     */
    public void setIP(String ip){
        this.ip = ip ;
    }

    /**
     * Getter for ip
     * @return ip , the IP address of the user
     */
    public String getIP(){
        return this.ip;
    }


    /**
     * Setter for pseudo
     * @param pseudo , the pseudo of the user
     */
    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }

    /**
     * Getter for pseudo
     * @return pseudo , the pseudo of the user
     */
    public String getPseudo(){
        return pseudo;
    }

    /**
     * Setter for port
     * @param port , the port of the user
     */
    public void setPort(int port){
        this.port = port;
    }

    /**
     * Getter for port
     * @return port , the port of the user
     */
    public int getPort(){
        return this.port;
    }
}
