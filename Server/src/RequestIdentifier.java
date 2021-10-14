import Request.LoginRequest;
import RequestHandler.LoginRequestHandler;

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
            if(request instanceof LoginRequest){
                LoginRequestHandler loginRequestHandler=new LoginRequestHandler(oos,(LoginRequest)request,Server.getConnection());
                try {
                    loginRequestHandler.sendResponse();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
