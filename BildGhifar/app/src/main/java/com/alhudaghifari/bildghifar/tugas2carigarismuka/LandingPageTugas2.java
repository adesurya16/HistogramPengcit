package com.alhudaghifari.bildghifar.tugas2carigarismuka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alhudaghifari.bildghifar.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LandingPageTugas2 extends AppCompatActivity {
    private static final String TAG = LandingPageTugas2.class.getSimpleName();

    private static final int PICK_IMAGE = 23;

    private Uri imageUri;

    private static final int MAX_COLOR = 256;

    private ImageView imageView;
    private ImageView iv_photo_hasil;
    private int[] redPixel = new int[MAX_COLOR];
    private int[] greenPixel = new int[MAX_COLOR];
    private int[] bluePixel = new int[MAX_COLOR];
    private int[] grayPixel = new int[MAX_COLOR];

    private int[] redPixelHasil = new int[MAX_COLOR];
    private int[] greenPixelHasil = new int[MAX_COLOR];
    private int[] bluePixelHasil = new int[MAX_COLOR];
    private int[] grayPixelHasil = new int[MAX_COLOR];

    private BarChart redChart;
    private BarChart greenChart;
    private BarChart blueChart;
    private BarChart grayChart;

    private BarChart redChartHasil;
    private BarChart greenChartHasil;
    private BarChart blueChartHasil;
    private BarChart grayChartHasil;

    private ProgressBar progressBar;
    private TextView tvTitle;
    private TextView tvThreshold;
    
    private SeekBar seekBarThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page_tugas2);

        imageView = (ImageView) findViewById(R.id.iv_photo);
        iv_photo_hasil = (ImageView) findViewById(R.id.iv_photo_hasil); 
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        redChart = (BarChart) findViewById(R.id.red_chart);
        greenChart = (BarChart) findViewById(R.id.green_chart);
        blueChart = (BarChart) findViewById(R.id.blue_chart);
        grayChart = (BarChart) findViewById(R.id.gray_chart);

        redChartHasil = (BarChart) findViewById(R.id.red_chart_hasil);
        greenChartHasil = (BarChart) findViewById(R.id.green_chart_hasil);
        blueChartHasil = (BarChart) findViewById(R.id.blue_chart_hasil);
        grayChartHasil = (BarChart) findViewById(R.id.gray_chart_hasil);

        tvTitle = (TextView) findViewById(R.id.first_title);
        tvThreshold = (TextView) findViewById(R.id.tvThreshold);

        seekBarThreshold = (SeekBar) findViewById(R.id.seekBarThreshold);

        seekBarThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvThreshold.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LandingPageTugas2.this.getMenuInflater().inflate(R.menu.menu_pick_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ambil_gambar:
                openGallery();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK ) {
            imageUri = data.getData();

            Log.d(TAG,"imageUri : " + imageUri);

            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size

                imageView.setImageBitmap(selectedImage);

                bitmapAnalyzer();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LandingPageTugas2.this, "foto ga ada gan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onCLickAnalyzeBitmap(View view) {
        bitmapAnalyzerUsingThreshold();
        setBitmapNewVal();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void bitmapAnalyzer() {
        progressBar.setVisibility(View.VISIBLE);
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
        progressBar.setVisibility(View.GONE);
        histogramVisualizer();
    }
    
    private void bitmapAnalyzerUsingThreshold() {
        int threshold = seekBarThreshold.getProgress();
        int newVal;
        for (int i = 0; i < threshold; ++i) {
            newVal = (MAX_COLOR - 1) / threshold * i % 256;
            redPixelHasil[newVal] = redPixel[i];
            greenPixel[newVal] = greenPixel[i];
            bluePixel[newVal] = bluePixel[i];
            grayPixel[newVal] = grayPixel[i];
        }
        histogramThresholdVisualizer();
    }

    private void setBitmapNewVal() {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        int threshold = seekBarThreshold.getProgress();
        final Bitmap output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                double redHasil = (MAX_COLOR - 1) / threshold * red % MAX_COLOR;
                double greenHasil = (MAX_COLOR - 1) / threshold * green % MAX_COLOR;
                double blueHasil = (MAX_COLOR - 1) / threshold * blue % MAX_COLOR;

                int newPixel = Color.rgb(
                        (int)redHasil,
                        (int)greenHasil,
                        (int)blueHasil);
                output.setPixel(j, i, newPixel);
            }
        }
        iv_photo_hasil.setImageBitmap(output);
    }

    private void histogramThresholdVisualizer() {
        List<BarEntry> redEntries = new ArrayList<>();
        List<BarEntry> greenEntries = new ArrayList<>();
        List<BarEntry> blueEntries = new ArrayList<>();
        List<BarEntry> grayEntries = new ArrayList<>();

        for (int i = 0; i < MAX_COLOR; ++i) {
            redEntries.add(new BarEntry(i, redPixelHasil[i]));
            greenEntries.add(new BarEntry(i, greenPixelHasil[i]));
            blueEntries.add(new BarEntry(i, bluePixelHasil[i]));
            grayEntries.add(new BarEntry(i, grayPixelHasil[i]));
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

        redChartHasil.setData(redData);
        greenChartHasil.setData(greenData);
        blueChartHasil.setData(blueData);
        grayChartHasil.setData(grayData);

        redChartHasil.invalidate();
        greenChartHasil.invalidate();
        blueChartHasil.invalidate();
        grayChartHasil.invalidate();
    }

    private void histogramVisualizer() {
        List<BarEntry> redEntries = new ArrayList<>();
        List<BarEntry> greenEntries = new ArrayList<>();
        List<BarEntry> blueEntries = new ArrayList<>();
        List<BarEntry> grayEntries = new ArrayList<>();

        for (int i = 0; i < MAX_COLOR; ++i) {
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
