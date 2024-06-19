# Sudoku Solver in Java

A simple Sudoku Solver (for order-3 problems i.e. 9x9 boards) in standard Java.

[JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide/) is used as testing framework for (quite minimal) testing.

This was written to test the idea of "Constraint Solving" and "Constraint Propagation" on a simple problem.

Currently the intial board is not given on the command line but is constructed through a dedicated method. Take a look at the test methods for this.

If you run `Sudoku.main()`, default initial settings will be loaded into the `Board` structure and the corresponding problem will be solved.

Take a log at `example.log` for example output.

## TODO

- Read the initial board as text input from the command line and output a more nicely printed board.
- The "alldifferent" constraint misses the trick that if there are n decision variables with the same domain of size n, none of the values
  in that domain can appear in any decision variable being monitored by that constraint. 
- More tests.

## History

- 2021-10-05 Original version in Java 16.
- 2024-06-19 Updated for Java 21, reorganized into a "project", fixed code according to IDE suggestions.

## Done differently

With languages that provide the right abstraction you can go right for it in a few lines:

### Prolog

   * [Solving Sudoku with (Scryer) Prolog](https://www.metalevel.at/sudoku/) by Markus Triska
   * [In SWI-Prolog](https://www.swi-prolog.org/pldoc/man?section=clpfd-sudoku) using [library(clpfd)](https://www.swi-prolog.org/pldoc/man?section=clpfd) (also originally by Markus Triska I guess)
   * [YouTube: "Sudoku in Prolog"](https://www.youtube.com/watch?v=5KUdEZTu06o) by Markus Triska 

### MiniZinc
   
   * [heavens.mzn](https://github.com/MiniZinc/specialization-examples/blob/master/CP/heavens/heavens.mzn) by Peter Stuckey
      * This is from the course [Solving Algorithms for Discrete Optimization](https://www.coursera.org/learn/solving-algorithms-discrete-optimization)
   * [Sudoku using the 'all_different' constraint](https://github.com/hakank/hakank/blob/master/minizinc/sudoku_alldifferent.mzn) by Hakan Kjellerstrand (possibly old-ish code)
      * [Solving Pi Day Sudoku 2009 with the global cardinality constraint](http://www.hakank.org/constraint_programming_blog/2009/03/solving_pi_day_sudoku_2009_wit.html) 

## Some reading

   * [Sudoku](https://en.wikipedia.org/wiki/Sudoku) at Wikipedia.
   * [Mathematics of Sudoku](https://en.wikipedia.org/wiki/Mathematics_of_Sudoku) at Wikipedia.   
   * [Sudoku as a Constraint Problem](https://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.88.2964) by Helmut Simonis.
   * [Sudoku as a SAT Problem](http://sat.inesc-id.pt/~ines/publications/aimath06.pdf) (PDF) by Inês Lynce and Joël Ouaknine. We read:
     _"In the extended encoding, the resulting CNF formula will have 11,988 clauses, apart from the unit clauses representing
     the pre-assigned entries. From these clauses, 324 clauses are nine-ary and the remaining 11,664 clauses are binary.
     The nine-ary clauses result from the four sets of at-least-one clauses (4⋅9⋅9 = 324). The 11,664 binary clauses
     result from the four sets of at-most-one clauses (4⋅9⋅9·[36 pairings of 2 elements from 9])."_
   * [Analysis of Sudoku Solving Algorithms ](http://www.enggjournals.com/ijet/docs/IJET17-09-03-043.pdf) by
     M.Thenmozhi, Palash Jain, Sai Anand, Saketh Ram (2017)
      * This paper references the "Dancing Links" algorithm by Donald Knuth described in:
        [Dancing links](https://arxiv.org/abs/cs/0011047): _"The author presents two tricks to accelerate depth-first
        search algorithms for a class of combinatorial puzzle problems, such as tiling a tray by a fixed set of polyominoes."_
   * [Complexity and Completeness of Finding Another Solution and Its Application to Puzzles](https://www-imai.is.s.u-tokyo.ac.jp/~yato/data2/SIGAL87-2.pdf) 
      by Takayuki YATO and Takahiro SETA: The "Number Place" (i.e. Sudoku) problem is [NP-complete](https://www.scottaaronson.com/democritus/lec6.html) 
      (i.e. belongs to the set of "hardest" problems in NP). In the paper _Sudoku as a SAT Problem_ we also read _"Note, however, that 
      generalized Sudoku puzzles satisfying "has only one solution" and "can be solved with only reasoning, i.e. with no search" are 
      polynomial-time solvable."_ 
   * [Sudoku Puzzle Complexity](https://www.researchgate.net/publication/264572573_Sudoku_Puzzle_Complexity) by Sian K. Jones, 
     Paul A. Roach and Stephanie Perkins (2009): How "hard" a puzzle feels in an informal sense.
   * [A Hybrid Approach for the Sudoku Problem: Using Constraint Programming in Iterated Local Search](https://ieeexplore.ieee.org/document/7887637), 
     appears in _IEEE Intelligent Systems (Volume: 32, Issue: 2, Mar.-Apr. 2017)_ 
      * This article is paywalled but a (non-beautified) version of the paper as well as the software that goes with it can
        officially be found [here](https://www.dbai.tuwien.ac.at/research/project/arte/sudoku/). 
      * The paper describes a hybrid approach to solve Sudoku problems of rank 3 to 5. Experiments show that Constraint Propgation does not do well
        on rank 4 problems. For rank 5 problems, only the hybrid approach and a simulated annealing-based algorithm still manage, with the hybrid
        approach needing much less time to find an optimal solution (i.e. a solution fulfilling all constraints). At rank 5, problems around ~45%
        of fixed cells cause both approaches to fail at finding an optimal solution.
   * [Solving and Analyzing Sudokus with Cultural Algorithms](https://www.researchgate.net/publication/224330246_Solving_and_Analyzing_Sudokus_with_Cultural_Algorithms) by Timo Mantere and Janne Koljonen: On using genetic algorithms on Sudoku problems.

### Specifically about the "All Different" Constraint

The present code does not use this goodness!

   * [A filtering algorithm for constraints of difference in CSPs.](https://aaai.org/Papers/AAAI/1994/AAAI94-055.pdf) by Jean-Charles Régin.
     Appears in _Proceedings of the National Conference on Artificial Intelligence (AAAI), pp. 362-367, 1994_
   * [The Alldifferent Constraint: A Survey](http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.104.8388) by Willem-Jan van Hoeve, 2001.
   * [The Hopcroft-Karp algorithm](https://en.wikipedia.org/wiki/Hopcroft%E2%80%93Karp_algorithm) 
   * In MiniZinc:
     * [`all_different`](https://www.minizinc.org/doc-2.5.5/en/lib-globals.html?highlight=all_different#index-29) -- although the preferred spelling is `alldifferent`, as in the [tutorial](https://www.minizinc.org/doc-2.5.5/en/predicates.html?highlight=alldifferent)
   * The entry for [alldifferent](http://sofdem.github.io/gccat/gccat/Calldifferent.html) in the [Global Constraint Catalog](http://sofdem.github.io/gccat/gccat/)
