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