import java.util.ArrayList;

/**
 * This is the SetBlobs Class. It starts off a sequence of event in creating the Blobs
 * and applying the colour to the pixels (or give Colour ints to a 2-D array that will
 * be used in colouring the pixels.
 */
public class SetBlobs {
    private ArrayList<Blob> blobs, oldBlobs;
    private int[][] colourTable;
    private double[][] distanceTable;
    private CurrentBlob[][] blobTable;
    private double threshold, min, max;
    private int width, height, currentLabel;

    /**
     * The Constructor. After initializing variables, it sends out to a method to have the Blobs set.
     * @param dTable the table of distances
     * @param blobsIn the arraylist of Blobs
     * @param thresholdIn the threshold
     * @param widthIn the table's width
     * @param heightIn the table's height
     * @param minimum the minimum distance evaluated
     * @param maximum the maximum distance evaluated
     */
    public SetBlobs(double[][]dTable, ArrayList<Blob> blobsIn, double thresholdIn, int widthIn, int heightIn,
                    double minimum, double maximum, ArrayList<Blob> oBlobs){
        min = minimum;
        max = maximum;
        blobs = blobsIn;
        oldBlobs = oBlobs;
        blobs.clear();
        height = heightIn;
        width = widthIn;
        distanceTable = dTable;
        blobTable = new CurrentBlob[heightIn][widthIn];
        threshold  = thresholdIn;
        colourTable = new int[height][width];
        currentLabel = 1;
        setBlobs();
    }

    /**
     * This method calls out to ConnectedBlob to set up all of the Blobs, and then calls
     * adjustTabels (to adjust the labels for the Blobs) and setColourTable to set the pixels' colours
     */
    private void setBlobs(){
        setBlankBlobs();
        ConnectedBlob connectedBlob = new ConnectedBlob(blobTable,distanceTable,max,min,blobs,height,width,threshold);
        blobTable = connectedBlob.getBlobTable();
        adjustTables();
        setColourTable();
        }

    /**
     * This method reads the labels from the last set of Blobs, and (if the labels were in a position
     * that corresdponds to a current Blob (is within a Blob's boundaries and is within 10 units from
     * the current Blob's centroid)) it will make that Blob's label the previous label. If a Blob finishes
     * the first section withou a label, it will get the next label from the list before the labels are
     * incremented.
     */
    private void adjustTables(){
        if(oldBlobs.size() > 0){
            for(int i = 0; i < oldBlobs.size(); i++){
                CurrentBlob b = (CurrentBlob)oldBlobs.get(i);
                CurrentBlob blob = blobTable[(int)b.getCentroid().getY()][(int)b.getCentroid().getX()];
                if (blob != null && Math.abs(Math.sqrt(Math.pow(b.getCentroid().getX(),2) +
                        Math.pow(b.getCentroid().getY(),2)) - Math.sqrt(Math.pow(blob.getCentroid().getY(), 2) +
                        Math.pow(blob.getCentroid().getX(), 2))) < 10) {
                    blob.setLabel(b.getLabel());
                    if (b.getLabel() > currentLabel) {
                        currentLabel = blob.getLabel() + 1;
                    }
                }
            }
        }
        if(blobs.size()>0){
            for(int i = 0; i < blobs.size(); i++){
                CurrentBlob blob = (CurrentBlob) blobs.get(i);
                if(blob.getLabel() == 0){
                    blob.setLabel(currentLabel);
                    currentLabel++;
                }
            }
        }
    }

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


    public ArrayList<Blob> getBlobs(){return blobs;}

    public CurrentBlob[][] getBlobTable(){return blobTable;}

    public int[][] getColourTable(){return colourTable;}
}

