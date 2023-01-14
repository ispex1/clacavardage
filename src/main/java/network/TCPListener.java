package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import controller.UserController;

public class TCPListener extends Thread {

    // ** ATTRIBUTES **
    private boolean isRunning;
    private int port;
    private ServerSocket serverSocket;
    //table of all the sessions sockets
    public ArrayList<TCPSession> sessionsList;

    /**
     * Constructor of the TCPListener class
     * TCP Listener is just a thread that listen for incoming TCP connections
     * @param port
     */
    public TCPListener(int port){
        setPort(port);
        sessionsList = new ArrayList<TCPSession>();
        start();
    }

    /**
     * run method
     */
    public void run(){
        this.isRunning = true;

        try {
            serverSocket = new ServerSocket(port);
            while(isRunning){
                System.out.println("<Listener | "+ Thread.currentThread().getId() +" > : TCPListener is listening on port " + port);
                Socket link = serverSocket.accept();
                System.out.println("<Listener | "+ Thread.currentThread().getId() + " > : Socket printing\n" + link.toString());
                TCPSession session = new TCPSession(link, UserController.getMyUser());
                sessionsList.add(session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopListner(){
        this.isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TCPSession getSessionWithAdress(String ipString){
        InetAddress ip;
        try {
            ip = InetAddress.getByName(ipString);
            for(TCPSession session : sessionsList){
                if(session.getSocket().getInetAddress().equals(ip)){
                    return session;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ** GETTERS AND SETTERS**
    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public ServerSocket getSocket(){
        return serverSocket;
    }
    public void setSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
}
