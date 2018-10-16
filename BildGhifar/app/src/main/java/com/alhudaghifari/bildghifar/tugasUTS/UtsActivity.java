package com.alhudaghifari.bildghifar.tugasUTS;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alhudaghifari.bildghifar.R;
import com.alhudaghifari.bildghifar.tugas2carigarismuka.LandingPageTugas2;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UtsActivity extends AppCompatActivity {

    private static final String TAG = UtsActivity.class.getSimpleName();

    private static final int PICK_IMAGE = 23;
    private static final int MAX_COLOR = 256;

    private Uri imageUri;

    private PhotoView photoView;

    private RecyclerView mRecyclerView;
    private RecyclerUts recyclerUts;
    private RecyclerUts.OnButtonClickListener onButtonClickListener;

    private LinearLayout linlaySubMenu;
    private LinearLayout linlayJudul;
    private LinearLayout linlayThreshold;

    private RelativeLayout rellayMainPhoto;

    private ImageView ivCheckDone;

    private Bitmap originalPhoto;
    private Bitmap output;

    private ScrollView svEqualizer;
    private ScrollView svHistogram;

    private TextView tvJudulMenu;
    private TextView tvPrediction;
    private TextView tvThreshold;
    private TextView tvSeekBarLeft;
    private TextView tvSeekBarCenter;
    private TextView tvSeekBarRight;

    private SeekBar seekBarThreshold;
    private SeekBar seekBarLeft;
    private SeekBar seekBarCenter;
    private SeekBar seekBarRight;

    private int statusPage;
    private int threshold = 128;
    private int thresholdLeft;
    private int thresholdCenter;
    private int thresholdRight;

    private BarChart redChart;
    private BarChart greenChart;
    private BarChart blueChart;
    private BarChart grayChart;

    private int[] redPixel = new int[MAX_COLOR];
    private int[] greenPixel = new int[MAX_COLOR];
    private int[] bluePixel = new int[MAX_COLOR];
    private int[] grayPixel = new int[MAX_COLOR];

    private boolean isOpen = false;
    private boolean isHistogramOpened = false;

    private final int SHOW_BRIGHTNESS = 0;
    private final int SHOW_GRAYSCALE = 1;
    private final int SHOW_BW = 2;
    private final int SHOW_EQUALIZER = 3;
    private final int SHOW_CHAINCODE = 4;
    private final int SHOW_THINNING = 5;
    private final int SHOW_HOME = 6;
    private final int SHOW_MAIN_PHOTO = 7;
    private final int SHOW_HISTOGRAM = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uts);

        photoView = (PhotoView) findViewById(R.id.photo_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        linlaySubMenu = (LinearLayout) findViewById(R.id.linlaySubMenu);
        linlayJudul = (LinearLayout) findViewById(R.id.linlayJudul);
        rellayMainPhoto = (RelativeLayout) findViewById(R.id.rellayMainPhoto);
        svEqualizer = (ScrollView) findViewById(R.id.svEqualizer);
        svHistogram = (ScrollView) findViewById(R.id.svHistogram);
        linlayThreshold = (LinearLayout) findViewById(R.id.linlayThreshold);
        tvJudulMenu = (TextView) findViewById(R.id.tvJudulMenu);
        tvPrediction = (TextView) findViewById(R.id.tvPrediction);
        tvThreshold = (TextView) findViewById(R.id.tvThreshold);
        tvSeekBarLeft = (TextView) findViewById(R.id.tvSeekBarLeft);
        tvSeekBarCenter = (TextView) findViewById(R.id.tvSeekBarCenter);
        tvSeekBarRight = (TextView) findViewById(R.id.tvSeekBarRight);
        seekBarThreshold = (SeekBar) findViewById(R.id.seekBarThreshold);
        seekBarLeft = (SeekBar) findViewById(R.id.seekBarLeft);
        seekBarCenter = (SeekBar) findViewById(R.id.seekBarCenter);
        seekBarRight = (SeekBar) findViewById(R.id.seekBarRight);
        ivCheckDone = (ImageView) findViewById(R.id.ivCheckDone);

        redChart = (BarChart) findViewById(R.id.red_chart);
        greenChart = (BarChart) findViewById(R.id.green_chart);
        blueChart = (BarChart) findViewById(R.id.blue_chart);
        grayChart = (BarChart) findViewById(R.id.gray_chart);

        // untuk brightness
        redChartHasil = (BarChart) findViewById(R.id.red_chart_hasil);
        greenChartHasil = (BarChart) findViewById(R.id.green_chart_hasil);
        blueChartHasil = (BarChart) findViewById(R.id.blue_chart_hasil);
        grayChartHasil = (BarChart) findViewById(R.id.gray_chart_hasil);


        initializeListener();

        setAdapterUts(-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        UtsActivity.this.getMenuInflater().inflate(R.menu.menu_pick_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ambil_gambar:
                if (!isHistogramOpened)
                openGallery();
                showPage(SHOW_MAIN_PHOTO);
                isHistogramOpened = false;
                return true;
            case R.id.action_histogram:
                isHistogramOpened = true;
                showPage(SHOW_HISTOGRAM);
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
                originalPhoto = BitmapFactory.decodeStream(imageStream);

                selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                originalPhoto = selectedImage;

                photoView.setImageBitmap(selectedImage);

                bitmapAnalyzer();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(UtsActivity.this, "foto ga ada gan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
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



    private void initializeListener() {
        onButtonClickListener = new RecyclerUts.OnButtonClickListener() {
            @Override
            public void onClick(int posisi) {
                Log.d(TAG, "onButtonClickListener posisi " + posisi);
                if (posisi == SHOW_HOME) {
                    setAdapterUts(-1);
                    photoView.setImageBitmap(originalPhoto);
                } else if (posisi == SHOW_GRAYSCALE) {
                    setBitmapGrayscaleEqualization();
                    setAdapterUts(posisi);
                } else if (posisi == SHOW_BW) {
                    setImageToBlackAndWhite();
                    setAdapterUts(posisi);
                }
                else setAdapterUts(posisi);
                showPage(posisi);
            }
        };

        linlayJudul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPage(SHOW_HOME);
                setAdapterUts(-1);
            }
        });

        seekBarThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold = progress;
                tvThreshold.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ivCheckDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (statusPage) {
                    case SHOW_BRIGHTNESS:
                        photoView.setImageBitmap(originalPhoto);
                        initChangePixel();
                        bitmapAnalyzerUsingThreshold();
                        setBitmapNewValEqualization();
                        break;
                    case SHOW_BW:
                        break;
                    case SHOW_CHAINCODE:
                        break;
                    case SHOW_THINNING:
                        break;
                }
            }
        });
    }

    private void bitmapAnalyzer() {
        redPixel = new int[MAX_COLOR];
        greenPixel = new int[MAX_COLOR];
        bluePixel = new int[MAX_COLOR];
        grayPixel = new int[MAX_COLOR];

        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
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
        histogramVisualizer();
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

    private void setAdapterUts(final int clicked) {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(UtsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        recyclerUts = new RecyclerUts(UtsActivity.this, 7, clicked);
        recyclerUts.setOnButtonYaListener(onButtonClickListener);
        mRecyclerView.setAdapter(recyclerUts);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"recyclerview down");
                if (clicked != -1 && clicked <= 3)
                    mRecyclerView.smoothScrollToPosition(clicked);
                else if (clicked > 3 && clicked < 6)
                    mRecyclerView.smoothScrollToPosition(clicked + 2);
            }
        });
    }

    private void showPage(int page) {
        linlaySubMenu.setVisibility(View.VISIBLE);
        linlayThreshold.setVisibility(View.GONE);
        svEqualizer.setVisibility(View.GONE);
        tvPrediction.setVisibility(View.GONE);

        if (isOpen && page == statusPage) {
            page = SHOW_HOME;
            isOpen = false;
            setAdapterUts(-1);
        }

        switch (page) {
            case SHOW_HOME:
                linlaySubMenu.setVisibility(View.GONE);
                statusPage = SHOW_HOME;
                isOpen = false;
                break;
            case SHOW_BRIGHTNESS:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Brighness");
                statusPage = SHOW_BRIGHTNESS;
                isOpen = true;
                break;
            case SHOW_GRAYSCALE:
                linlaySubMenu.setVisibility(View.GONE);
                statusPage = SHOW_GRAYSCALE;
                isOpen = true;
                break;
            case SHOW_BW:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Black and White");
                statusPage = SHOW_BW;
                isOpen = true;
                break;
            case SHOW_EQUALIZER:
                svEqualizer.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Equalizer");
                statusPage = SHOW_EQUALIZER;
                isOpen = true;
                break;
            case SHOW_CHAINCODE:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvPrediction.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Number Recognition using Chain code");
                statusPage = SHOW_CHAINCODE;
                isOpen = true;
                break;
            case SHOW_THINNING:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvPrediction.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Number Recognition using Zhang Suen");
                statusPage = SHOW_THINNING;
                isOpen = true;
                break;
            case SHOW_MAIN_PHOTO:
                linlaySubMenu.setVisibility(View.GONE);
                rellayMainPhoto.setVisibility(View.VISIBLE);
                svHistogram.setVisibility(View.GONE);
                break;
            case SHOW_HISTOGRAM:
                linlaySubMenu.setVisibility(View.GONE);
                rellayMainPhoto.setVisibility(View.GONE);
                svHistogram.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


    /// =========== kode untuk brightness - start ============= //

    private  int[] redPixelChange = new int[MAX_COLOR];
    private  int[] greenPixelChange = new int[MAX_COLOR];
    private  int[] bluePixelChange = new int[MAX_COLOR];
    private  int[] grayPixelChange = new int[MAX_COLOR];

    private int[] redPixelHasil = new int[MAX_COLOR];
    private int[] greenPixelHasil = new int[MAX_COLOR];
    private int[] bluePixelHasil = new int[MAX_COLOR];
    private int[] grayPixelHasil = new int[MAX_COLOR];

    private int[] redPixelCumulativeHasil = new int[MAX_COLOR];
    private int[] greenPixelCumulativeHasil = new int[MAX_COLOR];
    private int[] bluePixelCumulativeHasil = new int[MAX_COLOR];
    private int[] grayPixelCumulativeHasil = new int[MAX_COLOR];

    private int[] redPixelCumulative = new int[MAX_COLOR];
    private int[] greenPixelCumulative = new int[MAX_COLOR];
    private int[] bluePixelCumulative = new int[MAX_COLOR];
    private int[] grayPixelCumulative = new int[MAX_COLOR];

    private BarChart redChartHasil;
    private BarChart greenChartHasil;
    private BarChart blueChartHasil;
    private BarChart grayChartHasil;

    protected void initChangePixel(){
        for (int i = 0;i < MAX_COLOR; ++i) {
            redPixelChange[i] = -1;
            greenPixelChange[i] = -1;
            bluePixelChange[i] = -1;
            greenPixelChange[i] = -1;
        }
    }

    private void bitmapAnalyzerUsingThreshold() {
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
        histogramEqualizationVisualizer();
    }

    private void equalizationPixel(){
        redPixelCumulativeHasil = new int[MAX_COLOR];
        greenPixelCumulativeHasil = new int[MAX_COLOR];
        bluePixelCumulativeHasil = new int[MAX_COLOR];
        grayPixelCumulativeHasil = new int[MAX_COLOR];

        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
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


    private void setBitmapNewValEqualization() {

        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

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

                int newPixel = Color.rgb(
                        (int)redHasil,
                        (int)greenHasil,
                        (int)blueHasil);
                output.setPixel(j, i, newPixel);
            }
        }
        photoView.setImageBitmap(output);
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

    ///** =========== kode untuk brightness - end ============= **///
    ///** =========== kode untuk brightness - end ============= **///


    ///** =========== kode untuk GRAYSCALE - start ============= **///

    private void setBitmapGrayscaleEqualization() {
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
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
        photoView.setImageBitmap(output);
    }

    ///** =========== kode untuk GRAYSCALE - end ============= **///


    ///** =========== kode untuk BLACK and WHITE - START ============= **///

    private void setImageToBlackAndWhite() {
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        int count = 0;
        output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gray = ((red + green + blue) / 3) % 256;

                if (gray > threshold) gray = 255;
                else  gray = 0;

                int newPixel = Color.argb(
                        alpha,
                        gray,
                        gray,
                        gray);

                if (count < 50) {
                    count++;
                }
                output.setPixel(j, i, newPixel);
            }
        }
        photoView.setImageBitmap(output);
    }

    ///** =========== kode untuk BLACK and WHITE - START ============= **///
}
