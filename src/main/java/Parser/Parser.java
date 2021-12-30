package Parser;


import Entenies.*;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Parser {
    private final char separator;

    public Parser() {
        separator = ';';
    }

    public Table parseFileToTable(String fileName) {
        var table = new Table();
        table.setCourseName("Java. Основы программирования на РТФ.");
        List<String[]> entries = readFile(fileName);
        for (int i = 3; i < entries.get(0).length; i++) {
            if (!entries.get(0)[i].equals("")) {
                table.addNameTopic(entries.get(0)[i]);
            }
        }
        var size = 0;
        for (int i = 4; i < entries.get(1).length; i++)
            if (entries.get(1)[i].equals("ДЗ")) {
                table.addSizeTopic(size);
                size = 0;
            } else {
                size += 1;
                table.addNameTask(entries.get(1)[i]);
            }
        table.addSizeTopic(size);
        table.setMaxScores(parseLineToCourse(entries.get(2), table));

        for (int i = 3; i < entries.size(); i++) {
            table.addStudent(parseLineToStudent(entries.get(i), table));
        }
        return table;
    }

    private Course parseLineToCourse(String[] words, Table table) {
        var course = new Course(Integer.valueOf(words[2]));
        var i = 3;
        while (i < words.length) {
            var topic = new Topic(table.getTopicsNames().get(course.getThemes().size()));
            topic.setTotalScore(Integer.valueOf(words[i]));
            i += 1;
            for (int j = 0; j < table.getCountTasksInTopic().get(course.getThemes().size()); j++) {
                topic.addTask(new Task(Integer.valueOf(words[i + j]),
                        table.getTasksNames().get(i - course.getThemes().size() - 4 + j)));
            }
            course.addTopic(topic);
            i += table.getCountTasksInTopic().get(course.getThemes().size() - 1);
        }
        return course;
    }

    private Student parseLineToStudent(String[] words, Table table) {
        String username = words[0];
        String group = words[1];
        var course = parseLineToCourse(words, table);

        var usernameArr = username.split(" ");
        var firstname = new StringBuilder();
        var lastname = new StringBuilder();
        for (int i = 0; i < usernameArr.length / 2; i++) {
            lastname.append(usernameArr[i]).append(" ");
        }
        lastname.setLength(lastname.length() - 1);
        for (int i = usernameArr.length / 2; i < usernameArr.length; i++) {
            firstname.append(usernameArr[i]).append(" ");
        }
        firstname.setLength(firstname.length() - 1);

        return new Student(firstname.toString(), lastname.toString(), group, course);
    }

    private List<String[]> readFile(String fileName) {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
        var parser = new CSVParserBuilder().withSeparator(separator).build();
        var reader = new CSVReaderBuilder(new InputStreamReader(resourceAsStream)).withCSVParser(parser).build();
        try {
            List<String[]> entries = reader.readAll();
            return entries;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
