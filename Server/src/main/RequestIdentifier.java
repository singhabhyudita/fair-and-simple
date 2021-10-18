package main;

import request.*;
import requestHandler.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class RequestIdentifier implements Runnable{
    Socket socket;
    ObjectOutputStream oos=null;
    ObjectInputStream ois=null;
    String userId;

    public RequestIdentifier(Socket socket){
        this.socket=socket;
        try {
            oos=new ObjectOutputStream(socket.getOutputStream());
            ois=new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0); // dont create this client connection.
        }
    }

    @Override
    public void run() {
        System.out.println("We are here");
        while (socket.isConnected()){
            Object request= null;
            try {
                System.out.println("Waiting for a request");
                request = Server.receiveRequest(ois);
                System.out.println("Request received");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace(); // basically the client disconnected so end this thread.
                return;
            }
            System.out.println("Request came");
            if(request==null) break;
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
                System.out.println("Teacher register request came.");
                TeacherRegisterRequestHandler teacherRegisterRequestHandler=new TeacherRegisterRequestHandler(Server.getConnection(), oos,(TeacherRegisterRequest)request);
                try {
                    teacherRegisterRequestHandler.sendResponse();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if(request instanceof TeacherCoursesRequest) {
                TeacherCoursesRequestHandler handler = new TeacherCoursesRequestHandler(Server.getConnection(), oos, (TeacherCoursesRequest) request);
                handler.sendResposne();
            } else if(request instanceof CreateTeamRequest) {
                System.out.println("Request is a create team request by teacher " + ((CreateTeamRequest) request).getTeacherId());
                CreateTeamRequestHandler handler = new CreateTeamRequestHandler(Server.getConnection(), oos, (CreateTeamRequest) request);
                handler.sendResponse();
            } else if(request instanceof ExamResultRequest) {
                ExamResultRequestHandler handler = new ExamResultRequestHandler(Server.getConnection(), oos, (ExamResultRequest) request);
                handler.sendResponse();
            } else if(request instanceof SetExamRequest) {
                SetExamRequestHandler handler = new SetExamRequestHandler(Server.getConnection(), oos, (SetExamRequest) request);
                handler.sendResponse();
            } else if(request instanceof ExamsHistoryRequest) {
                ExamsHistoryRequestHandler handler = new ExamsHistoryRequestHandler(Server.getConnection(), oos, (ExamsHistoryRequest) request,this.userId);
                handler.sendResponse();
            } else if(request instanceof UpcomingExamsRequest) {
                UpcomingExamsRequestHandler handler = new UpcomingExamsRequestHandler(Server.getConnection(), oos, (UpcomingExamsRequest) request,this.userId);
                handler.sendResponse();
            } else if(request instanceof ChangeProfilePicRequest) {
                ChangeProfilePicRequestHandler handler = new ChangeProfilePicRequestHandler(Server.getConnection(), oos, (ChangeProfilePicRequest) request,((ChangeProfilePicRequest) request).getFileInputStream() ,this.userId);
                handler.sendResponse();
            } else if(request instanceof GetProfilePicRequest) {
                GetProfilePicRequestHandler handler = new GetProfilePicRequestHandler(Server.getConnection(), oos, (GetProfilePicRequest) request ,this.userId);
                handler.sendResponse();
            }
        }
    }
}
