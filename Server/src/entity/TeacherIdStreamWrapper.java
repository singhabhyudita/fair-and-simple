package entity;

import java.io.ObjectOutputStream;

public class TeacherIdStreamWrapper {
    private final String teacherId;
    private final ObjectOutputStream oos;

    public TeacherIdStreamWrapper(String teacherId, ObjectOutputStream oos) {
        this.teacherId = teacherId;
        this.oos = oos;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }
}
