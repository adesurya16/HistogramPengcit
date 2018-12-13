package com.alhudaghifari.bildghifar.tugasUTS;

import android.graphics.Color;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SkinningField {
    final int redVal = Color.rgb(
            200,
            0,
            0);
    final int greenVal = Color.rgb(
            0,
            200,
            0);
    final int blueVal = Color.rgb(
            0,
            0,
            200);
    final int whiteVal = Color.rgb(
            255,
            255,
            255);
    final int blackVal = Color.rgb(
            0,
            0,
            0);

    private ArrayList<ObjSkin> pListObjSkin;

    private int[][] redPixel;
    private int[][] bluePixel;
    private int[][] greenPixel;
    private int[][] matrixBW;

    private int width;
    private int height;

    private final int iterationDirections[][] = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};

    public SkinningField(int redPx[][], int greenPx[][], int bluePx[][], int matrixBW[][], int height, int width){
        this.pListObjSkin = new ArrayList<>();
        // init
        this.redPixel = new int[height][];
        for(int i=0;i<height;i++){
            this.redPixel[i] = new int[width];
        }

        this.greenPixel = new int[height][];
        for(int i=0;i<height;i++){
            this.greenPixel[i] = new int[width];
        }

        this.bluePixel = new int[height][];
        for(int i=0;i<height;i++){
            this.bluePixel[i] = new int[width];
        }

        this.matrixBW = new int[height][];
        for(int i=0;i<height;i++){
            this.matrixBW[i] = new int[width];
        }

        // add value
        this.height = height;
        this.width = width;

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                this.redPixel[i][j] = redPx[i][j];
            }
        }

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                this.greenPixel[i][j] = greenPx[i][j];
            }
        }

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                this.bluePixel[i][j] = bluePx[i][j];
            }
        }

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                this.matrixBW[i][j] = matrixBW[i][j];
            }
        }

        setObjectSkin();
    }

    public int[][] getGreenPixel() {
        return greenPixel;
    }

    public int[][] getBluePixel() {
        return bluePixel;
    }

    public int[][] getRedPixel() {
        return redPixel;
    }

    public int[][] getMatrixBW(){
        return matrixBW;
    }

    public void setMatrixBW(int mBW[][]){
        for(int i = 0;i< this.height;i++){
            for(int j = 0;j< this.width;j++){
                this.matrixBW[i][j] = mBW[i][j];
            }
        }
    }

    // utils

    public void copyToMatrix(int matrix[][]){
        for(int i =0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                matrix[i][j] = this.matrixBW[i][j];
            }
        }
    }

    public void copyToMatrixRGB(int matrix[][]){
        for(int i =0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                matrix[i][j] = Color.rgb(
                        this.redPixel[i][j],
                        this.greenPixel[i][j],
                        this.bluePixel[i][j]);
            }
        }
    }

    long lalala = 0;

    public void getDeletedPointSkin(ArrayList<point> pList, int px, int py){
        // pList harus udah clear dipanggilan pertama

            if (this.matrixBW[px][py] == whiteVal){
//                if (lalala < 500) {
                    pList.add(new point(px, py));
//                    Log.d(SkinningField.class.getSimpleName(), "lala : " + lalala + " px : " + px
//                            + " py : " + py);
                    lalala++;
//                }
                this.matrixBW[px][py] = blackVal;
                for(int i=0;i < this.iterationDirections.length - 1 ;i++){
                    int dx = px + iterationDirections[i][1];
                    int dy = py + iterationDirections[i][0];
                    if ((dx >= 0 && dx < this.height) && (dy >= 0 && dx < this.width)){
                        if (lalala <1000)
                        getDeletedPointSkin(pList, dx, dy);
                    }
                }
            }

    }

    public int[][] getMatrixFromListPoint(ArrayList<point> pList){
        int[][] matrixBWTmp;
        // auto assign 0
        matrixBWTmp = new int[height][];
        for(int i=0;i<height;i++){
            matrixBWTmp[i] = new int[width];
        }

        for(int i = 0;i < height;i++){
            for(int j = 0;j < width;j++){
                matrixBWTmp[i][j] = blackVal;
            }
        }

        for(point p: pList){
            matrixBWTmp[p.x][p.y] = whiteVal;
        }

        return matrixBWTmp;
    }
    ArrayList<point> toDelete;
    public void setObjectSkin(){
        this.pListObjSkin.clear();

        int[][] matBWTmp = new int[this.height][];
        for(int i = 0;i<this.height;i++){
            matBWTmp[i] = new int[this.width];
        }

        copyToMatrix(matBWTmp);
        for(int i = 0;i<this.height;i++){
            for(int j = 0;j<this.width;j++){
                int x = i;
                int y = j;
                toDelete = new ArrayList<>();
                if (this.matrixBW[x][y] == whiteVal){
                    getDeletedPointSkin(toDelete, x, y);
                    this.pListObjSkin.add(new ObjSkin(toDelete, this.height, this.width));
                }
            }
        }


        setMatrixBW(matBWTmp);
    }

    public int[][] getMarkedObjectToBW(){
        int[][] matBWTmp = new int[this.height][];
        for(int i = 0;i<this.height;i++){
            matBWTmp[i] = new int[this.width];
        }

        copyToMatrix(matBWTmp);

        for(ObjSkin p: this.pListObjSkin){
            if(p.IsFaceDetected()){

                boundingObject(matBWTmp, redVal, p.Xmax, p.Xmin, p.Ymax, p.Ymin);
                // per component
                ArrayList<Component> pComp = p.getComponentList();
                for(Component c: pComp){
                    boundingObject(matBWTmp, redVal, c.Xmax, c.Xmin, c.Ymax, c.Ymin);
                }
            }else{
                boundingObject(matBWTmp, blueVal, p.Xmax, p.Xmin, p.Ymax, p.Ymin);
            }
        }
        return matBWTmp;
    }

    public void boundingObject(int mat[][], int val, int xmax, int xmin, int ymax, int ymin){
        for(int i = xmin; i <xmin+1;i++){
            for(int j = ymin; j< ymin+1;j++){
                if((i == xmin || i == xmax ) && (j == ymin || j == ymax) ){
                    mat[i][j] = val;
                }
            }
        }
    }

    public int[][] getmarkedObjectToRGBvalue(){
        int[][] matRGBTmp = new int[this.height][];
        for(int i = 0;i < this.height;i++){
            matRGBTmp[i] = new int[this.width];
        }

        copyToMatrixRGB(matRGBTmp);

        for(ObjSkin p: this.pListObjSkin){
            if(p.IsFaceDetected()){

                boundingObject(matRGBTmp, redVal, p.Xmax, p.Xmin, p.Ymax, p.Ymin);
                // per component
                ArrayList<Component> pComp = p.getComponentList();
                for(Component c: pComp){
                    boundingObject(matRGBTmp, redVal, c.Xmax, c.Xmin, c.Ymax, c.Ymin);
                }
            }else{
                boundingObject(matRGBTmp, blueVal, p.Xmax, p.Xmin, p.Ymax, p.Ymin);
            }
        }
        return matRGBTmp;
    }


}
