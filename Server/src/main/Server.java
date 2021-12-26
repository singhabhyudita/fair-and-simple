package main;

import entity.RegistrationStreamWrapper;
import entity.TeacherIdStreamWrapper;
import response.Response;
import util.RandomString;
import util.SendNotification;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server {

    //Declaring the required variables.
    private static Connection connection;
    private static RandomString randomString;

    // Creating arrayLists of Wrapper class which contains the userId along with
    // the ObjectOutputStream, which are used to send messages and notifications
    // to both student and teacher respectively.
    public static ArrayList<RegistrationStreamWrapper> socketArrayList=new ArrayList<>();
    public static ArrayList<TeacherIdStreamWrapper> teacherSocketArrayList=new ArrayList<>();

    public static void main(String[] args) {
        //Declaring sockets
        ServerSocket serverSocket= null;
        ServerSocket chatServerSocket=null;
        Socket socket;
        try {
            // Creating Server Sockets, one for client requests and Chat.
            serverSocket=new ServerSocket(6969);
            chatServerSocket=new ServerSocket(6970);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                assert serverSocket != null;

                //ServerSocket accepts the incoming socket from the client.
                socket=serverSocket.accept();

                //Starting a thread that listens for client requests and creates a chat socket connection.
                Thread thread=new Thread(new RequestIdentifier(socket, chatServerSocket));
                thread.start();

                //Starting a thread that sends notifications to client dynamically
                Thread notification=new Thread(new SendNotification());
                notification.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  Static method to create a connection with database using JDBC.
     *  Need to add the my 'mysql-connector-java-8.0.21' Jar in the library.
     *   Import the DriverManager class from the java.sql package
     * @return SQL Connection
     */
    public static Connection getConnection() {
        if(connection!=null)return connection;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/fairnsimple";
            connection= DriverManager.getConnection(url,"root","12345678");
            System.out.println("Database connected!!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Static function to send the response object to the client.
     * @param outputStream we use the write method of this objectOutputStream instance to send the response.
     * @param response Object to be sent
     */
    public static void sendResponse(ObjectOutputStream outputStream, Response response){
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Static method to receive requests from the client
     * @param inputStream used to read the incoming object
     * @return returns an Object
     */
    public static Object receiveRequest(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }

    /**
     * Used to generate random strings
     * @return Returns a String object
     */
    public static String getRandomString() {
        if(randomString == null) {
            randomString = new RandomString(8);
        }
        return randomString.nextString();
    }
}
