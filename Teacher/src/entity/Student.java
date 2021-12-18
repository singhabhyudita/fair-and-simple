package entity;

import java.io.Serializable;

public class Student implements Serializable {
    private int registrationNumber;
    private String name;

    public Student(int registrationNumber, String name) {
        this.registrationNumber = registrationNumber;
        this.name = name;
    }
    public int getRegistrationNumber() {
        return registrationNumber;
    }

    public String getName() {
        return name;
    }
}
