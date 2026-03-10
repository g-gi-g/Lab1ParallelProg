import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import parcs.*;

public class SieveWorker implements AM {
    @Override
    public void run(AMInfo info) {
        Segment segment = (Segment) info.parent.readObject();
        int start = segment.getStart();
        int end = segment.getEnd();
        int[] basePrimes = segment.getBasePrimes();

        int length = end - start + 1;
        boolean[] isPrime = new boolean[length];
        for (int i = 0; i < length; i++) {
            isPrime[i] = true;
        }

        for (int p : basePrimes) {
            if (p * (long) p > end) {
                break;
            }
            int first = (start + p - 1) / p * p;
            if (first == p) {
                first += p;
            }
            for (int x = first; x <= end; x += p) {
                isPrime[x - start] = false;
            }
        }

        ArrayList<Integer> primes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int val = start + i;
            if (val >= 2 && isPrime[i]) {
                primes.add(val);
            }
        }

        info.parent.write((Serializable) primes);
    }
}
