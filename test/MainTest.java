import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private int sum(Map<String, Integer> S) {
        int sum = 0;
        for (var entry : S.entrySet()) {
            sum += entry.getValue();
        }
        return sum;
    }

    private List<Map<String, Integer>> slowSolution(
            Map<String, Integer> S,
            List<Map.Entry<String, Integer>> entries,
            int sumS,
            Map<String, Integer> A,
            int sumA,
            int pos) {
        if (pos >= S.size()) {
            return null;
        }

        if (A.size() == S.size() / 2) {
            Map<String, Integer> B = new HashMap<>();
            for (var entry : S.entrySet()) {
                if (!A.containsKey(entry.getKey())) {
                    B.put(entry.getKey(), entry.getValue());
                }
            }
            return List.of(new HashMap<>(A), B);
        }

        // skip current item
        var result1 = slowSolution(S, entries, sumS, A, sumA, pos + 1);
        // take current item
        A.put(entries.get(pos).getKey(), entries.get(pos).getValue());
        var result2 = slowSolution(S, entries, sumS, A, sumA + entries.get(pos).getValue(), pos + 1);
        A.remove(entries.get(pos).getKey());

        if (result1 == null || result2 == null) {
            return result1 == null ? result2 : result1;
        }

        int R1 = Math.abs(sum(result1.get(0)) - sum(result1.get(1)));
        int R2 = Math.abs(sum(result2.get(0)) - sum(result2.get(1)));

        return R1 < R2 ? result1 : result2;
    }

    private List<Map<String, Integer>> slowSolution(Map<String, Integer> S) {
        var entries = S.entrySet().stream().toList();
        int sumS = entries.stream().map(Map.Entry::getValue).reduce(0, Integer::sum);
        Map<String, Integer> A = new HashMap<>();
        int sumA = 0;
        int pos = 0;

        return slowSolution(S, entries, sumS, A, sumA, pos);
    }

    @Test
    void splitTwoElements() {
        var entryA = Map.entry("a", 1);
        var entryB = Map.entry("b", 2);

        Map<String, Integer> S = Map.ofEntries(entryA, entryB);

        List<Map<String, Integer>> result = Main.split(S);

        assertNotNull(result);
        assertEquals(2, result.size());

        Map<String, Integer> A = result.get(0);
        Map<String, Integer> B = result.get(1);

        assertEquals(1, A.size());
        assertEquals(1, B.size());

        if (!A.containsKey(entryA.getKey())) {
            var tmp = A;
            A = B;
            B = tmp;
        }

        // A must contain entryA
        assertTrue(A.containsKey(entryA.getKey()));
        assertEquals(A.get(entryA.getKey()), entryA.getValue());

        // B must contain entryB
        assertTrue(B.containsKey(entryB.getKey()));
        assertEquals(B.get(entryB.getKey()), entryB.getValue());
    }

    @Test
    void splitEvenNumberOfElements() {
        for (int iteration = 0; iteration < 1000; ++iteration) {
            Map<String, Integer> S = Map.ofEntries(
                    Map.entry("a", 10),
                    Map.entry("b", 23),
                    Map.entry("c", 4),
                    Map.entry("d", 1),
                    Map.entry("e", 15),
                    Map.entry("f", 7)
            );

            List<Map<String, Integer>> actual = Main.split(S);
            int actualDiff = Math.abs(sum(actual.get(0)) - sum(actual.get(1)));

            assertNotNull(actual);
            assertEquals(2, actual.size());

            Map<String, Integer> A = actual.get(0);
            Map<String, Integer> B = actual.get(1);

            assertEquals(S.size() / 2, A.size());
            assertEquals(S.size() / 2, B.size());

            List<Map<String, Integer>> expected = slowSolution(S);
            int expectedDiff = Math.abs(sum(expected.get(0)) - sum(expected.get(1)));

            assertEquals(expectedDiff, actualDiff);
        }
    }

    @Test
    void splitOddNumberOfElements() {
        for (int iteration = 0; iteration < 1000; ++iteration) {
            Map<String, Integer> S = Map.ofEntries(
                    Map.entry("a", 10),
                    Map.entry("b", 23),
                    Map.entry("c", 4),
                    Map.entry("d", 1),
                    Map.entry("e", 15),
                    Map.entry("f", 7),
                    Map.entry("g", 27)
            );

            List<Map<String, Integer>> actual = Main.split(S);
            int actualDiff = Math.abs(sum(actual.get(0)) - sum(actual.get(1)));

            assertNotNull(actual);
            assertEquals(2, actual.size());

            Map<String, Integer> A = actual.get(0);
            Map<String, Integer> B = actual.get(1);

            if (A.size() < B.size()) {
                var tmp = A;
                A = B;
                B = tmp;
            }

            assertEquals(S.size() / 2 + 1, A.size());
            assertEquals(S.size() / 2, B.size());

            List<Map<String, Integer>> expected = slowSolution(S);
            int expectedDiff = Math.abs(sum(expected.get(0)) - sum(expected.get(1)));

            assertEquals(expectedDiff, actualDiff);
        }
    }

    @Test
    void splitStressTestEvenNumber() {
        Random rnd = new Random();

        for (int iteration = 0; iteration < 10; ++iteration) {
            int n = 2 * rnd.nextInt(1, 5);
            Map<String, Integer> S = new HashMap<>();

            for (int i = 0; i < n; ++i) {
                S.put(Integer.toString(i), rnd.nextInt(1, 10));
            }

            System.out.println(S);

            List<Map<String, Integer>> actual = Main.split(S);
            int actualDiff = Math.abs(sum(actual.get(0)) - sum(actual.get(1)));

            assertNotNull(actual);
            assertEquals(2, actual.size());

            Map<String, Integer> A = actual.get(0);
            Map<String, Integer> B = actual.get(1);

            assertEquals(S.size() / 2, A.size());
            assertEquals(S.size() / 2, B.size());

            List<Map<String, Integer>> expected = slowSolution(S);
            int expectedDiff = Math.abs(sum(expected.get(0)) - sum(expected.get(1)));

            assertEquals(expectedDiff, actualDiff);
        }
    }
}