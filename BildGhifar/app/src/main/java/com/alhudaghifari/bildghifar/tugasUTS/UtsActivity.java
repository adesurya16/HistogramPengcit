package com.alhudaghifari.bildghifar.tugasUTS;

import android.app.ProgressDialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alhudaghifari.bildghifar.R;
import com.alhudaghifari.bildghifar.tugas2carigarismuka.LandingPageTugas2;
import com.alhudaghifari.bildghifar.tugas3Equalizer.LandingPageTugas3Equalizer;
import com.alhudaghifari.bildghifar.tugas4ChainCode.TemplateChainCode;
import com.alhudaghifari.bildghifar.tugas5Thinning.ZhangSuen;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.IOException;
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
    private LinearLayout linlayThresholdThinning;

    private RelativeLayout rellayMainPhoto;

    private ImageView ivCheckDone;
    private ImageView ivCheckEqualizer;

    private Bitmap originalPhoto;
    private Bitmap output;

    private ScrollView svEqualizer;
    private ScrollView svHistogram;

    private TextView tvJudulMenu;
    private TextView tvPrediction;
    private TextView tvThreshold;
    private TextView tvThresholdThinning;
    private TextView tvSeekBarLeft;
    private TextView tvSeekBarCenter;
    private TextView tvSeekBarRight;
    private TextView tvJudulThresholdThinning;

    private SeekBar seekBarThreshold;
    private SeekBar seekBarLeft;
    private SeekBar seekBarCenter;
    private SeekBar seekBarRight;
    private SeekBar seekBarThresholdThinning;

    private int statusPage;
    private int threshold = 128;
    private int thresholdThinning = 5;
    private int thresholdLeft;
    private int thresholdCenter;
    private int thresholdRight;

    private ProgressBar progress_bar;
    private Collection cs;

    ProgressDialog dialog;

    private BarChart redChart;
    private BarChart greenChart;
    private BarChart blueChart;
    private BarChart grayChart;

    private int[] redPixel = new int[MAX_COLOR];
    private int[] greenPixel = new int[MAX_COLOR];
    private int[] bluePixel = new int[MAX_COLOR];
    private int[] grayPixel = new int[MAX_COLOR];

    private int[][] redPixel2;
    private int[][] greenPixel2;
    private int[][] bluePixel2;

    private BarChart buzier_red_chart_hasil;
    private BarChart buzier_green_chart_hasil;
    private BarChart buzier_blue_chart_hasil;
    private BarChart buzier_gray_chart_hasil;

    private ZhangSuen zhangSuen;
    private PredictCharUsingChainCode predictCharUsingChainCode;

    private boolean isOpen = false;
    private boolean isHistogramOpened = false;
    private boolean isPhotoSet = false;

    private final int SHOW_BRIGHTNESS = 0;
    private final int SHOW_GRAYSCALE = 1;
    private final int SHOW_BW = 2;
    private final int SHOW_EQUALIZER = 3;
    private final int SHOW_CHAINCODE = 4;
    private final int SHOW_THINNING = 5;
    private final int SHOW_MAIN_PHOTO = 7;
    private final int SHOW_HISTOGRAM = 8;
    private final int SHOW_MEAN = 6;
    private final int SHOW_MEDIAN = 7;
    private final int SHOW_DIFFERENCE = 8;
    private final int SHOW_GRADIENT = 9;
    private final int SHOW_HOME = 10;

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
        linlayThresholdThinning = (LinearLayout) findViewById(R.id.linlayThresholdThinning);
        tvJudulMenu = (TextView) findViewById(R.id.tvJudulMenu);
        tvPrediction = (TextView) findViewById(R.id.tvPrediction);
        tvThreshold = (TextView) findViewById(R.id.tvThreshold);
        tvSeekBarLeft = (TextView) findViewById(R.id.tvSeekBarLeft);
        tvSeekBarCenter = (TextView) findViewById(R.id.tvSeekBarCenter);
        tvSeekBarRight = (TextView) findViewById(R.id.tvSeekBarRight);
        tvJudulThresholdThinning = (TextView) findViewById(R.id.tvJudulThresholdThinning);
        tvThresholdThinning = (TextView) findViewById(R.id.tvThresholdThinning);
        seekBarThreshold = (SeekBar) findViewById(R.id.seekBarThreshold);
        seekBarLeft = (SeekBar) findViewById(R.id.seekBarLeft);
        seekBarCenter = (SeekBar) findViewById(R.id.seekBarCenter);
        seekBarRight = (SeekBar) findViewById(R.id.seekBarRight);
        seekBarThresholdThinning = (SeekBar) findViewById(R.id.seekBarThresholdThinning);
        ivCheckDone = (ImageView) findViewById(R.id.ivCheckDone);
        ivCheckEqualizer = (ImageView) findViewById(R.id.ivCheckEqualizer);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        chart_buzier = (BarChart) findViewById(R.id.chart_buzier);

        redChart = (BarChart) findViewById(R.id.red_chart);
        greenChart = (BarChart) findViewById(R.id.green_chart);
        blueChart = (BarChart) findViewById(R.id.blue_chart);
        grayChart = (BarChart) findViewById(R.id.gray_chart);

        // untuk brightness
        redChartHasil = (BarChart) findViewById(R.id.red_chart_hasil);
        greenChartHasil = (BarChart) findViewById(R.id.green_chart_hasil);
        blueChartHasil = (BarChart) findViewById(R.id.blue_chart_hasil);
        grayChartHasil = (BarChart) findViewById(R.id.gray_chart_hasil);

        buzier_red_chart_hasil = (BarChart) findViewById(R.id.buzier_red_chart_hasil);
        buzier_green_chart_hasil = (BarChart) findViewById(R.id.buzier_green_chart_hasil);
        buzier_blue_chart_hasil = (BarChart) findViewById(R.id.buzier_blue_chart_hasil);
        buzier_gray_chart_hasil = (BarChart) findViewById(R.id.buzier_gray_chart_hasil);


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
                tvPrediction.setText("Prediction : ");
                setAdapterUts(-1);

                output = selectedImage.copy(Bitmap.Config.RGB_565, true);
                bitmapAnalyzer();
                isPhotoSet = true;
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
                if (isPhotoSet) {

                    if (posisi == SHOW_HOME) {
                        setAdapterUts(-1);
                        photoView.setImageBitmap(originalPhoto);
                        bitmapAnalyzer();
                        showPage(posisi);
                    } else if (posisi == SHOW_GRAYSCALE) {
                        setBitmapGrayscaleEqualization();
                        setAdapterUts(posisi);
                        showPage(posisi);
                    } else if (posisi == SHOW_EQUALIZER) {
                        getPointQuadraticbuzier();
                        histogramBuzierVisualizer();
                        setAdapterUts(posisi);
                        showPage(posisi);
                    } else if (posisi == SHOW_MEAN) {
                        new FilterImage().execute(new Integer(SHOW_MEAN));
                    } else if (posisi == SHOW_MEDIAN) {
                        new FilterImage().execute(new Integer(SHOW_MEDIAN));
                    } else if (posisi == SHOW_DIFFERENCE) {
                        new FilterImage().execute(new Integer(SHOW_DIFFERENCE));
                    } else if (posisi == SHOW_GRADIENT) {
                        new FilterImage().execute(new Integer(SHOW_GRADIENT));
                    }
                    else  {
                        setAdapterUts(posisi);
                        showPage(posisi);
                    }
                }
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

        ivCheckEqualizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPointQuadraticbuzier();
                specificateHistogram();
                histogramHasilVisualizer();
                setBitmapHasilSpecification();
            }
        });

        ivCheckDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPhotoSet) {
                    progress_bar.setVisibility(View.VISIBLE);
                    switch (statusPage) {
                        case SHOW_BRIGHTNESS:
                            photoView.setImageBitmap(originalPhoto);
                            initChangePixel();
                            bitmapAnalyzerUsingThreshold();
                            setBitmapNewValEqualization();
                            break;
                        case SHOW_BW:
                            photoView.setImageBitmap(originalPhoto);
                            setImageToBlackAndWhite();
                            break;
                        case SHOW_CHAINCODE:
                            new PredictUsingChainCode().execute();
                            break;
                        case SHOW_THINNING:
                            BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
                            height = bd.getBitmap().getHeight();
                            width = bd.getBitmap().getWidth();
                            initMatrixBlackWhiteThinning();
                            fillMatrixBlackWhiteThinning();
                            cs = new Collection(matrixBlackWhite, height, width);
                            cs.setThreshold(thresholdThinning);
                            cs.thinImage();
//
                            analyzeNumberThinningResult();
                            cs.copyToMatrix(matrixBlackWhite);
                            setImageToBlackAndWhiteResultThinning();
                            break;
                    }
                    progress_bar.setVisibility(View.GONE);
                }
            }
        });

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

        seekBarThresholdThinning.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvThresholdThinning.setText(String.valueOf(progress));
                thresholdThinning = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void bitmapAnalyzer() {
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        height = bd.getBitmap().getHeight();
        width = bd.getBitmap().getWidth();

        redPixel = new int[MAX_COLOR];
        greenPixel = new int[MAX_COLOR];
        bluePixel = new int[MAX_COLOR];
        grayPixel = new int[MAX_COLOR];

        redPixel2 = new int[height][];
        greenPixel2 = new int[height][];
        bluePixel2 = new int[height][width];


        for (int i = 0; i < height; ++i) {
            redPixel2[i] = new int[width];
            greenPixel2[i] = new int[width];
            bluePixel2[i] = new int[width];

            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gray = ((red + green + blue) / 3) % 256;

                redPixel2[i][j] = red;
                greenPixel2[i][j] = green;
                bluePixel2[i][j] = blue;

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
        recyclerUts = new RecyclerUts(UtsActivity.this, 11, clicked);
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
        linlayThresholdThinning.setVisibility(View.GONE);
        tvJudulThresholdThinning.setVisibility(View.GONE);
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
                linlayThresholdThinning.setVisibility(View.VISIBLE);
                tvJudulThresholdThinning.setVisibility(View.VISIBLE);
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
                statusPage = page;
                linlaySubMenu.setVisibility(View.GONE);
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

    private void setBitmapResultMatrix2d() {
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        final Bitmap output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int red = redPixel2[i][j];
                int green = greenPixel2[i][j];
                int blue = bluePixel2[i][j];

                int newPixel = Color.rgb(
                        red,
                        green,
                        blue);
                output.setPixel(j, i, newPixel);
            }
        }
        photoView.setImageBitmap(output);
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
        height = bd.getBitmap().getHeight();
        width = bd.getBitmap().getWidth();
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

    ///** =========== kode untuk BLACK and WHITE - end ============= **///


    ///** =========== kode untuk EQUALIZER - START ============= **///

    private class point{
        public int x;
        public int y;
        public point(){
            this.x = 0;
            this.y = 0;
        }
    }

    private static final int MAX_POINT = 3;
    private point[] points;
    private int[] histogramPixel2;

    private BarChart chart_buzier;

    private int[] buzierPixelCumulative;

    private int[] lookupRedPixel;
    private int[] lookupGreenPixel;
    private int[] lookupBluePixel;
    private int[] lookupGrayPixel;

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

        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
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

    private void setBitmapHasilSpecification() {
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

                // lookup
                double redHasil = (double) lookupRedPixel[red];
                double greenHasil = (double) lookupGreenPixel[green];
                double blueHasil = (double) lookupBluePixel[blue];

                int newPixel = Color.rgb(
                        (int)redHasil,
                        (int)greenHasil,
                        (int)blueHasil);
                output.setPixel(j, i, newPixel);
            }
        }
        photoView.setImageBitmap(output);
    }

    ///** =========== kode untuk EQUALIZER - end ============= **///


    ///** =========== kode untuk chain code - start ============= **///

    private int matrixBlackWhite[][];
    private ArrayList<Integer> chainCode;
    private static final int MAX_DIRECTION = 8;
    int height;
    int width;

    public void initMatrixBlackWhite(){
        this.matrixBlackWhite = new int[height][];
        for (int i=0;i<height;i++){
            this.matrixBlackWhite[i] = new int[width];
        }
    }

    public void initChainCode(){
        this.chainCode = new ArrayList<Integer>();
    }

    public void fillMatrixBlackWhite(){
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = output.getPixel(j, i);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gray = ((red + green + blue) / 3) % 256;

                if (gray > threshold) {
                    this.matrixBlackWhite[i][j] = 0;
                } else {
                    this.matrixBlackWhite[i][j] = 1;
                }
            }
        }
    }

    public void mainBorderTracing(){
        // red = -1 border mark
        // black = 1
        // white = 0
        // asumsi 1 object shape dulu
        boolean isFound = false;
        for (int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                if (matrixBlackWhite[i][j] > 0 && !isFound){
                    // call proses
                    tracingImage(i,j);
                    // add to List of shape (for multiple number image)
                    isFound = true;
                }
            }
        }
    }

    private void setImageToBlackAndWhiteResult(){
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = output.getHeight();
        int width = output.getWidth();

        output = output.copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = output.getPixel(j, i);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                if (this.matrixBlackWhite[i][j] == -1){
                    red = MAX_COLOR - 1;
                    green = 0;
                    blue = 0;
                }

                int newPixel = Color.argb(
                        alpha,
                        red,
                        green,
                        blue);

                output.setPixel(j, i, newPixel);
            }
        }
        photoView.setImageBitmap(output);
    }


    private void chainCodeRecognition() {
        String resultChainCode = "";
        for (Integer s : chainCode)
        {
            resultChainCode += s;
        }

        int minEditDist = editDistDP(TemplateChainCode.templateChainCode[0], resultChainCode,
                TemplateChainCode.templateChainCode[0].length(), resultChainCode.length());
        Log.d(TAG, "0. minEditDistance : " + minEditDist);
        int tempMin;
        int index = 0;
        for (int i=1;i<10;i++) {
            tempMin = editDistDP(TemplateChainCode.templateChainCode[i], resultChainCode,
                    TemplateChainCode.templateChainCode[i].length(), resultChainCode.length());
            Log.d(TAG, i + ". tempMin : " + tempMin);
            if (tempMin < minEditDist) {
                minEditDist = tempMin;
                index = i;
            }
        }
        Log.d(TAG, "hasil prediksi : " + index);

        tvPrediction.setText("Prediction : " + index);
    }


    private int editDistDP(String str1, String str2, int m, int n)
    {
        int dp[][] = new int[m+1][n+1];

        for (int i=0; i<=m; i++)
        {
            for (int j=0; j<=n; j++)
            {
                if (i==0)
                    dp[i][j] = j;
                else if (j==0)
                    dp[i][j] = i;
                else if (str1.charAt(i-1) == str2.charAt(j-1))
                    dp[i][j] = dp[i-1][j-1];
                else
                    dp[i][j] = 1 + min(dp[i][j-1],  // Insert
                            dp[i-1][j],  // Remove
                            dp[i-1][j-1]); // Replace
            }
        }

        return dp[m][n];
    }

    private int min(int x, int y, int z)
    {
        if (x <= y && x <= z) return x;
        if (y <= x && y <= z) return y;
        else return z;
    }

    public void tracingImage(int x, int y){
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = output.getHeight();
        int width = output.getWidth();

        // isShape = true if tracing result make a shape
        int xBegin = x;
        int yBegin = y;
        int dir = MAX_DIRECTION - 1;

        int xPrev = xBegin;
        int yPrev = yBegin;
        // init
        int from = 0;
        if (dir % 2 == 0){
            from = (dir + 7) % MAX_DIRECTION;
        }else{
            from = (dir + 6) % MAX_DIRECTION;
        }
        boolean found = false;
        // System.out.println("LOOP");
        for(int i=0;i<MAX_DIRECTION;i++){
            if (from == 0){
                xBegin = xPrev;
                yBegin = yPrev + 1;
            }else if (from == 1){
                xBegin = xPrev - 1;
                yBegin = yPrev + 1;
            }else if (from == 2){
                xBegin = xPrev - 1;
                yBegin = yPrev;
            }else if (from == 3){
                xBegin = xPrev - 1;
                yBegin = yPrev - 1;
            }else if (from == 4){
                xBegin = xPrev;
                yBegin = yPrev - 1;
            }else if (from == 5){
                xBegin = xPrev + 1;
                yBegin = yPrev - 1;
            }else if (from == 6){
                xBegin = xPrev + 1;
                yBegin = yPrev;
            }else if (from == 7){
                xBegin = xPrev + 1;
                yBegin = yPrev + 1;
            }

            if ((xBegin >= 0 && xBegin < height) && (yBegin >= 0 && yBegin < width)){
                // System.out.println(xBegin + " " + yBegin);
                if (this.matrixBlackWhite[xBegin][yBegin] == 1){
                    // System.out.println("masuk");
                    found = true;
                    // in case multiple object make it different method
                    this.matrixBlackWhite[xBegin][yBegin] = -1;
                }
            }

            if (found){
                break;
            }else{
                from = (from + 1) % MAX_DIRECTION; // counter
            }
        }
        dir = from;
        this.chainCode.add(dir);
        // loop
        while(xBegin != x || yBegin != y){
            from = 0;

            xPrev = xBegin;
            yPrev = yBegin;
            if (dir % 2 == 0){
                from = (dir + 7) % MAX_DIRECTION;
            }else{
                from = (dir + 6) % MAX_DIRECTION;
            }
            found = false;
            // System.out.println("LOOP");
            for(int i=0;i<MAX_DIRECTION;i++){
                // System.out.println( "dir = " + from);
                if (from == 0){
                    xBegin = xPrev;
                    yBegin = yPrev + 1;
                }else if (from == 1){
                    xBegin = xPrev - 1;
                    yBegin = yPrev + 1;
                }else if (from == 2){
                    xBegin = xPrev - 1;
                    yBegin = yPrev;
                }else if (from == 3){
                    xBegin = xPrev - 1;
                    yBegin = yPrev - 1;
                }else if (from == 4){
                    xBegin = xPrev;
                    yBegin = yPrev - 1;
                }else if (from == 5){
                    xBegin = xPrev + 1;
                    yBegin = yPrev - 1;
                }else if (from == 6){
                    xBegin = xPrev + 1;
                    yBegin = yPrev;
                }else if (from == 7){
                    xBegin = xPrev + 1;
                    yBegin = yPrev + 1;
                }

                if ((xBegin >= 0 && xBegin < height) && (yBegin >= 0 && yBegin < width)){
                    // System.out.println(xBegin + " " + yBegin);
                    if (this.matrixBlackWhite[xBegin][yBegin] == 1){
                        // System.out.println("masuk");
                        found = true;
                        this.matrixBlackWhite[xBegin][yBegin] = -1;
                    }
                }

                if (found){
                    break;
                }else{
                    from = (from + 1) % MAX_DIRECTION;
                }
            }
            // gak mungkin not found
            int last = this.chainCode.size() - 1;
            if (!found || (Math.abs(this.chainCode.get(last) - from) == 4)){
                break; //stop
            }else{
                dir = from;
                this.chainCode.add(dir);
            }
        }
    }

    private void initializedImageToBitmap() {
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        height = bd.getBitmap().getHeight();
        width = bd.getBitmap().getWidth();
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

                output.setPixel(j, i, newPixel);
            }
        }
    }

    private class FilterImage extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            dialog = ProgressDialog.show(UtsActivity.this, "Loading",
                    "Loading. Please wait...", true);
            dialog.setCancelable(false);
            showPage(SHOW_MAIN_PHOTO);
        }

        @Override
        protected Integer doInBackground(Integer... buttonClicked) {
            int value = buttonClicked[0].intValue();

            OperatorFilter of = new OperatorFilter(redPixel2, greenPixel2, bluePixel2, height, width);

            if (value == SHOW_MEAN) {
                of.meanOperation();
            } else if (value == SHOW_MEDIAN) {
                of.medianOperation();
            } else if (value == SHOW_DIFFERENCE) {
                of.differenceOperation();
            } else if (value == SHOW_GRADIENT) {
                of.gradientOperation();
            }

            redPixel2 = of.getPixImageRed();
            greenPixel2 = of.getPixImageGreen();
            bluePixel2 = of.getPixImageBlue();

            return value;
        }

        @Override
        protected void onPostExecute(Integer a) {
            Log.d(TAG, "onPostExecute");
            dialog.dismiss();

            setBitmapResultMatrix2d();
            setAdapterUts(a);
        }
    }

    private class PredictUsingChainCode extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            progress_bar.setVisibility(View.VISIBLE);
            dialog = ProgressDialog.show(UtsActivity.this, "Loading",
                    "Loading. Please wait...", true);
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground");
            initializedImageToBitmap();
            initMatrixBlackWhite();
            initChainCode();
            fillMatrixBlackWhite();
            mainBorderTracing();
//                        chainCodeRecognition();
            Log.d(TAG, "chaincode array length : " + chainCode.size());
            predictCharUsingChainCode = new PredictCharUsingChainCode(getApplicationContext());
            predictCharUsingChainCode.predictCharKnn(chainCode,10);
            return "";
        }

        @Override
        protected void onPostExecute(String a) {
            Log.d(TAG, "onPostExecute");
            dialog.dismiss();
//            progress_bar.setVisibility(View.GONE);
            setImageToBlackAndWhiteResult();
            tvPrediction.setText("Prediction : " + predictCharUsingChainCode.getCharPredicted() + "\n" +
                                "nearest char : " + predictCharUsingChainCode.getNearestNeighbour());
        }
    }

    ///** =========== kode untuk chain code - end ============= **///

    private final int THRESHOLD_POST_PROCESSING = 20;

    private void analyzeNumberThinningResult(){
        cs.setThinningList();
        cs.getBoundPoints();
        cs.getSkeletons();
        cs.postProcessingThresholdAll();
        cs.getUpdateMatrix();

        int numberAscii = cs.recognizeCharacterAscii();

        Log.d(TAG, "hasil prediksi : " + numberAscii);
        tvPrediction.setText("Prediction : " + (char) numberAscii);
    }


    private void setImageToBlackAndWhiteResultThinning(){
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        output = output.copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);


                if (this.matrixBlackWhite[i][j] == 0){
                    red = MAX_COLOR - 1;
                    green = MAX_COLOR - 1;
                    blue = MAX_COLOR - 1;
                }else{
                    red = 0;
                    green = 0;
                    blue = 0;
                }

                int newPixel = Color.argb(
                        alpha,
                        red,
                        green,
                        blue);


                output.setPixel(j, i, newPixel);
            }
        }
        photoView.setImageBitmap(output);
    }


    public void initMatrixBlackWhiteThinning(){
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        this.matrixBlackWhite = new int[height][];
        for (int i=0;i<height;i++){
            this.matrixBlackWhite[i] = new int[width];
        }
    }

    public void fillMatrixBlackWhiteThinning(){
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gray = ((red + green + blue) / 3) % 256;

                if (gray > threshold) {
                    this.matrixBlackWhite[i][j] = 0;
                }else {
                    this.matrixBlackWhite[i][j] = 1;
                }

            }
        }
    }

    ///** =========== kode untuk thinning - start ============= **///

}
