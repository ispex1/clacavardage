package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controller.SessionController;
import javafx.application.Platform;
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
     * @param port , the port to listen to
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

                if (this.frame != null && this.frame.isShowing()) {
                    Platform.runLater(() -> {
                        try {
                            frame.updateChatPane();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        } catch (IOException e) {
            if (isRunning) e.printStackTrace();
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
    public void setPort(int port) {
        this.port = port;
    }
    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }
}
