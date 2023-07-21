import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private record Pair(int s, int k) {}

    private static int price(List<Map.Entry<String, Integer>> S, int index) {
        return S.get(index).getValue();
    }

    /**
     *
     * @param dp - array with data
     * @param n - size of 1st dim of `dp`
     * @param totalSum - sum of all items of `S`
     * @param k - require number of items
     * @return best sum `s` of items in A such that
    its |price(A) - price(B)| -> min and size(A) == k.
     */
    private static int findBestSum(boolean[][][] dp, int n, int totalSum, int k) {
        int diff = -1;
        int selectedS = -1;

        for (int s = 0; s <= totalSum; ++s) {
            if (dp[n - 1][s][k]) {
                if (diff == -1 || Math.abs(totalSum - 2 * s) < diff) {
                    diff = Math.abs(totalSum - 2 * s);
                    selectedS = s;
                }
            }
        }

        return selectedS;
    }

    static List<Map<String, Integer>> split(Map<String, Integer> set) {
        Objects.requireNonNull(set);

        // construct list and retrieve size and total sum
        List<Map.Entry<String, Integer>> S = set.entrySet().stream().toList();

        int n = S.size();
        int s = S.stream().map(Map.Entry::getValue).reduce(0, Integer::sum);

        // init arrays and base cases
        boolean[][][] dp = new boolean[n + 1][s + 1][n + 1];
        Pair[][][] path = new Pair[n + 1][s + 1][n + 1];

        dp[0][0][0] = true;
        path[0][0][0] = new Pair(0, 0);
        dp[0][price(S, 0)][1] = true;
        path[0][price(S, 0)][1] = new Pair(0, 0);

        // iterate over prefix i
        for (int i = 0; i < n; ++i) {
            int p = price(S, i);

            // iterate over sum s (here call j)
            for (int j = 0; j <= s - p; ++j) {
                // if current sum j is 0 then update dp since sum of 0 could be collected via 0 items
                if (j == 0) {
                    dp[i][j][0] = true;
                    path[i][j][0] = new Pair(0, 0);
                }

                // iterate over number of items of set A
                for (int k = 0; k < n; ++k) {
                    // if current state is reachable then update state
                    // that could be reached without i-th item and state
                    // that could be reach with i-th item
                    if (dp[i][j][k]) {
                        dp[i+1][j][k] = true;
                        path[i+1][j][k] = new Pair(j, k);

                        dp[i+1][j+p][k+1] = true;
                        path[i+1][j+p][k+1] = new Pair(j, k);
                    }
                }
            }
        }

        // construct answer
        Map<String, Integer> A = new HashMap<>();
        Map<String, Integer> B = new HashMap<>();

        int i = n;
        int k = n / 2;
        // find sum of items of set A so that |price(A) - price(B)| -> min
        // and size of A is k
        int j = findBestSum(dp, n, s, k);

        // collect all items into set A
        while(k > 0) {
            int index = Math.max(i-1, 0);
            int taken = 0;

            // (i-1)-th item was taken into set A if the transition
            // used price of the item
            boolean currentItemSelected = (path[i][j][k].s + price(S, index) == j);
            if (currentItemSelected) {
                A.put(S.get(index).getKey(), S.get(index).getValue());
                taken = 1;
            }

            // proceed to the next state
            int newi = i - 1;
            int newj = path[i][j][k].s;
            int newk = k - taken;

            i = newi;
            j = newj;
            k = newk;
        }

        // insert all the rest items into B
        for (var entry : S) {
            if (!A.containsKey(entry.getKey())) {
                B.put(entry.getKey(), entry.getValue());
            }
        }

        return List.of(A, B);
    }
}