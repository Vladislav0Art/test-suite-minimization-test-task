# Test suite minimization for SE/CT

## Task 1

### Question:

What do you think are pros and cons of the two proposed ways of addressing the problem?

### Answer:

1. **Implementing a test suite minimization module:**
   1. Pros:
      1. The approach does not require rewriting or modifying of the existing analysis algorithm.
      2. The overall workflow is easier to understand since two potentially complex algorithms (analysis algorithm and test suite minimization) are decoupled.
   1. Cons:
      1. Possible efficiency issues since the overall idea implies first to create test suites via the analysis, and after that partially remove the created results.
      1. Possible loss of test coverage; minimization might remove the cases that may be considered redundant by the implemented functionality but in fact contribute into covering the edge cases.
1. **Improving the analysis process of Kex itself:**
   1. Pros:
      1. The resulting codebase is clear and concise since we do not implement additional complex functionality but improving the existing one.
      1. If the enhanced implementation is correct the resulting test coverage will be the same with fewer amount of tests, i.e. we do not lose tests that cover edge cases.
   1. Cons:
      1. The enhancement of the analysis algorithm may be extremely hard and may require significant amount of time for both research and implementation.
      1. There might be a limitation of the improvement we could possibly get, i.e. the generated code may still contain redundant test cases.

## Task 2

### Question:

Which of the two methods, in your opinion, will perform better in terms of efficiency and quality? What could the
efficiency and quality of them possibly depend on?

### Answer:

The second approach of improving the analysis process seems to be better in terms of performance since it does not create execution overhead of additional functionality (unlike the first approach where the test suite minimization module is introduced).

But efficiency and quality highly depend on the research of the available enhancements and their potential advantages over the current solution. If all the available options result in insignificant improvements or no improvements, the 1st option is the only one remaining. Implementing a test suite minimization module may also be an expedient solution in case of having a limited amount of time for the feature to be released since it is easier to implement.

## Task 3

### Question:

You are given a set of items `S`. Each item is identified by its unique name and has some positive integer price. Write a function that will divide an original set into two subsets `A` and `B` such that:

1. `|size(A) - size(B)| == size(S) mod 2`
2. the difference between the sums of prices of elements of subsets `A` and `B` is minimal

### Answer:

**Note:**

Conditions 1. and 2. are equivalent to the following formulation:

"Among `n` items of set `S` choose `[n/2]` items in such a way that difference between prices of selected items and left items is minimal".

**Notice that (assume `size()` function over each letter):**

`|A - B| -> min <=>`

`(A - B)^2 -> min <=>`

`A^2 + B^2 + 2AB - 4AB -> min <=>`

`(A + B)^2 - 4AB -> min <=>`

`S^2 - 4AB -> min` - since `S^2` is **const**:

`S^2 - 4AB -> min <=> AB -> max <=> (S - A) * A -> max`

Now the problem has the following formulation:

"Choose `[n/2]` items in a set `A`, so that `(S - A) * A -> max`".

**Idea of solution:**

1. `dp[i][s][k] = 0|1` - were you able to take the sum of `s` on the prefix `i` by taking `k` elements.
1. `path[i][s][k] = {s', k'}` - tracing the previous state, i.e. `dp[i-1][s'][k']`.

**Transition:** if we could take `dp[i][s][k]` then we can take `dp[i+1][s][k]` (by skipping `i`-th element) and `dp[i+1][s+price(i)][k+1]` (by taking `i`-th element).

**Base:**
   1. `dp[0][0][0] = 1, path[0][0][0] = {0, 0}`
   2. `dp[0][price(0)][1] = 1, path[0][price(0)][1] = {0, 0}`



The implementation is provided below and in the [Main](./src/Main.java) class.

```java
class Example {
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
```