package com.alhudaghifari.bildghifar.tugas1Histogram;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alhudaghifari.bildghifar.Constant;
import com.alhudaghifari.bildghifar.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class MpChartPageHistogram extends AppCompatActivity {

    private static final int COLOR_BOUNDARIES = 256;

    private ImageView imageView;
    private int[] redPixel = new int[COLOR_BOUNDARIES];
    private int[] greenPixel = new int[COLOR_BOUNDARIES];
    private int[] bluePixel = new int[COLOR_BOUNDARIES];
    private int[] grayPixel = new int[COLOR_BOUNDARIES];

    private BarChart redChart;
    private BarChart greenChart;
    private BarChart blueChart;
    private BarChart grayChart;
    private ProgressBar progressBar;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp_chart_page_histogram);

        imageView = (ImageView) findViewById(R.id.iv_photo);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        redChart = (BarChart) findViewById(R.id.red_chart);
        greenChart = (BarChart) findViewById(R.id.green_chart);
        blueChart = (BarChart) findViewById(R.id.blue_chart);
        grayChart = (BarChart) findViewById(R.id.gray_chart);
        tvTitle = (TextView) findViewById(R.id.first_title);

        String judul = getIntent().getStringExtra(Constant.imageTitle);
        setImageResource(judul);
        analyzeBitmap();
    }

    private void setImageResource(String img) {
        switch (img){
            case "a" :
                imageView.setImageResource(R.drawable.fari_smp);
                break;
            case "b" :
                imageView.setImageResource(R.drawable.fari_sma);
                break;
            case "c" :
                imageView.setImageResource(R.drawable.fari_kuliah);
                break;
            case "d" :
                imageView.setImageResource(R.drawable.ade_smp);
                break;
            case "e" :
                imageView.setImageResource(R.drawable.ade_sma);
                break;
            case "f" :
                imageView.setImageResource(R.drawable.ade_kuliah);
                break;
            case "g" :
                imageView.setImageResource(R.drawable.kemal_smp);
                break;
            case "h" :
                imageView.setImageResource(R.drawable.kemal_sma);
                break;
            case "i" :
                imageView.setImageResource(R.drawable.kemal_kuliah);
                break;
        }
    }
    
    private void analyzeBitmap() {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gray = ((red + green + blue) / 3) % 256;
                redPixel[red]++;
                greenPixel[green]++;
                bluePixel[blue]++;
                grayPixel[gray]++;
            }
        }
        visualizeHistogram();
    }

    private void visualizeHistogram() {
        List<BarEntry> redEntries = new ArrayList<>();
        List<BarEntry> greenEntries = new ArrayList<>();
        List<BarEntry> blueEntries = new ArrayList<>();
        List<BarEntry> grayEntries = new ArrayList<>();

        for (int i = 0; i < COLOR_BOUNDARIES; ++i) {
            redEntries.add(new BarEntry(i, redPixel[i]));
            greenEntries.add(new BarEntry(i, greenPixel[i]));
            blueEntries.add(new BarEntry(i, bluePixel[i]));
            grayEntries.add(new BarEntry(i, grayPixel[i]));
        }

        BarDataSet redDataSet = new BarDataSet(redEntries, "Red");
        BarDataSet greenDataSet = new BarDataSet(greenEntries, "Green");
        BarDataSet blueDataSet = new BarDataSet(blueEntries, "Blue");
        BarDataSet grayDataSet = new BarDataSet(grayEntries, "Gray");

        redDataSet.setColor(Color.RED);
        greenDataSet.setColor(Color.GREEN);
        blueDataSet.setColor(Color.BLUE);
        grayDataSet.setColor(Color.GRAY);

        BarData redData = new BarData(redDataSet);
        BarData greenData = new BarData(greenDataSet);
        BarData blueData = new BarData(blueDataSet);
        BarData grayData = new BarData(grayDataSet);

        redChart.setData(redData);
        greenChart.setData(greenData);
        blueChart.setData(blueData);
        grayChart.setData(grayData);

        redChart.invalidate();
        greenChart.invalidate();
        blueChart.invalidate();
        grayChart.invalidate();
    }
}
