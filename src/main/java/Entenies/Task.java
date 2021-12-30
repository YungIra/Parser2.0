package Entenies;

import Entenies.Interface.TaskInterface;

public class Task implements TaskInterface {
    private String title;
    private final int score;

    public Task(int score, String title) {
        this.score = score;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("%s: %d;", title, score);
    }
}
