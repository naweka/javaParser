import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import java.sql.SQLException;
import java.util.Map;

public class Chart extends ApplicationFrame {

    public Chart() throws SQLException {
        super("");
        JFreeChart barChart = ChartFactory.createBarChart(
                "Соотношение количества домов с разным количеством этажей",
                "Количество этажей",
                "Количество зданий",
                createDataset(Queries.getQuery1()),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset(Map<String, String> chartData) throws SQLException {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String a : chartData.keySet())
            dataset.addValue(Float.parseFloat(chartData.get(a)), a, "Количество этажей");

        return dataset;
    }
}
