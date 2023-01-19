package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controller.SessionController;
import view.MainFrame;

public class TCPListener extends Thread {

    // ** ATTRIBUTES **
    private boolean isRunning;
    private int port;
    private ServerSocket serverSocket;

    private MainFrame frame;
    

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
            serverSocket = new ServerSocket(port);
            while(isRunning){
                System.out.println("<Listener | "+ Thread.currentThread().getId() +" > : TCPListener is listening on port " + port);
                Socket link = serverSocket.accept();

                SessionController.sessionCreated(link);

                System.out.println("===update frame===");
                if (this.frame != null){
                    frame.updateUsersList();
                    System.out.println("===frame updated ?===");
                }
                //System.out.println("<Listener | "+ Thread.currentThread().getId() + " > : Socket printing\n" + link.toString());
                //TCPSession session = new TCPSession(link, UserController.getMyUser());
                //sessionsList.add(session);// a verifier
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeListener(){
        this.isRunning = false;
        try {
            serverSocket.close();
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
    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }
    public ServerSocket getSocket(){
        return serverSocket;
    }
    public void setSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
}
