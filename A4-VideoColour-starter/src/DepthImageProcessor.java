import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Complete this class as part of the assignment
 */
public class DepthImageProcessor implements ImageProcessor{
    private String fileName;
    //private SetGraph setGraph;
    private double threshold;
    private int[][] converted;
    private ArrayList<Blob> blobs;

    private CurrentBlob[][] currentBlobTable;
    private SetBlobs setBlobs;

    private double minDistance;
    private double maxDistance;

    //private double highestDistance, lowestDistance;

    public DepthImageProcessor(){
        blobs = new ArrayList<>();
    }


    @Override
    public void processFile(String fileString) { //do not throw anything here{
        this.fileName = fileString;
        /*
        Need this call otherwise the tests will not pass.
         */
        getRawImg();
    }

    @Override
    /**
     * Gets the raw image of the text files in order of number
     */
    public double[][] getRawImg() {
        int counterW = 0;
        int counterH = 0;
        //int width = getWidth();
        //int height = getHeight();

        Scanner in = initializeScanner();
        //in.nextInt();
        //in.nextInt();
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
        //setBlobs = new SetBlobs(rawImg, blobs, threshold);
        //blobs = setBlobs.getBlobs();
        //currentBlobTable = setBlobs.getBlobTable();
        /**
        Processes a graph that takes width and height of each text file as well as a 2d double array of the distances
         */
        //processGraph(width, height, rawImg);
        setBlobs = new SetBlobs(rawImg, blobs, threshold, width, height, minDistance, maxDistance);
        return rawImg;
    }

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


    public void processGraph(int width, int height, double[][] rawImg){
        /**
         * Flattening a 2d array into a 1d array if there is a way to optimize the code I think it is here
         */
        ArrayList<Double> list = new ArrayList<Double>();
        double[] flatten = new double[width*height];
        for(int i = 0; i < rawImg.length; i++){
            for(int j = 0; j < rawImg[i].length; j++){
                list.add(rawImg[i][j]);
            }
        }


        for(int i = 0; i < list.size(); i++){
            flatten[i] = list.get(i);
        }

        //Creating an empty graph with all of the vertices as pixels
       // Graph g = new Graph(width*height);


        /**
         * This call is to add edges to the graph if a pixel is adjacent and it meets the threshold
         */
        //setGraph = new SetGraph(flatten, g, threshold, height, width, minDistance, maxDistance);
        //Gets the modified graph back
        //Graph modifiedGraph = setGraph.getGraph();
        //System.out.println(modifiedGraph.toString());
        //processComponent(modifiedGraph, width, height);
    }



    /*public void processComponent(Graph modifiedGraph, int width, int height){

        /**
         * The connected components class is a modified version of the class that appears in the slides
         */

        /**
         * Puts the graph into a connected components class that separates the graph into distinct components
         */
        //ConnectedComponents components = new ConnectedComponents(modifiedGraph);
        //Sets the colors in the connected components
        //components.funnelColors(COLORS);

        //Assigns each group to its own color
        Map<Integer, Integer> groupToColor = new HashMap<Integer, Integer>();

        //Assigns the map to the result of the assignment of the map in connected components
        //groupToColor = components.transformIntoColor();

        //Is the array returned from the connected components it is the id[] the id will show connected components as the same number
        //int[] componentArray = components.returnArray();
        //Counts the number of connected components
        //int count = components.count();


        //Processes the connected components this is for the labels
        //int[] processArray = componentArray;
        int x = 0;
        int y = 0;
        //double[] times = new double[count];
        //double[] xStorage = new double[count];
        //double[] yStorage = new double[count];
        //for(int i = 0; i < processArray.length; i++){
        //    xStorage[processArray[i]] = xStorage[processArray[i]] + x;
        //    yStorage[processArray[i]] = yStorage[processArray[i]] + y;
       //     times[processArray[i]] = times[processArray[i]] + 1;
            /*if((x + 1) % width == 0){
                x = -1;
                y = y + 1;
            }
            x++;
        }

        //Point[] centroid = new Point[count];

        /**
         * Processes the centroids the id is the component
         */
        /*for(int i = 0; i < times.length; i++){
            if(times[i] == 1){
                continue;
            }
            int pointX = (int)Math.floor(xStorage[i]/times[i]);
            int pointY = (int)Math.floor(yStorage[i]/times[i]);
            Point point = new Point(pointX, pointY);
            centroid[i] = point;
        }*/

        /**
         * Gets the blobs and assigns it to the private variable.
         */
       /* ArrayList<Blob> localBlobs = new ArrayList<Blob>();
        for(int i = 0; i < centroid.length; i++){
            if(centroid[i] == null){
                continue;
            }
            BlobO blob = new BlobO(centroid[i], (int)times[i]);
            localBlobs.add(blob);
        }

        this.blobs = localBlobs;


        //System.out.print(components.count());
        /**
         * This assigns the color to each component, if there is an individual component it is ignored and painted black
         */

       /* for(int i = 0; i < componentArray.length; i++){
            if(i != componentArray.length - 1 && componentArray[i] != componentArray[i + 1]){
                componentArray[i] = 000000;
            }
            else{
                int color = groupToColor.get(componentArray[i]);
                componentArray[i] = color;
            }
        }


        /**
        * This converts the array back to a 2d array there is probably another performance issue here.
         */

        /*int[][] convertBack = new int[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                convertBack[i][j] = componentArray[j%width + i*width];
            }
        }


        this.converted = convertBack;
    }*/

    @Override
    public int[][] getColorImg() {return setBlobs.getColourTable();
        //return converted;
        }

    @Override
    public void setThreshold(double thres) {
        this.threshold = thres;
    }



    @Override
    public void setMinDist(double minDist) {
        this.minDistance = minDist;
    }

    @Override
    public void setMaxDist(double maxDist){
        this.maxDistance = maxDist;

    }

    public void printArray(int[][] arr){
        System.out.println(Arrays.deepToString(arr).replace("], ", "]\n"));
    }

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
    public ArrayList<Blob> getBlobs() {
            //System.out.println("blobs called: " + blobs.size());
            return blobs;
    }










































}
