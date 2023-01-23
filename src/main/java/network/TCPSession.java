package network;

import controller.SessionController;
import controller.UserController;
import database.DatabaseManager;
import javafx.application.Platform;
import model.Message;
import model.User;
import view.OpenedChatFrame;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static controller.SessionController.sessionsList;

public class TCPSession extends Thread{

    // ** ATTRIBUTES **
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isRunning;
    private final PrintWriter writer;
    private final BufferedReader bufferedReader;
    private final User userDist;
    private final User myUser = UserController.getMyUser();
    private final Message msg = new Message();
    public Boolean isOpenDisplayed = false;
    private view.OpenedChatFrame openedFrame;

    /**
     * Constructor of the TCPSession when a user start a session with us
     * @param link , the socket of the connection
     */
    public TCPSession(Socket link) {
        setSocket(link);
        this.userDist = UserController.getUserByIP(link.getInetAddress().getHostAddress());
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

        //creating a table in the DB, SQL will check if the table already exists
        assert userDist != null;
        DatabaseManager.createNewConvo(userDist.getIP());
        start();
    }

    /**
     * Constructor of the TCPSession when we start a session with a user
     */
    public TCPSession(User userDist) {
        this.userDist = userDist;
        try {
            setSocket(new Socket(InetAddress.getByName(userDist.getIP()), SessionController.PORT));
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
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : Trying to connect to " + userDist.getIP() + " on port " + SessionController.PORT);

        DatabaseManager.createNewConvo(userDist.getIP());
        start();
    }

    public void sendMessage(String data){
        msg.setSender(myUser);
        msg.setReceiver(userDist);
        msg.setData(data);
        System.out.println("<Session | " + Thread.currentThread().getId() +" >  Sending message : " + msg.getData());
        writer.println(msg.getData());
    }

    /**
     * run method
     * This thread is used to listen for incoming messages, every message sending will be executed externally
     */
    public void run(){
        setRunning(true);
        String data;
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : TCPSession is running, a connection has been established");
        while(isRunning){
            try {
                data = bufferedReader.readLine();
                if (data != null) {
                    // split data to get sender receiver message and date
                    String[] dataSplit = data.split("\\|");

                    String message = dataSplit[2].split(";")[1];

                    msg.setData(message);
                    msg.setTime(dataSplit[3].split(";")[1]);
                    msg.setSender(userDist);
                    msg.setReceiver(myUser);
                    //print all element of the message
                    System.out.println("<Session | " + Thread.currentThread().getId() + " > : Message received from " + msg.getSender().getPseudo() + " : " + msg.getData());

                    DatabaseManager.insertMessage(userDist.getIP(), msg);
                    if (isOpenDisplayed) {
                        Platform.runLater(() -> openedFrame.receiveMessage(msg));
                    }
                } else {
                    System.out.println("<Session | " + Thread.currentThread().getId() + " > : Connection closed by " + userDist.getIP());
                    Platform.runLater(() -> {
                        sessionsList.remove(getTCPSession());

                        if (isOpenDisplayed) {
                            try {
                                openedFrame.parentController.updateChatter();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    setRunning(false);
                    socket.close();
                }
            } catch (IOException e) {
                if (isRunning) e.printStackTrace();
            }
        }
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : TCPSession is closed");
    }

    public void closeSession(){
        this.isRunning = false;

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ** GETTERS AND SETTERS **
    public TCPSession getTCPSession(){
        return this;
    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    public User getUserDist() {
        return userDist;
    }
    public void setOpenDisplay(Boolean display) {
        isOpenDisplayed = display;
    }
    public void setOpenedFrame(OpenedChatFrame frame){
        this.openedFrame = frame;
    }
}
