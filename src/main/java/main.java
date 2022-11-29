import java.net.*;
import java.util.Enumeration;

import database.DatabaseManager;
import model.Message;
import model.user.*;

public class Main{

    // ip 12:48:42 -> 192.168.56.1

    public static void main (String[] args){
        Message msg = new Message("gros caca");
        User este = new User("este");
        User gaboche = new User("gaboche");
        msg.setSender(este);
        msg.setReceiver(gaboche);
        System.out.println(msg.toString());

        DatabaseManager db = new DatabaseManager();

        InetAddress ia = getCurrentIp();  
        String str = ia.getHostAddress();  
        System.out.println(str);
    }
    
    public static InetAddress getCurrentIp() { 
        try { 
            Enumeration networkInterfaces = NetworkInterface .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) { 
                NetworkInterface ni = (NetworkInterface) networkInterfaces .nextElement(); 
                Enumeration nias = ni.getInetAddresses(); 
                while(nias.hasMoreElements()) { 
                    InetAddress ia= (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) { 
                        return ia; 
                    } 
                }
            }
        } 
        catch (SocketException e) {
            System.out.println("unable to get current IP " + e.getMessage());
        } 
    return null;
    } 

}
