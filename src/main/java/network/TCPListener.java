package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPListener extends Thread {

    // ** ATTRIBUTES **
    private boolean isRunning;
    private int port;
    //table of all the sessions sockets
    public ArrayList<TCPSession> sessionsList;

    /**
     * Constructor of the TCPListener class
     * TCP Listener is just a thread that listen for incoming TCP connections
     * @param port
     */
    public TCPListener(int port){
        setPort(port);
        start();
    }

    /**
     * run method
     */
    public void run(){
        this.isRunning = true;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(isRunning){
                System.out.println("<Listener | "+ Thread.currentThread().getId() +" > : TCPListener is listening on port " + port);
                Socket link = serverSocket.accept();
                TCPSession session = new TCPSession(link);
                sessionsList.add(session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
