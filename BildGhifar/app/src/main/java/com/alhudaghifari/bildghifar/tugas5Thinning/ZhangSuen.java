package com.alhudaghifari.bildghifar.tugas5Thinning;


import android.graphics.Point;
import android.util.Log;

import java.sql.Array;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class ZhangSuen {

    private static final String TAG = ZhangSuen.class.getSimpleName();
    private static final int MAX_DIRECTION = 8;
    private int matrixBlackWhite[][];
    private int width;
    private int height;
    private ArrayList<Point> resultThinningList;
    private Point pointMax;
    private Point pointMin;

    /*
    P9 P2 P3
    P8 P1 P4
    P7 P6 P5

    Dir

    */

    private final int iterationDirections[][] = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};

    private final  int[][][] nbrGroups = {{{0, 2, 4}, {2, 4, 6}}, {{0, 2, 6}, {0, 4, 6}}};

    private ArrayList<Point> toZerolist;

    public ZhangSuen(int matrix[][],int height ,int width){
        this.width = width;
        this.height = height;

        this.matrixBlackWhite = new int[height][];
        for (int i=0;i<height;i++){
            this.matrixBlackWhite[i] = new int[width];
        }

//        bacaFile();
        for(int i =0;i<height;i++){
            for(int j=0;j<width;j++){
//                Log.d("isi matrix : ", " " + matrix[i][j]);
                this.matrixBlackWhite[i][j] = matrix[i][j];
            }
        }
        this.toZerolist = new ArrayList<>();
        this.resultThinningList = new ArrayList<>();
        this.pointMax = new Point();
        this.pointMin = new Point();
    }

    public void copyToMatrix(int matrix[][]){
        for(int i =0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                matrix[i][j] = this.matrixBlackWhite[i][j];
            }
        }
    }

    public void bacaFile(String file){

        try{
            BufferedReader objReader = new BufferedReader(new FileReader("D:\\coder\\pengcit\\" + file));
            String s;
            int i = 0;

            while ((s = objReader.readLine()) != null) {
                // System.out.println(s);
                String[] sampleStringSplit = s.split(" ");
                // this.matrixBlackWhite = new int[this][];
                for(int j=0;j<sampleStringSplit.length;j++){
                    // System.out.println(Integer.parseInt(sampleStringSplit[j]));
                    this.matrixBlackWhite[i][j] = Integer.parseInt(sampleStringSplit[j]);
                }
                // for (int j=0;j<sampleStringSplit.length;j++){
                //     this.matrixBlackWhite[i][j] = Integer.parseInt(sampleStringSplit[j]);
                // }
                i++;
            }
            // this.height = i;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void printFile(){
        for(int i = 0;i<height;i++){
            for(int j = 0;j < width;j++){
                System.out.print(this.matrixBlackWhite[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int numNeighbors(int h, int w){
        int count = 0;
        // System.out.println("num neighbors");
        for(int i=0;i < this.iterationDirections.length - 1 ;i++){
            // System.out.println(this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]]);
            if (this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]] == 1){
                count++;
            }
        }
        return count;
    }

    public int numTransitions(int h, int w){
        int count = 0;
        for(int i=0;i < this.iterationDirections.length - 1 ;i++){
            if (this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]] == 0){
                if (this.matrixBlackWhite[h + this.iterationDirections[i + 1][1]][w + this.iterationDirections[i + 1][0]] == 1){
                    count++;
                }
            }
        }
        return count;
    }

    public boolean atLeastOneIsZero(int h, int w, int step){
        int count = 0;
        int[][] currentGroup = this.nbrGroups[step];
        for(int i=0;i<2;i++){
            for(int j=0;j< currentGroup[i].length;j++){
                int[] nbr = this.iterationDirections[currentGroup[i][j]];
                if (this.matrixBlackWhite[h + nbr[1]][w + nbr[0]] == 0){
                    count++;
                    break;
                }
            }
        }
        return count > 1;
    }

    public void thinImage(){
        boolean firstStep = true;
        boolean changed = false;
        int ii = 0;
        do{
            ii++;
            // System.out.println("LOOP - " + ii);
            changed = false;
            firstStep = !firstStep;

            for(int i = 1;i < height - 1; i++){
                for(int j = 1;j < width - 1;j++){

                    if (this.matrixBlackWhite[i][j] == 0){
                        continue;
                    }

                    int nn = numNeighbors(i, j);
                    if (nn < 2 || nn > 6){
                        continue;
                    }

                    if (numTransitions(i, j) != 1){
                        continue;
                    }

                    if (!atLeastOneIsZero(i, j, firstStep ? 0 : 1)){
                        continue;
                    }

                    this.toZerolist.add(new Point(i, j));
                    changed = true;
                }
            }

            for(Point p: this.toZerolist){
                this.matrixBlackWhite[p.x][p.y] = 0;
            }

            this.toZerolist.clear();
            // printFile();
        }while(changed || firstStep);
    }

    public void setThinningList(){
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                if (this.matrixBlackWhite[i][j] == 1){
                    this.resultThinningList.add(new Point(i, j));
                }
            }
        }
    }

    public void printThinningList(){
        // for(int i=0;i<this.resultThinningList.size();i++){
        //     System.out.println("(" + this.resultThinningList[i].getX() + ", " + this.resultThinningList[i].getY() + ")");
        // }
        for (Point p: this.resultThinningList){
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }

    public ArrayList<Point> getEndPoint(){
        ArrayList<Point> pList = new ArrayList<>();
        for (Point p: this.resultThinningList){
            // System.out.println("(" + p.x + ", " + p.y + ")");
            if (numNeighbors(p.x, p.y) == 1){
                // System.out.println("(" + p.x + ", " + p.y + ")");
                pList.add(new Point(p));
            }
        }
        return pList;
    }

    public void printEndpoint(){
        ArrayList<Point> pList = getEndPoint();
        for (Point p: pList){
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }

    public void getBoundPoints(){
        // ArrayList<Point> pList = getEndPoint();
        int i = 0;
        int xmax = 0,xmin = 0,ymax = 0,ymin = 0;
        for (Point p: this.resultThinningList){
            i++;
            if (i == 1){
                xmax = p.x;
                xmin = p.x;
                ymax = p.y;
                ymin = p.y;
            }else{
                if(p.x > xmax){
                    xmax = p.x;
                }
                if(p.x < xmin){
                    xmin = p.x;
                }
                if(p.y > ymax){
                    ymax = p.y;
                }
                if(p.y < ymin){
                    ymin = p.y;
                }
            }
            // System.out.println("(" + p.x + ", " + p.y + ")");
        }

        // System.out.println("-----------------------------  Bounded Point  --------------------------");
        this.pointMax.set(xmax + 1, ymax + 1);
        // System.out.println(xmax + " " + ymax);
        this.pointMin.set(xmin - 1, ymin - 1);
        // System.out.println(xmin + " " + ymin);
    }

    public void printBoundedPoint(){
        System.out.println("max : " + this.pointMax.x + ", " + this.pointMax.y);
        System.out.println("min : " + this.pointMin.x + ", " + this.pointMin.y);
    }

    public int getArea(int h, int w){
        // 1 .. 4
        int area = 0;
        if ((h > this.pointMin.x) && (h < ((this.pointMax.x + this.pointMin.x) / 2)) && (w > this.pointMin.y) && (w < (this.pointMax.y + this.pointMin.y) / 2)){
            area = 1;
        }else if(h > this.pointMin.x && h < ((this.pointMax.x + this.pointMin.x) / 2) && w > ((this.pointMax.y + this.pointMin.y) / 2) && w < this.pointMax.y){
            area = 2;
        }else if(h > ((this.pointMax.x + this.pointMin.x) / 2) && h < this.pointMax.x && w > ((this.pointMax.y + this.pointMin.y) / 2) && w < this.pointMax.y){
            area = 3;
        }else if(h > ((this.pointMax.x + this.pointMin.x) / 2) && h < this.pointMax.x && w > this.pointMin.y && (w < (this.pointMax.y + this.pointMin.y) / 2)){
            area = 4;
        }
        // else originv
        return area;
    }

    public int getDirection(Point p){
        int h = p.x;
        int w = p.y;
        int dir = 0;
        for(int i=0;i < this.iterationDirections.length - 1 ;i++){
            // System.out.println(this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]]);
            if (this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]] == 1){
                dir = i + 1;
            }
        }
        return dir;
    }

    public void deleteLineToCenter(Point p, Point pCenter, ArrayList<Integer> dirL){
//        ArrayList<Point> p = getEndPoint();
        int xStart = p.x;
        int yStart = p.y;
        int i = 0;
        while(xStart != pCenter.x || yStart != pCenter.y){
            Log.d(TAG, "" + i + " " + xStart + " " + yStart + " -> " + this.matrixBlackWhite[xStart][yStart]);
            this.matrixBlackWhite[xStart][yStart] = 0;
            if (i == dirL.size()){
                break;
            }
            xStart += this.iterationDirections[dirL.get(i) ][1];
            yStart += this.iterationDirections[dirL.get(i) ][0];

            i++;
        }
    }

    public void copyList(ArrayList<Integer> l1, ArrayList<Integer> l2){
        // copy isi l1 ke l2
        // l2 dalam keadaan kososng/empty l2
        for(int i: l1){
            l2.add(i);
        }
    }

    public void postProcessing(String number){
        ArrayList<Point> pList = getEndPoint();
        int count = pList.size();
        if (count == 3){
            Point p1 = new Point(pList.get(0));
            Point p2 = new Point(pList.get(1));
            Point p3 = new Point(pList.get(2));
            if (p1.x > p2.x){
                Point tmp = new Point(p1);
                p1.set(p2.x, p2.y);
                p2.set(tmp.x, tmp.y);
            }
            if(p1.x > p3.x){
                Point tmp = new Point(p1);
                p1.set(p3.x, p3.y);
                p3.set(tmp.x, tmp.y);
            }
            if(p2.x > p3.x){
                Point tmp = new Point(p2);
                p2.set(p3.x, p3.y);
                p3.set(tmp.x, tmp.y);
            }
            if (number.equals("1") || number.equals("4") || number.equals("7")){
                deleteLine(p1);
            }else if(number.equals("5")){
                deleteLine(p2);
            }else if(number.equals("2")){
                deleteLine(p3);
            }
        }
        setThinningList();
    }

    public int getDistance(Point p){
        int xStart = p.x;
        int yStart = p.y;
        int currentX = -1;
        int currentY = -1;
        int len = 0;
        while (numNeighbors(xStart, yStart) <= 2){
            int h = xStart;
            int w = yStart;
            int dir = -1;
            len++;
//            this.matrixBlackWhite[xStart][yStart] = 0;
            for(int i=0;i < this.iterationDirections.length - 1 ;i++){

                // System.out.println(this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]]);
                if (this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]] == 1 && dir == -1 && (h + this.iterationDirections[i][1]) != currentX && (w + this.iterationDirections[i][0]) != currentY){
                    dir = i;
                }
            }
            Log.d("direction  : ", " " + dir);
            xStart = h + this.iterationDirections[dir][1];
            yStart = w + this.iterationDirections[dir][0];
            currentX = xStart;
            currentY = yStart;
        }
        return len;
    }

    public void deleteLine(Point p){
        int xStart = p.x;
        int yStart = p.y;
        Log.d("pointdelete  : ", " " + p.x + " " + p.y);
        while (numNeighbors(xStart, yStart) <= 1){
            int h = xStart;
            int w = yStart;
            int dir = -1;

            this.matrixBlackWhite[xStart][yStart] = 0;
            for(int i=0;i < this.iterationDirections.length - 1 ;i++){

                // System.out.println(this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]]);
                if (this.matrixBlackWhite[h + this.iterationDirections[i][1]][w + this.iterationDirections[i][0]] == 1 && dir == -1){
                    dir = i;
                }
            }
            Log.d("direction  : ", " " + dir);
            xStart = h + this.iterationDirections[dir][1];
            yStart = w + this.iterationDirections[dir][0];
        }
    }

    public String recognizeCharacter(){
        // return character 0..9 , unknwon character -1
        ArrayList<Point> pList = getEndPoint();
        int count = pList.size();
        Log.d(TAG, "jumlah titik : " + count);
        String character = "-1";
        switch(count){
            case 0:
                // 0 atau 8
                // cari di line apakah ada garis
                int xmin = this.pointMin.x;
                int midy = (this.pointMax.y + this.pointMin.y) / 2;
                System.out.println(midy);
                for(int i=xmin+2;i<pointMax.x - 1;i++){
                    if (this.matrixBlackWhite[i][midy] == 1){
                        character = "8";
                        break;
                    }
                }
                if (character == "-1"){
                    character = "0";
                }
                break;
            case 1:
                // 6 atau 9
                int midx = (this.pointMin.x + this.pointMax.x) / 2;
                // System.out.println(midx);


                if (pList.get(0).x <= midx){
                    character = "6";
                }else{
                    character = "9";
                }
                break;
            case 2:
                Point p2_1 = new Point(pList.get(0));
                Point p2_2 = new Point(pList.get(1));

                // ini kayaknya dari awal inisiasi antara x dan y kebalik
                int lengthX = p2_2.x - p2_1.x;
                int lengthY = p2_2.y - p2_1.y;

                Log.i(TAG, "length x : " + lengthX);
                Log.i(TAG, "length y : " + lengthY);

                // batas untuk menentukan condong / kemiringan garis untuk I dan -
                int skewedStraightLimit = 30;

                int skewedLimitMax = 80;
                int skewedLimitMin = 50;

                if (lengthY >= skewedStraightLimit*-1 && lengthY <= skewedStraightLimit && lengthY < lengthX)
                    character = "I";
                else if (lengthX >= skewedStraightLimit*-1 && lengthX <= skewedStraightLimit && lengthY > lengthX)
                    character = "-";
                else if (lengthY >= skewedLimitMax*-1 && lengthY <= skewedLimitMin && lengthY < lengthX)
                    character = "/";
                else if (lengthY >= skewedLimitMin && lengthY <= skewedLimitMax && lengthY < lengthX)
                    character = "\\";


                Log.i(TAG, "character : " + character);
                break;
            case 3:
                Point p1 = new Point(pList.get(0));
                Point p2 = new Point(pList.get(1));
                Point p3 = new Point(pList.get(2));
                if (p1.x > p2.x){
                    Point tmp = new Point(p1);
                    p1.set(p2.x, p2.y);
                    p2.set(tmp.x, tmp.y);
                }
                if(p1.x > p3.x){
                    Point tmp = new Point(p1);
                    p1.set(p3.x, p3.y);
                    p3.set(tmp.x, tmp.y);
                }
                if(p2.x > p3.x){
                    Point tmp = new Point(p2);
                    p2.set(p3.x, p3.y);
                    p3.set(tmp.x, tmp.y);
                }
                int dir1 = getDirection(p1);
                int dir2 = getDirection(p2);
                int dir3 = getDirection(p3);

                int area1 = getArea(p1.x, p1.y);
                int area2 = getArea(p2.x, p2.y);
                int area3 = getArea(p3.x, p3.y);

                int middlex = (this.pointMax.x + this.pointMin.x) / 2;
                int middley = (this.pointMax.y + this.pointMin.y) / 2;

                if (area1 == 1 && area3 == 4 && (p2.x < (middlex + this.pointMax.x)/2) && (p2.x > (this.pointMin.x + middlex)/2)){
                    character = "3";
                }else if(p2.x > middlex && p3.x > middlex && area2 != 4 && area3 != 4 && (dir3 == 8 || dir3 == 1 || dir3 == 2) && (dir2 == 8 || dir2 == 7 || dir2 == 6)){
                    character = "4";
                }else if(dir1 == 7 && p1.y > middley && (dir3 == 2 || dir3 == 3 || dir3 == 4 || dir3 == 5) && area3 == 4 && p2.y < middley){
                    character = "5";
                }else if((dir1 == 2 || dir1 == 3 || dir1 == 4 || dir1 == 1) && area1 == 1 && (dir2 == 6 || dir2 == 7 || dir2 == 8) && area2 == 3 && p3.x > middlex){
                    character = "2";
                }else if((dir2 == 2 || dir2 == 3) && area2 == 1 && p1.y > middley){
                    if(dir3 == 1){
                        character = "1";
                    }else character = "7";
                }
                break;
            case 4:
//                Point p4_1 = new Point(pList.get(0));
//                Point p4_2 = new Point(pList.get(1));
//                Point p4_3 = new Point(pList.get(2));
//                Point p4_4 = new Point(pList.get(3));
//
//                Log.d(TAG, "p4_1 | x : " + p4_1.x + " | y : " + p4_1.y);
//                Log.d(TAG, "p4_2 | x : " + p4_2.x + " | y : " + p4_2.y);
//                Log.d(TAG, "p4_3 | x : " + p4_3.x + " | y : " + p4_3.y);
//                Log.d(TAG, "p4_4 | x : " + p4_4.x + " | y : " + p4_4.y);

                character = "3";
                break;
            case 5:
                character = "*";
                break;
            default:
                // unknown
                character = "-1";
                break;

//            case -1:
//                 // preprocessing false endpoint
//
//                // calculate endpoint to branching
//                Point pCenter = new Point();
//                ArrayList<Integer> DirList = new ArrayList<>();
//                ArrayList<Integer> MinList = new ArrayList<>();
//                int jarakMin = 0;
//                int idx = 0;
//                for(int i=0;i<pList.size();i++){
//                    int nbx = pList.get(0).x; //neighboor
//                    int nby = pList.get(0).y; //neighboor
//                    int nbxstart = 0;
//                    int nbystart = 0;
//                    int start = 0;
//                    boolean found = false;
//
//                    int xBefore = 0;
//                    int yBefore = 0;
//                    DirList.clear();
//                    while(!found){
//                        int dir = 0;
//                        int numNeighboor = 0;
//                        for(int j=0;j<iterationDirections.length - 1;j++){
//                            if ((nbx + this.iterationDirections[j][1] != xBefore) || (nby + this.iterationDirections[j][1] != yBefore)) {
//                                if ( this.matrixBlackWhite[nbx + this.iterationDirections[j][1]][nby + this.iterationDirections[j][0]] == 1){
//                                    if (numNeighboor < 2) {
//                                        dir = j;
//                                        nbxstart = nbx + this.iterationDirections[j][1];
//                                        nbystart = nby + this.iterationDirections[j][0];
//
//                                    }else{
//                                        found = true;
//                                        pCenter.set(nbx, nby);
//                                    }
//                                    numNeighboor++;
//                                }
//
//                            }
//
//                        }
//                        nbx = nbxstart;
//                        nby = nbystart;
//                        if (yBefore == nby && xBefore == nbx){
//                            break;
//                        }else if (!found){
//                            DirList.add(dir);
//                        }
//                        xBefore = nbx;
//                        yBefore = nby;
//                        start++;
//                    }
//
//                    if(i == 0){
//                        idx = i;
//                        jarakMin = start;
//                        MinList.clear();
//                        copyList(DirList, MinList);
//                    }else if(start < jarakMin){
//                        idx = i;
//                        jarakMin = start;
//                        MinList.clear();
//                        copyList(DirList, MinList);
//                    }
//                }
//                Log.d("direction 0  : ", " " + MinList.get(0));
//                Log.d("idx  : ", " " + idx);
//                Log.d("idxPoint  : ", " " + pList.get(idx));
//                Log.d("center  : ", " " + pCenter.x + " " + pCenter.y);
//                for(Point p: pList){
//                    Log.d("EndPoint  : ", " " + p);
//                }
//
//
//                deleteLineToCenter(pList.get(idx), pCenter, MinList);
//                pList = getEndPoint();
//                if (pList.size() > 2){
//                    Log.d("endpointbaru : ", " Kok Bisa Sih Endpointnya ada lebih dari 2 :( ");
//                }
//                setThinningList();
//                getBoundPoints();
//
//                // sama kayak kasus 2
//                Point p1 = new Point();
//                Point p2 = new Point();
//                pList = getEndPoint();
//                if (pList.get(0).x < pList.get(1).x){
//                    p1.set(pList.get(0).x, pList.get(0).y);
//                    p2.set(pList.get(1).x, pList.get(1).y);
//                }else{
//                    p2.set(pList.get(0).x, pList.get(0).y);
//                    p1.set(pList.get(1).x, pList.get(1).y);
//                }
//                int dir1 = getDirection(p1);
//                int area1 = getArea(p1.x, p1.y);
//                int dir2 = getDirection(p2);
//                int area2 = getArea(p2.x, p2.y);
//                if ((dir1 == 2 || dir1 == 3) && (dir2 == 2 || dir2 == 3)){
//                    character = 3;
//                }
//                int middlex = (this.pointMax.x + this.pointMin.x) / 2;
//                if( p1.x >= middlex && p2.x >= middlex){
//                    if ((dir2 == 8 || dir2 == 1 || dir2 == 2) && (dir1 == 6 || dir1 == 7 || dir1 == 8)){
//                        character = 4;
//                    }
//                }else if((dir2 == 2 || dir2 == 3) && dir1 == 7){
//                    character = 5;
//                }else if((dir2 == 7 || dir2 == 8) && (dir1 == 1 || dir1 == 2 || dir1 == 3)){
//                    character = 2;
//                }else if(dir1 == 3 || dir1 == 2){
//                    boolean found = false;
//                    for(int i = p2.x ;i>p1.x;i--){
//                        if(this.matrixBlackWhite[i][p2.y] == 0){
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (found){
//                        character = 7;
//                    }else{
//            character = 1;
//        }
//    }
//
//                break;
        }
        return character;
    }
}