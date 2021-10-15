package response;

public class Student {
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
