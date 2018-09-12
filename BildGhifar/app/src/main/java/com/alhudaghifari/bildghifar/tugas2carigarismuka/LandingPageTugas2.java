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

    private ImageView iv_photo_grayscale;
    private ImageView iv_photo_hasil_grayscale;

    private int[] redPixel = new int[MAX_COLOR];
    private int[] greenPixel = new int[MAX_COLOR];
    private int[] bluePixel = new int[MAX_COLOR];
    private int[] grayPixel = new int[MAX_COLOR];

    private int[] redPixelHasil = new int[MAX_COLOR];
    private int[] greenPixelHasil = new int[MAX_COLOR];
    private int[] bluePixelHasil = new int[MAX_COLOR];
    private int[] grayPixelHasil = new int[MAX_COLOR];

    private int[] redPixelCumulative = new int[MAX_COLOR];
    private int[] greenPixelCumulative = new int[MAX_COLOR];
    private int[] bluePixelCumulative = new int[MAX_COLOR];
    private int[] grayPixelCumulative = new int[MAX_COLOR];

    private int[] redPixelCumulativeHasil = new int[MAX_COLOR];
    private int[] greenPixelCumulativeHasil = new int[MAX_COLOR];
    private int[] bluePixelCumulativeHasil = new int[MAX_COLOR];
    private int[] grayPixelCumulativeHasil = new int[MAX_COLOR];

    private  int[] redPixelChange = new int[MAX_COLOR];
    private  int[] greenPixelChange = new int[MAX_COLOR];
    private  int[] bluePixelChange = new int[MAX_COLOR];
    private  int[] grayPixelChange = new int[MAX_COLOR];


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

        iv_photo_grayscale = (ImageView) findViewById(R.id.iv_photo_grayscale);
        iv_photo_hasil_grayscale = (ImageView) findViewById(R.id.iv_photo_hasil_grayscale);

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

                setBitmapGrayscaleEqualization();

                bitmapAnalyzer();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LandingPageTugas2.this, "foto ga ada gan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onCLickAnalyzeBitmap(View view) {
        initChangePixel();
        bitmapAnalyzerUsingThreshold();
//        setBitmapNewVal();
        setBitmapNewValEqualization();

        // now we create photo of grayscale
        setBitmapNewValGrayscaleEqualization();
    }

    protected void initChangePixel(){
        for (int i = 0;i < MAX_COLOR; ++i) {
            redPixelChange[i] = -1;
            greenPixelChange[i] = -1;
            bluePixelChange[i] = -1;
            greenPixelChange[i] = -1;
        }
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
        redPixelHasil = new int[MAX_COLOR];
        greenPixelHasil = new int[MAX_COLOR];
        bluePixelHasil = new int[MAX_COLOR];
        grayPixelHasil = new int[MAX_COLOR];

        for (int i = 0; i < threshold; ++i) {
            newVal = (MAX_COLOR - 1) / threshold * i % 256;
            redPixelHasil[newVal] = redPixel[i];
            greenPixelHasil[newVal] = greenPixel[i];
            bluePixelHasil[newVal] = bluePixel[i];
            grayPixelHasil[newVal] = grayPixel[i];
        }
        // Equalization from threshold result
        equalizationPixel();
//        histogramThresholdVisualizer();
        histogramEqualizationVisualizer();
    }

    private void equalizationPixel(){
        redPixelCumulativeHasil = new int[MAX_COLOR];
        greenPixelCumulativeHasil = new int[MAX_COLOR];
        bluePixelCumulativeHasil = new int[MAX_COLOR];
        grayPixelCumulativeHasil = new int[MAX_COLOR];

        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        // Red Pixel Equalization
        int cumulative = 0;
        int min = 0;
        for (int i = 0; i < MAX_COLOR; ++i){
            if (redPixelHasil[i] > 0 ){
                if (min == 0){
                    min = redPixelHasil[i];
                }
                cumulative += redPixelHasil[i];
                redPixelCumulative[i] = cumulative;
            }else{
                redPixelCumulative[i] = redPixelHasil[i];
            }
        }
        // Cumulative distribution function for Red Pixel
        for (int i = 0; i < MAX_COLOR; ++i){
            if (redPixelCumulative[i] > 0){
                int hv = Math.round( ((float)(redPixelCumulative[i] - min) / (float)((height * width) - min)) * (float)(MAX_COLOR - 1));
//                Log.d(TAG,"hv : " + hv + ", pixel : " + redPixelCumulative[i] + ", height x width : " + height * width);
//                Log.d(TAG," (redPixelCumulative[i] - min) : " + (redPixelCumulative[i] - min) + " (height * width) - min) " + ((height * width) - min));
//                Log.d(TAG,"(redPixelCumulative[i] - min)/((height * width) - min) * (MAX_COLOR - 1)" + ((float)(redPixelCumulative[i] - min)/(float)((height * width) - min)));
                redPixelCumulativeHasil[hv] = redPixelHasil[i];
                redPixelChange[i] = hv;
            }
        }

        // Green Pixel Equalization
        cumulative = 0;
        min = 0;
        for (int i = 0; i < MAX_COLOR; ++i){
            if (greenPixelHasil[i] > 0 ){
                if (min == 0){
                    min = greenPixelHasil[i];
                }
                cumulative += greenPixelHasil[i];
                greenPixelCumulative[i] = cumulative;
            }else{
                greenPixelCumulative[i] = greenPixelHasil[i];
            }
        }
        // Cumulative distribution function for Green Pixel
        for (int i = 0; i < MAX_COLOR; ++i){
            if (greenPixelCumulative[i] > 0){
                int hv = Math.round( ((float)(greenPixelCumulative[i] - min) / (float)((height * width) - min)) * (float)(MAX_COLOR - 1));
                greenPixelCumulativeHasil[hv] = greenPixelHasil[i];
                greenPixelChange[i] = hv;
            }
        }

        // Blue Pixel Equalization
        cumulative = 0;
        min = 0;
        for (int i = 0; i < MAX_COLOR; ++i){
            if (bluePixelHasil[i] > 0 ){
                if (min == 0){
                    min = bluePixelHasil[i];
                }
                cumulative += bluePixelHasil[i];
                bluePixelCumulative[i] = cumulative;
            }else{
                bluePixelCumulative[i] = bluePixelHasil[i];
            }
        }
        // Cumulative distribution function for Green Pixel
        for (int i = 0; i < MAX_COLOR; ++i){
            if (bluePixelCumulative[i] > 0){
//                int hv = Math.round((bluePixelCumulative[i] - min)/(height * width - min) * (MAX_COLOR - 1));
                int hv = Math.round( ((float)(bluePixelCumulative[i] - min) / (float)((height * width) - min)) * (float)(MAX_COLOR - 1));
                bluePixelCumulativeHasil[hv] = bluePixelHasil[i];
                bluePixelChange[i] = hv;
            }
        }

        // Grayscale Pixel Equalization
        cumulative = 0;
        min = 0;
        for (int i = 0; i < MAX_COLOR; ++i){
            if (grayPixelHasil[i] > 0 ){
                if (min == 0){
                    min = grayPixelHasil[i];
                }
                cumulative += grayPixelHasil[i];
                grayPixelCumulative[i] = cumulative;
            }else{
                grayPixelCumulative[i] = grayPixelHasil[i];
            }
        }
        // Cumulative distribution function for Grayscale Pixel
        for (int i = 0; i < MAX_COLOR; ++i){
            if (grayPixelCumulative[i] > 0){
                int hv = Math.round( ((float)(grayPixelCumulative[i] - min) / (float)((height * width) - min)) * (float)(MAX_COLOR - 1));
                grayPixelCumulativeHasil[hv] = grayPixelHasil[i];
                grayPixelChange[i] = hv;
            }
        }
    }

    private void setBitmapGrayscaleEqualization() {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        final Bitmap output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gray = ((red + green + blue) / 3) % 256;

                int newPixel = Color.rgb(
                        gray,
                        gray,
                        gray);
                output.setPixel(j, i, newPixel);
            }
        }
        iv_photo_grayscale.setImageBitmap(output);
    }
    
    private void setBitmapNewValGrayscaleEqualization() {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        
        final Bitmap output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gray = ((red + green + blue) / 3) % 256;

                double grayscaleHasil = gray;
                if (grayPixelChange[gray] > -1){
                    grayscaleHasil = (double) grayPixelChange[gray];
                }

                int newPixel = Color.rgb(
                        (int)grayscaleHasil,
                        (int)grayscaleHasil,
                        (int)grayscaleHasil);
                output.setPixel(j, i, newPixel);
            }
        }
        iv_photo_hasil_grayscale.setImageBitmap(output);
    }

    private void setBitmapNewValEqualization() {

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

                double redHasil = red;
                if (redPixelChange[red] > -1){
                    redHasil = (double) redPixelChange[red];
                }

                double greenHasil = green;
                if (greenPixelChange[green] > -1){
                    greenHasil = (double) greenPixelChange[green];
                }

                double blueHasil = blue;
                if (bluePixelChange[blue] > -1){
                    blueHasil = (double) bluePixelChange[blue];
                }

//                double redHasil = (MAX_COLOR - 1) / threshold * red % MAX_COLOR;
//                double greenHasil = (MAX_COLOR - 1) / threshold * green % MAX_COLOR;
//                double blueHasil = (MAX_COLOR - 1) / threshold * blue % MAX_COLOR;

                int newPixel = Color.rgb(
                        (int)redHasil,
                        (int)greenHasil,
                        (int)blueHasil);
                output.setPixel(j, i, newPixel);
            }
        }
        iv_photo_hasil.setImageBitmap(output);
    }

    private void histogramEqualizationVisualizer(){
        List<BarEntry> redEntries = new ArrayList<>();
        List<BarEntry> greenEntries = new ArrayList<>();
        List<BarEntry> blueEntries = new ArrayList<>();
        List<BarEntry> grayEntries = new ArrayList<>();

        for (int i = 0; i < MAX_COLOR; ++i) {
            redEntries.add(new BarEntry(i, redPixelCumulativeHasil[i]));
            greenEntries.add(new BarEntry(i, greenPixelCumulativeHasil[i]));
            blueEntries.add(new BarEntry(i, bluePixelCumulativeHasil[i]));
            grayEntries.add(new BarEntry(i, grayPixelCumulativeHasil[i]));
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
