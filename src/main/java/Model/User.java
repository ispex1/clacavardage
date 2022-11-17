package Model;

public class User {
    private static String pseudo;
    private static String IP;
    private static int port;

    public static void user (){

    }

    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }

    public String getPseudo(){
        return this.pseudo;
    }

    public void setIP(String IP){
        this.IP = IP;
    }

    public String getIP(){
        return this.IP;
    }

    public void setPort(int port){
        this.port = port;
    }

    public int getPort(){
        return this.port;
    }
}
