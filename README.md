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

Let `d[i][k][t]` be the sum of prices of elements of set `A` (price(A)) where `A * (S - A) -> max` when the prefix of length `i` of set `S` is observed, `k = size(A), k <= i + 1`, and the current `i`-th element is taken iff `t = 1` (i.e. `t = 1` - we take `i`-th item, `t = 0` - we skip it).

Let's provide a formula of recalculating `d[i][k][t]` over the previous results:

1. `d[i][k][0] = d[i-1][k][t]`, where `t` is such that:
   1. `A' := d[i-1][k][t]`
   2. `(S - A') * A' -> max`
   3. **Note:** `k >= 0`
1. `d[i][k][1] = price(i) + d[i-1][k-1][t]`, where `t` is such that:
   1. `A' := price(i) + d[i-1][k-1][t]`
   2. `(S - A') * A' -> max`
   3. **Note:** `k > 0`
1. **Base:**
   1. `d[0][0][0] = d[0][0][1] = 0`
   2. `d[0][1][1] = price(0)`
   3. All the rest are 0
1. **Answer**:
   1. `A0 := d[n - 1][n/2][0]`, `A1 = d[n - 1][n/2][1]`: select such `i` that `(S - Ai) * Ai -> max`.
   2. In order to retrieve the elements of the set `A` let's trace the state from which we updated the current state `d[i][k][t]`, i.e. let's create `path[i][k][t] = t0` where `t0` is the selected 3rd parameter from the transition formula. 

The implementation is provided below and in the [Main](./src/Main.java) class, tests .

```java
class Example {
   // we expect that you will return a list of size 2
   static List<Map<String, Integer>> split(Map<String, Integer> set) {
        // TODO
   }
}
```