package Entenies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {
    private Course maxScores;
    private final ArrayList<Student> students;
    private final Map<String, Student> studentsMap;
    private ArrayList<String> topicsNames;
    private ArrayList<String> tasksNames;
    private ArrayList<Integer> countTasksInTopic;
    private String courseName;

    public Table() {
        students = new ArrayList<>();
        topicsNames = new ArrayList<>();
        countTasksInTopic = new ArrayList<>();
        tasksNames = new ArrayList<>();
        studentsMap = new HashMap<>();
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setMaxScores(Course course) {
        maxScores = course;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Course getMaxScores() {
        return maxScores;
    }

    public String getCourseName() {
        return courseName;
    }

    public ArrayList<String> getTopicsNames() {
        return topicsNames;
    }

    public ArrayList<String> getTasksNames() {
        return tasksNames;
    }

    public ArrayList<Integer> getCountTasksInTopic() {
        return countTasksInTopic;
    }

    public Student getStudent(String firstname, String lastname) {
        return studentsMap.get(String.format("%s %s", firstname, lastname));
    }

    public void addNameTopic(String topicName) {
        topicsNames.add(topicName);
    }

    public void addSizeTopic(int size) {
        countTasksInTopic.add(size);
    }

    public void addNameTask(String name) {
        tasksNames.add(name);
    }

    public void addStudent(Student student) {
        studentsMap.put(String.format("%s %s", student.getFirstname(), student.getLastname()), student);
        students.add(student);
    }

    public boolean containStudent(String firstname, String lastname) {
        return studentsMap.containsKey(String.format("%s %s", firstname, lastname));
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (String topicName : getTopicsNames()) {
            sb.append(topicName + ";");
        }
        sb.append("\n");
        for (String taskName : getTasksNames())
            System.out.print(taskName + ";");
        sb.append('\n');
        sb.append(maxScores);
        sb.append('\n');
        for (Student student : students) {
            sb.append(student.toString()).append('\n');
        }
        return sb.toString();
    }
}