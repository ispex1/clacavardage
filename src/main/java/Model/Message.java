package model;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.user.User;

public class Message {
    private String data; //message
    private String time;
    private User sender;
    private User receiver;
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    // COMMENT FAIRE ARRIVER LA DATA ??

    public Message(){
    }

    public Message(String msg){
        this.setData(msg);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		this.setTime(sdf.format(new Date()));
    }

    public Message(User sender, User receiver, String msg){
        this.setData(msg);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        this.setTime(sdf.format(new Date()));
        this.setSender(sender);
        this.setReceiver(receiver);
    }

    public Message(User sender, User receiver, String msg, String time){
        this.setData(msg);
        this.setTime(time);
        this.setSender(sender);
        this.setReceiver(receiver);
    }

    @Override 
    public String toString(){
        return ("Sender : "     + this.getSender().getIP()      + " | "  
            +   "Receiver : "   + this.getReceiver().getIP()    + " | "  
            +   "Message : "    + this.getData()                + " | "  
            +   "Time : "       + this.getTime()                + " | "
            );
    }

    // GETTER & SETTER

    public void setData(String data){
        this.data = data;
    }
    public String getData(){
        return this.data;
    }

    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return this.time;
    }

    public void setSender(User sender){
        this.sender = sender;
    }
    public User getSender(){
        return this.sender;
    }

    public void setReceiver(User receiver){
        this.receiver = receiver;
    }
    public User getReceiver(){
        return this.receiver;
    }

}
