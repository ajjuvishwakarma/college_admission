package collegeadmission.com;

public class Student {
	private int id;
    private String name;
    private String email;
    private String dob;
    private String gender;
    private int courseId;
    private String contactNumber;

    public Student( int id,String name, String email, String dob, String gender, int courseId, String contactNumber) {
    	this.id=id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.courseId = courseId;
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Student Details:\n" +
                "ID: "+ id+ "\n"+
               "Name: " + name + "\n" +
               "Email: " + email + "\n" +
               "DOB: " + dob + "\n" +
               "Gender: " + gender + "\n" +
               "Course ID: " + courseId + "\n" +
               "Contact Number: " + contactNumber;
    }
}