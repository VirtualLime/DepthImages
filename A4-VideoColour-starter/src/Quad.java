public class Quad {
    private int first, second;
    private CurrentBlob blob;
    private double fourth;

    public Quad(int f, int s, CurrentBlob c, double p){
        first = f;
        second = s;
        blob = c;
        fourth = p;
    }

    public int getFirst(){return first;}
    public int getSecond(){return second;}
    public CurrentBlob getBlob(){return blob;}
    public double getFourth(){return fourth;}
}
