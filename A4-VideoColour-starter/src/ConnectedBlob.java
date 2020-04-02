import java.util.ArrayList;
import java.util.Stack;

/**
 * This is a modified class based on the ConnectedComponents code presented by
 * Dr. Godbout.
 */

public class ConnectedBlob {
    private boolean[][] marked;
    private double [][]distances;
    private CurrentBlob[][] blobTable;
    private ArrayList<Blob> blobs;
    private ArrayList<Integer> xvalues, yvalues;
    private double max, min, threshold, rangedMax, rangedMin, currentMax, currentMin,
                    boundaryMax, boundaryMin;
    private int width, height;//, count;

    /**
     * This is the Constructor for Connected Blob. It accepts a number of variables and utilizes
     * a ConnectedComponents approach to determine which Pixels are within given Blobs.
     * @param b this is a 2-D array of Blobs
     * @param d this is a 2-d array of the distances
     * @param givenMax This is the maximum value being examined
     * @param giveMin This is the minimum value being examined
     * @param givenBlobs the ArrayList of Blobs
     * @param h The number of rows
     * @param w The number of columns
     * @param t The Threshold
     */
    public ConnectedBlob(CurrentBlob[][] b, double[][] d, double givenMax, double giveMin,
                         ArrayList<Blob> givenBlobs, int h, int w, double t){
        blobTable = b;
        xvalues = new ArrayList<>();
        yvalues = new ArrayList<>();
        max = givenMax;
        min = giveMin;
        blobs = givenBlobs;
        distances = d;
        width = w;
        height = h;
        marked = new boolean[height][width];
        threshold = t;
        boundaryMax = 0;
        boundaryMin = 0;
        //count = 1;
        checkBoundaries();
        setBlobs();
    }

    /**
     * This looks at each pixel. If it has a distance out of range, it marks it
     * to be ignored
     */
    private void checkBoundaries(){
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                if(distances[i][m] < min || distances[i][m] > max){marked[i][m] = true;}
            }
        }
    }

    /**
     * This creates a Blob in an empty location, and allows it to propogate out to a
     * Blob of chained adjacent pixels. It also supplies a value for the Blob's label
     */
    private void setBlobs(){
        CurrentBlob blob = null;
        double thresholdCompare = 0;
        if(max > 0 && min > 0){
            thresholdCompare = max - min;
        }
        if(max > 0 && min < 0){
            thresholdCompare = max + min;
        }
        if(max < 0 && min < 0){
            thresholdCompare = max - min;
        }
        if(thresholdCompare < threshold){
            oneBlob();
            return;
        }
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                if(!marked[i][m]){
                    currentMax = distances[i][m];
                    currentMin = distances[i][m];
                    boundaryMax = currentMax + threshold;
                    boundaryMin = currentMin - threshold;
                    blob = new CurrentBlob(currentMax,m,i,threshold);
                    //System.out.println("Is cb null? " + blob.getColour());
                    blobTable[i][m] = blob;
                    blobs.add(blob);
                    //blob.setLabel(count);
                    //count++;
                    if(threshold > 0) {
                        Stack<Quad> stack = new Stack<Quad>();
                        xvalues.clear();
                        yvalues.clear();
                        marked[i][m] = true;
                        depthFirstSearch(i, m, blob, currentMax,stack);//, stack);
                        while(stack.size()>0){
                            Quad quad = stack.pop();
                            depthFirstSearch(quad.getFirst(), quad.getSecond(), quad.getBlob(),
                                    quad.getFourth(), stack);
                        }
                            blob.addArrays(xvalues,yvalues);
                        //Search search = new Search(i,m,blob,currentMax,distances,blobTable,blobs,
                          //      marked,threshold,height,width);
                        //Thread thread = new Thread(search);
                        //thread.start();
                    }
                }
            }
        }
    }

    private void oneBlob(){
        CurrentBlob blob = null;
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                if(!marked[i][m]){
                    if(blob == null){
                        blob = new CurrentBlob(distances[i][m], m,i,threshold);
                        blobs.add(blob);
                        blobTable[i][m] = blob;
                        //blob.setLabel(count);
                        //count++;
                    }
                    else{
                        blob.addSquare(m,i,distances[i][m]);
                        blobTable[i][m] = blob;
                    }
                }
            }
        }
    }

    /**
     * This method searches through adjacent squares to determine if they are within the Blob. If they
     * are, it accepts the square into the Blob, and spreads through continued adjacency.
     * @param first the previous pixel's row
     * @param second the previous pixels' column
     * @param cb the CurrentBlob
     */
    private void depthFirstSearch(int first, int second, CurrentBlob cb, double pixel1, Stack<Quad> stack){
        //boolean blobPresent = cb !=null;
        //System.out.println("Is cb null? " + blobPresent);
        double pixel2;
        //marked[first][second] = true;
        blobTable[first][second] = cb;
        if(first - 1 >= 0){
            pixel2 = distances[first-1][second];
            if(!marked[first-1][second] && Math.abs(pixel1 - pixel2) < threshold){//cb.inRange(distances[first-1][second])){
                blobTable[first-1][second] = cb;
                //cb.addSquare(second, first-1, pixel2);
                xvalues.add(second);
                yvalues.add(first-1);
                Quad q = new Quad(first-1, second, cb, pixel2);
                stack.push(q);
                marked[first-1][second] = true;
                //depthFirstSearch(first-1,second,cb, pixel2);
            }
        }
        if(second - 1 >= 0){
            pixel2 = distances[first][second-1];
            if(!marked[first][second-1] && Math.abs(pixel1 - pixel2) < threshold){// && cb.inRange(distances[first][second-1])){
                blobTable[first][second-1] = cb;
                //cb.addSquare(second-1, first, pixel2);
                xvalues.add(second-1);
                yvalues.add(first);
                marked[first][second-1] = true;
                //depthFirstSearch(first,second-1,cb,pixel2);
                Quad q = new Quad(first, second-1, cb, pixel2);
                stack.push(q);
            }
        }
        if(first + 1 < height){
            pixel2 = distances[first+1][second];
            if(!marked[first+1][second] && Math.abs(pixel1 - pixel2) < threshold){// && cb.inRange(distances[first+1][second])){
                blobTable[first+1][second] = cb;
                //cb.addSquare(second, first+1, pixel2);
                xvalues.add(second);
                yvalues.add(first+1);
                marked[first+1][second] = true;
                //depthFirstSearch(first+1,second,cb,pixel2);
                Quad q = new Quad(first+1, second, cb, pixel2);
                stack.push(q);
            }
        }
        if(second + 1 < width){
            pixel2 = distances[first][second+1];
            if(!marked[first][second+1] && Math.abs(pixel1 - pixel2) < threshold){// && cb.inRange(distances[first][second+1])){
                blobTable[first][second+1] = cb;
                //cb.addSquare(second+1, first, pixel2);
                xvalues.add(second+1);
                yvalues.add(first);
                marked[first][second+1] = true;
                //depthFirstSearch(first,second+1,cb,pixel2);
                Quad q = new Quad(first, second+1, cb, pixel2);
                stack.push(q);
            }
        }
        if(first - 1 >= 0 && second - 1 >= 0){
            pixel2 = distances[first-1][second-1];
            if(!marked[first-1][second-1] && Math.abs(pixel1 - pixel2) < threshold){// && cb.inRange(distances[first-1][second-1])){
                blobTable[first-1][second-1] = cb;
                //cb.addSquare(second-1, first-1, pixel2);
                xvalues.add(second-1);
                yvalues.add(first-1);
                marked[first-1][second-1] = true;
                //depthFirstSearch(first-1,second-1,cb,pixel2);
                Quad q = new Quad(first-1, second-1, cb, pixel2);
                stack.push(q);
            }
        }
        if(second - 1 >= 0 && first + 1 < height){
            pixel2 = distances[first+1][second-1];
            if(!marked[first+1][second-1] && Math.abs(pixel1 - pixel2) < threshold){// && cb.inRange(distances[first+1][second-1])){
                blobTable[first+1][second-1] = cb;
                //cb.addSquare(second-1, first+1, pixel2);
                xvalues.add(second-1);
                yvalues.add(first+1);
                marked[first+1][second-1] = true;
                //depthFirstSearch(first+1,second-1,cb,pixel2);
                Quad q = new Quad(first+1, second-1, cb, pixel2);
                stack.push(q);
            }
        }
        if(first + 1 < height && second + 1 < width){
            pixel2 = distances[first+1][second+1];
            if(!marked[first+1][second+1] && Math.abs(pixel1 - pixel2) < threshold){// && cb.inRange(distances[first+1][second+1])){
                blobTable[first+1][second+1] = cb;
                //cb.addSquare(second+1, first+1, pixel2);
                xvalues.add(second+1);
                yvalues.add(first+1);
                marked[first+1][second+1] = true;
                //depthFirstSearch(first+1,second+1,cb,pixel2);
                Quad q = new Quad(first+1, second+1, cb, pixel2);
                stack.push(q);
            }
        }
        if(second + 1 < width && first - 1 >= 0){
            pixel2 = distances[first-1][second+1];
            if(!marked[first-1][second+1] && Math.abs(pixel1 - pixel2) < threshold){// && cb.inRange(distances[first-1][second+1])){
                blobTable[first-1][second+1] = cb;
                //cb.addSquare(second+1, first-1, pixel2);
                xvalues.add(second+1);
                yvalues.add(first-1);
                marked[first-1][second+1] = true;
                //depthFirstSearch(first-1,second+1,cb,pixel2);
                Quad q = new Quad(first-1, second+1, cb, pixel2);
                stack.push(q);
            }
        }
        /*if(stack.size()>0){
            Triple triple = stack.pop();
            depthFirstSearch(triple.getFirst(), triple.getSecond(), triple.getBlob(), stack);
        }*/
    }

    /**
     * An unused method.
     * @param value
     */
    private void adjustGuidelines(double value){
        if(value > currentMax){currentMax = value;}
        if(value < currentMin){currentMin = value;}

    }

    public CurrentBlob[][] getBlobTable(){return blobTable;}
}
