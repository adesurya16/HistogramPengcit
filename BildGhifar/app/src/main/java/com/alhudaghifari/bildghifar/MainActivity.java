package com.alhudaghifari.bildghifar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;

    private Button buttonRgbHistogram;
    private Button buttonGrayscaleHistogram;

    private Toast toast;

    private RecyclerPhoto mRecyclerPhoto;

    private RecyclerPhoto.OnArtikelClickListener mOnArtikelClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        buttonRgbHistogram = (Button) findViewById(R.id.btnRgbHistogram);
        buttonGrayscaleHistogram = (Button) findViewById(R.id.btnGrayscaleHistogram);

        inisialisasiListener();
        inisialisasiTampilan();
    }

    private void inisialisasiListener() {
        buttonRgbHistogram.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intentdetil = new Intent(MainActivity.this, AllHistogram.class);
                intentdetil.putExtra(Constant.jenisGambar, Constant.rgb);

                MainActivity.this.startActivity(intentdetil);
            }
        });

        buttonGrayscaleHistogram.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intentdetil = new Intent(MainActivity.this, AllHistogram.class);
                intentdetil.putExtra(Constant.jenisGambar, Constant.grayscale);

                MainActivity.this.startActivity(intentdetil);
            }
        });

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
                            Intent intentdetil = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil.putExtra(Constant.imageTitle, Constant.a);
                            intentdetil.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil);
                            break;

                        case 1:
                            Intent intentdetil1 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil1.putExtra(Constant.imageTitle, Constant.b);
                            intentdetil1.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil1);
                            break;

                        case 2:
                            Intent intentdetil2 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil2.putExtra(Constant.imageTitle, Constant.c);
                            intentdetil2.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil2);
                            break;

                        case 3:
                            Intent intentdetil3 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil3.putExtra(Constant.imageTitle, Constant.d);
                            intentdetil3.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil3);
                            break;

                        case 4:
                            Intent intentdetil4 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil4.putExtra(Constant.imageTitle, Constant.e);
                            intentdetil4.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil4);
                            break;

                        case 5:
                            Intent intentdetil5 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil5.putExtra(Constant.imageTitle, Constant.f);
                            intentdetil5.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil5);
                            break;

                        case 6:
                            Intent intentdetil6 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil6.putExtra(Constant.imageTitle, Constant.g);
                            intentdetil6.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil6);
                            break;

                        case 7:
                            Intent intentdetil7 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil7.putExtra(Constant.imageTitle, Constant.h);
                            intentdetil7.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil7);
                            break;
                        case 8:
                            Intent intentdetil8 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil8.putExtra(Constant.imageTitle, Constant.i);
                            intentdetil8.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil8);
                            break;
                        case 9:
                            Intent intentdetil9 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil9.putExtra(Constant.imageTitle, Constant.j);
                            intentdetil9.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil9);
                            break;
                        case 10:
                            Intent intentdetil10 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil10.putExtra(Constant.imageTitle, Constant.k);
                            intentdetil10.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil10);
                            break;
                        case 11:
                            Intent intentdetil11 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil11.putExtra(Constant.imageTitle, Constant.l);
                            intentdetil11.putExtra(Constant.jenisGambar, Constant.rgb);

                            MainActivity.this.startActivity(intentdetil11);
                            break;

                        case 12:
                            Intent intentdetil12 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil12.putExtra(Constant.imageTitle, Constant.a);
                            intentdetil12.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil12);
                            break;

                        case 13:
                            Intent intentdetil13 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil13.putExtra(Constant.imageTitle, Constant.b);
                            intentdetil13.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil13);
                            break;

                        case 14:
                            Intent intentdetil14 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil14.putExtra(Constant.imageTitle, Constant.c);
                            intentdetil14.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil14);
                            break;

                        case 15:
                            Intent intentdetil15 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil15.putExtra(Constant.imageTitle, Constant.d);
                            intentdetil15.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil15);
                            break;

                        case 16:
                            Intent intentdetil16 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil16.putExtra(Constant.imageTitle, Constant.e);
                            intentdetil16.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil16);
                            break;

                        case 17:
                            Intent intentdetil17 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil17.putExtra(Constant.imageTitle, Constant.f);
                            intentdetil17.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil17);
                            break;

                        case 18:
                            Intent intentdetil18 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil18.putExtra(Constant.imageTitle, Constant.g);
                            intentdetil18.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil18);
                            break;

                        case 19:
                            Intent intentdetil19 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil19.putExtra(Constant.imageTitle, Constant.h);
                            intentdetil19.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil19);
                            break;
                        case 20:
                            Intent intentdetil20 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil20.putExtra(Constant.imageTitle, Constant.i);
                            intentdetil20.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil20);
                            break;
                        case 21:
                            Intent intentdetil21 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil21.putExtra(Constant.imageTitle, Constant.j);
                            intentdetil21.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil21);
                            break;
                        case 22:
                            Intent intentdetil22 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil22.putExtra(Constant.imageTitle, Constant.k);
                            intentdetil22.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil22);
                            break;
                        case 23:
                            Intent intentdetil23 = new Intent(MainActivity.this, PageHistogram.class);
                            intentdetil23.putExtra(Constant.imageTitle, Constant.l);
                            intentdetil23.putExtra(Constant.jenisGambar, Constant.grayscale);

                            MainActivity.this.startActivity(intentdetil23);
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
