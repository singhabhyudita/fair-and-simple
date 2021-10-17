package response;

import entity.Student;

import java.io.Serializable;
import java.util.ArrayList;

public class ParticipantsListResponse extends Response implements Serializable {
    private ArrayList<Student> participantsList;
    public ParticipantsListResponse(ArrayList<Student> participantsList) {
        this.participantsList = participantsList;
    }

    public ArrayList<Student> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<Student> participantsList) {
        this.participantsList = participantsList;
    }
}
