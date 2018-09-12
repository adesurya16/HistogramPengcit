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

    private Uri imageUri;

    private static final int MAX_COLOR = 256;

    private ImageView imageView;
    private ImageView iv_photo_hasil;

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

                selectedImage = getResizedBitmap(selectedImage, 200);// 400 is for example, replace with desired size

                imageView.setImageBitmap(selectedImage);

                progressBar.setVisibility(View.VISIBLE);

                new SetImageBeginning().execute();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LandingPageTugas3Equalizer.this, "foto ga ada gan", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class SetImageBeginning extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            setBitmapGrayscaleEqualization();
            bitmapAnalyzer();

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

                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void listenerAll() {
        seekBarLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekBarLeft.setText(String.valueOf(progress));
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
}
