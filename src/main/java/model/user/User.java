package model.user;

public class User {
    private String ip;
    private String pseudo;
    private int idUser;
    private int port; //utile ?? + rajouter à l'UML
    private EtatUser etatUser;

    public User (String ip){
        this.ip = ip;
    }

    public User (String pseudo, String ip, int idUser, int port, EtatUser etatUser){
        this.setPseudo(pseudo);
        this.setIP(ip);
        this.setIdUser(idUser);
        this.setPort(port);
        this.setEtatUser(etatUser);
    }

    // GETTER & SETTER

    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }
    public String getPseudo(){
        return this.pseudo;
    }

    public void setIP(String ip){
        this.ip = ip ;
    }
    public String getIP(){
        return this.ip;
    }

    public void setIdUser(int idUser){
        this.idUser = idUser ;
    }
    public int getIdUser(){
        return this.idUser;
    }

    public void setPort(int port){
        this.port = port;
    }
    public int getPort(){
        return this.port;
    }

    public void setEtatUser(EtatUser etatUser){
        this.etatUser = etatUser;
    }
    public EtatUser getEtatUser(){
        return this.etatUser;
    }

}
