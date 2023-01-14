package network;

import model.User;
import model.Message;
import model.Session;
import controller.FrameController;
import controller.SessionController;
import controller.UserController;

import java.io.*;
import java.lang.ModuleLayer.Controller;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TCPSession extends Thread{

    // ** ATTRIBUTES **
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isRunning;
    private PrintWriter writer;
    private BufferedReader bufferedReader;
    private User userDist;
    private User myUser = UserController.getMyUser();

    /**
     * Constructor of the TCPSession when a use start a session with us
     * @param link
     */
    public TCPSession(Socket link, User userDist) {
        setSocket(link);
        this.userDist = userDist;
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
        this.userDist = userdist;
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

    public void sendMessage(String data){
        Message msg = new Message(myUser, userDist, data);//TODO : ajouter le temps au message
        SessionController.archiveMsg(msg, userDist);
        System.out.println("<Session | " + Thread.currentThread().getId() +" >  Sending message : " + msg.getData());
        writer.println(msg.getData());
    }

    /**
     * run method
     * This thread is used to listen for incoming messages, every message sending will be executed externally
     */
    public void run(){
        setRunning(true);
        String data = null;
        Message msg = new Message();
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : TCPSession is running, a connection has been established");
        while(isRunning){
            try {
                data = bufferedReader.readLine();

                msg.setData(data);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                msg.setTime(dtf.format(now));
                msg.setSender(userDist);
                msg.setReceiver(myUser);
                SessionController.archiveMsg(msg, userDist);
                // To printin the data in Terminal
                System.out.println("<Session | " + Thread.currentThread().getId() +" >  Message recu : " + data);
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
