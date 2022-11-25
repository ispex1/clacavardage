package Model;

import Model.User.User;

public class Message {
    private String data; //message
    private String time;
    private User sender;
    private User receiver;

    // CONSERVER L'ENVOI DE FICHIERS ??
    // COMMENT FAIRE ARRIVER LA DATA ??
    // CHANGER TYPE TIME ?? dd:mm:yyyy / hh:mm:ss

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
