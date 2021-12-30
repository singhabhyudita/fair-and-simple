package main;

import entity.*;
import request.*;
import requestHandler.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  This class acts as an identifier that categorises the requests and handles them accordingly.
 *  Implements runnable interface so that it's thread can be created.
 */
public class RequestIdentifier implements Runnable{

    //Declaring and initialising required variables.
    Socket socket;
    ObjectOutputStream oos=null;
    ObjectInputStream ois=null;
    ServerSocket chatServerSocket;
    public String userID;

    /**
     * Constructor that takes multiple socket parameters and initialises the I/O streams accordingly
     * @param socket is used to create ObjectOutput/Input streams through which communication takes place
     * @param chatServerSocket used to create a chat connection
     */
    public RequestIdentifier(Socket socket, ServerSocket chatServerSocket){
        this.socket=socket;
        this.chatServerSocket = chatServerSocket;
        try {
            oos=new ObjectOutputStream(socket.getOutputStream());
            ois=new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Abstract method of the Runnable interface.
     * This method is called when the thread starts.
     * Listens for requests from the client, uses if else to categorise the request
     * Creates the respective requestHandler instance and calls the sendResponse method
     * sendResponse method processes the request and sends the appropriate response back to the client
     */
    @Override
    public void run() {
        while (socket.isConnected()){
            Object request;
            // Waiting for the request to arrive
            try {
                request = Server.receiveRequest(ois);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }

            if(request instanceof LoginRequest){
                  //When login request arrives we create an instance of the LoginRequestHandler,
                  // pass the required parameters and call the sendResponse method.
                userID=((LoginRequest) request).getUsername();
                LoginRequestHandler loginRequestHandler=new LoginRequestHandler(oos,(LoginRequest)request,Server.getConnection());
                loginRequestHandler.sendResponse(userID);

                //Once the login is successful we create the chat connection
                if(loginRequestHandler.isLoginSuccessful()) {
                    try {
                        Socket chatSocket=chatServerSocket.accept();
                        ObjectOutputStream objectOutputStream=new ObjectOutputStream(chatSocket.getOutputStream());
                        ObjectInputStream objectInputStream = new ObjectInputStream(chatSocket.getInputStream());
                        String registrationNumber = (String) objectInputStream.readObject();

                        // After chat connection is created we maintain an ArrayList of the userID and their respective
                        // Output streams
                        Server.socketArrayList.add(new RegistrationStreamWrapper(registrationNumber, objectOutputStream));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(request instanceof RegisterRequest){
                RegisterRequestHandler registerRequestHandler=new RegisterRequestHandler((RegisterRequest)request,oos,Server.getConnection());
                registerRequestHandler.sendResponse(userID);
            }
            //Same for Teacher Login
            else if(request instanceof TeacherLoginRequest){
                userID=((TeacherLoginRequest) request).getUsername();
                TeacherLoginRequestHandler teacherLoginRequestHandler=new TeacherLoginRequestHandler(Server.getConnection(),oos,(TeacherLoginRequest)request);
                teacherLoginRequestHandler.sendResponse(userID);
                if(teacherLoginRequestHandler.isLoginSuccessful()) {
                    try {
                        Socket chatSocket=chatServerSocket.accept();
                        ObjectOutputStream objectOutputStream=new ObjectOutputStream(chatSocket.getOutputStream());
                        ObjectInputStream objectInputStream = new ObjectInputStream(chatSocket.getInputStream());
                        String teacherId = (String) objectInputStream.readObject();

                        Server.teacherSocketArrayList.add(new TeacherIdStreamWrapper(teacherId, objectOutputStream));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(request instanceof TeacherRegisterRequest){
                TeacherRegisterRequestHandler teacherRegisterRequestHandler=new TeacherRegisterRequestHandler(Server.getConnection(), oos,(TeacherRegisterRequest)request);
                teacherRegisterRequestHandler.sendResponse(userID);
            } else if(request instanceof TeacherCoursesRequest) {
                TeacherCoursesRequestHandler handler = new TeacherCoursesRequestHandler(Server.getConnection(), oos, (TeacherCoursesRequest) request);
                handler.sendResponse(userID);
            } else if(request instanceof CreateCourseRequest) {
                CreateCourseRequestHandler handler = new CreateCourseRequestHandler(Server.getConnection(), oos, (CreateCourseRequest) request);
                handler.sendResponse(userID);
            } else if(request instanceof ExamResultRequest) {
                ExamResultRequestHandler handler = new ExamResultRequestHandler(Server.getConnection(), oos, (ExamResultRequest) request);
                handler.sendResponse(userID);
            } else if(request instanceof SetExamRequest) {
                SetExamRequestHandler handler = new SetExamRequestHandler(Server.getConnection(), oos, (SetExamRequest) request);
                handler.sendResponse(userID);
            } else if(request instanceof TeacherExamRequest) {
                TeacherExamRequestHandler handler = new TeacherExamRequestHandler(Server.getConnection(), oos, (TeacherExamRequest) request);
                handler.sendResponse(userID);
            } else if(request instanceof TeacherChangePasswordRequest) {
                TeacherChangePasswordRequestHandler handler = new TeacherChangePasswordRequestHandler(Server.getConnection(), oos, (TeacherChangePasswordRequest) request);
                handler.sendResponse(userID);
            }
            else if(request instanceof JoinCourseRequest){
                JoinCourseRequestHandler joinCourseRequestHandler=new JoinCourseRequestHandler(Server.getConnection(),oos,(JoinCourseRequest)request);
                joinCourseRequestHandler.sendResponse(userID);
            }
            else if(request instanceof CoursesListRequest){
                CoursesListRequestHandler coursesListRequestHandler=new CoursesListRequestHandler(Server.getConnection(),oos);
                coursesListRequestHandler.sendResponse(userID);
            }
            else if(request instanceof ChangePasswordRequest){
                ChangePasswordRequestHandler changePasswordRequestHandler=new ChangePasswordRequestHandler(Server.getConnection(),oos,(ChangePasswordRequest)request);
                changePasswordRequestHandler.sendResponse(userID);
            }
            else if(request instanceof LogOutRequest){
                LogOutRequestHandler logOutRequestHandler=new LogOutRequestHandler(Server.getConnection(),oos);
                logOutRequestHandler.sendResponse(userID);
                deleteChatSocketConnection();
            }
            else if(request instanceof ParticipantsListRequest){
                ParticipantsListRequestHandler participantsListRequestHandler=new ParticipantsListRequestHandler(Server.getConnection(),oos,(ParticipantsListRequest)request);
                participantsListRequestHandler.sendResponse(userID);
            }
            else if(request instanceof ExamsListRequest){
                ExamsListRequestHandler examsListRequestHandler=new ExamsListRequestHandler(Server.getConnection(),oos,(ExamsListRequest)request);
                examsListRequestHandler.sendResponse(userID);
            }
            else if(request instanceof CourseStudentRequest) {
                CourseStudentRequestHandler courseStudentRequestHandler = new CourseStudentRequestHandler(Server.getConnection(), oos, (CourseStudentRequest) request);
                courseStudentRequestHandler.sendResponse(userID);
            }
            else if(request instanceof TeacherChangeProfilePictureRequest) {
                TeacherChangeProfilePictureRequestHandler teacherChangeProfilePictureRequestHandler = new TeacherChangeProfilePictureRequestHandler(Server.getConnection(), oos, (TeacherChangeProfilePictureRequest) request);
                teacherChangeProfilePictureRequestHandler.sendResponse(userID);
            }
            else if(request instanceof AttemptExamRequest) {
                AttemptExamRequestHandler requestHandler = new AttemptExamRequestHandler(Server.getConnection(), oos, (AttemptExamRequest) request);
                requestHandler.sendResponse(userID);
            }
            else if(request instanceof SubmitExamRequest) {
                SubmitExamRequestHandler requestHandler = new SubmitExamRequestHandler(Server.getConnection(), oos, ois, (SubmitExamRequest) request);
                requestHandler.sendResponse(userID);
            }
            else if (request instanceof CourseDetailsRequest){
                CourseDetailsRequestHandler courseDetailsRequestHandler=new CourseDetailsRequestHandler(Server.getConnection(),oos,(CourseDetailsRequest)request);
                courseDetailsRequestHandler.sendResponse(userID);
            }
            else if (request instanceof ChangeProfilePicRequest){
                ChangeProfilePicRequestHandler changeProfilePicRequestHandler=new ChangeProfilePicRequestHandler(Server.getConnection(),oos,(ChangeProfilePicRequest)request);
                changeProfilePicRequestHandler.sendResponse(userID);
            }
            else if( request instanceof GetProfilePicRequest){
                GetProfilePicRequestHandler getProfilePicRequestHandler=new GetProfilePicRequestHandler(Server.getConnection(),oos,(GetProfilePicRequest)request);
                getProfilePicRequestHandler.sendResponse(userID);
            }
            else if(request instanceof UpcomingExamsRequest){
                UpcomingExamsRequestHandler upcomingExamsRequestHandler=new UpcomingExamsRequestHandler(Server.getConnection(),oos,(UpcomingExamsRequest)request);
                upcomingExamsRequestHandler.sendResponse(userID);
            }
            else if(request instanceof ExamsHistoryRequest){
                ExamsHistoryRequestHandler examsHistoryRequestHandler=new ExamsHistoryRequestHandler(Server.getConnection(),oos,(ExamsHistoryRequest)request);
                examsHistoryRequestHandler.sendResponse(userID);
            }
            else if(request instanceof ChangeTeacherProfilePicRequest){
                ChangeTeacherProfilePicRequestHandler changeTeacherProfilePicRequestHandler=new ChangeTeacherProfilePicRequestHandler(Server.getConnection(),oos,(ChangeTeacherProfilePicRequest)request);
                changeTeacherProfilePicRequestHandler.sendResponse(userID);
            }
            else if(request instanceof GetTeacherProfilePicRequest) {
                GetTeacherProfilePicRequestHandler getTeacherProfilePicRequestHandler = new GetTeacherProfilePicRequestHandler(Server.getConnection(), oos, (GetTeacherProfilePicRequest) request);
                getTeacherProfilePicRequestHandler.sendResponse(userID);
            }
            else if(request instanceof Warning) {
                SendMessageRequestHandler sendMessageRequestHandler = new SendMessageRequestHandler(Server.getConnection(), oos, (Message) request);
//                sendMessageRequestHandler.sendResponse(userID);
                // Sends the message to every connected client from the course in which the message was sent
                sendMessageRequestHandler.sendWarningToRecipient((Warning)request);
            }else if(request instanceof AddedInformation) {
                System.out.println("Added Information has to be sent to = " + ((AddedInformation) request).getReceiverId());
                SendMessageRequestHandler sendMessageRequestHandler = new SendMessageRequestHandler(Server.getConnection(), oos, (Message) request);
//                sendMessageRequestHandler.sendResponse(userID);
                sendMessageRequestHandler.sendAddedInformationToRecipient((AddedInformation) request);
            } else if(request instanceof ExamScheduledInformation) {
                SendMessageRequestHandler sendMessageRequestHandler = new SendMessageRequestHandler(Server.getConnection(), oos, (Message) request);
                sendMessageRequestHandler.sendResponse(userID);
                sendMessageRequestHandler.sendToAll();
            }
            else if(request instanceof Message) {
                SendMessageRequestHandler sendMessageRequestHandler = new SendMessageRequestHandler(Server.getConnection(), oos, (Message) request);
                sendMessageRequestHandler.sendResponse(userID);
                  // Sends the message to every connected client from the course in which the message was sent
                sendMessageRequestHandler.sendToAll();
            }
            else if(request instanceof ProctoringDutyRequest) {
                ProctoringDutyRequestHandler requestHandler = new ProctoringDutyRequestHandler(Server.getConnection(), oos, (ProctoringDutyRequest) request);
                requestHandler.sendResponse(userID);
            }
            else if(request instanceof GetQuestionsRequest) {
                GetQuestionsRequestHandler getQuestionsRequestHandler = new GetQuestionsRequestHandler(Server.getConnection(),oos,(GetQuestionsRequest) request);
                getQuestionsRequestHandler.sendResponse(userID);
            }
            else if(request instanceof ProctoringRequest) {
                ProctoringRequestHandler requestHandler = new ProctoringRequestHandler(Server.getConnection(), oos, (ProctoringRequest) request);
                requestHandler.sendResponse(userID);
            }
            else if(request instanceof DisplayMessagesRequest) {
                DisplayMessagesRequestHandler displayMessagesRequestHandler = new DisplayMessagesRequestHandler(Server.getConnection(), oos, (DisplayMessagesRequest) request);
                displayMessagesRequestHandler.sendResponse(userID);
            }
            else if(request instanceof GetNotificationRequest){
                GetNotificationRequestHandler getNotificationRequestHandler=new GetNotificationRequestHandler(Server.getConnection(), oos);
                getNotificationRequestHandler.sendResponse(userID);
            }
            else if(request instanceof SubmitCorrectionRequest) {
                System.out.println("This is the handler");
                SubmitCorrectionRequestHandler handler = new SubmitCorrectionRequestHandler(Server.getConnection(), oos, (SubmitCorrectionRequest) request);
                handler.sendResponse(userID);
            }
            else if(request instanceof GetResultForStudentRequest) {
                GetResultForStudentRequestHandler handler = new GetResultForStudentRequestHandler(Server.getConnection(), oos, (GetResultForStudentRequest) request);
                handler.sendResponse(userID);
            }
            else if(request instanceof AddStudentRequest){
                AddStudentRequestHandler addStudentRequestHandler=new AddStudentRequestHandler(Server.getConnection(),oos,(AddStudentRequest)request);
                addStudentRequestHandler.sendResponse(userID);
            } else if(request instanceof ProctorPortForExamRequest) {
                ProctorPortForExamRequestHandler handler = new ProctorPortForExamRequestHandler(Server.getConnection(), oos, (ProctorPortForExamRequest) request);
                handler.sendResponse(userID);
            }
            else if(request instanceof CheckProctorJoinedRequest){
                CheckProctorJoinedRequestHandler checkProctorJoinedRequestHandler=new CheckProctorJoinedRequestHandler(Server.getConnection(),oos,(CheckProctorJoinedRequest)request);
                checkProctorJoinedRequestHandler.sendResponse(userID);
            }
            else{
                Server.sendResponse(oos, null);
            }
        }
        System.out.println("Should have broken");
        deleteChatSocketConnection();
    }

    private void deleteChatSocketConnection() {
        //Remove the OOS after disconnection
        System.out.println(userID + " disconnected");
        Server.socketArrayList.removeIf(r -> {
            if(r.getRegistrationNumber().equals(userID)) {
                try {
                    System.out.println("Sending disconnected to their oos");
                    r.getOos().writeObject("disconnected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
        Server.teacherSocketArrayList.removeIf(r-> {
            if(r.getTeacherId().equals(userID)) {
                try {
                    System.out.println("Sending disconnected to their oos");
                    r.getOos().writeObject("disconnected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }
}
