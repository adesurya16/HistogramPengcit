package com.alhudaghifari.bildghifar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;

    private Toast toast;

    private RecyclerPhoto mRecyclerPhoto;

    private RecyclerPhoto.OnArtikelClickListener mOnArtikelClickListener;

    private Button btnPickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        btnPickImage = (Button) findViewById(R.id.btnPickImage);

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PickImageActivity.class);
                startActivity(intent);
            }
        });

        inisialisasiListener();
        inisialisasiTampilan();
    }

    private void inisialisasiListener() {
        mOnArtikelClickListener = new RecyclerPhoto.OnArtikelClickListener() {
            @Override
            public void onClick(int posisi) {
                try {
                    if (toast != null) {
                        toast.cancel();
                    }

                    Log.d("KLIK Posisi : ", posisi + "");

                    switch (posisi) {
                        case 0:
                            Intent intentdetil = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil.putExtra(Constant.imageTitle, Constant.a);
                            intentdetil.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil);
                            break;

                        case 1:
                            Intent intentdetil1 = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil1.putExtra(Constant.imageTitle, Constant.b);
                            intentdetil1.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil1);
                            break;

                        case 2:
                            Intent intentdetil2 = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil2.putExtra(Constant.imageTitle, Constant.c);
                            intentdetil2.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil2);
                            break;

                        case 3:
                            Intent intentdetil3 = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil3.putExtra(Constant.imageTitle, Constant.d);
                            intentdetil3.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil3);
                            break;

                        case 4:
                            Intent intentdetil4 = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil4.putExtra(Constant.imageTitle, Constant.e);
                            intentdetil4.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil4);
                            break;

                        case 5:
                            Intent intentdetil5 = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil5.putExtra(Constant.imageTitle, Constant.f);
                            intentdetil5.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil5);
                            break;

                        case 6:
                            Intent intentdetil6 = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil6.putExtra(Constant.imageTitle, Constant.g);
                            intentdetil6.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil6);
                            break;

                        case 7:
                            Intent intentdetil7 = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil7.putExtra(Constant.imageTitle, Constant.h);
                            intentdetil7.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil7);
                            break;
                        case 8:
                            Intent intentdetil8 = new Intent(MainActivity.this, MpChartPageHistogram.class);
                            intentdetil8.putExtra(Constant.imageTitle, Constant.i);
                            intentdetil8.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil8);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public void inisialisasiTampilan() {
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerPhoto = new RecyclerPhoto(MainActivity.this);
        mRecyclerPhoto.setOnArtikelClickListener(mOnArtikelClickListener);
        mRecyclerView.setAdapter(mRecyclerPhoto);
    }

}
