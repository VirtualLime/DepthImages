/**
 * This is the Quad Class. It is used to contain four different pieces and return them
 */
public class Quad {
    private int first, second;
    private CurrentBlob blob;
    private double fourth;

    /**
     * The constructor takes the values in and transfers them to the ivars. The description below
     * is for this particular use.
     * @param firstIn Table's y
     * @param secondIn Table's x
     * @param blobIn Blob
     * @param fourthIn distance value of pixel
     */
    public Quad(int firstIn, int secondIn, CurrentBlob blobIn, double fourthIn){
        first = firstIn;
        second = secondIn;
        blob = blobIn;
        fourth = fourthIn;
    }

    public int getFirst(){return first;}
    public int getSecond(){return second;}
    public CurrentBlob getBlob(){return blob;}
    public double getFourth(){return fourth;}
}
