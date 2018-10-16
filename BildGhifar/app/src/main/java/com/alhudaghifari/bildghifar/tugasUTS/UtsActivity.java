package com.alhudaghifari.bildghifar.tugasUTS;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alhudaghifari.bildghifar.R;
import com.alhudaghifari.bildghifar.tugas2carigarismuka.LandingPageTugas2;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.InputStream;

public class UtsActivity extends AppCompatActivity {

    private static final String TAG = UtsActivity.class.getSimpleName();

    private static final int PICK_IMAGE = 23;

    private Uri imageUri;

    private PhotoView photoView;

    private RecyclerView mRecyclerView;
    private RecyclerUts recyclerUts;
    private RecyclerUts.OnButtonClickListener onButtonClickListener;

    private LinearLayout linlaySubMenu;
    private LinearLayout linlayJudul;
    private LinearLayout linlayThreshold;
    private ScrollView svEqualizer;
    private TextView tvJudulMenu;
    private TextView tvPrediction;

    private int statusPage;

    private final int SHOW_BRIGHTNESS = 0;
    private final int SHOW_GRAYSCALE = 1;
    private final int SHOW_BW = 2;
    private final int SHOW_EQUALIZER = 3;
    private final int SHOW_CHAINCODE = 4;
    private final int SHOW_THINNING = 5;
    private final int SHOW_HOME = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uts);

        photoView = (PhotoView) findViewById(R.id.photo_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        linlaySubMenu = (LinearLayout) findViewById(R.id.linlaySubMenu);
        linlayJudul = (LinearLayout) findViewById(R.id.linlayJudul);
        svEqualizer = (ScrollView) findViewById(R.id.svEqualizer);
        linlayThreshold = (LinearLayout) findViewById(R.id.linlayThreshold);
        tvJudulMenu = (TextView) findViewById(R.id.tvJudulMenu);
        tvPrediction = (TextView) findViewById(R.id.tvPrediction);

        setAdapterUts();

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

                photoView.setImageBitmap(selectedImage);

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

    private void setAdapterUts() {
        onButtonClickListener = new RecyclerUts.OnButtonClickListener() {
            @Override
            public void onClick(int posisi) {
                Log.d(TAG, "onButtonClickListener posisi " + posisi);
                showPage(posisi);
            }
        };

        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(UtsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerUts = new RecyclerUts(UtsActivity.this, 7);
        recyclerUts.setOnButtonYaListener(onButtonClickListener);
        mRecyclerView.setAdapter(recyclerUts);
    }

    private void showPage(int page) {
        linlaySubMenu.setVisibility(View.GONE);
        linlayThreshold.setVisibility(View.GONE);
        svEqualizer.setVisibility(View.GONE);
        tvPrediction.setVisibility(View.GONE);

        switch (page) {
            case SHOW_HOME:
                statusPage = SHOW_HOME;
                break;
            case SHOW_BRIGHTNESS:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Brighness");
                statusPage = SHOW_BRIGHTNESS;
                break;
            case SHOW_GRAYSCALE:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Grayscale");
                statusPage = SHOW_GRAYSCALE;
                break;
            case SHOW_BW:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Black and White");
                statusPage = SHOW_BW;
                break;
            case SHOW_EQUALIZER:
                svEqualizer.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Equalizer");
                statusPage = SHOW_EQUALIZER;
                break;
            case SHOW_CHAINCODE:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvPrediction.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Number Recognition using Chain code");
                statusPage = SHOW_CHAINCODE;
                break;
            case SHOW_THINNING:
                linlayThreshold.setVisibility(View.VISIBLE);
                tvPrediction.setVisibility(View.VISIBLE);
                tvJudulMenu.setText("Number Recognition using Zhang Suen");
                statusPage = SHOW_THINNING;
                break;
            default:
                break;
        }
    }
}
