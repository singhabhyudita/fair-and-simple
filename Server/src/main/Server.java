package main;

import entity.RegistrationStreamWrapper;
import entity.TeacherIdStreamWrapper;
import response.CreateCourseResponse;
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

    private static Connection connection;
    private static RandomString randomString;
    public static ArrayList<RegistrationStreamWrapper> socketArrayList=new ArrayList<>();
    public static ArrayList<TeacherIdStreamWrapper> teacherSocketArrayList=new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket= null,chatServerSocket=null;
        Socket socket,chatSocket;
        try {
            serverSocket=new ServerSocket(6969);
            chatServerSocket=new ServerSocket(6970);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                System.out.println("Listening for clients");
                assert serverSocket != null;
                socket=serverSocket.accept();
                System.out.println("request socket created");
                System.out.println(socket);
                Thread thread=new Thread(new RequestIdentifier(socket, chatServerSocket));
                thread.start();

                Thread notification=new Thread(new SendNotification());
                notification.start();
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
            connection= DriverManager.getConnection(url,"utkarsh","Hello@123");
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
