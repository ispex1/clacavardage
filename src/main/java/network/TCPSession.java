package network;

import model.User;

import java.io.*;
import java.net.Socket;

public class TCPSession extends Thread{

    // ** ATTRIBUTES **
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    /**
     * Constructor of the TCPSession when a use start a session with us
     * @param link
     */
    public TCPSession(Socket link) {
        setSocket(link);
        start();
    }

    /**
     * Constructor of the TCPSession when we start a session with a user
     */
    public TCPSession(User userdist) {
        try {
            setSocket(new Socket(userdist.getIP(), userdist.getPort()));
            setInputStream(new ObjectInputStream(socket.getInputStream()));
            setOutputStream(new ObjectOutputStream(socket.getOutputStream()));
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void run(){
        //TODO
    }

    // ** GETTERS AND SETTERS **
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public OutputStream getOutputStream() {
        return outputStream;
    }
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
