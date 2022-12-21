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
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;
import java.util.Random;

import me.rubik.rubikscube.ui.solver.InsertCubeActivity;

public class TimerGraphActivity extends AppCompatActivity {
    private ActionBar actionBar;

    private TimeSeries timeSeries;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer renderer;
    private GraphicalView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Camera");

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

        XYSeriesRenderer rendererSeries = new XYSeriesRenderer();
        rendererSeries.setColor(Color.RED);
        renderer.addSeriesRenderer(rendererSeries);
        rendererSeries.setFillPoints(true);
        rendererSeries.setPointStyle(PointStyle.CIRCLE);

        timeSeries = new TimeSeries("Random");
        Random random = new Random();
        Thread mThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timeSeries.add(new Date(), random.nextInt(10));
                    view.repaint();
                }
            }
        };
        mThread.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent myIntent = new Intent(this, InsertCubeActivity.class);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataset.addSeries(timeSeries);
        view = ChartFactory.getTimeChartView(this, dataset, renderer, "Test");
        view.refreshDrawableState();
        view.repaint();
        setContentView(view);
    }
}