package com.alhudaghifari.bildghifar.tugas3Equalizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alhudaghifari.bildghifar.R;
import com.alhudaghifari.bildghifar.tugas2carigarismuka.LandingPageTugas2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LandingPageTugas3Equalizer extends AppCompatActivity {

    private static final String TAG = LandingPageTugas3Equalizer.class.getSimpleName();

    private static final int PICK_IMAGE = 23;

    private static final int MAX_POINT = 3;

    private class point{
        public int x;
        public int y;
        public point(){
            this.x = 0;
            this.y = 0;
        }
    }



    private point[] points;
    private int[] histogramPixel2;
    private Uri imageUri;

    private static final int MAX_COLOR = 256;

    private int[] redPixelCumulative;
    private int[] greenPixelCumulative;
    private int[] bluePixelCumulative;
    private int[] grayPixelCumulative;

    private int[] buzierPixelCumulative;

    private int[] lookupRedPixel;
    private int[] lookupGreenPixel;
    private int[] lookupBluePixel;
    private int[] lookupGrayPixel;

    private int[] redPixelHasil;
    private int[] greenPixelHasil;
    private int[] bluePixelHasil;
    private int[] grayPixelHasil;

    private ImageView imageView;
    private ImageView iv_photo_hasil;

    private ImageView iv_photo_result_specification;
    private ImageView iv_photo_grayscale_result_specification;

    private SeekBar seekBarLeft;
    private SeekBar seekBarCenter;
    private SeekBar seekBarRight;

    private TextView tvSeekBarLeft;
    private TextView tvSeekBarCenter;
    private TextView tvSeekBarRight;

    private BarChart redChart;
    private BarChart greenChart;
    private BarChart blueChart;
    private BarChart grayChart;

    private BarChart chart_buzier;
    private BarChart buzier_red_chart_hasil;
    private BarChart buzier_green_chart_hasil;
    private BarChart buzier_blue_chart_hasil;
    private BarChart buzier_gray_chart_hasil;

    private ProgressBar progressBar;

    private LinearLayout linlayGrafilBeginning;
    private LinearLayout linlayHasilChart;

    private int[] redPixel;
    private int[] greenPixel;
    private int[] bluePixel;
    private int[] grayPixel;

    Bitmap output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page_tugas3_equalizer);

        imageView = (ImageView) findViewById(R.id.iv_photo);
        iv_photo_hasil = (ImageView) findViewById(R.id.iv_photo_grayscale);

        iv_photo_result_specification = (ImageView) findViewById(R.id.iv_photo_result_specification);
        iv_photo_grayscale_result_specification = (ImageView) findViewById(R.id.iv_photo_grayscale_result_specification);

        seekBarLeft = (SeekBar) findViewById(R.id.sbLeft);
        seekBarCenter = (SeekBar) findViewById(R.id.sbCenter);
        seekBarRight = (SeekBar) findViewById(R.id.sbRight);

        tvSeekBarLeft = (TextView) findViewById(R.id.tvSeekBarLeft);
        tvSeekBarCenter = (TextView) findViewById(R.id.tvSeekBarCenter);
        tvSeekBarRight = (TextView) findViewById(R.id.tvSeekBarRight);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        redChart = (BarChart) findViewById(R.id.red_chart);
        greenChart = (BarChart) findViewById(R.id.green_chart);
        blueChart = (BarChart) findViewById(R.id.blue_chart);
        grayChart = (BarChart) findViewById(R.id.gray_chart);

        chart_buzier = (BarChart) findViewById(R.id.chart_buzier);
        buzier_red_chart_hasil = (BarChart) findViewById(R.id.buzier_red_chart_hasil);
        buzier_green_chart_hasil = (BarChart) findViewById(R.id.buzier_green_chart_hasil);
        buzier_blue_chart_hasil = (BarChart) findViewById(R.id.buzier_blue_chart_hasil);
        buzier_gray_chart_hasil = (BarChart) findViewById(R.id.buzier_gray_chart_hasil);

        linlayGrafilBeginning = (LinearLayout) findViewById(R.id.linlayGrafilBeginning);
        linlayHasilChart = (LinearLayout) findViewById(R.id.linlayHasilChart);

        linlayGrafilBeginning.setVisibility(View.GONE);

        listenerAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LandingPageTugas3Equalizer.this.getMenuInflater().inflate(R.menu.menu_pick_image, menu);
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

                selectedImage = getResizedBitmap(selectedImage, 300);// 400 is for example, replace with desired size

                imageView.setImageBitmap(selectedImage);

                progressBar.setVisibility(View.VISIBLE);

                new SetImageBeginning().execute();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LandingPageTugas3Equalizer.this, "foto ga ada gan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void matchBuzier(View view) {

        linlayHasilChart.setVisibility(View.VISIBLE);
        getPointQuadraticbuzier();
        specificateHistogram();
        histogramHasilVisualizer();
        setBitmapHasilSpecification();
        setGrayscaleBitmapHasilSpecification();
    }

    private void initPoint(){
        this.histogramPixel2 = new int[MAX_COLOR];
        this.points = new point[MAX_POINT];
        for (int i=0;i<this.points.length;++i){
            this.points[i] = new point();
        }

        int leftSeek = seekBarLeft.getProgress();
        int centerSeek = seekBarCenter.getProgress();
        int rightSeek = seekBarRight.getProgress();

        this.points[0].x = 0;
        this.points[0].y = leftSeek;

        this.points[1].x = 128;
        this.points[1].y = centerSeek;

        this.points[2].x = 255;
        this.points[2].y = rightSeek;
    }

    private float getQuadraticbuzierConst(int a, int b, int c){
        double d = 0;
        try{
            d = Math.sqrt((float)b*(float)b - 4*(float)a*(float)c);
            // System.out.println(d);
        }catch(Exception e){
            e.printStackTrace();
        }
        float x1 = ((float)-b + (float)d) / (float)2*a;
        float x2 = ((float)-b - (float)d) / (float)2*a;
        if (x1 >= 0 && x1 <= 1){
            return x1;
        }else return x2;
    }

    public void getPointQuadraticbuzier(){
        // process point X [0..255]
        initPoint();
        for(int i = 0; i < MAX_COLOR; ++i){
            int Xpoint = i;

            // get value a,b,c from point quadratic buzzier X
            int aX = this.points[0].x - 2*this.points[1].x + this.points[2].x;
            int bX = 2*(this.points[1].x - this.points[0].x);
            int cX = this.points[0].x - Xpoint;
            float t = getQuadraticbuzierConst(aX, bX, cX);

            // get value a,b,c from point quadratic buzzier X
            int aY = this.points[0].y - 2*this.points[1].y + this.points[2].y;
            int bY = 2*(this.points[1].y - this.points[0].y);
            int cY = this.points[0].y;

            int Ypoint = Math.round((float)aY * t * t + (float)bY * t + cY);
            this.histogramPixel2[Xpoint] = Ypoint;

        }
    }

    private class SetImageBeginning extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            setBitmapGrayscaleEqualization();
            bitmapAnalyzer();

            getPointQuadraticbuzier();
            histogramBuzierVisualizer();
            return null;
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

    private void setBitmapGrayscaleEqualization() {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv_photo_hasil.setImageBitmap(output);
            }
        });
    }

    private void bitmapAnalyzer() {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        redPixel = new int[MAX_COLOR];
        greenPixel = new int[MAX_COLOR];
        bluePixel = new int[MAX_COLOR];
        grayPixel = new int[MAX_COLOR];

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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                histogramVisualizer();
                chart_buzier.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void listenerAll() {
        seekBarLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekBarLeft.setText(String.valueOf(progress));
                getPointQuadraticbuzier();
                histogramBuzierVisualizer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarCenter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekBarCenter.setText(String.valueOf(progress));
                getPointQuadraticbuzier();
                histogramBuzierVisualizer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekBarRight.setText(String.valueOf(progress));
                getPointQuadraticbuzier();
                histogramBuzierVisualizer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initCumulativePixel(){
        this.redPixelCumulative = new int[MAX_COLOR];
        this.greenPixelCumulative = new int[MAX_COLOR];
        this.bluePixelCumulative= new int[MAX_COLOR];
        this.grayPixelCumulative= new int[MAX_COLOR];
        this.buzierPixelCumulative = new int[MAX_COLOR];

        int cumulativeRed = 0;
        int cumulativeGreen = 0;
        int cumulativeBlue = 0;
        int cumulativeGray = 0;
        int cumulativeBuzier = 0;
        for(int i=0;i<MAX_COLOR;i++){
            cumulativeRed += this.redPixel[i];
            this.redPixelCumulative[i] = cumulativeRed;

            cumulativeGreen += this.greenPixel[i];
            this.greenPixelCumulative[i] = cumulativeGreen;

            cumulativeBlue += this.bluePixel[i];
            this.bluePixelCumulative[i] = cumulativeBlue;

            cumulativeGray += this.grayPixel[i];
            this.grayPixelCumulative[i] = cumulativeGray;

            cumulativeBuzier += this.histogramPixel2[i];
            this.buzierPixelCumulative[i] = cumulativeBuzier;
        }
    }

    private void initLookupTable(){
        this.lookupRedPixel = new int[MAX_COLOR];
        this.lookupGreenPixel = new int[MAX_COLOR];
        this.lookupBluePixel = new int[MAX_COLOR];
        this.lookupGrayPixel = new int[MAX_COLOR];
    }

    private void specificateHistogram(){
        initCumulativePixel();
        int cumulativeBuzier = this.buzierPixelCumulative[MAX_COLOR - 1];

        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        int cumulativeRGB = this.grayPixelCumulative[MAX_COLOR - 1];

        initLookupTable();
        // create lookup table red
        for(int i=0;i<MAX_COLOR;i++){
            float prob1 = (float) this.redPixelCumulative[i] / (float) cumulativeRGB;

            // get the minimum distance
            int resultLookup = 0;
            float distance = Math.abs(prob1 - ((float) this.buzierPixelCumulative[0] / (float) cumulativeBuzier));
            for(int j=1;j<MAX_COLOR;j++){
                float prob2 = (float) this.buzierPixelCumulative[j] / (float) cumulativeBuzier;
                if (Math.abs(prob1 - prob2) < distance){
                    distance = Math.abs(prob1 - prob2);
                    resultLookup = j;
                }
            }

            // get the result
            // add to lookup table
            this.lookupRedPixel[i] = resultLookup;
        }

        // create lookup table green
        for(int i=0;i<MAX_COLOR;i++){
            float prob1 = (float) this.greenPixelCumulative[i] / (float) cumulativeRGB;

            // get the minimum distance
            int resultLookup = 0;
            float distance = Math.abs(prob1 - ((float) this.buzierPixelCumulative[0] / (float) cumulativeBuzier));
            for(int j=1;j<MAX_COLOR;j++){
                float prob2 = (float) this.buzierPixelCumulative[j] / (float) cumulativeBuzier;
                if (Math.abs(prob1 - prob2) < distance){
                    distance = Math.abs(prob1 - prob2);
                    resultLookup = j;
                }
            }

            // get the result
            // add to lookup table
            this.lookupGreenPixel[i] = resultLookup;
        }

        // create lookup table blue
        for(int i=0;i<MAX_COLOR;i++){
            float prob1 = (float) this.bluePixelCumulative[i] / (float) cumulativeRGB;

            // get the minimum distance
            int resultLookup = 0;
            float distance = Math.abs(prob1 - ((float) this.buzierPixelCumulative[0] / (float) cumulativeBuzier));
            for(int j=1;j<MAX_COLOR;j++){
                float prob2 = (float) this.buzierPixelCumulative[j] / (float) cumulativeBuzier;
                if (Math.abs(prob1 - prob2) < distance){
                    distance = Math.abs(prob1 - prob2);
                    resultLookup = j;
                }
            }

            // get the result
            // add to lookup table
            this.lookupBluePixel[i] = resultLookup;
        }

        // create lookup table gray
        for(int i=0;i<MAX_COLOR;i++){
            float prob1 = (float) this.grayPixelCumulative[i] / (float) cumulativeRGB;

            // get the minimum distance
            int resultLookup = 0;
            float distance = Math.abs(prob1 - ((float) this.buzierPixelCumulative[0] / (float) cumulativeBuzier));
            for(int j=1;j<MAX_COLOR;j++){
                float prob2 = (float) this.buzierPixelCumulative[j] / (float) cumulativeBuzier;
                if (Math.abs(prob1 - prob2) < distance){
                    distance = Math.abs(prob1 - prob2);
                    resultLookup = j;
                }
            }

            // get the result
            // add to lookup table
            this.lookupGrayPixel[i] = resultLookup;
        }
        initPixelHasil();
        fillPixelHasil();
    }

    private void initPixelHasil(){
        redPixelHasil = new int[MAX_COLOR];
        greenPixelHasil = new int[MAX_COLOR];
        bluePixelHasil = new int[MAX_COLOR];
        grayPixelHasil = new int[MAX_COLOR];
    }

    private void fillPixelHasil(){
        // from lookup HARUS DIINIT TERLEBIH DAHULU
        for(int i=0;i<MAX_COLOR;i++){
            redPixelHasil[lookupRedPixel[i]] += redPixel[i];
            greenPixelHasil[lookupGreenPixel[i]] += greenPixel[i];
            bluePixelHasil[lookupGreenPixel[i]] += bluePixel[i];
            grayPixelHasil[lookupGreenPixel[i]] += grayPixel[i];
        }
    }

    private void histogramHasilVisualizer(){
        linlayGrafilBeginning.setVisibility(View.VISIBLE);

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

        buzier_red_chart_hasil.setData(redData);
        buzier_green_chart_hasil.setData(greenData);
        buzier_blue_chart_hasil.setData(blueData);
        buzier_gray_chart_hasil.setData(grayData);

        buzier_red_chart_hasil.invalidate();
        buzier_green_chart_hasil.invalidate();
        buzier_blue_chart_hasil.invalidate();
        buzier_gray_chart_hasil.invalidate();
    }

    private void histogramBuzierVisualizer(){
        List<BarEntry> buzierEntries = new ArrayList<>();
        for (int i = 0; i < MAX_COLOR; ++i) {
            buzierEntries.add(new BarEntry(i ,histogramPixel2[i]));
        }
        BarDataSet buzierDataSet = new BarDataSet(buzierEntries, "buzier");
        buzierDataSet.setColor(Color.MAGENTA);
        BarData buzierData = new BarData(buzierDataSet);
        chart_buzier.setData(buzierData);
        chart_buzier.invalidate();
    }

    private void histogramVisualizer() {
        linlayGrafilBeginning.setVisibility(View.VISIBLE);

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

    private void setBitmapHasilSpecification() {

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

                // lookup
                double redHasil = (double) lookupRedPixel[red];
                double greenHasil = (double) lookupGreenPixel[green];
                double blueHasil = (double) lookupBluePixel[blue];

//                double redHasil = red;
//                if (lookupRedPixel[red] > -1){
//                    redHasil = (double) lookupRedPixel[red];
//                }
//
//                double greenHasil = green;
//                if (lookupGreenPixel[green] > -1){
//                    greenHasil = (double) lookupGreenPixel[green];
//                }
//
//                double blueHasil = blue;
//                if (lookupBluePixel[blue] > -1){
//                    blueHasil = (double) lookupBluePixel[blue];
//                }

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
        iv_photo_result_specification.setImageBitmap(output);
    }

    private void setGrayscaleBitmapHasilSpecification(){
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

                // lookup
                int gray = ((red + green + blue) / 3) % 256;
                double redHasil = (double) lookupGrayPixel[gray];
                double greenHasil = (double) lookupGrayPixel[gray];
                double blueHasil = (double) lookupGrayPixel[gray];

                int newPixel = Color.rgb(
                        (int)redHasil,
                        (int)greenHasil,
                        (int)blueHasil);
                output.setPixel(j, i, newPixel);
            }
        }
        iv_photo_grayscale_result_specification.setImageBitmap(output);
    }
}
