import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;

/**
 * The CurrentBob class. It implements Blob, but also adds methods helpful for
 * determining if points are within the Blob.
 */
public class CurrentBlob implements Blob {
    private ArrayList<Integer> xvalues, yvalues;
    private int  label, colour, averagex, averagey;
    private double min,max,rangedMax,rangedMin;
    private double threshold;
    private Point p;
    private int[] COLORS = {
            0x80AA80, //green
            0xEEE8AA, //yellow
            0xDC143C, //red
            0xFF8C00, //orange
            0xCD5C5C, //reddish
            0xFA8072, //more red
            0x808000, //dark greenish
            0xFFD700, //yellow
            0x6B8E23, //dark green
            0xADFF2F}; //light green

    /**
     * This is the Constructor for CurrentBlob. It accepts value, x, y, then adds the x and
     * y values to their arrayLists, and sets the colour for the Blob.
     * @param value This is the distance for the pixel
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @param t the threshold value
     */
    public CurrentBlob(double value, int x, int y, double t){
        label = 0;
        p = new Point(x,y);
        min = value;
        max = value;
        threshold = t;
        rangedMax = max + threshold;
        rangedMin = min - threshold;
        xvalues = new ArrayList<>();
        yvalues = new ArrayList<>();
        xvalues.add(x);
        yvalues.add(y);
        colour = setColour();
    }

    /**
     * A currently unused method to get the String of a single pixel's x and y
     * @param index the position of the ArrayLists
     * @return the pixel's location
     */
    public String getXandYValues(int index){
        String s = "";
        s += xvalues.get(index) + yvalues.get(index);
        return s;
    }

    public int setColour(){
        //return COLORS[(int)(Math.random()*10)];
        colour = COLORS[(label + 1)%10];
        return colour;
    }
    public void setColour(int c){colour = c;}
    public int getColour(){return colour;}
    //public boolean inRange(double value){return value <= min + threshold && value >= max - threshold;}
    //public boolean inRange(double value){return Math.abs(value-min) <= threshold && Math.abs(value-max)<= threshold;}

    /**
     * This method checks to see if the given value is within the accepted range
     * by comparing it to the calculated current absolute max/min
     * @param value the point distance being checked
     * @return returns whether or not it is in range
     */
    public boolean inRange(double value) {//return value >= rangedMin && value <= rangedMax;}
        if(value > rangedMax || value < rangedMin){
            //System.out.println("rMax: " + rangedMax + " rMin: " + rangedMin + " value: " + value);
            return false;

        }
        return true;
    }

    /**
     * This method adjusts the max, min, ranged max, and rangedmin based on current
     * values being added
     * @param value
     */
    private void setExtremes(double value){
        if(value > max){max = value;}
        if(value < min){min = value;}
        double buffer = threshold - (max - min);
        rangedMax = max + buffer;
        rangedMin = min - buffer;
    }

    @Override
    public Point getCentroid() {
        return p;
    }

    @Override
    public int getLabel() {
        return label;
    }

    @Override
    public void setLabel(int label) {
        this.label = label;
        setColour();
    }

    /**
     * An unused method... probably to be deleted
     * @param x
     * @param y
     */
    public void setXandY(ArrayList x, ArrayList y){
        xvalues = x;
        yvalues = y;
        findP();
    }

    /**
     * This method takes a pixels coordinates and the distance being displayed, adjusts the
     * Point, and makes other adjustments to the interior values as necessary
     * @param x x-coordinate of the pixel
     * @param y y-coordinate of the pixel
     * @param value distance value
     */
    public void addSquare(int x, int y, double value){
        xvalues.add(x);
        yvalues.add(y);
        findP();
        //setExtremes(value);
    }

    public void addArrays(ArrayList<Integer> x, ArrayList<Integer> y){
        for(int i = 0; i < x.size(); i++){
            xvalues.add(x.get(i));
            yvalues.add(y.get(i));
        }
        findP();
    }

    /**
     * This method finds hte Point (where label will be afixed) for the Blob. It does this by
     * calculating the (int)(average) of the x and y coordinates
     */
    private void findP(){
        int temp = 0;
        for(int i = 0 ; i < xvalues.size(); i++){temp += xvalues.get(i);}
        averagex = temp / xvalues.size();
        temp = 0;
        for(int i = 0 ; i < yvalues.size(); i++){temp += yvalues.get(i);}
        averagey = temp / yvalues.size();
        p = new Point(averagex,averagey);
    }

    /**
     * This method takes all of the x and y coordinates and applies the Blob's colour to
     * the grid sections that have that colour
     * @param cTable
     */
    public void addColoursToTable(int[][] cTable){
        for(int i = 0; i < xvalues.size(); i++){
            cTable[yvalues.get(i)][xvalues.get(i)] = colour;
        }
    }
}
