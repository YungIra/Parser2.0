package Entenies;

import Entenies.Interface.CourseInterface;

import java.util.ArrayList;

public class Course implements CourseInterface {
    private final int scoreForCourse;
    private final ArrayList<Topic> themes;

    public Course(int scoreForCourse){
        this.scoreForCourse = scoreForCourse;
        themes = new ArrayList<>();
    }

    public void addTopic(Topic topic){
        themes.add(topic);
    }

    public ArrayList<Topic> getThemes(){
        return themes;
    }

    public int getScoreForCourse(){
        return scoreForCourse;
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();
        sb.append(String.format("Оценка за курс: %d; ", scoreForCourse));
        for(Topic topic: themes)
            sb.append(topic.toString());

        return sb.toString();
    }
}
