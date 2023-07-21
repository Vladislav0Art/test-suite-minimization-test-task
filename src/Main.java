import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private record Pair(int i, int k) {}

    private static int price(List<Map.Entry<String, Integer>> S, int index) {
        return S.get(index).getValue();
    }

    private static int func(int S, int x) {
        return (S - x) * x;
    }

    static List<Map<String, Integer>> split(Map<String, Integer> set) {
        Objects.requireNonNull(set);
        List<Map.Entry<String, Integer>> S = set.entrySet().stream().toList();

        Map<String, Integer> A = new HashMap<>();
        Map<String, Integer> B = new HashMap<>();

        int sumA = 0;
        int sumB = 0;

        for (int i = 0; i < S.size(); ++i) {
            if (sumA < sumB || A.size() < B.size()) {
                A.put(S.get(i).getKey(), S.get(i).getValue());
                sumA += S.get(i).getValue();
            }
            else {
                B.put(S.get(i).getKey(), S.get(i).getValue());
                sumB += S.get(i).getValue();
            }
        }

        return List.of(A, B);
    }

    /*static List<Map<String, Integer>> split(Map<String, Integer> set) {
        Objects.requireNonNull(set);
        List<Map.Entry<String, Integer>> S = set.entrySet().stream().toList();
        int n = S.size();
        int sum = S.stream().map(Map.Entry::getValue).reduce(0, Integer::sum);

        int[][] dp = new int[n][n + 1];
        Pair[][] path = new Pair[n][n + 1];

        dp[0][0] = 0;
        dp[0][1] = price(S, 0);

        for (int i = 1; i < n; ++i) {
            dp[i][0] = 0;

            for (int k = 1; k <= i + 1; ++k) {
                int val1 = dp[i-1][k-1] + price(S, i);
                int val2 = dp[i-1][k];

                if (func(sum, val1) < func(sum, val2)) {
                    dp[i][k] = val2;
                    path[i][k] = new Pair(i-1, k);
                }
                else {
                    dp[i][k] = val1;
                    path[i][k] = new Pair(i-1, k-1);
                }
            }
        }

        Map<String, Integer> A = new HashMap<>();

        int k = n / 2;
        int i = n - 1;

        while (k > 0) {
            if (path[i][k].k < k) {
                A.put(S.get(i).getKey(), S.get(i).getValue());
            }

            int newi = path[i][k].i;
            int newk = path[i][k].k;

            i = newi;
            k = newk;
        }

        Map<String, Integer> B = new HashMap<>();

        for (var entry : S) {
            if (!A.containsKey(entry.getKey())) {
                B.put(entry.getKey(), entry.getValue());
            }
        }

        return List.of(A, B);
    }*/
}