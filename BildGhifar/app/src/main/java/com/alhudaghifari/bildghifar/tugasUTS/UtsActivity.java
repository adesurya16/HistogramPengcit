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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alhudaghifari.bildghifar.R;
import com.alhudaghifari.bildghifar.tugas4ChainCode.TemplateChainCode;
import com.alhudaghifari.bildghifar.tugas5Thinning.ZhangSuen;
import com.alhudaghifari.bildghifar.utils.OtsuThresholder;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.ByteArrayOutputStream;
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
    private LinearLayout linlayCustomOperator;

    private RelativeLayout rellayMainPhoto;

    private ImageView ivCheckDone;
    private ImageView ivCheckEqualizer;

    private EditText et00;
    private EditText et01;
    private EditText et02;
    private EditText et10;
    private EditText et11;
    private EditText et12;
    private EditText et20;
    private EditText et21;
    private EditText et22;

    private Bitmap originalPhoto;
    private Bitmap output;
    private byte[] bytePhoto;

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

    private ProgressBar progress_bar;
    private Collection cs;

    private ProgressDialog dialog;
    private Button btnCustomOperator;

    private BarChart redChart;
    private BarChart greenChart;
    private BarChart blueChart;
    private BarChart grayChart;

    private int[] redPixel = new int[MAX_COLOR];
    private int[] greenPixel = new int[MAX_COLOR];
    private int[] bluePixel = new int[MAX_COLOR];
    private int[] grayPixel = new int[MAX_COLOR];
    private int[][] grayScaleValues;

    private int[][] redPixel2;
    private int[][] greenPixel2;
    private int[][] bluePixel2;
    private int[][] pixel;
    private int[][] matrixBw;

    private BarChart buzier_red_chart_hasil;
    private BarChart buzier_green_chart_hasil;
    private BarChart buzier_blue_chart_hasil;
    private BarChart buzier_gray_chart_hasil;

    private double val00;
    private double val01;
    private double val02;
    private double val10;
    private double val11;
    private double val12;
    private double val20;
    private double val21;
    private double val22;

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
    private final int SHOW_SOBEL = 10;
    private final int SHOW_PREWIT = 11;
    private final int SHOW_FREI_CHEN = 12;
    private final int SHOW_ROBERT = 13;
    private final int SHOW_CUSTOM_OPERATOR = 14;
    private final int SHOW_FACE_DETECTION = 15;
    private final int SHOW_HOME = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uts);

        photoView = (PhotoView) findViewById(R.id.photo_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        linlaySubMenu = (LinearLayout) findViewById(R.id.linlaySubMenu);
        linlayJudul = (LinearLayout) findViewById(R.id.linlayJudul);
        linlayCustomOperator = (LinearLayout) findViewById(R.id.linlayCustomOperator);
        et00 = (EditText) findViewById(R.id.et00);
        et01 = (EditText) findViewById(R.id.et01);
        et02 = (EditText) findViewById(R.id.et02);
        et10 = (EditText) findViewById(R.id.et10);
        et11 = (EditText) findViewById(R.id.et11);
        et12 = (EditText) findViewById(R.id.et12);
        et20 = (EditText) findViewById(R.id.et20);
        et21 = (EditText) findViewById(R.id.et21);
        et22 = (EditText) findViewById(R.id.et22);
        btnCustomOperator = (Button) findViewById(R.id.btnCustomOperation);
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
                        photoView.setImageBitmap(originalPhoto);
                        bitmapAnalyzer();
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
                    } else if (posisi == SHOW_SOBEL) {
                        new FilterImage().execute(new Integer(SHOW_SOBEL));
                    } else if (posisi == SHOW_PREWIT) {
                        new FilterImage().execute(new Integer(SHOW_PREWIT));
                    } else if (posisi == SHOW_FREI_CHEN) {
                        new FilterImage().execute(new Integer(SHOW_FREI_CHEN));
                    } else if (posisi == SHOW_ROBERT) {
                        new FilterImage().execute(new Integer(SHOW_ROBERT));
                    } else if (posisi == SHOW_CUSTOM_OPERATOR) {
                        showPage(SHOW_CUSTOM_OPERATOR);
                    } else if (posisi == SHOW_FACE_DETECTION) {
                        new DetectFace().execute();
                    }
                    else  {
                        setAdapterUts(posisi);
                        showPage(posisi);
                    }
                }
            }
        };

        btnCustomOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et00.getText().toString().trim().isEmpty()) val00 = 1;
                else val00 = Double.parseDouble(et00.getText().toString().trim());
                if (et01.getText().toString().trim().isEmpty()) val01 = 1;
                else val01 = Double.parseDouble(et01.getText().toString().trim());
                if (et02.getText().toString().trim().isEmpty()) val02 = 1;
                else val02 = Double.parseDouble(et02.getText().toString().trim());
                if (et10.getText().toString().trim().isEmpty()) val10 = 1;
                else val10 = Double.parseDouble(et10.getText().toString().trim());
                if (et11.getText().toString().trim().isEmpty()) val11 = 1;
                else val11 = Double.parseDouble(et11.getText().toString().trim());
                if (et12.getText().toString().trim().isEmpty()) val12 = 1;
                else val12 = Double.parseDouble(et12.getText().toString().trim());
                if (et20.getText().toString().trim().isEmpty()) val20 = 1;
                else val20 = Double.parseDouble(et20.getText().toString().trim());
                if (et21.getText().toString().trim().isEmpty()) val21 = 1;
                else val21 = Double.parseDouble(et21.getText().toString().trim());
                if (et22.getText().toString().trim().isEmpty()) val22 = 1;
                else val22 = Double.parseDouble(et22.getText().toString().trim());

                Log.d(TAG, "val00 : " + val00 + " val01 : " + val01 + " val02 : " + val02 + "\n" +
                                "val10 : " + val10 + " val11 : " + val11 + " val12 : " + val12 + "\n" +
                                "val20 : " + val20 + " val21 : " + val21 + " val22 : " + val22 + "\n");

                new FilterImage().execute(new Integer(SHOW_CUSTOM_OPERATOR));
            }
        });

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
        grayScaleValues = new int[height][];
        pixel = new int[height][width];

        for (int i = 0; i < height; ++i) {
            redPixel2[i] = new int[width];
            greenPixel2[i] = new int[width];
            bluePixel2[i] = new int[width];
            grayScaleValues[i] = new int[width];

            for (int j = 0; j < width; ++j) {
                int pixel1 = bd.getBitmap().getPixel(j, i);
                int red = Color.red(pixel1);
                int green = Color.green(pixel1);
                int blue = Color.blue(pixel1);
                int gray = ((red + green + blue) / 3) % 256;

                pixel[i][j] = pixel1;
                redPixel2[i][j] = red;
                greenPixel2[i][j] = green;
                bluePixel2[i][j] = blue;
                grayScaleValues[i][j] = gray;

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
        recyclerUts = new RecyclerUts(UtsActivity.this, 17, clicked);
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
            case SHOW_CUSTOM_OPERATOR:
                linlaySubMenu.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Custom Operator");
                linlayCustomOperator.setVisibility(View.VISIBLE);
                break;
            default:
                statusPage = page;
                linlaySubMenu.setVisibility(View.GONE);
                break;
        }
    }

    ///** =========== kode untuk FACE DETECTION - start ============= **///
    ///** =========== kode untuk FACE DETECTION - start ============= **///

    private int sumOfRowColValue;
    private int[] sumHeight;
    private int[] sumWidth;

    private class DetectFace extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            dialog = ProgressDialog.show(UtsActivity.this, "Loading",
                    "Loading. Please wait...", true);
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground");
//            imageToYCbCrOperation();
            imageSkinRgbOperation();
//            findMinimumBoundingInmatrixBw(initListFromMatrix(matrixBw));

            SkinningField sf = new SkinningField(redPixel2, greenPixel2, bluePixel2, matrixBw, height, width);
            outToFile(sf.getMarkedObjectToBW());
            return "";
        }

        @Override
        protected void onPostExecute(String a) {
            Log.d(TAG, "onPostExecute");
            dialog.dismiss();
            setImageToYCbCr();
        }
    }

    private ArrayList<point> initListFromMatrix(int[][] matBW){
        ArrayList<point> pListResult = new ArrayList<point>();
        pListResult.clear();
        int white = Color.rgb(
                255,
                255,
                255);
        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < this.width;j++){
                if (matBW[i][j] == white){
                    pListResult.add(new point(i, j));
                }
            }
        }
        return pListResult;
    }

    private void outToFile(int mat[][]){
        int val;
        for(int i = 0;i < this.height; i++){
            for(int j = 0; j< this.width; j++){
                val = mat[i][j];
                output.setPixel(j, i, val);
            }
        }
    }

    private void findMinimumBoundingInmatrixBw(ArrayList<point> pList){
        int y_start = -1;
        int y_end = this.height;
        int x_start = -1;
        int x_end = this.width;
        int val;
        for(point p : pList){
            if (x_start == -1){
                x_start = p.x;
            }else if(p.x < x_start){
                x_start = p.x;
            }

            if (x_end == this.width){
                x_end = p.x;
            }else if(p.x > x_end){
                x_end = p.x;
            }

            if (y_start == -1){
                y_start = p.y;
            }else if(p.y < y_start){
                y_start = p.y;
            }

            if(y_end == this.height){
                y_end = p.y;
            }else if(p.y > y_end){
                y_end = p.y;
            }
        }

        // jangan dipojok banget ntar error

        x_start -= 1;
        x_end += 1;

        y_start -= 1;
        y_end += 1;

        int temp;
        temp = y_start;
        y_start = x_start;
        x_start = temp;

        temp = y_end;
        y_end = x_end;
        x_end = temp;

        for (int i=0;i<height;i++) {
            for (int j=0;j<width;j++) {
//                if ((i > y_start && i < y_start + 5 &&
//                        j > x_start && j < x_end))
//                    val = Color.rgb(
//                            200,
//                            0,
//                            0);
//                else if (i > y_end + 15 && i < y_end + 10 &&
//                        j > x_start && j < x_end)
//                    val = Color.rgb(
//                            200,
//                            0,
//                            0);
                if ((i == y_start || i == y_end) &&
                        j > x_start && j < x_end)
                    val = Color.rgb(
                            200,
                            0,
                            0);
                else if ((j == x_start || j == x_end) &&
                        i > y_start && i < y_end)
                    val = Color.rgb(
                            200,
                            0,
                            0);
//                if ((i == y_start || i == y_end) && (j == x_start || j == x_end) ){
//                    val = Color.rgb(
//                            200,
//                            0,
//                            0);
//                }
                else val = matrixBw[i][j];

                output.setPixel(j, i, val);
            }
        }
    }

    private void findMinimumBounding() {
        double row_rt = sumOfRowColValue / height;
        double col_rt = sumOfRowColValue / width;
        double ht = 0.66 * row_rt;
        double vt = 0.43 * col_rt;
        int row_new[] = new int[height];
        int col_new[] = new int[width];
        int x_start = 0;
        int x_end = 0;
        int y_start = 0;
        int y_end = 0;
        int x1 = height;
        int x2 = 0;
        int y1 = width;
        int y2 = 0;
        int val;

        for (int i=0;i<height;i++) {
            if (sumHeight[i] >= ht)
                row_new[i] = 1;
            else
                row_new[i] = 0;
        }

        for (int i=0;i<width;i++) {
            if (sumWidth[i] >= vt)
                col_new[i] = 1;
            else
                col_new[i] = 0;
        }

        for (int i=0;i<height;i++) {
            for (int j=0;j<width;j++) {
                if (row_new[i] == 1 &&
                        col_new[j] == 1) {
                    if (i < x1) x1 = i;
                    if (i > x2) x2 = i;
                    if (j < y1) y1 = j;
                    if (j > y2) y2 = j;

                    x_start = x1;
                    x_end = x2;
                    y_start = y1;
                    y_end = y2;
                }
            }
        }

        Log.d(TAG, "height : " + height + " width : " + width);
        Log.d(TAG, "X_START : " + x_start + " x_end : " + x_end);
        Log.d(TAG, "Y_START : " + y_start + " y_end : " + y_end);

        for (int i=0;i<height;i++) {
            for (int j=0;j<width;j++) {
                if ((i > y_start && i < y_start + 5 &&
                        j > x_start && j < x_end))
                    val = Color.rgb(
                            200,
                            0,
                            0);
                else if (i > y_end - 15 && i < y_end - 10 &&
                        j > x_start && j < x_end)
                    val = Color.rgb(
                            200,
                            0,
                            0);
                else if ((j == x_start || j == x_end) &&
                        i > y_start && i < y_end - 10)
                    val = Color.rgb(
                            200,
                            0,
                            0);
                else val = matrixBw[i][j];
                output.setPixel(j, i, val);
            }
        }
    }

    // dari paper https://ieeexplore.ieee.org/abstract/document/6982471
    private void imageSkinRgbOperation() {
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        height = bd.getBitmap().getHeight();
        width = bd.getBitmap().getWidth();
        int val;
        sumOfRowColValue = 0;
        sumHeight = new int[height];
        sumWidth = new int[width];

        matrixBw = new int[height][width];
        output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int r = redPixel2[i][j];
                int g = greenPixel2[i][j];
                int b = bluePixel2[i][j];

                // Under flashlight or daylight called lateral illumination:
                //if (r > 20 && g > 210 && b > 170 && Math.abs(r-g) < 15 &&
                //      r > g && g > b)

                // For uniform daylight illumination:
                if (r > 95 && g > 40 && b > 20 && r > g &&
                        r > b && Math.abs(r-g) > 15 &&
                        (Math.max(Math.max(r,g), b) - Math.min(Math.min(r,g), b)) > 15)
                    val = Color.rgb(
                            255,
                            255,
                            255);
                else
                    val = Color.rgb(
                            0,
                            0,
                            0);

                sumOfRowColValue += pixel[i][j];
                matrixBw[i][j] = val;
                sumHeight[i] += pixel[i][j];
                sumWidth[j] += pixel[i][j];
            }
        }
    }

    private void imageToYCbCrOperation() {
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        // dari paper https://ieeexplore.ieee.org/abstract/document/6982471
        double tetha = 2.53;
        double costetha = Math.cos(tetha);
        double sintetha = Math.sin(tetha);
        double Cx = 109.38;
        double Cy = 152.02;
        double ecx = 2.41;
        double ecy = 2.53;
        double a = 25.39;
        double b = 14.03;
        int val;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {

                int r = redPixel2[i][j];
                int g = greenPixel2[i][j];
                int blue = bluePixel2[i][j];

                int Y = (int)(0.299*r+0.587*g+0.114*blue);
                int Cb=(int)(128-0.169*r-0.332*g+0.500*blue);
                int Cr =(int)(128+0.500*r-0.419*g-0.081*blue);

                double CbMinCx = Cb - Cx;
                double CrMinCy = Cr - Cy;
                double x = costetha * CbMinCx + sintetha * CrMinCy;
                double y = (-1*sintetha*CbMinCx) + costetha * CrMinCy;
                double equation1 = ((x - ecx) * (x - ecx)) / (a * a);
                double equation2 = ((y - ecy) * (y - ecy)) / (b * b);

                Log.d(TAG, "equation : " + (int)(equation1 + equation2));

                if ((int) (equation1 + equation2) == 1)
                    val = Color.rgb(
                            255,
                            255,
                            255);
                else
                    val = (Y<<16) | (Cb<<8) | Cr;

                output.setPixel(j, i, val);
            }
        }
    }

    private void setImageToYCbCr() {
        photoView.setImageBitmap(output);
    }

    ///** =========== kode untuk FACE DETECTION - END ============= **///
    ///** =========== kode untuk FACE DETECTION - END ============= **///

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

    private void bitmapAnalyzerForOtsu() {
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

    private void setBitmapGrayscale() {
        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        final Bitmap output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int newPixel = Color.rgb(
                        grayScaleValues[i][j],
                        grayScaleValues[i][j],
                        grayScaleValues[i][j]);
                output.setPixel(j, i, newPixel);
            }
        }
        photoView.setImageBitmap(output);
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

    private void setGrayScaleValuesFromBitmap() {

        grayScaleValues = new int[height][width];

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int red = Color.red(pixel[i][j]);
                int green = Color.green(pixel[i][j]);
                int blue = Color.blue(pixel[i][j]);

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

                int gray = (int) ((redHasil + greenHasil + blueHasil) / 3) % 256;
                grayScaleValues[i][j] = gray;
            }
        }
    }

    private void setBitmapNewValEqualization() {

        BitmapDrawable bd = (BitmapDrawable) photoView.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        final Bitmap output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        grayScaleValues = new int[height][width];

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

                int gray = (int) ((redHasil + greenHasil + blueHasil) / 3) % 256;
                grayScaleValues[i][j] = gray;

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
        public point(int x, int y){
            this.x = x;
            this.y = y;
        }
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

            OperatorFilter of;

            if (value == SHOW_MEAN) {
                of = new OperatorFilter(redPixel2, greenPixel2, bluePixel2, height, width);
                of.meanOperation();
                redPixel2 = of.getPixImageRed();
                greenPixel2 = of.getPixImageGreen();
                bluePixel2 = of.getPixImageBlue();
            } else if (value == SHOW_MEDIAN) {
                of = new OperatorFilter(redPixel2, greenPixel2, bluePixel2, height, width);
                of.medianOperation();
                redPixel2 = of.getPixImageRed();
                greenPixel2 = of.getPixImageGreen();
                bluePixel2 = of.getPixImageBlue();
            } else if (value == SHOW_DIFFERENCE) {
                of = new OperatorFilter(redPixel2, greenPixel2, bluePixel2, height, width);
                of.differenceOperation();
                redPixel2 = of.getPixImageRed();
                greenPixel2 = of.getPixImageGreen();
                bluePixel2 = of.getPixImageBlue();
            } else if (value == SHOW_GRADIENT) {
                of = new OperatorFilter(redPixel2, greenPixel2, bluePixel2, height, width);
                of.gradientOperation();
                redPixel2 = of.getPixImageRed();
                greenPixel2 = of.getPixImageGreen();
                bluePixel2 = of.getPixImageBlue();
            } else if (value == SHOW_SOBEL) {
                runOperator(SHOW_SOBEL);
            } else if (value == SHOW_PREWIT) {
                runOperator(SHOW_PREWIT);
            } else if (value == SHOW_FREI_CHEN) {
                runOperator(SHOW_FREI_CHEN);
            } else if (value == SHOW_ROBERT) {
                runOperator(SHOW_ROBERT);
            } else if (value == SHOW_CUSTOM_OPERATOR) {
                runOperator(SHOW_CUSTOM_OPERATOR);
            }

            return value;
        }

        @Override
        protected void onPostExecute(Integer a) {
            Log.d(TAG, "onPostExecute");
            dialog.dismiss();

            if (a == SHOW_SOBEL || a == SHOW_PREWIT || a == SHOW_FREI_CHEN || a == SHOW_ROBERT || a == SHOW_CUSTOM_OPERATOR) {
                setBitmapGrayscale();
            } else setBitmapResultMatrix2d();
        }
    }

    private void runOperator(int type) {
        OperatorFilter of;
        OtsuThresholder otsuThresholder;
        otsuThresholder = new OtsuThresholder();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        originalPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bytePhoto = stream.toByteArray();
        byte[] monoData = new byte[bytePhoto.length];

        Log.d(TAG, "threshold : " + threshold);
        threshold = otsuThresholder.doThreshold(bytePhoto, monoData);
        Log.d(TAG, "threshold : " + threshold);

        initChangePixel();
        bitmapAnalyzerForOtsu();
        setGrayScaleValuesFromBitmap();

        of = new OperatorFilter(grayScaleValues, height, width);

        if (type == SHOW_SOBEL) of.runSobelOperation();
        else if (type == SHOW_PREWIT) of.runPrewitOperation();
        else if (type == SHOW_FREI_CHEN) of.runFreiChenOperation();
        else if (type == SHOW_ROBERT) of.runRobertOperation();
        else if (type == SHOW_CUSTOM_OPERATOR) of.runCustomOperation(val00, val01, val02,
                                                                    val10, val11, val12,
                                                                    val20, val21, val22);

        grayScaleValues = new int[height][width];
        grayScaleValues = of.getPixImageGS();
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
