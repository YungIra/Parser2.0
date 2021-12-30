import DbManager.DbManager;
import Entenies.Table;
import Parser.Parser;
import VkApi.VkApiHandler;
import charts.CityChart;
import charts.AgeChart;
import com.vk.api.sdk.exceptions.ClientException;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClientException, SQLException, ClassNotFoundException {
        var parser = new Parser();
        Table table = parser.parseFileToTable("java-rtf.csv");
        var vkApi = new VkApiHandler();
        vkApi.loadDataAboutUserInfo("198188261", table);

        DbManager.Connection();
//        DbManager.dropTables();
//        DbManager.CreateStructureDB();
//        DbManager.loadDataInDB(table);
//        DbManager.getCountStudentsOnCourseFromCities(0);

        var cityChart = new CityChart();
        cityChart.paintGraphic();

        var ageChart = new AgeChart();
        ageChart.paintGraphics();
    }
}
