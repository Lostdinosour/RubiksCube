package me.rubik.rubikscube.ui.timer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import me.rubik.rubikscube.database.DatabaseHandler;
import me.rubik.rubikscube.database.Times;
import me.rubik.rubikscube.database.TimesDao;
import me.rubik.rubikscube.ui.solver.InsertCubeActivity;

public class TimerGraphActivity extends AppCompatActivity {

    private XYSeries series;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Graph");

        dataset = new XYMultipleSeriesDataset();

        renderer = new XYMultipleSeriesRenderer();
        renderer.setAxesColor(Color.BLUE);
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitle("Times");
        renderer.setChartTitleTextSize(15);
        renderer.setFitLegend(true);
        renderer.setGridColor(Color.LTGRAY);
        renderer.setPanEnabled(true, true);
        renderer.setPointSize(10);
        renderer.setXTitle("Date");
        renderer.setYTitle("Time");
        renderer.setMargins( new int []{20, 30, 15, 0});
        renderer.setZoomButtonsVisible(true);
        renderer.setBarSpacing(10);
        renderer.setShowGrid(true);
        renderer.setYLabelsColor(0, Color.RED);
        renderer.setLabelsTextSize(50);


        XYSeriesRenderer rendererSeries = new XYSeriesRenderer();
        rendererSeries.setColor(Color.RED);
        renderer.addSeriesRenderer(rendererSeries);
        rendererSeries.setFillPoints(true);
        rendererSeries.setPointStyle(PointStyle.CIRCLE);

        series = new XYSeries("Solve time");

        new Thread() {
            @Override
            public void run() {
                TimesDao database = DatabaseHandler.getDatabase();
                Iterator<Times> times = database.getAll().iterator();

                while (times.hasNext()) {
                    Times time = times.next();
                    series.add((double) new Date(time.date).getTime(), time.time / 1000f);
                }
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataset.addSeries(series);
        GraphicalView view = ChartFactory.getTimeChartView(this, dataset, renderer, null);
        view.refreshDrawableState();
        view.repaint();
        setContentView(view);
    }
}