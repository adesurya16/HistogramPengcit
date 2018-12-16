package com.alhudaghifari.bildghifar.tugasUTS;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

public class ObjSkin {
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
    private final int iterationDirections[][] = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};
    private int height;
    private int width;

    private boolean isHole;
    private ArrayList<point> pList;
    private Stack<point> floodFillStack;
    public Component LeftEye;
    public Component RightEye;
    private Component mouth;
    public boolean isEyeFound;

    // bouded point
    public int Xmax;
    public int Ymax;
    public int Xmin;
    public int Ymin;


    private ArrayList<point> pAreaSkinList;
    private int[][] matrixBW;
    private ArrayList<Integer> chainCodeList;
    private ArrayList<Component> componentList;

    public ObjSkin(ArrayList<point> pList2, int height, int width){
        this.LeftEye = null;
        this.RightEye = null;
        isEyeFound = false;
        this.height = height;
        this.width = width;
        floodFillStack = new Stack<>();

        this.pAreaSkinList = new ArrayList<>();
        this.componentList = new ArrayList<>();
        this.matrixBW = new int[height][];
        for(int i = 0; i < height; i++){
            this.matrixBW[i] = new int[width];
        }

        this.pAreaSkinList.addAll(pList2);

        initMatrixFromList(pList2);
        Log.d("blackval","blackval in matrixbw objSkin : "+ this.matrixBW[0][23]);

        getBoundedPoint();
        detectHoleToList();
        findEyes();
        findMouth();
        findNose();
    }

    public ArrayList<Integer> getChainCodeList(){
        return this.chainCodeList;
    }

    public ArrayList<Component> getComponentList() {
        return componentList;
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

    public void initMatrixFromList(ArrayList<point> pListRes){
        Log.d("blackval","blackval value in objskin init : "+ blackVal);
        Log.d("blackval","whiteval value in objskin init : "+ whiteVal);

        for(int i=0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                this.matrixBW[i][j] = blackVal;
            }
        }
        Log.d("list init", "init list skin size : " + this.pAreaSkinList.size());
        for(point p: pListRes){

            this.matrixBW[p.x][p.y] = whiteVal;
        }
    }

    public void getBoundedPoint(){
        int y_start = -1;
        int y_end = this.width;
        int x_start = -1;
        int x_end = this.height;
//        int val;
        for(point p : this.pAreaSkinList){
            if (x_start == -1){
                x_start = p.x;
            }else if(p.x < x_start){
                x_start = p.x;
            }

            if (x_end == this.height){
                x_end = p.x;
            }else if(p.x > x_end){
                x_end = p.x;
            }

            if (y_start == -1){
                y_start = p.y;
            }else if(p.y < y_start){
                y_start = p.y;
            }

            if(y_end == this.width){
                y_end = p.y;
            }else if(p.y > y_end){
                y_end = p.y;
            }
        }

        if (x_start - 1 >= 0){
            x_start -= 1;
        }

        if (x_start + 1 < this.height){
            x_end += 1;            
        }

        if (y_start - 1 >= 0){
            y_start -= 1;
        }
        if (y_start + 1 < this.width){
            y_end += 1;
        }

        this.Xmin = x_start;
        this.Xmax = x_end;

        this.Ymin = y_start;
        this.Ymax = y_end;
    }

    public void getChainCode(){

    }

    public void detectHoleToList(){
        this.componentList.clear();
        Log.d("blackval","value black : " + blackVal);
        int[][] matBWTmp = new int[this.height][];
        for(int i = 0;i<this.height;i++){
            matBWTmp[i] = new int[this.width];
        }
        copyToMatrix(matBWTmp);
        Log.d("detect", "detect hole");
        for(int i = this.Xmin;i<this.Xmax;i++){
            for(int j = this.Ymin;j < this.Ymax;j++){
                int x = i;
                int y = j;
//                Log.d("valbw","val : " + this.matrixBW[i][j]);
                if (this.matrixBW[x][y] == blackVal){
                    Log.d("detectnew", "detect new hole");

                    // System.out.println("current hole");
                    isHole = true;
                    pList = new ArrayList<>();
                    getDeletedPointSkinComponentUsingStack(x, y);
                    Log.d("comp","complist : " + pList.size());
                    if (isHole) {
                        this.componentList.add(new Component(pList, this.height, this.width));
                        // System.out.println("NEW component");
                    }else {
                        // System.out.println("NOT component");
                    }
                }
            }
        }
        Log.d("component list","size : " + this.componentList.size());
        setMatrixBW(matBWTmp);
    }

    public void getDeletedPointSkinComponentUsingStack(int px, int py){
        this.floodFillStack.push(new point(px, py));
        while(!floodFillStack.empty()){
            point p = this.floodFillStack.peek();
            this.floodFillStack.pop();
            if (this.matrixBW[p.x][p.y] == blackVal){
                pList.add(p);
                this.matrixBW[p.x][p.y] = whiteVal;
                for(int i=0;i < this.iterationDirections.length - 1 ;i++){
                    int dx = p.x + iterationDirections[i][1];
                    int dy = p.y + iterationDirections[i][0];
                    if ((dx >= this.Xmin && dx < this.Xmax) && (dy >= this.Ymin && dy < this.Ymax)){
                        this.floodFillStack.push(new point(dx, dy));
                        // this.matrixBW[dx][dy] = blackVal;
                    }else{
                        isHole = false;
                    }
                }
            }
        }
    }

    public void getDeletedPointSkinComponent(int px, int py){
        // pList harus udah clear dipanggilan pertama

            if (this.matrixBW[px][py] == blackVal){
                pList.add(new point(px, py));
                this.matrixBW[px][py] = whiteVal;
                for(int i=0;i < this.iterationDirections.length - 1 ;i++){
                    int dx = px + iterationDirections[i][1];
                    int dy = py + iterationDirections[i][0];
                    if ((dx >= this.Xmin && dx < this.Xmax) && (dy >= this.Ymin && dy < this.Ymax)){
                        getDeletedPointSkinComponent(dx, dy);
                    }else {
                        isHole = false;
                    }
                }
            }
    }

    public void findEyes(){
        // System.out.println("eye detection");
        Log.d("XX :","(Xmax,Xmin) : " + "(" + Xmax + ", " + Xmin + ")");
        Log.d("YY :","(Ymax,Ymin) : " + "(" + Ymax + ", " + Ymin +")");
        Log.d("list Component", "size of component" + componentList.size());

        int jarakmata = (int)(this.Ymax - this.Ymin) / 2;

        ArrayList<Component> pList2 = new ArrayList<>();
        int batas = this.Xmin + ((this.Xmax - this.Xmin) / 3);
        for(Component p : this.componentList){
            if (p.Xmax < batas){
                pList2.add(p);
            }
        }
        Log.d("mata","mata : " + pList2.size());
//        System.out.println(pList2.size());

        ArrayList<Component> pList2Sorted = new ArrayList<>();
        while (pList2.size() > 0){
            int jmax = -1;
            Component pMax = null;
            int j = 0;
            for(Component p : pList2){
                if (jmax == -1){
                    pMax = p;
                    jmax = j;
                }else if(p.Xmax > pMax.Xmax){
                    pMax = p;
                    jmax = j;
                }
                j++;
            }
            pList2.remove(jmax);
            pList2Sorted.add(pMax);
        }
//        System.out.println(pList2Sorted.size());


        // perlu diubah
        for(int i=0;i<pList2Sorted.size()-1 ;i++){
            for(int j = i;j < pList2Sorted.size();j++){
            // pList2Sorted.get(i).isEye = true;
            // int idx1 = this.componentList.indexOf(pList2Sorted.get(i));
            // this.componentList.get(idx1).isEye = true;

            // bisa tambahin chain code
                Component c1 = pList2Sorted.get(i);
                Component c2 = pList2Sorted.get(j);
                
                if( (Math.abs(c1.Xmax - c2.Xmax) < 20) && (Math.abs(c1.pAreaComponent.size() - c2.pAreaComponent.size()) < 100) && ((c1.Ymax < c2.Ymin) || (c1.Ymin > c2.Ymax)) 
                ){
                    if (c1.Ymax < c2.Ymin)
                    {
                        this.LeftEye = c1;
                        this.RightEye = c2;
                    }else{
                        this.LeftEye = c2;
                        this.RightEye = c1;
                    }
                    int mid = Ymin + ((Ymax - Ymin) / 2);
//                    this.LeftEye.printBounded();
//                    this.RightEye.printBounded();
                    if( Math.abs(this.RightEye.Ymin - this.LeftEye.Ymin) < jarakmata && (this.LeftEye.Ymin < mid && this.LeftEye.Ymax < mid) && (this.RightEye.Ymin > mid && this.RightEye.Ymax > mid)){
                        // && (Math.abs(c1.Xmax - c2.Xmin) < jarakmata) || (Math.abs(c1.Xmin - c2.Xmax) < jarakmata)
                    c1.isEye = true;
                    // System.out.println(this.componentList.indexOf(pList2Sorted.get(i)));
                    c2.isEye = true;
                    isEyeFound = true;
                    // System.out.println(this.componentList.indexOf(pList2Sorted.get(i + 1)));
                        
                    }else{
                        this.LeftEye = null;
                        this.RightEye = null;
                    }

                    
                    

                    break;
                }
            }
            if (isEyeFound) break;
        }
    }

    // perlu diubah
    public void findMouth(){
        if (this.isEyeFound) {
            int Ymax = this.Ymax;
            int Ymin = this.Ymin;
            int Xmax = this.Xmax;
            int Xmin = this.Xmin;
            int sel = Ymax-Ymin;
            Xmax = Xmin + sel;
            int jarakatas = 0;
            // jarak min y 2 mata
            int selXmata = 0;
            if (this.LeftEye.Xmin < this.RightEye.Xmin){
                selXmata = this.LeftEye.Xmin - Xmin;
            }else{
                selXmata = this.RightEye.Xmin - Xmin;
            }
            // dapet 1
            int XmaxMouth = Xmax - selXmata + ((LeftEye.Xmax - LeftEye.Xmin)*2);

            // cari center mata
            int centerMataKiri = this.LeftEye.Ymin + ((this.LeftEye.Ymax - this.LeftEye.Ymin) / 2);
            int centerMataKanan = this.RightEye.Ymin + ((this.RightEye.Ymax - this.RightEye.Ymin) / 2);
            int jarakcentermata = centerMataKanan - centerMataKiri;

            // dapet 2
            int YminMouth = centerMataKiri;
            // System.out.println("xminmouth : " + XminMouth);
            int YmaxMouth = centerMataKanan;

            // dapet 1
            int XminMouth = XmaxMouth - ((LeftEye.Xmax - LeftEye.Xmin)*4);
            ArrayList<point> pListMouth = new ArrayList<>();
            for(int i = XminMouth;i < XmaxMouth;i++){
                for(int j = YminMouth;j < YmaxMouth;j++){
                    // System.out.println(i + ", " + j);
                    pListMouth.add(new point(i,j));
                }
            }
            // System.out.println("max min mouth");
            // System.out.println(XmaxMouth + ", " + XminMouth);
            // System.out.println(YmaxMouth + ", " + YminMouth);

            Component cmouth = new Component(pListMouth, this.height, this.width);
            cmouth.isMouth = true;
            this.mouth = cmouth;
            this.componentList.add(cmouth);
        } else {
            // System.out.println("tidak ada mata");
        }
    }

    private void findNose() {
        int lengthMouth = mouth.Ymax - mouth.Ymin;

        int YmaxNose = mouth.Ymax - (lengthMouth / 4);
        int YminNose = mouth.Ymin + (lengthMouth / 4);
        int XmaxNose = mouth.Xmin - 5;
        int XminNose = LeftEye.Xmax;

        ArrayList<point> pListNose = new ArrayList<>();
        for(int i = XminNose;i < XmaxNose;i++){
            for(int j = YminNose;j < YmaxNose;j++) {
                pListNose.add(new point(i,j));
            }
        }

        Component cNose = new Component(pListNose, this.height, this.width);
        cNose.isNose = true;
        this.componentList.add(cNose);
    }

    public boolean IsFaceDetected(){
        int isEye = 0;
        int isMouth = 0;
        int isNose = 0; 
        for(Component p : this.componentList){
            if (p.isEye) isEye++;
            if (p.isMouth) isMouth++;
            if (p.isNose) isNose++;
        }
        if(isEye == 2 && isMouth == 1){
            return true;
        }else return false;
//        int isEye = 0;
//        int isNose = 0;
//        int isMouth = 0;
//        for(Component p: this.componentList){
//            if (p.isEye()) {
//                isEye++;
//            }else if (p.isNose()){
//                isNose++;
//            }else if(p.isMouth()){
//                isNose++;
//            }
//        }
//        if(isEye == 2 && isNose == 1 && isMouth == 1){
//            return true;
//        }else return false;
    }
}
