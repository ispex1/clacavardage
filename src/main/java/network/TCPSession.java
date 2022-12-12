package network;

import model.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPSession extends Thread{

    // ** ATTRIBUTES **
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isRunning;
    private PrintWriter writer;
    private BufferedReader bufferedReader;
    //TODO mettre des attributs avec les communicants des deux utilisateurs
    /**
     * Constructor of the TCPSession when a use start a session with us
     * @param link
     */
    public TCPSession(Socket link) {
        setSocket(link);
        try {
            setInputStream(link.getInputStream());
            setOutputStream(link.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Input buffer setup
        InputStreamReader reader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(reader);
        //Output buffer setup
        writer = new PrintWriter(outputStream, true);
        //starting the thread
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : TCPSession asked by " + link.getInetAddress().getHostAddress());
        start();
    }

    /**
     * Constructor of the TCPSession when we start a session with a user
     */
    public TCPSession(User userdist, int port) {
        try {
            setSocket(new Socket(InetAddress.getByName(userdist.getIP()), port));

            //le code bloque ici
            setInputStream(socket.getInputStream());
            setOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Input buffer setup
        InputStreamReader reader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(reader);
        //Output buffer setup
        writer = new PrintWriter(outputStream, true);
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : Trying to connect to " + userdist.getIP() + " on port " + port);

        //starting the thread
        start();

    }

    public void sendMessage(String message){
        System.out.println("<Session | " + Thread.currentThread().getId() +" >  Sending message : " + message);
        writer.println(message);
    }

    /**
     * run method
     * This thread is used to listen for incoming messages, every message sending will be executed externally
     */
    public void run(){
        setRunning(true);
        String message;
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : TCPSession is running, a connection has been established");
        while(isRunning){
            try {
                message = bufferedReader.readLine();
                //TODO Appel fonction pour afficher le message dans la fenetre
                // Just printing the data for now
                System.out.println("<Session | " + Thread.currentThread().getId() +" >  Message recu : " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
