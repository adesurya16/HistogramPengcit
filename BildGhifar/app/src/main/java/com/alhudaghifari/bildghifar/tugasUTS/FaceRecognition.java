package com.alhudaghifari.bildghifar.tugasUTS;

import android.util.Log;

import com.alhudaghifari.bildghifar.utils.DataStructure;
import com.alhudaghifari.bildghifar.utils.DbFaceDistance;

import java.util.ArrayList;

/**
 * Created by Alhudaghifari on 11:54 17/12/18
 */
public class FaceRecognition {

    private ArrayList<DataStructure> listDistance;

    public FaceRecognition() {
        listDistance = new ArrayList<>();
    }

    public String recognizeFace(double normalizationDistance) {
        double threshold = 0.5;
        double minDistance = Math.abs(normalizationDistance - DbFaceDistance.dbAll[0]);
        double minTemp;
        int indexMin = 0;

        for (int i=0;i<DbFaceDistance.dbAll.length;i++) {
            minTemp = Math.abs(normalizationDistance - DbFaceDistance.dbAll[i]);

            if (minTemp < minDistance) {
                minDistance = minTemp;
                indexMin = i;
            }
        }

        Log.d("FaceRecognition","detectEyeDistance - minDistance : " + minDistance);

        if (minDistance > threshold || Double.isNaN(minDistance)) indexMin = -99;

        return whoIsThisNumber(indexMin);
    }

    private String whoIsThisNumber(int index) {
        if (index >= 0 && index <= 3) return "Fari";
        else if (index >= 4 && index <= 5) return "Ade";
        else return "I don't know who the h*ll is this";
    }

}
