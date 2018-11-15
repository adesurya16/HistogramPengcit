package com.alhudaghifari.bildghifar.tugasUTS;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

// 3 x 3
public class OperatorFilter{

    private static final String TAG = OperatorFilter.class.getSimpleName();
    private int width;
    private int height;

    private int pixImageRed[][];
    private int pixImageGreen[][];
    private int pixImageBlue[][];

    public int pixImageGS[][];
    private int resultOperation[][];
    private final int iterationDirections[][] =
                    {{0, -1}, {1, -1}, {1, 0},
                     {1, 1},  {0, 1}, {-1, 1},
                     {-1, 0}, {-1, -1}};

    public OperatorFilter(int[][] pixRed, int[][] pixGreen, int[][] pixBlue, int height, int width){
        this.width = width;
        this.height = height;
        this.pixImageRed = new int[height][];
        this.pixImageGreen = new int[height][];
        this.pixImageBlue = new int[height][];
        this.pixImageGS = new int[height][];
        for(int i=0;i<height;i++){
            this.pixImageRed[i] = new int[width];
            this.pixImageGreen[i] = new int[width];
            this.pixImageBlue[i] = new int[width];
            this.pixImageGS[i] = new int[width];

        }
        setColorPix(pixRed, pixGreen, pixBlue);
    }

    public OperatorFilter(int[][] img, int height, int width) {
        this.width = width;
        this.height = height;
        this.pixImageGS = new int[height][];
        for(int i=0;i<height;i++){
            this.pixImageGS[i] = new int[width];
        }
        setImage(img);
    }

    private void setImage(int[][] img) {
        for(int i=0;i<height;i++) {
            for (int j=0;j<width;j++) {
                pixImageGS[i][j] = img[i][j];
            }
        }
    }

    public int[][] getPixImageGS(){
        return this.pixImageGS;
    }

    public int[][] getPixImageRed(){ return this.pixImageRed; }

    public int[][] getPixImageGreen(){ return this.pixImageGreen;}

    public int[][] getPixImageBlue(){ return this.pixImageBlue; }

    public void initMatrixGS(){
        for(int i = 0;i < this.height;i++){
            for(int j = 0;j < this.width;j++){
//                Color c = this.pixImage[i][j];
                this.pixImageGS[i][j] =(int) (this.pixImageRed[i][j] + this.pixImageGreen[i][j] + this.pixImageBlue[i][j]) / 3;
            }
        }
    }

    public void setColorPixRed(int[][] pixRed){
        for(int i=0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                this.pixImageRed[i][j] = pixRed[i][j];
            }
        }
    }

    public void setColorPixGreen(int[][] pixGreen){
        for(int i=0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                this.pixImageRed[i][j] = pixGreen[i][j];
            }
        }
    }

    public void setColorPixBlue(int[][] pixBlue){
        for(int i=0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                this.pixImageRed[i][j] = pixBlue[i][j];
            }
        }
    }

    public void setColorPix(int[][] pixRed, int[][] pixGreen, int[][] pixBlue){
        // harus sama dengan height dan width yang diset awal
        for(int i=0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                this.pixImageRed[i][j] = pixRed[i][j];
                this.pixImageGreen[i][j] = pixGreen[i][j];
                this.pixImageBlue[i][j] = pixBlue[i][j];
            }
        }
    }

    public void setColorPixGS(int[][] pix){
        for(int i=0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                this.pixImageGS[i][j] = pix[i][j];
            }
        }
    }

    public void runRobertOperation() {

        int[][] edgeColors = new int[width][height];
        int maxGradient = -1;

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int val00 = pixImageGS[j-1][i-1];
                int val01 = pixImageGS[j][i-1];

                int val10 = pixImageGS[j-1][i];
                int val11 = pixImageGS[j][i];

                int gx =  ((1 * val00) + (0 * val01))
                        + ((0 * val10) + (-1 * val11));

                int gy =  ((0 * val00) + (1 * val01))
                        + ((-1 * val10) + (0 * val11));

                double gval = Math.sqrt((gx * gx) + (gy * gy));
                int g = (int) gval;

                if(maxGradient < g) {
                    maxGradient = g;
                }

                edgeColors[i][j] = g;
            }
        }

        double scale = 255.0 / maxGradient;

        pixImageGS = new int[height][width];

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                pixImageGS[j][i] = edgeColor;
            }
        }
    }

    public void runCustomOperation(double vval00, double vval01, double vval02,
                                   double vval10, double vval11, double vval12,
                                   double vval20, double vval21, double vval22) {

        Log.d(TAG, "val00 : " + vval00 + " val01 : " + vval01 + " val02 : " + vval02 + "\n" +
                "val10 : " + vval10 + " val11 : " + vval11 + " val12 : " + vval12 + "\n" +
                "val20 : " + vval20 + " val21 : " + vval21 + " val22 : " + vval22 + "\n");
        int[][] edgeColors = new int[width][height];
        int maxGradient = -1;

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int val00 = pixImageGS[j-1][i-1];
                int val01 = pixImageGS[j][i-1];
                int val02 =  pixImageGS[j+1][i-1];

                int val10 = pixImageGS[j-1][i];
                int val11 = pixImageGS[j][i];
                int val12 =  pixImageGS[j+1][i];

                int val20 = pixImageGS[j-1][i+1];
                int val21 = pixImageGS[j][i+1];
                int val22 = pixImageGS[j+1][i+1];

                int gx = (int) (((vval00 * val00) + (vval01 * val01) + (vval02 * val02))
                        + ((vval10 * val10) + (vval11 * val11) + (vval12 * val12))
                        + ((vval20 * val20) + (vval21 * val21) + (vval22 * val22)));

                int gy = (int) (((vval00 * val00) + (vval10 * val01) + (vval20 * val02))
                        + ((vval01 * val10) + (vval11 * val11) + (vval21 * val12))
                        + ((vval02 * val20) + (vval12 * val21) + (vval22 * val22)));

                double gval = Math.sqrt((gx * gx) + (gy * gy));
                int g = (int) gval;

                if(maxGradient < g) {
                    maxGradient = g;
                }

                edgeColors[i][j] = g;
            }
        }

        double scale = 255.0 / maxGradient;

        pixImageGS = new int[height][width];

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                pixImageGS[j][i] = edgeColor;
            }
        }
    }

    public void runPrewitOperation() {

        int[][] edgeColors = new int[width][height];
        int maxGradient = -1;

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int val00 = pixImageGS[j-1][i-1];
                int val01 = pixImageGS[j][i-1];
                int val02 =  pixImageGS[j+1][i-1];

                int val10 = pixImageGS[j-1][i];
                int val11 = pixImageGS[j][i];
                int val12 =  pixImageGS[j+1][i];

                int val20 = pixImageGS[j-1][i+1];
                int val21 = pixImageGS[j][i+1];
                int val22 = pixImageGS[j+1][i+1];

                int gx =  ((-1 * val00) + (0 * val01) + (1 * val02))
                        + ((-1 * val10) + (0 * val11) + (1 * val12))
                        + ((-1 * val20) + (0 * val21) + (1 * val22));

                int gy =  ((-1 * val00) + (-1 * val01) + (-1 * val02))
                        + ((0 * val10) + (0 * val11) + (0 * val12))
                        + ((1 * val20) + (1 * val21) + (1 * val22));


                double gval = Math.sqrt((gx * gx) + (gy * gy));
                int g = (int) gval;

                if(maxGradient < g) {
                    maxGradient = g;
                }

                edgeColors[i][j] = g;
            }
        }

        double scale = 255.0 / maxGradient;

        pixImageGS = new int[height][width];

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                pixImageGS[j][i] = edgeColor;
            }
        }
    }

    public void runFreiChenOperation() {
        double squareRootTwo = 1.4142;
        int[][] edgeColors = new int[width][height];
        int maxGradient = -1;

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int val00 = pixImageGS[j-1][i-1];
                int val01 = pixImageGS[j][i-1];
                int val02 =  pixImageGS[j+1][i-1];

                int val10 = pixImageGS[j-1][i];
                int val11 = pixImageGS[j][i];
                int val12 =  pixImageGS[j+1][i];

                int val20 = pixImageGS[j-1][i+1];
                int val21 = pixImageGS[j][i+1];
                int val22 = pixImageGS[j+1][i+1];

                int gx = (int) (((-1 * val00) + (0 * val01) + (1 * val02))
                        + ((-1 * squareRootTwo * val10) + (0 * val11) + (squareRootTwo * val12))
                        + ((-1 * val20) + (0 * val21) + (1 * val22)));

                int gy = (int) (((-1 * val00) + (-1 * squareRootTwo * val01) + (-1 * val02))
                        + ((0 * val10) + (0 * val11) + (0 * val12))
                        + ((1 * val20) + (squareRootTwo * val21) + (1 * val22)));


                double gval = Math.sqrt((gx * gx) + (gy * gy));
                int g = (int) gval;

                if(maxGradient < g) {
                    maxGradient = g;
                }

                edgeColors[i][j] = g;
            }
        }

        double scale = 255.0 / maxGradient;

        pixImageGS = new int[height][width];

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                pixImageGS[j][i] = edgeColor;
            }
        }
    }

    public void runSobelOperation() {

        int maxGval = 0;
        int[][] edgeColors = new int[width][height];
        int maxGradient = -1;

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int val00 = pixImageGS[j-1][i-1];
                int val01 = pixImageGS[j][i-1];
                int val02 =  pixImageGS[j+1][i-1];

                int val10 = pixImageGS[j-1][i];
                int val11 = pixImageGS[j][i];
                int val12 =  pixImageGS[j+1][i];

                int val20 = pixImageGS[j-1][i+1];
                int val21 = pixImageGS[j][i+1];
                int val22 = pixImageGS[j+1][i+1];

                int gx =  ((-1 * val00) + (0 * val01) + (1 * val02))
                        + ((-2 * val10) + (0 * val11) + (2 * val12))
                        + ((-1 * val20) + (0 * val21) + (1 * val22));

                int gy =  ((-1 * val00) + (-2 * val01) + (-1 * val02))
                        + ((0 * val10) + (0 * val11) + (0 * val12))
                        + ((1 * val20) + (2 * val21) + (1 * val22));


                double gval = Math.sqrt((gx * gx) + (gy * gy));
                int g = (int) gval;

                if(maxGradient < g) {
                    maxGradient = g;
                }

                edgeColors[i][j] = g;
            }
        }

        double scale = 255.0 / maxGradient;

        pixImageGS = new int[height][width];

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                pixImageGS[j][i] = edgeColor;
            }
        }
    }

    public void medianOperation(){
        int[][] resRed = new int[this.height][];
        int[][] resGreen = new int[this.height][];
        int[][] resBlue = new int[this.height][];

        for(int i=0;i<this.height;i++){
            resRed[i] = new int[this.width];
            resGreen[i] = new int[this.width];
            resBlue[i] = new int[this.width];

            for(int j=0;j<this.width;j++){
//                Color c = this.pixImage[i][j];
                int ii = i;
                int jj = j;
                ArrayList<Integer> intListRed = new ArrayList<>();
                intListRed.add(this.pixImageRed[i][j]);
                ArrayList<Integer> intListGreen = new ArrayList<>();
                intListGreen.add(this.pixImageGreen[i][j]);
                ArrayList<Integer> intListBlue = new ArrayList<>();
                intListBlue.add(this.pixImageBlue[i][j]);
                for(int k = 0;k < this.iterationDirections.length;k++){    
                    if(ii + this.iterationDirections[k][1] >= 0 && ii + this.iterationDirections[k][1] < this.height){
                        ii += this.iterationDirections[k][1];
                    }

                    if(jj + this.iterationDirections[k][0] >= 0 && jj + this.iterationDirections[k][0] < this.width){
                        jj += this.iterationDirections[k][0];
                    }

//                    Color cNeighboor = this.pixImage[ii][jj];
                    intListRed.add(this.pixImageRed[ii][jj]);
                    intListGreen.add(this.pixImageGreen[ii][jj]);
                    intListBlue.add(this.pixImageBlue[ii][jj]);
                }

                int medRed = getMedian(intListRed);
                int medGreen = getMedian(intListGreen);
                int medBlue = getMedian(intListBlue);
//                Color newColor = new Color(medRed, medGreen, medBlue);
                resRed[i][j] = medRed;
                resGreen[i][j] = medGreen;
                resBlue[i][j] = medBlue;
            }
        }
        setColorPix(resRed, resGreen, resBlue);
//        return res;
    }

    // JANGAN LUPA INIT DULU
    public void medianOperationGS(){
        int [][] res = new int[this.height][];
        for(int i=0;i<this.height;i++){
            res[i] = new int[this.width];
            for(int j=0;j<this.width;j++){
                int ii = i;
                int jj = j;
                ArrayList<Integer> intList = new ArrayList<>();
                intList.add(this.pixImageGS[i][j]);
                for(int k = 0;k < this.iterationDirections.length;k++){    
                    if(ii + this.iterationDirections[k][1] >= 0 && ii + this.iterationDirections[k][1] < this.height){
                        ii += this.iterationDirections[k][1];
                    }

                    if(jj + this.iterationDirections[k][0] >= 0 && jj + this.iterationDirections[k][0] < this.width){
                        jj += this.iterationDirections[k][0];
                    }

                    intList.add(this.pixImageGS[ii][jj]);
                }
                int med = getMedian(intList);
                res[i][j] = med;
            }
        }
//        return res;
        setColorPixGS(res);
    }

    /*
    P9 P2 P3
    P8 P1 P4
    P7 P6 P5
    Dir
    */

    public void gradientOperation(){
        int maxLength = 3;
//        Color[][] res = new Color[this.height][];
        int[][] resRed = new int[this.height][];
        int[][] resGreen = new int[this.height][];
        int[][] resBlue = new int[this.height][];

        int[][] kernelRed = new int[maxLength][];
        int[][] kernelGreen = new int[maxLength][];
        int[][] kernelBlue = new int[maxLength][];
        for(int i = 0;i<maxLength;i++){
            kernelRed[i] = new int[maxLength];
            kernelGreen[i] = new int[maxLength];
            kernelBlue[i] = new int[maxLength];
        }

        for(int i=0;i<this.height;i++){
//            res[i] = new Color[this.width];
            resRed[i] = new int[this.width];
            resGreen[i] = new int[this.width];
            resBlue[i] = new int[this.width];
            for(int j=0;j<this.width;j++){
                int ii = i;
                int jj = j;
                int mid = maxLength / 2;
                kernelRed[mid][mid] = this.pixImageRed[i][j];
                kernelGreen[mid][mid] = this.pixImageGreen[i][j];
                kernelBlue[mid][mid] = this.pixImageBlue[i][j];

                for(int k = 0;k < this.iterationDirections.length;k++){
                    if(ii + this.iterationDirections[k][1] >= 0 && ii + this.iterationDirections[k][1] < this.height){
                        ii += this.iterationDirections[k][1];
                    }

                    if(jj + this.iterationDirections[k][0] >= 0 && jj + this.iterationDirections[k][0] < this.width){
                        jj += this.iterationDirections[k][0];
                    }
//                    Color cNeighboor = this.pixImage[ii][jj];
                    kernelRed[mid + this.iterationDirections[k][1]][mid + this.iterationDirections[k][0]] = this.pixImageRed[ii][jj];
                    kernelGreen[mid + this.iterationDirections[k][1]][mid + this.iterationDirections[k][0]] = this.pixImageGreen[ii][jj];
                    kernelBlue[mid + this.iterationDirections[k][1]][mid + this.iterationDirections[k][0]] = this.pixImageBlue[ii][jj];
                }

                int gradRed = getGradientMax(kernelRed);
                int gradGreen = getGradientMax(kernelGreen);
                int gradBlue = getGradientMax(kernelBlue);
//                Color newColor = new Color(gradRed, gradGreen, gradBlue);
                resRed[i][j] = gradRed;
                resGreen[i][j] = gradGreen;
                resBlue[i][j] = gradBlue;

            }
        }
        setColorPix(resRed, resGreen, resBlue);
    }

    public void gradientOperationGS(){
        int maxLength = 3;
        int[][] res = new int[this.height][];
        int[][] kernel = new int[maxLength][];
        for(int i = 0;i<maxLength;i++){
            kernel[i] = new int[maxLength];
        }
        for(int i = 0;i< this.height;i++){
            res[i] = new int[this.width];
            for(int j=0;j<this.width;j++){
                int ii = i;
                int jj = j;
                int mid = maxLength / 2;
                kernel[i][j] = this.pixImageGS[i][j];
                for(int k = 0;k < this.iterationDirections.length;k++){
                    if(ii + this.iterationDirections[k][1] >= 0 && ii + this.iterationDirections[k][1] < this.height){
                        ii += this.iterationDirections[k][1];
                    }

                    if(jj + this.iterationDirections[k][0] >= 0 && jj + this.iterationDirections[k][0] < this.width){
                        jj += this.iterationDirections[k][0];
                    }
                    kernel[mid + this.iterationDirections[k][1]][mid + this.iterationDirections[k][0]] = this.pixImageGS[ii][jj];
                }
                int grad = getGradientMax(kernel);
                res[i][j] = grad;
            }
        }
        setColorPixGS(res);
    }

    public void differenceOperation(){
        int maxLength = 3;
//        Color[][] res = new Color[this.height][];
        int[][] resRed = new int[this.height][];
        int[][] resGreen = new int[this.height][];
        int[][] resBlue = new int[this.height][];

        int[][] kernelRed = new int[maxLength][];
        int[][] kernelGreen = new int[maxLength][];
        int[][] kernelBlue = new int[maxLength][];
        for(int i = 0;i<maxLength;i++){
            kernelRed[i] = new int[maxLength];
            kernelGreen[i] = new int[maxLength];
            kernelBlue[i] = new int[maxLength];
        }

        for(int i=0;i<this.height;i++){
//            res[i] = new Color[this.width];
            resRed[i] = new int[this.width];
            resGreen[i] = new int[this.width];
            resBlue[i] = new int[this.width];
            for(int j=0;j<this.width;j++){
                int ii = i;
                int jj = j;
                int mid = maxLength / 2;
                kernelRed[mid][mid] = this.pixImageRed[i][j];
                kernelGreen[mid][mid] = this.pixImageGreen[i][j];
                kernelBlue[mid][mid] = this.pixImageBlue[i][j];

                for(int k = 0;k < this.iterationDirections.length;k++){
                    if(ii + this.iterationDirections[k][1] >= 0 && ii + this.iterationDirections[k][1] < this.height){
                        ii += this.iterationDirections[k][1];
                    }

                    if(jj + this.iterationDirections[k][0] >= 0 && jj + this.iterationDirections[k][0] < this.width){
                        jj += this.iterationDirections[k][0];
                    }
//                    Color cNeighboor = this.pixImage[ii][jj];
                    kernelRed[mid + this.iterationDirections[k][1]][mid + this.iterationDirections[k][0]] = this.pixImageRed[ii][jj];
                    kernelGreen[mid + this.iterationDirections[k][1]][mid + this.iterationDirections[k][0]] = this.pixImageGreen[ii][jj];
                    kernelBlue[mid + this.iterationDirections[k][1]][mid + this.iterationDirections[k][0]] = this.pixImageBlue[ii][jj];
                }

                int diffRed = getDifferenceMax(kernelRed);
                int diffGreen = getDifferenceMax(kernelGreen);
                int diffBlue = getDifferenceMax(kernelBlue);
//                Color newColor = new Color(gradRed, gradGreen, gradBlue);
                resRed[i][j] = diffRed;
                resGreen[i][j] = diffGreen;
                resBlue[i][j] = diffBlue;

            }
        }
        setColorPix(resRed, resGreen, resBlue);
    }


    public void differenceOperationGS(){
        int maxLength = 3;
        int[][] res = new int[this.height][];
        int[][] kernel = new int[maxLength][];
        for(int i = 0;i<maxLength;i++){
            kernel[i] = new int[maxLength];
        }
        for(int i = 0;i< this.height;i++){
            res[i] = new int[this.width];
            for(int j=0;j<this.width;j++){
                int ii = i;
                int jj = j;
                int mid = maxLength / 2;
                kernel[i][j] = this.pixImageGS[i][j];
                for(int k = 0;k < this.iterationDirections.length;k++){
                    if(ii + this.iterationDirections[k][1] >= 0 && ii + this.iterationDirections[k][1] < this.height){
                        ii += this.iterationDirections[k][1];
                    }

                    if(jj + this.iterationDirections[k][0] >= 0 && jj + this.iterationDirections[k][0] < this.width){
                        jj += this.iterationDirections[k][0];
                    }
                    kernel[mid + this.iterationDirections[k][1]][mid + this.iterationDirections[k][0]] = this.pixImageGS[ii][jj];
                }
                int diff = getDifferenceMax(kernel);
                res[i][j] = diff;
            }
        }
        setColorPixGS(res);
//        return res;
    }

    public void meanOperation(){
        int[][] resRed = new int[this.height][];
        int[][] resGreen = new int[this.height][];
        int[][] resBlue = new int[this.height][];

        for(int i=0;i<this.height;i++){
            resRed[i] = new int[this.width];
            resGreen[i] = new int[this.width];
            resBlue[i] = new int[this.width];

            for(int j=0;j<this.width;j++){
//                Color c = this.pixImage[i][j];
                int ii = i;
                int jj = j;
                ArrayList<Integer> intListRed = new ArrayList<>();
                intListRed.add(this.pixImageRed[i][j]);
                ArrayList<Integer> intListGreen = new ArrayList<>();
                intListGreen.add(this.pixImageGreen[i][j]);
                ArrayList<Integer> intListBlue = new ArrayList<>();
                intListBlue.add(this.pixImageBlue[i][j]);
                for(int k = 0;k < this.iterationDirections.length;k++){
                    if(ii + this.iterationDirections[k][1] >= 0 && ii + this.iterationDirections[k][1] < this.height){
                        ii += this.iterationDirections[k][1];
                    }

                    if(jj + this.iterationDirections[k][0] >= 0 && jj + this.iterationDirections[k][0] < this.width){
                        jj += this.iterationDirections[k][0];
                    }

//                    Color cNeighboor = this.pixImage[ii][jj];
                    intListRed.add(this.pixImageRed[ii][jj]);
                    intListGreen.add(this.pixImageGreen[ii][jj]);
                    intListBlue.add(this.pixImageBlue[ii][jj]);
                }

                int meanRed = getMean(intListRed);
                int meanGreen = getMean(intListGreen);
                int meanBlue = getMean(intListBlue);
//                Color newColor = new Color(medRed, medGreen, medBlue);
                resRed[i][j] = meanRed;
                resGreen[i][j] = meanGreen;
                resBlue[i][j] = meanBlue;
            }
        }
        setColorPix(resRed, resGreen, resBlue);
    }

    public void meanOperationGS(){
        int [][] res = new int[this.height][];
        for(int i=0;i<this.height;i++){
            res[i] = new int[this.width];
            for(int j=0;j<this.width;j++){
                int ii = i;
                int jj = j;
                ArrayList<Integer> intList = new ArrayList<>();
                intList.add(this.pixImageGS[i][j]);
                for(int k = 0;k < this.iterationDirections.length;k++){    
                    if(ii + this.iterationDirections[k][1] >= 0 && ii + this.iterationDirections[k][1] < this.height){
                        ii += this.iterationDirections[k][1];
                    }

                    if(jj + this.iterationDirections[k][0] >= 0 && jj + this.iterationDirections[k][0] < this.width){
                        jj += this.iterationDirections[k][0];
                    }

                    intList.add(this.pixImageGS[ii][jj]);
                }
                int mean = getMean(intList);
                res[i][j] = mean;
            }
        }
        setColorPixGS(res);
    }

    public int getMedian(ArrayList<Integer> intList){
        Collections.sort(intList);
        // 3 x 3
        int size = intList.size();
        if(size % 2 == 1){
            return intList.get(size/2 + 1);
        }else{
            return (intList.get(size/2) + intList.get(size/2 + 1)) / 2;
        }
    }

    public int getMean(ArrayList<Integer> intList){
        int sum = 0;
        for(int pInt : intList){
            sum += pInt;
        }
        return sum / intList.size();
    }

    public int getGradientMax(int[][] pix){
        // int maxLength = 3;
        ArrayList<Integer> gList = new ArrayList<>();
        int max = Math.abs(pix[0][0] - pix[2][2]);

        gList.add( Math.abs(pix[0][1] - pix[2][1]) ); 
        gList.add( Math.abs(pix[0][2] - pix[2][0]) );
        gList.add( Math.abs(pix[1][0] - pix[1][2]) );
        for(int px : gList){
            if (px > max){
                max = px;
            }
        } 
        return max;
    }

    public int getDifferenceMax(int[][] pix){
        ArrayList<Integer> dList = new ArrayList<>();
        int max = Math.abs(pix[0][0] - pix[1][1]);
        dList.add(Math.abs(pix[0][1] - pix[1][1]));
        dList.add(Math.abs(pix[0][2] - pix[1][1]));
        dList.add(Math.abs(pix[1][0] - pix[1][1]));
        dList.add(Math.abs(pix[1][2] - pix[1][1]));
        dList.add(Math.abs(pix[2][0] - pix[1][1]));
        dList.add(Math.abs(pix[2][1] - pix[1][1]));
        dList.add(Math.abs(pix[2][2] - pix[1][1]));

        for(int px : dList){
            if (px > max){
                max = px;
            }
        } 
        return max;
    }
}