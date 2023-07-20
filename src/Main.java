import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static int price(List<Map.Entry<String, Integer>> S, int index) {
        return S.get(index).getValue();
    }

    private static int func(int S, int x) {
        return (S - x) * x;
    }

    static List<Map<String, Integer>> split(Map<String, Integer> set) {
        Objects.requireNonNull(set);
        List<Map.Entry<String, Integer>> S = set.entrySet().stream().toList();
        int n = S.size();
        int sum = S.stream().map(Map.Entry::getValue).reduce(0, Integer::sum);

        int[][][] dp = new int[n][n + 1][2];
        int[][][] path = new int[n][n + 1][2];

        // base
        dp[0][0][0] = dp[0][0][1] = 0;
        path[0][0][0] = path[0][0][1] = -1;

        dp[0][1][1] = price(S, 0);
        path[0][1][1] = -1;

        for (int i = 1; i < n; ++i) {
            dp[i][0][0] = dp[i][0][1] = 0;
            path[i][0][0] = path[i][0][1] = -1;
        }

        for (int i = 1; i < n; ++i) {
            for (int k = 1; k <= i + 1; ++k) {
                // skip current item (t=0)

                for (int t = 0; t <= 1; ++t) {
                    int p = price(S, i);
                    for (int t0 = 0; t0 <= 1; ++t0) {
                        int A = p * t + dp[i-1][k - t][t0];
                        if (func(sum, dp[i][k][0]) < func(sum, A)) {
                            dp[i][k][0] = A;
                            path[i][k][0] = t0;
                        }
                    }

                }

                for (int t0 = 0; t0 <= 1; ++t0) {
                    int A = dp[i-1][k][t0];
                    if (func(sum, dp[i][k][0]) < func(sum, A)) {
                        dp[i][k][0] = A;
                        path[i][k][0] = t0;
                    }
                }
                /*{
                    int A0 = dp[i-1][k][0];
                    int A1 = dp[i-1][k][1];

                    if ((sum - A0) * A0 < (sum - A1) * A1) {
                        dp[i][k][0] = A1;
                        path[i][k][0] = 1;
                    }
                    else {
                        dp[i][k][0] = A0;
                        path[i][k][0] = 0;
                    }
                }*/

                // take current item (t=1)
                for (int t0 = 0; t0 <= 1; ++t0) {
                    int A = p + dp[i-1][k-1][t0];
                    if (func(sum, dp[i][k][1]) < func(sum, A)) {
                        dp[i][k][0] = A;
                        path[i][k][0] = t0;
                    }
                }
                /*int A0 = p + dp[i-1][k-1][0];
                int A1 = p + dp[i-1][k-1][1];

                if ((sum - A0) * A0 < (sum - A1) * A1) {
                    dp[i][k][1] = A1;
                    path[i][k][1] = 1;
                }
                else {
                    dp[i][k][1] = A0;
                    path[i][k][1] = 0;
                }*/
            }
        }

        // retrieve answer
        int i = n - 1;
        int k = n / 2;

        int A0 = dp[n-1][k][0];
        int A1 = dp[n-1][k][1];

        int t = ((sum - A0) * A0 < (sum - A1) * A1) ? 1 : 0;

        Map<String, Integer> A = new HashMap<>();

        while (k > 0) {
            boolean taken = false;
            if (t == 1) {
                A.put(S.get(i).getKey(), S.get(i).getValue());
                taken = true;
            }

            t = path[i][k][t];
            --i;
            if (taken) {
                --k;
            }
        }

        // create set B
        Map<String, Integer> B = new HashMap<>();

        for (var entry : set.entrySet()) {
            if (!A.containsKey(entry.getKey())) {
                B.put(entry.getKey(), entry.getValue());
            }
        }

        return List.of(A, B);
    }
}