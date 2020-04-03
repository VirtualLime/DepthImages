import java.awt.*;
import java.util.ArrayList;

/**
 * The CurrentBob class. It implements Blob. It determines the centroid of the Blob, as well as
 * the colour of the Blob (based on the label of the Blob).
 */
public class CurrentBlob implements Blob {
    private ArrayList<Integer> xvalues, yvalues;
    private int  label, colour, averagex, averagey;
    private Point p;
    private int[] COLORS = {//Copied from Image Processor
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
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     */
    public CurrentBlob(int x, int y){
        label = 0;
        p = new Point(x,y);
        xvalues = new ArrayList<>();
        yvalues = new ArrayList<>();
        xvalues.add(x);
        yvalues.add(y);
        colour = setColour();
    }

    /**
     * This sets the colour of the Blob based on the label value. Went with (label + 1)%10
     * as the colour for label%10 was a bit bland for a colour that should constantly be there
     * @return
     */
    public int setColour(){
        colour = COLORS[(label + 1)%10];
        return colour;
    }

    public void setColour(int c){colour = c;}
    public int getColour(){return colour;}

    @Override
    public Point getCentroid() {return p;}

    @Override
    public int getLabel() {return label;}

    @Override
    public void setLabel(int label) {
        this.label = label;
        setColour();
    }

    /**
     * This method takes a pixels coordinates and the distance being displayed, then sends
     * out to adjust the Point
     * @param x x-coordinate of the pixel
     * @param y y-coordinate of the pixel
     */
    public void addSquare(int x, int y){
        xvalues.add(x);
        yvalues.add(y);
        findP();
    }

    /**
     * This method takes ArrayLists of x and y values, and adds them to the Class' ArrayLists
     * then sends out to recalculate the Point
     * @param x ArrayList of xvalues in
     * @param y ArrayList of yvalues in
     */
    public void addArrays(ArrayList<Integer> x, ArrayList<Integer> y){
        for(int i = 0; i < x.size(); i++){
            xvalues.add(x.get(i));
            yvalues.add(y.get(i));
        }
        findP();
    }

    /**
     * This method finds the Point (where label will be afixed) for the Blob. It does this by
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
