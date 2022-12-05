package controller;

import java.util.ArrayList;

import model.*;

/**
 * This class represents the frame controller.
 * 
 */
public class FrameController {

    /**
     * Constructor
     */
    public FrameController(){

    }

    /**
     * Open the login frame
     */
    public void openLoginFrame(){
        //TODO: Open the login frame
    }

    /**
     * Close the login frame
     */
    public void closeLoginFrame(){
        //TODO: Close the login frame
    }

    /**
     * Open the main frame of the app
     * @param pseudo
     * @param ip
     */
    public void openApp(String pseudo, int ip){
        //TODO: Open the main frame of the app and display the pseudo and the ip of the user
    }

    /**
     * Close the main frame of the app
     */
    public void closeApp(){
        //TODO: Close the main frame of the app
    }

    /**
     * Open the research frame when the user click on the research button
     * @param msgList
     */
    public void openMsgResearchFrame(ArrayList<Message> msgList){
        for (Message msg : msgList) {
            //TODO: Open a new msgResearch frame and display all the message find
            System.out.println(msg.toString());
        }
    }

    /**
     * Close the research frame when the user click on the close button
     */
    public void closeMsgResearchFrame(){
        //TODO: Close the msgResearch frame
    }

    /**
     * Update the list of the user connected on the main frame
     * @param listUserOnline
     * @param listUserDisplay
     */
    public void updateUserOnline(ArrayList<User> listUserOnline, ArrayList<User> listUserDisplay){
        // New list of user to conserve the order
        ArrayList<User> listUser = new ArrayList<User>(); 

        // If the user display is online, we add it to the update list
        for (User user : listUserDisplay) {
            if (listUserOnline.contains(user)) {
                listUser.add(user);
            }
        }

        // We add all the other user online to the update list
        for (User user : listUserOnline) {
            if (!listUser.contains(user)) {
                listUser.add(user);
            }
        }

        // We update the list of user display
        for (User user : listUser) {
            //TODO: Display the new list of user online
            System.out.println(user.toString());
        }
    }

    /**
     * Open a new chat frame when the user click on a user online
     * @param user
     */
    public void openSession(User user){
        //TODO: Open a new chat frame and display the history of the conversation
    }

    /**
     * Close a session when the user click on the close button
     * @param user
     */
    public void closeSession(User user){
        //TODO: Close the session and close the chat frame
    }

    /**
     * Close a session when the other user disconnect
     * @param user
     */
    public void userNotConnected(){
        //TODO: Close the session and close the chat frame
    }

    /**
     * Update the chat frame when the user send a message
     * @param msg
     */
    public void sendText(Message msg){
        //TODO : Send the message to the other user
        updateHistory();
    }

    /**
     * Update the chat frame when the user receive a message
     * @param msg
     */
    public void updateHistory(){
        //TODO: Show the new history of the conversation
    }

    /**
     * Show an error message when the user can't connect to the server
     * @param error
     */
    public void showConnectionFailed(String error){
        if (error.equals("pseudo")) {
            //TODO: Display the error message "Pseudo already used"
        } else if (error.equals("server")) {
            //TODO: Display the error message "Connexion failed"   
        }else if (error.equals("length")) {
            //TODO: Display the error message "Pseudo too long"
        }
    }
}
