import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private record Pair(int s, int k) {}

    private static int price(List<Map.Entry<String, Integer>> S, int index) {
        return S.get(index).getValue();
    }

    private static int func(int S, int x) {
        return (S - x) * x;
    }

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
        List<Map.Entry<String, Integer>> S = set.entrySet().stream().toList();

        int n = S.size();
        int s = S.stream().map(Map.Entry::getValue).reduce(0, Integer::sum);

        boolean[][][] dp = new boolean[n][s + 1][n + 1];
        Pair[][][] path = new Pair[n][s + 1][n + 1];

        dp[0][0][0] = true;
        path[0][0][0] = new Pair(0, 0);
        dp[0][price(S, 0)][1] = true;
        path[0][price(S, 0)][1] = new Pair(0, 0);

        for (int i = 1; i < n; ++i) {
            int p = price(S, i);

            for (int j = s; j >= p; --j) {
                if (j == 0) {
                    dp[i][j][0] = true;
                    path[i][j][0] = new Pair(0, 0);
                }

                for (int k = 1; k <= n; ++k) {
                    // dp[i][j][k] |= (dp[i-1][j][k] || dp[i-1][j-p][k-1]);
                    if (dp[i-1][j][k]) {
                        dp[i][j][k] = true;
                        path[i][j][k] = new Pair(j, k);
                    }
                    else if (dp[i-1][j-p][k-1]) {
                        dp[i][j][k] = true;
                        path[i][j][k] = new Pair(j-p, k-1);
                    }
                }
            }
        }

        Map<String, Integer> A = new HashMap<>();
        Map<String, Integer> B = new HashMap<>();

        int i = n - 1;
        int k = n / 2;
        int j = findBestSum(dp, n, s, k);

        while(k > 0) {
            boolean currentItemSelected = (path[i][j][k].s + price(S, i) == j);
            if (currentItemSelected) {
                A.put(S.get(i).getKey(), S.get(i).getValue());
            }

            int newi = i - 1;
            int newj = path[i][j][k].s;
            int newk = path[i][j][k].k;

            i = newi;
            j = newj;
            k = newk;
        }

        for (var entry : S) {
            if (!A.containsKey(entry.getKey())) {
                B.put(entry.getKey(), entry.getValue());
            }
        }

        return List.of(A, B);
    }
}