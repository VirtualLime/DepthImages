import java.util.ArrayList;
import java.util.Stack;

/**
 * This is a modified class based on the ConnectedComponents code presented by Dr. Godbout.
 * It will compare the various distance values to determine if a pixel should be ignored,
 * or placed with a specific Blob. It then sends the pixels into a Blob.
 */

public class ConnectedBlob {
    private boolean[][] marked;
    private double [][]distances;
    private CurrentBlob[][] blobTable;
    private ArrayList<Blob> blobs;
    private ArrayList<Integer> xvalues, yvalues;
    private double max, min, threshold;
    private int width, height;

    /**
     * This is the Constructor for Connected Blob. It accepts a number of variables and utilizes
     * a ConnectedComponents approach to determine which Pixels are within given Blobs.
     * @param bTableIn this is a 2-D array of Blobs
     * @param distancesIn this is a 2-d array of the distances
     * @param givenMax This is the maximum value being examined
     * @param giveMin This is the minimum value being examined
     * @param givenBlobs the ArrayList of Blobs
     * @param heightIn The number of rows
     * @param widthIn The number of columns
     * @param thresholdIn The Threshold
     */
    public ConnectedBlob(CurrentBlob[][] bTableIn, double[][] distancesIn, double givenMax, double giveMin,
                         ArrayList<Blob> givenBlobs, int heightIn, int widthIn, double thresholdIn){
        blobTable = bTableIn;
        xvalues = new ArrayList<>();
        yvalues = new ArrayList<>();
        max = givenMax;
        min = giveMin;
        blobs = givenBlobs;
        distances = distancesIn;
        width = widthIn;
        height = heightIn;
        marked = new boolean[height][width];
        threshold = thresholdIn;
        checkBoundaries();
        setBlobs();
    }

    /**
     * This looks at each pixel. If it has a distance out of range, it marks it
     * to be ignored (marked as already seen, but not given a Blob on the blobTable
     */
    private void checkBoundaries(){
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                if(distances[i][m] < min || distances[i][m] > max){marked[i][m] = true;}
            }
        }
    }

    /**
     * This creates a Blob in an empty location, and allows it to propagate out to a
     * Blob of chained adjacent pixels. A Stack was added to prevent Stack Overflow issues.
     */
    private void setBlobs(){
        CurrentBlob blob = null;
        double thresholdCompare = 0;
        /*
        The below four if statements are used to determine if all of the pixels (in range) will
        be of one Blob. If it is, it sends the table out to have that set up, to prevent it from
        going to the search section.
         */
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
        /*
        The section below checks to see (pixel by pixel) if a pixel has been marked. If
        it hasn't been, a new Blob will be formed, and sent out to the depth first search.
        any pixels marked as within the Blob, will be placed in a Stack, to also be checked.
        Each x and y will be collected, and when the Stack is empty, it will be sent to the
        Blob
         */
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                if(!marked[i][m]){
                    blob = new CurrentBlob(m,i);
                    blobTable[i][m] = blob;
                    blobs.add(blob);
                    if(threshold > 0) {
                        Stack<Quad> stack = new Stack<Quad>();
                        xvalues.clear();
                        yvalues.clear();
                        marked[i][m] = true;
                        depthFirstSearch(i, m, blob, distances[i][m],stack);
                        while(stack.size()>0){
                            Quad quad = stack.pop();
                            depthFirstSearch(quad.getFirst(), quad.getSecond(), quad.getBlob(),
                                    quad.getFourth(), stack);
                        }
                            blob.addArrays(xvalues,yvalues);
                    }
                }
            }
        }
    }

    /**
     * This is a method used when the check indicates that all pixels in range are within a
     * single Blob. It will go through the entire table, and any pixel not marked will be
     * added to the single Blob.
     */
    private void oneBlob(){
        CurrentBlob blob = null;
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                if(!marked[i][m]){
                    if(blob == null){
                        blob = new CurrentBlob(m,i);
                        blobs.add(blob);
                        blobTable[i][m] = blob;
                    }
                    else{
                        blob.addSquare(m,i);
                        blobTable[i][m] = blob;
                    }
                }
            }
        }
    }

    /**
     * This method searches through adjacent squares to determine if they are within the Blob. If they
     * are, it accepts the square into the Blob, and spreads through continued adjacency. Any newly-marked
     * pixel will be added to the Stack so that it can also be run through this method without Stack
     * Overflow issues.
     * @param first the previous pixel's row
     * @param second the previous pixels' column
     * @param cb the CurrentBlob
     */
    private void depthFirstSearch(int first, int second, CurrentBlob cb, double pixel1, Stack<Quad> stack){
        double pixel2;
        blobTable[first][second] = cb;
        if(first - 1 >= 0){
            pixel2 = distances[first-1][second];
            if(!marked[first-1][second] && Math.abs(pixel1 - pixel2) < threshold){
                blobTable[first-1][second] = cb;
                xvalues.add(second);
                yvalues.add(first-1);
                Quad q = new Quad(first-1, second, cb, pixel2);
                stack.push(q);
                marked[first-1][second] = true;
            }
        }
        if(second - 1 >= 0){
            pixel2 = distances[first][second-1];
            if(!marked[first][second-1] && Math.abs(pixel1 - pixel2) < threshold){
                blobTable[first][second-1] = cb;
                xvalues.add(second-1);
                yvalues.add(first);
                marked[first][second-1] = true;
                Quad q = new Quad(first, second-1, cb, pixel2);
                stack.push(q);
            }
        }
        if(first + 1 < height){
            pixel2 = distances[first+1][second];
            if(!marked[first+1][second] && Math.abs(pixel1 - pixel2) < threshold){
                blobTable[first+1][second] = cb;
                xvalues.add(second);
                yvalues.add(first+1);
                marked[first+1][second] = true;
                Quad q = new Quad(first+1, second, cb, pixel2);
                stack.push(q);
            }
        }
        if(second + 1 < width){
            pixel2 = distances[first][second+1];
            if(!marked[first][second+1] && Math.abs(pixel1 - pixel2) < threshold){
                blobTable[first][second+1] = cb;
                xvalues.add(second+1);
                yvalues.add(first);
                marked[first][second+1] = true;
                Quad q = new Quad(first, second+1, cb, pixel2);
                stack.push(q);
            }
        }
        if(first - 1 >= 0 && second - 1 >= 0){
            pixel2 = distances[first-1][second-1];
            if(!marked[first-1][second-1] && Math.abs(pixel1 - pixel2) < threshold){
                blobTable[first-1][second-1] = cb;
                xvalues.add(second-1);
                yvalues.add(first-1);
                marked[first-1][second-1] = true;
                Quad q = new Quad(first-1, second-1, cb, pixel2);
                stack.push(q);
            }
        }
        if(second - 1 >= 0 && first + 1 < height){
            pixel2 = distances[first+1][second-1];
            if(!marked[first+1][second-1] && Math.abs(pixel1 - pixel2) < threshold){
                blobTable[first+1][second-1] = cb;
                xvalues.add(second-1);
                yvalues.add(first+1);
                marked[first+1][second-1] = true;
                Quad q = new Quad(first+1, second-1, cb, pixel2);
                stack.push(q);
            }
        }
        if(first + 1 < height && second + 1 < width){
            pixel2 = distances[first+1][second+1];
            if(!marked[first+1][second+1] && Math.abs(pixel1 - pixel2) < threshold){
                blobTable[first+1][second+1] = cb;
                xvalues.add(second+1);
                yvalues.add(first+1);
                marked[first+1][second+1] = true;
                Quad q = new Quad(first+1, second+1, cb, pixel2);
                stack.push(q);
            }
        }
        if(second + 1 < width && first - 1 >= 0){
            pixel2 = distances[first-1][second+1];
            if(!marked[first-1][second+1] && Math.abs(pixel1 - pixel2) < threshold){
                blobTable[first-1][second+1] = cb;
                xvalues.add(second+1);
                yvalues.add(first-1);
                marked[first-1][second+1] = true;
                Quad q = new Quad(first-1, second+1, cb, pixel2);
                stack.push(q);
            }
        }
    }

    public CurrentBlob[][] getBlobTable(){return blobTable;}
}
