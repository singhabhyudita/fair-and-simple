import Request.LoginRequest;

import Request.RegisterRequest;
import Request.TeacherLoginRequest;
import Request.TeacherRegisterRequest;
import RequestHandler.LoginRequestHandler;
import RequestHandler.RegisterRequestHandler;
import RequestHandler.TeacherLoginRequestHandler;
import RequestHandler.TeacherRegisterRequestHandler;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class RequestIdentifier implements Runnable{
    Socket socket;
    ObjectOutputStream oos=null;
    ObjectInputStream ois=null;
    public RequestIdentifier(Socket socket){
        this.socket=socket;
        try {
            oos=new ObjectOutputStream(socket.getOutputStream());
            ois=new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()){
            Object request=Server.ReceiveRequest(ois);
            if(request==null)break;
            else if(request instanceof LoginRequest){

                LoginRequestHandler loginRequestHandler=new LoginRequestHandler(oos,(LoginRequest)request,Server.getConnection());
                try {
                    loginRequestHandler.sendResponse();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(request instanceof RegisterRequest){
                RegisterRequestHandler registerRequestHandler=new RegisterRequestHandler((RegisterRequest)request,oos,Server.getConnection());
                try {
                    registerRequestHandler.sendResponse();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(request instanceof TeacherLoginRequest){
                TeacherLoginRequestHandler teacherLoginRequestHandler=new TeacherLoginRequestHandler(Server.getConnection(),oos,(TeacherLoginRequest)request);
                try {
                    teacherLoginRequestHandler.sendResponse();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(request instanceof TeacherRegisterRequest){
                TeacherRegisterRequestHandler teacherRegisterRequestHandler=new TeacherRegisterRequestHandler(Server.getConnection(), oos,(TeacherRegisterRequest)request);
                try {
                    teacherRegisterRequestHandler.sendResponse();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
