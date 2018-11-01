package com.alhudaghifari.bildghifar.tugasUTS;

import android.content.Context;
import android.util.Log;

import com.alhudaghifari.bildghifar.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alhudaghifari on 22:48 30/10/18
 */
public class PredictCharUsingChainCode {

    private static final String TAG = PredictCharUsingChainCode.class.getSimpleName();

    private String chainCode;
    private String chainCodeNormalized;
    private Context context;
    private int modus[];
    private int modusCharCode;
    private List<MinDistModel> nearestNeighbour;
    private List<MinDistModel> distanceComparation;
    private final int MAX_CHARACTER = 69;

    public PredictCharUsingChainCode(Context context) {
        this.context = context;
    }

    public String getChainCode() {
        return chainCode;
    }

    public String getChainCodeNormalized() {
        return chainCodeNormalized;
    }

    private void chainCodeConverter(ArrayList<Integer> chainCodeArray) {
        chainCode = "";
        for (Integer s : chainCodeArray)
        {
            chainCode += s;
        }
    }

    private void normalizeChainCode() {
        char c = chainCode.charAt(0);
        int counter = 1;
        chainCodeNormalized = c + "";

        if (chainCode.length() > 1) {
            for (int i=1;i<chainCode.length();i++) {
                if (chainCode.charAt(i) == c) {
                    if (counter < 1) {
                        chainCodeNormalized += chainCode.charAt(i);
                        counter++;
                    }
                } else {
                    c = chainCode.charAt(i);
                    chainCodeNormalized += chainCode.charAt(i);
                    counter = 1;
                }
            }


//            String chainCodeInp = chainCodeNormalized;
//            String tempLastChar = "";
//            counter = 0;
//            int counterNext = 0;
//            String temp = "";
//            String tempNext = "";
//            chainCodeNormalized = "";
//            for (int i=0;i<chainCodeInp.length();i++) {
//                if (counter < 2) {
//                    temp += chainCodeInp.charAt(i);
//                    counter++;
//                    counterNext = 0;
//                } else {
//                    if (counter == 2) {
//                        chainCodeNormalized += temp;
//                        counter++;
//                    }
//                    if (counterNext < 2) {
//                        tempNext += chainCodeInp.charAt(i);
//                        counterNext++;
//                    } else {
//                        if (temp.equals(tempNext)) {
//                            tempNext = chainCodeInp.charAt(i) + "";
//                            counterNext = 1;
//                        } else {
//                            chainCodeNormalized += tempNext;
//                            temp = tempNext;
//                            tempNext = chainCodeInp.charAt(i) + "";
//                            counterNext = 1;
//                        }
//                    }
//                }
//                tempLastChar = chainCodeInp.charAt(i) + "";
//            }
//            chainCodeNormalized += tempLastChar;
        }
    }

    /**
     * method ini digunakan untuk memprediksi karakter apakah pada sebuah gambar
     * inputPath : path gambar yang akan dipredict
     * outputPath : keluaran gambar yg telah dilingkari chaincode
     * threshold : threshold gambar
     * n : n Nearest Neighbour
     */
    public void predictCharKnn(ArrayList<Integer> chainCodeArray, int n) {
        chainCodeConverter(chainCodeArray);
        normalizeChainCode();
//        chainCodeNormalized = chainCode;
        Log.d(TAG, "chaincode normalized : " + chainCodeNormalized);
        MinDistModel minDistModel;
        distanceComparation = new ArrayList<>();
        int minDistance = 1;
        int tempDistance;
        int counter = 0;

        InputStream is = context.getResources().openRawResource(R.raw.chaincodenormalizedvsix);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";

        // Handling exceptions
        try {
            // If buffer is not empty
            while ((line = br.readLine()) != null) {
                // use comma as separator columns of CSV
                String[] data = line.split(",");
                minDistModel = new MinDistModel();
                if (counter == 0) {
                    minDistance = editDistDP(data[1], chainCodeNormalized, data[1].length(), chainCodeNormalized.length());
//                    minDistance = editDistDP(chainCodeNormalized, data[1], chainCodeNormalized.length(), data[1].length());
                    minDistModel.setCharCode(Integer.parseInt(data[0]));
                    minDistModel.setDistance(minDistance);
                    counter++;
                } else {
                    tempDistance = editDistDP(data[1], chainCodeNormalized, data[1].length(), chainCodeNormalized.length());
                    if (tempDistance < minDistance) {
                        minDistance = tempDistance;
                    }
                    minDistModel.setCharCode(Integer.parseInt(data[0]));
                    minDistModel.setDistance(tempDistance);
                }
                Log.d(TAG,"dist :" + minDistModel.getDistance() + " char : " + getCharFromCode(Integer.parseInt(data[0])) + " chaincode :" +data[1]);
                distanceComparation.add(minDistModel);
            }
            setNNearestNeighbour(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNearestNeighbour() {
        String nearestNeighbourListString = "";
        for (int i=0;i<nearestNeighbour.size();i++) {
            nearestNeighbourListString += getCharFromCode(nearestNeighbour.get(i).getCharCode()) + " ";
        }
        return nearestNeighbourListString;
    }

    public String getCharPredicted() {
        return getCharFromCode(modusCharCode);
    }

    private void setNNearestNeighbour(int n) {
        if (n < distanceComparation.size() / 2) {
            MinDistModel minDistModel;
            nearestNeighbour = new ArrayList<MinDistModel>();
            modus = new int[MAX_CHARACTER];
            double minValue;
            int indexMin;
            for (int i=0;i<n;i++) {
                minValue = distanceComparation.get(0).getDistance();
                indexMin = 0;
                minDistModel = new MinDistModel();
                for (int j=0;j<distanceComparation.size();j++) {
                    if (minValue > distanceComparation.get(j).getDistance()) {
                        minValue = distanceComparation.get(j).getDistance();
                        indexMin = j;
                    }
                }
                minDistModel.setCharCode(distanceComparation.get(indexMin).getCharCode());
                minDistModel.setDistance(distanceComparation.get(indexMin).getDistance());
                modus[distanceComparation.get(indexMin).getCharCode()]++;
                distanceComparation.remove(indexMin);
                nearestNeighbour.add(minDistModel);
            }
            int indexLargest = 0;
            modusCharCode = modus[0];
            for (int i=0;i<MAX_CHARACTER;i++) {
                if (modusCharCode < modus[i]) {
                    modusCharCode = modus[i];
                    indexLargest = i;
                }
            }
            modusCharCode = indexLargest;
        }
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


    private String getCharFromCode(int code) {
        switch(code) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "G";
            case 7:
                return "H";
            case 8:
                return "I";
            case 9:
                return "J";
            case 10:
                return "K";
            case 11:
                return "L";
            case 12:
                return "M";
            case 13:
                return "N";
            case 14:
                return "O";
            case 15:
                return "P";
            case 16:
                return "Q";
            case 17:
                return "R";
            case 18:
                return "S";
            case 19:
                return "T";
            case 20:
                return "U";
            case 21:
                return "V";
            case 22:
                return "W";
            case 23:
                return "X";
            case 24:
                return "Y";
            case 25:
                return "Z";
            case 26:
                return "a";
            case 27:
                return "b";
            case 28:
                return "c";
            case 29:
                return "d";
            case 30:
                return "e";
            case 31:
                return "f";
            case 32:
                return "g";
            case 33:
                return "h";
            case 34:
                return "i";
            case 35:
                return "j";
            case 36:
                return "k";
            case 37:
                return "l";
            case 38:
                return "m";
            case 39:
                return "n";
            case 40:
                return "o";
            case 41:
                return "p";
            case 42:
                return "q";
            case 43:
                return "r";
            case 44:
                return "s";
            case 45:
                return "t";
            case 46:
                return "u";
            case 47:
                return "v";
            case 48:
                return "w";
            case 49:
                return "x";
            case 50:
                return "y";
            case 51:
                return "z";
            case 52:
                return "1";
            case 53:
                return "2";
            case 54:
                return "3";
            case 55:
                return "4";
            case 56:
                return "5";
            case 57:
                return "6";
            case 58:
                return "7";
            case 59:
                return "8";
            case 60:
                return "9";
            case 61:
                return "0";
            case 62:
                return "#";
            case 63:
                return "=";
            case 64:
                return "!";
            case 65:
                return "?";
            case 66:
                return "(";
            case 67:
                return ")";
            case 68:
                return "";
            default:
                return "-";
        }
    }
}
