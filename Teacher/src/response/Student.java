package response;

import java.io.Serializable;

public class Student implements Serializable {
    private final String name;
    private final String registrationNumber;

    public Student(String name, String registrationNumber) {
        this.name = name;
        this.registrationNumber = registrationNumber;
    }

    public String getName() {
        return name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
}
