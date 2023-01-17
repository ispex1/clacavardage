package network;

import database.DatabaseManager;
import javafx.collections.ObservableList;
import model.User;
import model.Message;
import controller.SessionController;
import controller.UserController;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    private ArrayList<Message> history = new ArrayList<>();


    /**
     * Constructor of the TCPSession when a user start a session with us
     * @param link
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
        DatabaseManager.createNewConvo(userDist.getIP());
        start();
    }

    /**
     * Constructor of the TCPSession when we start a session with a user
     */
    public TCPSession(User userdist) {
        this.userDist = userdist;
        try {
            setSocket(new Socket(InetAddress.getByName(userdist.getIP()), SessionController.PORT));
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
        System.out.println("<Session | "+ Thread.currentThread().getId() +" > : Trying to connect to " + userdist.getIP() + " on port " + SessionController.PORT);

        //on creer une table dans la BDD, c'est le code SQL qui se charge de verifier si la table existe deja
        DatabaseManager.createNewConvo(userdist.getIP());
        //starting the thread
        start();

    }

    public void sendMessage(String data){
        Message msg = new Message(myUser, userDist, data);
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

                msg.setData((data.split("|")[2]).split(":")[1]);


                //msg.setData(data);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                msg.setTime(dtf.format(now));
                msg.setSender(userDist);
                msg.setReceiver(myUser);
                DatabaseManager.insertMessage(userDist.getIP(), msg);
                history = DatabaseManager.getHistory(userDist.getIP());
                // To printin the data in Terminal
                System.out.println("<Session | " + Thread.currentThread().getId() +" >  Message recu : " + data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public User getUserDist() {
        return userDist;
    }
    public ArrayList<Message> getHistory() {
        return history;
    }
}
