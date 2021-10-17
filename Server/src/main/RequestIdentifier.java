package main;

import request.*;
import requestHandler.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RequestIdentifier implements Runnable{
    Socket socket;
    ObjectOutputStream oos=null;
    ObjectInputStream ois=null;
    public static String userID;

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
            Object request;
            try {
                System.out.println("Waiting for a request");
                request = Server.receiveRequest(ois);
                System.out.println("Request received");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace(); // basically the client disconnected so end this thread.
                break;
            }
            System.out.println("Request came");
            if(request==null) break;
            else if(request instanceof LoginRequest){
                userID=((LoginRequest) request).getUsername();
                LoginRequestHandler loginRequestHandler=new LoginRequestHandler(oos,(LoginRequest)request,Server.getConnection());
                loginRequestHandler.sendResponse();
            }
            else if(request instanceof RegisterRequest){
                RegisterRequestHandler registerRequestHandler=new RegisterRequestHandler((RegisterRequest)request,oos,Server.getConnection());
                registerRequestHandler.sendResponse();
            }
            else if(request instanceof TeacherLoginRequest){
                TeacherLoginRequestHandler teacherLoginRequestHandler=new TeacherLoginRequestHandler(Server.getConnection(),oos,(TeacherLoginRequest)request);
                teacherLoginRequestHandler.sendResponse();
            }
            else if(request instanceof TeacherRegisterRequest){
                System.out.println("Teacher register request came.");
                TeacherRegisterRequestHandler teacherRegisterRequestHandler=new TeacherRegisterRequestHandler(Server.getConnection(), oos,(TeacherRegisterRequest)request);
                teacherRegisterRequestHandler.sendResponse();
            } else if(request instanceof TeacherCoursesRequest) {
                TeacherCoursesRequestHandler handler = new TeacherCoursesRequestHandler(Server.getConnection(), oos, (TeacherCoursesRequest) request);
                handler.sendResposne();
            } else if(request instanceof CreateCourseRequest) {
                System.out.println("Request is a create team request by teacher " + ((CreateCourseRequest) request).getTeacherId());
                CreateCourseRequestHandler handler = new CreateCourseRequestHandler(Server.getConnection(), oos, (CreateCourseRequest) request);
                handler.sendResponse();
            } else if(request instanceof ExamResultRequest) {
                ExamResultRequestHandler handler = new ExamResultRequestHandler(Server.getConnection(), oos, (ExamResultRequest) request);
                handler.sendResponse();
            } else if(request instanceof SetExamRequest) {
                SetExamRequestHandler handler = new SetExamRequestHandler(Server.getConnection(), oos, (SetExamRequest) request);
                handler.sendResponse();
            } else if(request instanceof TeacherExamRequest) {
                TeacherExamRequestHandler handler = new TeacherExamRequestHandler(Server.getConnection(), oos, (TeacherExamRequest) request);
                handler.sendResponse();
            } else if(request instanceof LogOutRequest) {
                break; // get out of the infinite loop.
            } else if(request instanceof TeacherChangePasswordRequest) {
                TeacherChangePasswordRequestHandler handler = new TeacherChangePasswordRequestHandler(Server.getConnection(), oos, (TeacherChangePasswordRequest) request);
                handler.sendResponse();
            }
            else if(request instanceof JoinCourseRequest){
                JoinCourseRequestHandler joinCourseRequestHandler=new JoinCourseRequestHandler(Server.getConnection(),oos,(JoinCourseRequest)request);
                joinCourseRequestHandler.sendResponse();
            }
            else if(request instanceof CoursesListRequest){
                CoursesListRequestHandler coursesListRequestHandler=new CoursesListRequestHandler(Server.getConnection(),oos);
                coursesListRequestHandler.sendResponse();
            }
            else if(request instanceof ChangePasswordRequest){
                ChangePasswordRequestHandler changePasswordRequestHandler=new ChangePasswordRequestHandler(Server.getConnection(),oos,(ChangePasswordRequest)request);
                changePasswordRequestHandler.sendResponse();
            }
            else if(request instanceof LogOutRequest){
                LogOutRequestHandler logOutRequestHandler=new LogOutRequestHandler(Server.getConnection(),oos);
                logOutRequestHandler.sendResponse();
            }
            else if(request instanceof ParticipantsListRequest){
                ParticipantsListRequestHandler participantsListRequestHandler=new ParticipantsListRequestHandler(Server.getConnection(),oos,(ParticipantsListRequest)request);
                participantsListRequestHandler.sendResponse();
            }
            else if(request instanceof ExamsListRequest){
                ExamsListRequestHandler examsListRequestHandler=new ExamsListRequestHandler(Server.getConnection(),oos,(ExamsListRequest)request);
                examsListRequestHandler.sendResponse();
            }
            else{
                Server.sendResponse(oos, null);
            }
        }
        System.out.println("Client disconnected!!");
    }
}
