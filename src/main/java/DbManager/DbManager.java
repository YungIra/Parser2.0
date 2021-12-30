package DbManager;

import Entenies.Student;
import Entenies.Table;
import Entenies.Task;
import Entenies.Topic;
import charts.tokens.AgeToken;
import charts.tokens.CityToken;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DbManager {
    public static Connection session;
    public static Statement statmt;
    public static ResultSet resSet;

    public static void Connection() throws ClassNotFoundException, SQLException {
        session = null;
        Class.forName("org.sqlite.JDBC");
        session = DriverManager.getConnection("jdbc:sqlite:students.db");
    }

    public static void dropTables() throws SQLException {
        statmt = session.createStatement();
        statmt.execute("DROP TABLE IF EXISTS task_score");
        statmt.execute("DROP TABLE IF EXISTS registrations");
        statmt.execute("DROP TABLE IF EXISTS students");
        statmt.execute("DROP TABLE IF EXISTS people");
        statmt.execute("DROP TABLE IF EXISTS cities");
        statmt.execute("DROP TABLE IF EXISTS tasks");
        statmt.execute("DROP TABLE IF EXISTS themes");
        statmt.execute("DROP TABLE IF EXISTS courses");
    }

    public static void CreateStructureDB() throws ClassNotFoundException, SQLException {
        statmt = session.createStatement();
        statmt.execute("CREATE TABLE IF NOT EXISTS cities (\n" +
                "    city_id   BIGINT     NOT NULL\n" +
                "                         PRIMARY KEY,\n" +
                "    city_name CHAR (255) \n" +
                ");\n");

        statmt.execute("CREATE TABLE IF NOT EXISTS courses (\n" +
                "    course_id   BIGINT     PRIMARY KEY\n" +
                "                           NOT NULL,\n" +
                "    course_name CHAR (255),\n" +
                "    max_score   INTEGER\n" +
                ");\n");

        statmt.execute("CREATE TABLE IF NOT EXISTS people (\n" +
                "    person_id  BIGINT     NOT NULL\n" +
                "                          PRIMARY KEY,\n" +
                "    birthday   CHAR (255),\n" +
                "    gender     CHAR (128),\n" +
                "    link_photo CHAR (255),\n" +
                "    vk_id      BIGINT,\n" +
                "    city_id    BIGINT     REFERENCES cities (city_id),\n" +
                "    student_id BIGINT     REFERENCES students (student_id),\n" +
                "    age        INTEGER\n" +
                ");\n");

        statmt.execute("CREATE TABLE IF NOT EXISTS students (\n" +
                "    student_id BIGINT     PRIMARY KEY\n" +
                "                          NOT NULL,\n" +
                "    firstname  CHAR (255),\n" +
                "    lastname   CHAR (255),\n" +
                "    [group]    CHAR (255)\n" +
                ");\n");

        statmt.execute("CREATE TABLE IF NOT EXISTS registrations (\n" +
                "    registration_id BIGINT PRIMARY KEY\n" +
                "                            NOT NULL,\n" +
                "    course_id        BIGINT REFERENCES courses (course_id),\n" +
                "    student_id       BIGINT REFERENCES students (student_id) \n" +
                ");\n");

        statmt.execute("CREATE TABLE IF NOT EXISTS themes (\n" +
                "    topic_id   BIGINT     PRIMARY KEY\n" +
                "                          NOT NULL,\n" +
                "    max_score  INTEGER,\n" +
                "    topic_name CHAR (255),\n" +
                "    course_id  BIGINT     REFERENCES courses (course_id) \n" +
                ");\n");

        statmt.execute("CREATE TABLE IF NOT EXISTS tasks (\n" +
                "    task_id   BIGINT     PRIMARY KEY\n" +
                "                         NOT NULL,\n" +
                "    max_score INTEGER,\n" +
                "    task_name CHAR (255),\n" +
                "    topic_id  BIGINT     REFERENCES themes (topic_id) \n" +
                ");\n");

        statmt.execute("CREATE TABLE IF NOT EXISTS task_score (\n" +
                "    task_score_id    BIGINT  PRIMARY KEY\n" +
                "                             NOT NULL,\n" +
                "    task_id          BIGINT  REFERENCES tasks (task_id),\n" +
                "    registration_id BIGINT  REFERENCES registrations (registrations_id),\n" +
                "    score            INTEGER\n" +
                ");\n");
    }

    public static void loadDataInDB(Table table) throws SQLException {
        loadCities(table);
        loadStudents(table);
        loadPeopleInformation(table);
        loadCourses(table);
        loadRegistrations(table);
        loadThemes(table);
        loadTasks(table);
        loadTasksScores(table);
    }

    public static List<CityToken> getCountStudentsOnCourseFromCities(long course_id) throws SQLException {
        statmt = session.createStatement();
        var citiesStatistics = new ArrayList<CityToken>();
        var statistic = statmt.executeQuery("SELECT city_name, COUNT(person_id) AS number_person " +
                "FROM cities JOIN people ON people.city_id = cities.city_id " +
                "GROUP BY city_name");
        while (statistic.next()) {
            citiesStatistics.add(new CityToken(statistic.getString("city_name"),
                    Integer.valueOf(statistic.getString("number_person"))));
        }
        return citiesStatistics;
    }

    public static List<AgeToken> getCountStudentsOnCourseByAge(long course_id) throws SQLException {
        statmt = session.createStatement();
        var ageStatistics = new ArrayList<AgeToken>();
        var statistic = statmt.executeQuery("SELECT age, COUNT(person_id) AS number_person " +
                "FROM people " +
                "WHERE age>0 " +
                "GROUP BY age ");
        while (statistic.next()) {
            ageStatistics.add(new AgeToken(Integer.valueOf(statistic.getString("age")),
                    Integer.valueOf(statistic.getString("number_person"))));
        }
        return ageStatistics;
    }

    private static void loadCities(Table table) throws SQLException {
        statmt = session.createStatement();
        var cities = new HashSet<String>();
        var id = 0;
        for (Student student : table.getStudents()) {
            if (student.getVkData() != null &&
                    student.getVkData().getCity() != null &&
                    !cities.contains(student.getVkData().getCity())) {
                statmt.execute("INSERT INTO cities ('city_id', 'city_name') VALUES ('" + id + "', '" + student.getVkData().getCity() + "')");
                id++;
                cities.add(student.getVkData().getCity());
            }
        }
    }

    private static void loadCourses(Table table) throws SQLException {
        statmt = session.createStatement();
        statmt.execute("INSERT INTO courses ('course_id', 'course_name', 'max_score') " +
                "VALUES ('" + 0 + "', ' " + table.getCourseName() + "', '" + table.getMaxScores().getScoreForCourse() + "')");
    }

    private static void loadPeopleInformation(Table table) throws SQLException {
        statmt = session.createStatement();
        var id = 0;
        for (Student student : table.getStudents()) {
            Object city_id = null;
            if (student.getVkData() != null && student.getVkData().getCity() != null) {
                city_id = statmt.executeQuery("SELECT city_id FROM cities " +
                        "WHERE city_name='" + student.getVkData().getCity() + "'").getInt("city_id");
            }
            var student_id = statmt.executeQuery("SELECT student_id FROM students " +
                    "WHERE firstname='" + student.getFirstname() + "' AND lastname='" + student.getLastname() + "'").getInt("student_id");
            if (student.getVkData() != null) {
                statmt.execute("INSERT INTO people ('person_id', 'birthday', 'gender', 'link_photo', 'vk_id', 'city_id', 'student_id', 'age') " +
                        "VALUES ('" + id + "', '" +
                        student.getVkData().getBirthDay() + "', '" +
                        student.getVkData().getGender() + "', '" +
                        student.getVkData().getLinkPhoto().toString() + "', '" +
                        student.getVkData().getUserVkId() + "', '" +
                        city_id + "', '" +
                        student_id + "', '" +
                        student.getVkData().getAge() + "')");
                id++;
            }
        }
    }

    private static void loadStudents(Table table) throws SQLException {
        statmt = session.createStatement();
        var student_id = 0;
        for (Student student : table.getStudents()) {
            statmt.execute("INSERT INTO students ('student_id', 'firstname', 'lastname', 'group') " +
                    "VALUES ('" + student_id + "', '" +
                    student.getFirstname() + "', '" +
                    student.getLastname() + "', '" +
                    student.getGroup() + "')");
            student_id++;

        }
    }

    private static void loadRegistrations(Table table) throws SQLException {
        statmt = session.createStatement();
        var registration_id = 0;
//        var course_id = statmt.executeQuery("SELECT course_id FROM courses " +
//                "WHERE course_name='" + table.getCourseName() + "'").getInt("course_id");
        var course_id = 0;
        for (Student student : table.getStudents()) {
            var student_id = statmt.executeQuery("SELECT student_id FROM students " +
                    "WHERE firstname='" + student.getFirstname() + "' AND lastname='" + student.getLastname() + "'").getInt("student_id");
            statmt.execute("INSERT INTO registrations ('registration_id', 'course_id', 'student_id') " +
                    "VALUES ('" + registration_id + "', '" +
                    course_id + "', '" +
                    student_id + "')");
            registration_id++;

        }
    }

    private static void loadThemes(Table table) throws SQLException {
        statmt = session.createStatement();
        var topic_id = 0;
//        var course_id = statmt.executeQuery("SELECT course_id FROM courses " +
//                "WHERE course_name='" + table.getCourseName() + "'").getInt("course_id");
        var course_id = 0;
        for (Topic topic : table.getMaxScores().getThemes()) {

            statmt.execute("INSERT INTO themes ('topic_id', 'max_score', 'topic_name', 'course_id') " +
                    "VALUES ('" + topic_id + "', '" +
                    topic.getTotalScore() + "', '" +
                    topic.getName() + "', '" +
                    course_id + "')");
            topic_id++;
        }
    }

    private static void loadTasks(Table table) throws SQLException {
        statmt = session.createStatement();
        var task_id = 0;
        for (Topic topic : table.getMaxScores().getThemes()) {
            var topic_id = statmt.executeQuery("SELECT topic_id FROM themes " +
                    "WHERE topic_name='" + topic.getName() + "'").getInt("topic_id");
            for (Task task : topic.getTasks()) {

                statmt.execute("INSERT INTO tasks ('task_id', 'max_score', 'task_name', 'topic_id') " +
                        "VALUES ('" + task_id + "', '" +
                        task.getScore() + "', '" +
                        task.getTitle() + "', '" +
                        topic_id + "')");
                task_id++;
            }
        }
    }

    private static void loadTasksScores(Table table) throws SQLException {
        statmt = session.createStatement();
        var task_score_id = 0;
        for (Student student : table.getStudents()) {
            var student_id = statmt.executeQuery("SELECT student_id FROM students " +
                    "WHERE firstname='" + student.getFirstname() +
                    "' AND lastname='" + student.getLastname() + "'").getInt("student_id");
            var registration_id = statmt.executeQuery("SELECT registration_id FROM registrations " +
                    "WHERE student_id='" + student_id +
                    "' AND course_id='" + 0 + "'").getInt("registration_id");
            for (Topic topic : student.getCourse().getThemes()) {
                var topic_id = statmt.executeQuery("SELECT topic_id FROM themes " +
                        "WHERE topic_name='" + topic.getName() + "'").getInt("topic_id");
                var resultSet = statmt.executeQuery("SELECT task_id FROM tasks " +
                        "WHERE topic_id='" + topic_id + "'");
                var tasks_id = new ArrayList<Integer>();
                while (resultSet.next()) {
                    tasks_id.add(resultSet.getInt("task_id"));
                }

                var i = 0;

                for (Task task : topic.getTasks()) {
                    statmt.execute("INSERT INTO task_score ('task_score_id', 'task_id', 'registration_id', 'score') " +
                            "VALUES ('" + task_score_id++ + "', '" +
                            tasks_id.get(i++) + "', '" +
                            registration_id + "', '" +
                            task.getScore() + "')");
                }
            }
        }
    }
}
