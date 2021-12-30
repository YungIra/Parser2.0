package charts;

import DbManager.DbManager;
import charts.tokens.CityToken;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CityChart {
    public JPanel createDemoPanel(List<CityToken> cityStatistics)
    {
        JFreeChart chart = createChart(createDataset(cityStatistics));
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(600, 300));
        return panel;
    }

    public void paintGraphic() throws SQLException, ClassNotFoundException {
        CityChart cityStatisticChart = new CityChart();
        DbManager.Connection();
        var cityStatistics = DbManager.getCountStudentsOnCourseFromCities(0l);
        JPanel panel = cityStatisticChart.createDemoPanel(cityStatistics);
        var jFrame = new JFrame();
        jFrame.getContentPane().add(panel);
        jFrame.setSize(600, 600);
        jFrame.setVisible(true);
    }

    private CategoryDataset createDataset(List<CityToken> cityStatistics)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (CityToken c: cityStatistics){
            dataset.addValue(c.getCountStudent(), "Число студентов", c.getCityName());
        }
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset)
    {
        JFreeChart chart = ChartFactory.createBarChart(
                "Число студентов по городам",
                "Город",                   // x-axis label
                "Число студентов",                // y-axis label
                dataset);
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }
}
