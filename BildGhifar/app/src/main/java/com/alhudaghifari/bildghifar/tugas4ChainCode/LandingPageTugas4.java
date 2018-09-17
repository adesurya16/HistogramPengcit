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
import java.util.ArrayList;

import com.alhudaghifari.bildghifar.R;
import com.alhudaghifari.bildghifar.tugas1Histogram.PickImageActivity;

import java.io.InputStream;

public class LandingPageTugas4 extends AppCompatActivity {

    private static final String TAG = LandingPageTugas4.class.getSimpleName();

    private static final int PICK_IMAGE = 23;
    private static final int MAX_DIRECTION = 8;
    private static  final int MAX_COLOR = 256;
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

    private int matrixBlackWhite[][];

    private ArrayList<Integer> chainCode;



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

    public void initMatrixBlackWhite(){
        BitmapDrawable bd = (BitmapDrawable) ivTextPhoto.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        this.matrixBlackWhite = new int[height][];
        for (int i=0;i<height;i++){
            this.matrixBlackWhite[i] = new int[width];
        }
    }

    public void initChainCode(){
        this.chainCode = new ArrayList<Integer>();
    }

    public void fillMatrixBlackWhite(){
        BitmapDrawable bd = (BitmapDrawable) ivTextPhoto.getDrawable();
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

    public void mainBorderTracing(){
        // red = -1 border mark
        // black = 1
        // white = 0
        BitmapDrawable bd = (BitmapDrawable) ivTextPhoto.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
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

    public void tracingImage(int x, int y){
        BitmapDrawable bd = (BitmapDrawable) ivTextPhoto.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

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

    private void setImageToBlackAndWhiteResult(){
        BitmapDrawable bd = (BitmapDrawable) ivTextPhoto.getDrawable();
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        output = bd.getBitmap().copy(Bitmap.Config.RGB_565, true);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = bd.getBitmap().getPixel(j, i);
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
        ivTextPhotoHasilIdentifikasi.setImageBitmap(output);
    }

    private void printChainCode() {
        String result = "";
        for(int i=0;i<this.chainCode.size();i++){
            if (i < this.chainCode.size() - 1){
                result = result + " ";
            }
            result = result + this.chainCode.get(i);
        }
        tvTextHasilIdentifikasi.setText(result);
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
        initMatrixBlackWhite();
        initChainCode();
        fillMatrixBlackWhite();
        mainBorderTracing();
        setImageToBlackAndWhiteResult();
        printChainCode();
    }
}
