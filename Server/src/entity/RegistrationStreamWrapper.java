package entity;

import java.io.ObjectOutputStream;

public class RegistrationStreamWrapper {
    private final String registrationNumber;
    private final ObjectOutputStream oos;

    public RegistrationStreamWrapper(String registrationNumber, ObjectOutputStream oos) {
        this.registrationNumber = registrationNumber;
        this.oos = oos;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }
}
