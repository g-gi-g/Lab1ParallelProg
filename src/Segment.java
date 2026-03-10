import java.io.Serializable;

public class Segment implements Serializable {
    private int start;
    private int end;
    private int[] basePrimes;

    public Segment(int start, int end, int[] basePrimes) {
        this.start = start;
        this.end = end;
        this.basePrimes = basePrimes;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int[] getBasePrimes() {
        return basePrimes;
    }
}

