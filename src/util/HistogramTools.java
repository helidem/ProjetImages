package util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class HistogramTools {


    /**
     * @param histogram tableau représsentant l'histogramme
     * @throws IOException
     */
    public static void plotHistogram(double[] histogram, Color couleur) throws IOException {

        XYSeries myseries = new XYSeries("Nombre de pixels");
        for (int i = 0; i < histogram.length; i++) {
            myseries.add(new Double(i), new Double(histogram[i]));
        }
        XYSeriesCollection myseriescollection = new XYSeriesCollection(myseries);

        JFreeChart jfreechart = ChartFactory.createXYBarChart("Histogramme de l'image", "Niveaux de gris", false, "Nombre de pixels", myseriescollection, PlotOrientation.VERTICAL, true, false, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = jfreechart.getXYPlot();

        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setRangeGridlinePaint(Color.black);
        xyplot.getRendererForDataset(xyplot.getDataset(0)).setSeriesPaint(0, couleur);
        NumberAxis axis = (NumberAxis) xyplot.getDomainAxis();

        axis.setLowerMargin(0);
        axis.setUpperMargin(0);

        // create and display a frame...
        ChartFrame frame = new ChartFrame("DUT 2 Informatique - Image", jfreechart);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param histogram  tableau représsentant l'histogramme
     * @param pathToSave chemin de sauvegarde de l'histogramme
     * @throws IOException
     */
    public static void saveHistogram(double[] histogram, String pathToSave) throws IOException {

        XYSeries myseries = new XYSeries("Nombre de pixels");
        for (int i = 0; i < histogram.length; i++) {
            myseries.add(new Double(i), new Double(histogram[i]));
        }
        XYSeriesCollection myseriescollection = new XYSeriesCollection(myseries);

        JFreeChart jfreechart = ChartFactory.createXYBarChart("Histogramme de l'image", "Niveaux de gris", false, "Nombre de pixels", myseriescollection, PlotOrientation.VERTICAL, true, false, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = jfreechart.getXYPlot();

        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setRangeGridlinePaint(Color.white);

        NumberAxis axis = (NumberAxis) xyplot.getDomainAxis();

        axis.setLowerMargin(0);
        axis.setUpperMargin(0);

        if (pathToSave != null)
            ChartUtilities.saveChartAsPNG(new File(pathToSave), jfreechart, 900, 600);
    }


}