package model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents a message.
 * It contains the text, the time, the sender and the receiver.
 *
 */

public class Message {
    private String data; // Text of the message
    private String time; // Time of the message (dd-MM-yyyy HH:mm:ss)
    private User sender; // Sender of the message
    private User receiver; // Receiver of the message
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss"; // Date format

    /**
     * Constructor
     */
    public Message(){
    }

    /**
     * Constructor
     * @param msg
     */
    public Message(String msg){
        this.setData(msg);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        this.setTime(sdf.format(new Date()));
    }

    /**
     * Constructor
     * @param sender
     * @param receiver
     * @param msg
     */
    public Message(User sender, User receiver, String msg){
        this.setData(msg);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        this.setTime(sdf.format(new Date()));
        this.setSender(sender);
        this.setReceiver(receiver);
    }

    /**
     * Constructor
     * @param sender
     * @param receiver
     * @param msg
     * @param time
     */
    public Message(User sender, User receiver, String msg, String time){
        this.setData(msg);
        this.setTime(time);
        this.setSender(sender);
        this.setReceiver(receiver);
    }

    /**
     * This method returns the message in a string format
     * @return String
     */
    //TODO: resolve error Method threw 'java.lang.NullPointerException' exception. Cannot evaluate model.Message.toString()

    public String toString(){
        return ("Sender ; "     + this.getSender().getIP()      + " | "
                +   "Receiver ; "   + this.getReceiver().getIP()    + " | "
                +   "Message ; "    + this.getData()                + " | "
                +   "Time ; "       + this.getTime()                + " | "
        );
    }

    /**
     * Setter for data
     * @param data
     */
    public void setData(String data){
        this.data = data;
    }

    /**
     * Getter for data
     * @return data
     */
    public String getData(){
        return this.data;
    }

    /**
     * Setter for time
     * @param time
     */
    public void setTime(String time){
        this.time = time;
    }

    /**
     * Getter for time
     * @return time
     */
    public String getTime(){
        return this.time;
    }

    /**
     * Setter for sender
     * @param sender
     */
    public void setSender(User sender){
        this.sender = sender;
    }

    /**
     * Getter for sender
     * @return sender
     */
    public User getSender(){
        return this.sender;
    }

    /**
     * Setter for receiver
     * @param receiver
     */
    public void setReceiver(User receiver){
        this.receiver = receiver;
    }

    /**
     * Getter for receiver
     * @return receiver
     */
    public User getReceiver(){
        return this.receiver;
    }
}
