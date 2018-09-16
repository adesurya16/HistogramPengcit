package com.alhudaghifari.bildghifar.tugas4ChainCode;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alhudaghifari.bildghifar.R;
import com.alhudaghifari.bildghifar.tugas1Histogram.PickImageActivity;

import java.io.InputStream;

public class LandingPageTugas4 extends AppCompatActivity {

    private static final String TAG = LandingPageTugas4.class.getSimpleName();

    private static final int PICK_IMAGE = 23;

    private Uri imageUri;

    private ImageView ivTextPhoto;
    private ImageView ivTextPhotoHasilBw;
    private ImageView ivTextPhotoHasilIdentifikasi;
    private TextView tvTextHasilIdentifikasi;

    private Bitmap selectedImage;
    private Bitmap output;

    private LinearLayout linlaySeekBar;

    private Button btnChangeToBw;
    private Button btnTebakAngka;

    private TextView tvThreshold;

    private SeekBar seekBarThreshold;

    private int threshold = 128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page_tugas4);

        ivTextPhoto = (ImageView) findViewById(R.id.ivTextPhoto);
        ivTextPhotoHasilBw = (ImageView) findViewById(R.id.ivTextPhotoHasilBw);
        ivTextPhotoHasilIdentifikasi = (ImageView) findViewById(R.id.ivTextPhotoHasilIdentifikasi);
        tvTextHasilIdentifikasi = (TextView) findViewById(R.id.tvTextHasilIdentifikasi);
        tvThreshold = (TextView) findViewById(R.id.tvThreshold);
        seekBarThreshold = (SeekBar) findViewById(R.id.seekBarThreshold);
        linlaySeekBar = (LinearLayout) findViewById(R.id.linlaySeekBar);
        btnChangeToBw = (Button) findViewById(R.id.btnChangeToBw);
        btnTebakAngka = (Button) findViewById(R.id.btnTebakAngka);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK ) {
            imageUri = data.getData();

            Log.d(TAG,"imageUri : " + imageUri);

            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);

                selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size

                ivTextPhoto.setImageBitmap(selectedImage);

                setImageToBlackAndWhite();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LandingPageTugas4.this, "foto ga ada gan", Toast.LENGTH_SHORT).show();
            }
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

    private void setImageToBlackAndWhite() {
        BitmapDrawable bd = (BitmapDrawable) ivTextPhoto.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        int count = 0;

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
                    Log.d(TAG, "gray : " + gray + " newPixe : " + newPixel);
                    count++;
                }
                output.setPixel(j, i, newPixel);
            }
        }
        ivTextPhotoHasilBw.setImageBitmap(output);
        linlaySeekBar.setVisibility(View.VISIBLE);
        btnChangeToBw.setVisibility(View.VISIBLE);
        btnTebakAngka.setVisibility(View.VISIBLE);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void changeImageBwByThreshold(View view) {
        linlaySeekBar.setVisibility(View.GONE);
        setImageToBlackAndWhite();
        linlaySeekBar.setVisibility(View.VISIBLE);
    }

    public void uploadPhoto(View view) {
        openGallery();
    }

    public void tebakPhoto(View view) {
        tvTextHasilIdentifikasi.setVisibility(View.VISIBLE);
        ivTextPhotoHasilIdentifikasi.setVisibility(View.VISIBLE);
    }
}
