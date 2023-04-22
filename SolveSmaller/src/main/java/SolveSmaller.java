import java.util.*;
import java.util.stream.Collectors;

class SolveSmaller {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Enter an integer for K followed by a comma-separated list for N.");
        }

        int K = Integer.valueOf(args[0]);
        List<Integer> N = Arrays.asList(args[1].split(",")).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        List<Integer> soln1 = solve(K, N);
        System.out.printf("solution for %d is %s", K, soln1.toString());
    }
    static class History {
        History ancestor;
        int value;
        int depth;
        History(History anc, int value) {
            this.ancestor = anc;
            this.value = value;
            this.depth = anc == null ? 1 : 1+anc.depth;
        }

        @Override
        public String toString() {
            return "History{" +
                    " value=" + value +
                    ", depth=" + depth +
                    '}';
        }

        static List<Integer> flattenValues(History ancestor, List<Integer> acc) {
            if(ancestor == null) {
                return acc;
            }
            acc.add(ancestor.value);
            return flattenValues(ancestor.ancestor, acc);
        }
    }
    static List<Integer> solve(int K, List<Integer> Nums) {
        HashSet<Integer> ksToSolve = new HashSet<>(Arrays.asList(K));
        HashMap<Integer, History> soln = new HashMap<>();
        HashSet<Integer> nextKsToSolve = new HashSet<>();
        while(!ksToSolve.isEmpty()) {
            nextKsToSolve = new HashSet<>();
            //System.out.println("ksToSolve"+ksToSolve);
            //System.out.println("soln is "+soln.keySet());
            for (int k:ksToSolve) {
                //System.out.println("k is"+k);

                for(int n: Nums) {
                    if (k - n >= 0) {
                        // we already solved K - k
                        int prevN = K - k;
                        History prevSoln = null;
                        if (prevN > 0) {
                            prevSoln = soln.get(prevN);
                        }
                        // we may already have  prevN+n
                        // If so, we don't need to store a longer solution.
                        if (!soln.containsKey(prevN+n)) {
                            //System.out.printf("Adding %d to soln.\n", prevN+n);
                            soln.put(prevN + n, new History(prevSoln, n));
                        } else {
                            //System.out.printf("skipping adding %d to solution\n", prevN+n);
                        }
                        if (k > n) nextKsToSolve.add(k-n);

                    }
                }
            }
            ksToSolve = nextKsToSolve; // terminates loop if this is empty
        } // end while
        if (soln.containsKey(K)) {

            History history = soln.get(K);
            List<Integer> histVals = History.flattenValues(history, new ArrayList<>());
            System.out.printf("Got K with depth %d, values %s\n" , history.depth, histVals);
            System.out.printf("Minimum number of elements to sum to K is %d\n", history.depth);
            return histVals;
        }
        return Collections.emptyList();
    }
}
