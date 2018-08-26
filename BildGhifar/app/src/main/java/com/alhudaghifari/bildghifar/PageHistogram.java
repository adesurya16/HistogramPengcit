package com.alhudaghifari.bildghifar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alhudaghifari on 8/29/2017.
 */

public class PageHistogram extends AppCompatActivity {

    private GraphView graph;
    private Bitmap bitmap;

    private BarGraphSeries<DataPoint> series;
    private LineGraphSeries<DataPoint> series2;
    private PointsGraphSeries<DataPoint> series3;
    private LineGraphSeries<DataPoint> series4;
    private PointsGraphSeries<DataPoint> series5;

    private ImageView mImageView;

    private float[] grascaleValue;
    private int[] rgbValuesOneDimensional;
    private String rgbBundel;
    private boolean isRgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.halfblue)));

        inisialisasiData();
        Bundle bundeldata = PageHistogram.this.getIntent().getExtras();
        String imageResource = bundeldata.getString(Constant.imageTitle);
        rgbBundel = bundeldata.getString(Constant.jenisGambar) + "";
        int rgbb = Integer.parseInt(rgbBundel);
        if (rgbb == 1)
            isRgb = true;
        else
            isRgb = false;
        setImageResource(imageResource);

        makeGraph();
        setRgbValues();
        generateGraph();
    }

    private void inisialisasiData() {
        graph = (GraphView) findViewById(R.id.graph);
        mImageView = (ImageView) findViewById(R.id.imageOnHistogram);
    }

    private void makeGraph() {
        series2 = new LineGraphSeries<>();
        series2.setColor(Color.RED);
        series3 = new PointsGraphSeries<>();
        series3.setSize(5);
        series4 = new LineGraphSeries<>();
        series4.setColor(Color.GRAY);
        series5 = new PointsGraphSeries<>();
        series5.setSize(5);
        series5.setColor(Color.BLACK);
    }

    public void generateGraph() {

        int x = 0;
        int dataX;
        int dataY = 0;
        float dataXGrayscale;
        int dataYGrayscale = 0;

        for (int i=0;i<rgbValuesOneDimensional.length;i++) {
            if (rgbValuesOneDimensional[i] == rgbValuesOneDimensional[x] ||
                    grascaleValue[i] == grascaleValue[x]) {
                dataY++;
                dataYGrayscale++;
            } else {
                dataX = rgbValuesOneDimensional[x];
                dataXGrayscale = grascaleValue[x];

                if (isRgb) {
                    series2.appendData(new DataPoint(dataX, dataY), true, 30000, false);
                    series3.appendData(new DataPoint(dataX, dataY), true, 30000, false);
                } else {
                    series4.appendData(new DataPoint(dataXGrayscale, dataYGrayscale), true, 30000, false);
                    series5.appendData(new DataPoint(dataXGrayscale, dataYGrayscale), true, 30000, false);
                }
                dataY = 1;
                dataYGrayscale = 1;
            }
            x = i;
        }

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(350);

        // enable scalling
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        if (isRgb) {
            graph.getViewport().setMaxX(15000000);
            graph.addSeries(series2);
            graph.addSeries(series3);
        }
        else {
            graph.getViewport().setMaxX(500);
            graph.addSeries(series4);
            graph.addSeries(series5);
        }


//        graph.getViewport().setScrollable(true);
//        graph.getViewport().setScrollableY(true);
    }

    public void setRgbValues() {
        bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
        rgbValuesOneDimensional = new int[bitmap.getWidth()*bitmap.getHeight()];
        grascaleValue = new float[bitmap.getWidth()*bitmap.getHeight()];

        Log.d("width :"," " + bitmap.getWidth());
        Log.d("height :"," " + bitmap.getHeight());
        int iterator = 0;

        //get the ARGB value from each pixel of the image and store it into the array
        for(int i=0; i < bitmap.getWidth(); i++)
        {
            for(int j=0; j < bitmap.getHeight(); j++)
            {
                int pixel = bitmap.getPixel(i,j) * -1;
                rgbValuesOneDimensional[iterator] = pixel;
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                grascaleValue[iterator] = (redValue + blueValue + greenValue) / 3;
                iterator++;
            }
        }
        Arrays.sort(rgbValuesOneDimensional);
        Arrays.sort(grascaleValue);

        Log.d("setRgbValue :"," done");

    }

    private void setImageResource(String img) {
        switch (img){
            case "a" :
                mImageView.setImageResource(R.drawable.a1998);
                break;
            case "b" :
                mImageView.setImageResource(R.drawable.b2000);
                break;
            case "c" :
                mImageView.setImageResource(R.drawable.c2009);
                break;
            case "d" :
                mImageView.setImageResource(R.drawable.d2010);
                break;
            case "e" :
                mImageView.setImageResource(R.drawable.e2011);
                break;
            case "f" :
                mImageView.setImageResource(R.drawable.f2013);
                break;
            case "g" :
                mImageView.setImageResource(R.drawable.g2013);
                break;
            case "h" :
                mImageView.setImageResource(R.drawable.h2014);
                break;
            case "i" :
                mImageView.setImageResource(R.drawable.i2015);
                break;
            case "j" :
                mImageView.setImageResource(R.drawable.j2015);
                break;
            case "k" :
                mImageView.setImageResource(R.drawable.k2016);
                break;
            case "l" :
                mImageView.setImageResource(R.drawable.l2016);
                break;
        }
    }


}
