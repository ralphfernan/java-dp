import java.util.*;
import java.util.stream.Collectors;

class SolveSmaller {
    public static void main(String[] args) {

        int[] Ks = {13, 68, 109};
        int[][] Ns = {{1,5,12}, {1,14,17,38}, {13,17,100,110,120}};
        int[] expected = {2,4,-1};
        for (int i = 0; i< 3; i++) {

            int soln = solve(Ks[i],Ns[i]);
            assert soln == expected[i];
            System.out.println("OK");
        }
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
    //static List<Integer> solve(int K, List<Integer> Nums) {
    static int solve(int K, int[] Nums) {
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
            //List<Integer> histVals = History.flattenValues(history, new ArrayList<>());
            //System.out.printf("Got K with depth %d, values %s\n" , history.depth, histVals);
            //System.out.printf("Minimum number of elements to sum to K is %d\n", history.depth);
            return history.depth;
        }
        //return Collections.emptyList();
        return -1; // no solution
    }
}
