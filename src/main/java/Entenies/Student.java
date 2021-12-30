package Entenies;

import Entenies.Interface.StudentInterface;

public class Student extends Person implements StudentInterface {

    private final String group;
    private VkData vkData;
    private Course course;

    public Student(String firstname, String lastname, String group, Course course) {
        super(firstname, lastname);
        this.group = group;
        this.course = course;
    }

    public void setVkData(VkData vkData) {
        this.vkData = vkData;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


    public String getGroup() {
        return group;
    }

    public VkData getVkData() {
        return vkData;
    }

    public Course getCourse() {
        return course;
    }

    public void convertUsernameToNameLastname(String username) {
        if (getLastname() == null) {
            var usernameArr = username.split(" ");
            var firstname = new StringBuilder();
            var lastname = new StringBuilder();
            for (int i = 0; i < usernameArr.length / 2; i++) {
                firstname.append(usernameArr[i]).append(" ");
            }
            firstname.setLength(firstname.length() - 1);
            for (int i = usernameArr.length / 2; i < usernameArr.length; i++) {
                lastname.append(usernameArr[i]).append(" ");
            }
            lastname.setLength(lastname.length() - 1);

            setFirstname(firstname.toString());
            setLastname(lastname.toString());
        }
    }

    @Override
    public String toString() {
        return getFirstname() +
                "," + getLastname() +
                "," + group;
    }
}
