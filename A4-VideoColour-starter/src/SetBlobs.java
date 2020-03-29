
import java.util.ArrayList;

/**
 * This is the SetBlobs Class. It starts off a sequence of event in creating the Blobs
 * and applying the colour to the pixels
 */
public class SetBlobs {
    private ArrayList<Blob> blobs;
    private int[][] colourTable;
    private double[][] distanceTable;
    private CurrentBlob[][] blobTable;
    private double threshold, min, max;
    private int width, height;

    /**
     * The Constructor. After initializing variables, it sends out to a method to have the Blobs set.
     * @param dTable the table of distances
     * @param b the arraylist of Blobs
     * @param t the threshold
     * @param w the table's width
     * @param h the table's height
     * @param minimum the minimum distance evaluated
     * @param maximum the maximum distance evaluated
     */
    public SetBlobs(double[][]dTable, ArrayList<Blob> b, double t, int w, int h, double minimum, double maximum){
        min = minimum;
        max = maximum;
        blobs = b;
        blobs.clear();
        height = h;
        width = w;
        distanceTable = dTable;
        blobTable = new CurrentBlob[h][w];
        threshold  = t;
        colourTable = new int[height][width];
        setBlobs();
        //setColourTable();
    }

    /**
     * This method calls out to ConnectedBlob to set up all of the Blbs, and then calls
     * setColourTable to set the pixels' colours
     */
    private void setBlobs(){
        setBlankBlobs();
        ConnectedBlob connectedBlob = new ConnectedBlob(blobTable,distanceTable,max,min,blobs,height,width,threshold);
        blobTable = connectedBlob.getBlobTable();

        setColourTable();
        /*for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                int c = colourTable[i][m]; System.out.print(c + " ");
            }
            System.out.println();
        }
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                CurrentBlob c = blobTable[i][m]; if(c == null){System.out.print("null ");}else{System.out.print("present ");}
            }
            System.out.println();
        }*/
        /*for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                CurrentBlob blob = null;
                boolean newBlob = true;
                if(distanceTable[i][m] > max || distanceTable[i][m] < min){
                    newBlob = false;
                }
                if(threshold <= 0){
                    blob = new CurrentBlob(distanceTable[i][m], i, m, threshold);
                    newBlob = false;
                    //System.out.println("Threshold low");
                    blobs.add(blob);
                }
                if(i > 0 && newBlob){
                    blob = blobTable[i-1][m];
                    if(blob != null){
                        if(blob.inRange(distanceTable[i][m])){
                            blob.addSquare(i,m,distanceTable[i][m]);
                            newBlob = false;
                            //System.out.println("above");
                        }
                    }
                }
                if(i > 0 && m > 0 && newBlob){
                    blob = blobTable[i-1][m-1];
                    if(blob != null){
                        if(blob.inRange(distanceTable[i][m])){
                            blob.addSquare(i,m,distanceTable[i][m]);
                            newBlob = false;
                            //System.out.println("top left");
                        }
                    }
                }
                if(i > 0 && m < width - 1 && newBlob){
                    blob = blobTable[i-1][m+1];
                    if(blob != null) {
                        if (blob.inRange(distanceTable[i][m])) {
                            blob.addSquare(i, m, distanceTable[i][m]);
                            newBlob = false;
                            //System.out.println("top right");
                        }
                    }
                }
                if(m > 0 && newBlob){
                    blob = blobTable[i][m-1];
                    if(blob != null) {
                        if (blob.inRange(distanceTable[i][m])) {
                            blob.addSquare(i, m, distanceTable[i][m]);
                            newBlob = false;
                            //System.out.println("left");
                        }
                    }
                }
                if(newBlob){blob = new CurrentBlob(distanceTable[i][m], i, m, threshold);blobs.add(blob);}
                blobTable[i][m] = blob;
                //blobs.add(blob);
            }
        }
        System.out.println("Height: " + height + " width: " + width + " Blobs total: " + blobs.size());
    */}

    /**
     * A method to fill a blank table
     */
    private void setBlankBlobs(){
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                blobTable[i][m] = null;
            }
        }
    }

    /**
     * A method to fill a blank table
     */
    private void blankCTable(){
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                colourTable[i][m] = 0;
            }
        }
    }

    /**
     * This method checks to see if the blobTable has a Blob in a given
     * pixel. If it doesn't, it gives the pixel the colour black, if not,
     * it gives the table over to the pixel's Blob to fill in all of its
     * pixels with its colour
     */
    private void setColourTable(){
        blankCTable();
        for(int i = 0; i < height; i++){
            for(int m = 0; m < width; m++){
                if(colourTable[i][m] == 0){
                    CurrentBlob b = blobTable[i][m];
                    if(b != null) {
                        b.addColoursToTable(colourTable);
                    }
                    else{
                        colourTable[i][m] = 00000000;
                    }
                }
            }
        }
    }


    public ArrayList<Blob> getBlobs(){
        return blobs;
    }

    public CurrentBlob[][] getBlobTable(){
        return blobTable;
    }
    public int[][] getColourTable(){return colourTable;}




}

