package request;

import java.net.InetAddress;

public class ProctoringRequest extends Request {
    final private String examId;
    final private int listeningOnPort;
    final private InetAddress address;

    public ProctoringRequest(String examId, int listeningOnPort, InetAddress address) {
        this.examId = examId;
        this.listeningOnPort = listeningOnPort;
        this.address = address;
    }

    public String getExamId() {
        return examId;
    }

    public int getListeningOnPort() {
        return listeningOnPort;
    }

    public InetAddress getAddress() {
        return address;
    }
}
