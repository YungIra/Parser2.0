package Entenies;

import Entenies.Interface.TopicInterface;

import java.util.ArrayList;

public class Topic implements TopicInterface {
    private String name;
    private int totalScore;
    private ArrayList<Task> tasks;

    public Topic(String name) {
        tasks = new ArrayList<>();
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalScore(int score) {
        totalScore = score;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public String getName() {
        return name;
    }

    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(String.format("Оценка за тему: %d; ", totalScore));
        sb.append(String.format("Название темы: %s; ", name));
        for (Task task : tasks)
            sb.append(task.toString());
        return sb.toString();
    }
}
