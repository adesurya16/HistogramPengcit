package com.alhudaghifari.bildghifar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alhudaghifari.bildghifar.tugas1Histogram.LandingPageHistogram;
import com.alhudaghifari.bildghifar.tugas2carigarismuka.LandingPageTugas2;
import com.alhudaghifari.bildghifar.tugas3Equalizer.LandingPageTugas3Equalizer;
import com.alhudaghifari.bildghifar.tugas4ChainCode.LandingPageTugas4;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
    }

    public void gotoTugas1(View view) {
        Intent intent = new Intent(FirstPage.this, LandingPageHistogram.class);
        startActivity(intent);
    }

    public void gotoTugas2(View view) {
        Intent intent = new Intent(FirstPage.this, LandingPageTugas2.class);
        startActivity(intent);
    }

    public void gotoTugas3(View view) {
        Intent intent = new Intent(FirstPage.this, LandingPageTugas3Equalizer.class);
        startActivity(intent);
    }

    public void gotoTugas4(View view) {
        Intent intent = new Intent(FirstPage.this, LandingPageTugas4.class);
        startActivity(intent);
    }

    public void gototugas5(View view){
        Intent intent = new Intent(FirstPage.this, LandingPageTugas5Thinning.class);
        startActivity(intent);
    }
}
