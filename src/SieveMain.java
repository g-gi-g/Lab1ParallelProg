import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import parcs.*;

public class SieveMain {
    public static void main(String[] args) throws Exception {
        task curtask = new task();
        String workerJar = curtask.findFile("SieveWorker.jar");
        curtask.addJarFile(workerJar != null ? workerJar : "SieveWorker.jar");

        String inputPath = curtask.findFile("input");
        int n;
        int parts;
        try (Scanner sc = new Scanner(new File(inputPath))) {
            n = sc.nextInt();
            parts = sc.nextInt();
        }

        if (n < 2 || parts < 1) {
            System.out.println("Nothing to do: n < 2 or parts < 1");
            curtask.end();
            return;
        }

        int[] basePrimes = computeBasePrimes(n);

        long startTime = System.nanoTime();

        AMInfo info = new AMInfo(curtask, null);
        List<channel> chans = new ArrayList<>();

        int totalNumbers = n - 1; // from 2 to n
        int segmentSize = (totalNumbers + parts - 1) / parts;
        int currentStart = 2;

        while (currentStart <= n) {
            int currentEnd = Math.min(n, currentStart + segmentSize - 1);
            point p = info.createPoint();
            channel c = p.createChannel();
            p.execute("SieveWorker");
            c.write(new Segment(currentStart, currentEnd, basePrimes));
            chans.add(c);
            currentStart = currentEnd + 1;
        }

        List<Integer> allPrimes = new ArrayList<>();
        for (channel c : chans) {
            @SuppressWarnings("unchecked")
            List<Integer> partPrimes = (List<Integer>) c.readObject();
            allPrimes.addAll(partPrimes);
        }

        long endTime = System.nanoTime();
        double elapsedMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("Primes up to " + n + ":");
        for (int p : allPrimes) {
            System.out.print(p + " ");
        }
        System.out.println();
        System.out.println("Total primes: " + allPrimes.size());
        System.out.println("Time (parallel): " + elapsedMs + " ms");

        curtask.end();
    }

    private static int[] computeBasePrimes(int n) {
        int limit = (int) Math.sqrt(n);
        boolean[] isPrime = new boolean[limit + 1];
        for (int i = 2; i <= limit; i++) {
            isPrime[i] = true;
        }
        for (int p = 2; p * p <= limit; p++) {
            if (isPrime[p]) {
                for (int x = p * p; x <= limit; x += p) {
                    isPrime[x] = false;
                }
            }
        }
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }
        int[] res = new int[primes.size()];
        for (int i = 0; i < primes.size(); i++) {
            res[i] = primes.get(i);
        }
        return res;
    }
}

