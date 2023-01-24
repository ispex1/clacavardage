package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controller.SessionController;
import view.MainFrame;

import javafx.application.Platform;


/**
 * TCPListener class is used to listen for TCP packets on a given port,
 * we can listen for connexion responses, changing pseudo response
 */
public class TCPListener extends Thread {
    private boolean isRunning; // Boolean to know if the thread is running
    private int port; // Port to listen on
    private ServerSocket serverSocket; // Server socket
    private MainFrame frame; // Main frame of the application
    

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
     * This method is called when the thread is started
     * It listens for incoming TCP connections
     * When a connection is received, it creates a new TCPSession
     * It also adds the session to the sessions list
     * It also updates the chat pane
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

    /**
     * It stops the thread
     * By setting the isRunning boolean to false
     * It also closes the server socket
     */
    public void closeListener(){
        this.isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter for the port
     * @param port , the port to listen to
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Setter for the main frame
     * @param frame , the main frame of the application
     */
    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }
}
