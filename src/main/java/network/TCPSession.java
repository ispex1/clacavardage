package network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import controller.SessionController;
import controller.UserController;
import database.DatabaseManager;
import model.Message;
import model.User;
import view.OpenedChatFrame;

import javafx.application.Platform;

import static controller.SessionController.sessionsList;

public class TCPSession extends Thread{

    private Socket socket; // Socket of the TCP connection
    private InputStream inputStream; // Input stream of the TCP connection
    private OutputStream outputStream; // Output stream of the TCP connection
    private boolean isRunning; // Boolean to know if the thread is running
    private final PrintWriter writer; // PrintWriter
    private final BufferedReader bufferedReader; // BufferedReader
    private final User userDist; // User distant
    private final User myUser = UserController.getMyUser(); // My user
    private final Message msg = new Message(); // Message
    public Boolean isOpenDisplayed = false; // Boolean to know if the chat is opened
    private view.OpenedChatFrame openedFrame; // Opened chat frame

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
        //Starting the thread
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : TCPSession asked by " + link.getInetAddress().getHostAddress());

        assert userDist != null;
        DatabaseManager.createNewConvo(userDist.getIP());
        start();
    }

    /**
     * Constructor of the TCPSession when we start a session with a user
     * @param userDist , the user distant
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

    /**
     * It writes a message to the writer
     * @param data , the message to write
     */
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
     * When a message is received, it is added to the messages list
     */
    public void run(){
        setRunning(true);
        String data;
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : TCPSession is running, a connection has been established");
        while(isRunning){
            try {
                data = bufferedReader.readLine();
                if (data != null) {
                    // Split data to get sender receiver message and date
                    String[] dataSplit = data.split("\\|");

                    String message = dataSplit[2].split(";")[1];

                    msg.setData(message);
                    msg.setTime(dataSplit[3].split(";")[1]);
                    msg.setSender(userDist);
                    msg.setReceiver(myUser);

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
                            openedFrame.parentController.updateChatter();
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

    /**
     * Close the session
     * It set the boolean isRunning to false
     * It closes the run method
     * It closes the socket
     */
    public void closeSession(){
        this.isRunning = false;

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter of the TCPSession
     * @return the TCPSession
     */
    public TCPSession getTCPSession(){
        return this;
    }

    /**
     * Setter of the socket
     * @param socket , the socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Getter of the socket
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Setter of the input stream
     * @param inputStream , the input stream
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Setter of the output stream
     * @param outputStream , the output stream
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Setter of the isRunning boolean
     * @param isRunning , the boolean
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    /**
     * Getter of UserDist
     * @return the user distant
     */
    public User getUserDist() {
        return userDist;
    }

    /**
     * Setter of the openDisplayed boolean
     * @param display , the openDisplayed boolean
     */
    public void setOpenDisplay(Boolean display) {
        isOpenDisplayed = display;
    }

    /**
     * Setter of the opened frame
     * @param frame , the opened chat frame corresponding
     */
    public void setOpenedFrame(OpenedChatFrame frame){
        this.openedFrame = frame;
    }
}
