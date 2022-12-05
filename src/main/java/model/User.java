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
    private UserState userState; // State of the user

    // Enum of the different user's state possible
    public enum UserState {
        CONNECTED, DISCONNECTED, BUSY, WAITING
    }

    /**
     * Constructor
     * @param ip
     */
    public User (String ip){
        this.ip = ip;
    }

    /**
     * Constructor
     * @param pseudo
     * @param ip
     * @param port
     * @param etatUser
     */
    public User (String pseudo, String ip, int port, UserState userState){
        this.setPseudo(pseudo);
        this.setIP(ip);
        this.setPort(port);
        this.setUserState(userState);
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
        return this.pseudo;
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

    /**
     * Setter for userState
     * @param userState
     */
    public void setUserState(UserState userState){
        this.userState = userState;
    }
    
    /**
     * Getter for userState
     * @return userState
     */
    public UserState getUserState(){
        return this.userState;
    }

}
