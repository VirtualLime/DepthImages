import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Complete this class as part of the assignment.
 * This is the DepthImageProcessor Class. It was provided for the assignment and then adjusted accordingly.
 * It has ivars with descriptive names that are dealt with thoughout the class and often referenced in other
 * classes.
 */
public class DepthImageProcessor implements ImageProcessor{
    private String fileName;
    private double threshold;
    private ArrayList<Blob> blobs;
    private ArrayList<Blob> oldBlobs;
    private SetBlobs setBlobs;
    private double minDistance;
    private double maxDistance;

    /**
     * The Constructor for this class only initializes the ArrayLists. This is done to prevent
     * errors. The rest being set to 0 or  "" won't be an issue.
     */
    public DepthImageProcessor(){
        blobs = new ArrayList<>();
        oldBlobs = new ArrayList<>();
    }


    /**
     * The instructions were to not put anything in here. Unfortunately, filename is filled here,
     * and the tests don't call for getRawImage elsewhere, so it is necessary to have this here as well.
     * @param fileString this is the name of the file containing the raw image information
     */
    @Override
    public void processFile(String fileString) { //do not throw anything here
        this.fileName = fileString;
        getRawImg();
    }


    /**
     * This class sends out to have the Scanner initialized for the file, reads the width, height,
     * and distances from the file, places the distances in a 2-D array (in a usual table fashion)
     * replaces "oldBlobs" with the most recent ArrayList of Blobs (so it now contains what will
     * be the old Blobs), sends out to have the new set of Blobs create (with SetBlobs), and
     * returns the 2-D array of RawImg.
     */
    @Override
    public double[][] getRawImg() {
        int counterW = 0;
        int counterH = 0;
        Scanner in = initializeScanner();
        int width = in.nextInt();
        int height = in.nextInt();
        double[][] rawImg = new double[height][width];
        while(in.hasNext()){
            rawImg[counterH][counterW] = in.nextDouble();
            if(counterW == width-1){
                counterW = -1;
                counterH++;
            }
            counterW++;
        }
        in.close();
        oldBlobs.clear();
        if(blobs.size()>0){
            for(int i = 0; i < blobs.size(); i++){
                oldBlobs.add(blobs.get(i));

            }
        }
        setBlobs = new SetBlobs(rawImg, blobs, threshold, width, height, minDistance, maxDistance, oldBlobs);
        return rawImg;
    }

    /**
     * This is a method provided with the assignment. It still works, but wasn't seen as necessary
     * for the assignment to work (and ends up making unnecessary method calls) and so isn't used.
     * @return width of the file array
     */
    @Override
    public int getWidth() {
        Scanner in = initializeScanner();
        int width = 0;
        while (in.hasNext()) {
            width = in.nextInt();
            break;
        }
        in.close();
        return width;
    }

    /**
     * This is a method provided with the assignment. It still works, but wasn't seen as necessary
     * for the assignment to work (and ends up making unnecessary method calls) and so isn't used.
     * @return height of the file array
     */
    @Override
    public int getHeight() {
        Scanner in = initializeScanner();
        int height = 0;
        while(in.hasNext()){
            int width = in.nextInt();
            height = in.nextInt();
            break;
        }
        in.close();
        return height;
    }

    @Override
    public int[][] getColorImg() {return setBlobs.getColourTable();}

    @Override
    public void setThreshold(double thres) {
        this.threshold = thres;
    }

    @Override
    public void setMinDist(double minDist) {
        this.minDistance = minDist;
    }

    @Override
    public void setMaxDist(double maxDist){this.maxDistance = maxDist;}

    /**
     * This is a method the Initializes a Scanner for use in various methos. It sets the Scanner up to read
     * from the current image test file.
     * @return
     */
    public Scanner initializeScanner(){
        File file = new File(fileName);
        try {
            Scanner in = new Scanner(file);
            return in;
        }catch(FileNotFoundException e){
            System.out.println("The file was not found");
        }
        return null;
    }

    @Override
    public ArrayList<Blob> getBlobs() {return blobs;}
}
