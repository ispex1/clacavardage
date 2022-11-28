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

    public Message(String msg, User sender, User receiver){
        this.setData(msg);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        this.setTime(sdf.format(new Date()));
        this.setSender(sender);
        this.setReceiver(receiver);
    }


    public Message(String data, String time, User sender, User receiver){
        this.setData(data);
        this.setTime(time);
        this.setSender(sender);
        this.setReceiver(receiver);
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
