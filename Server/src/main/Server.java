package main;

import response.CreateCourseResponse;
import response.Response;
import util.RandomString;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {

    private static Connection connection;
    private static RandomString randomString;

    public static void main(String[] args) {
        ServerSocket serverSocket= null;
        Socket socket;
        try {
            serverSocket=new ServerSocket(6969);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                System.out.println("Listening for clients");
                assert serverSocket != null;
                socket=serverSocket.accept();
                System.out.println("connection established with client");
                Thread thread=new Thread(new RequestIdentifier(socket));
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
    public static void sendResponse(ObjectOutputStream outputStream, Response response){
        try {
            System.out.println("The response begin sent is " + response);
            if(response instanceof CreateCourseResponse)
            System.out.println("This response is non null and team code = " + ((CreateCourseResponse) response).getTeamCode());
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object receiveRequest(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
            return inputStream.readObject();
    }

    public static String getRandomString() {
        if(randomString == null) {
            randomString = new RandomString(8);
        }
        return randomString.nextString();
    }
}
