# Sudoku Solver in Java

A simple Sudoku Solver (for order-3 problems i.e. 9x9 boards) in standard Java 16.

[JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide/) is used as testing framework for (minimal) testing.

This was written to test the idea of "Constraint Solving" and "Constraint Propagation" on a simple problem.

Currently the intial board is not given on the command line but is constructed through a dedicated method. Take a look at the test methods for this.

If you run `Sudoku.main()`, default initial settings will be loaded into the `Board` structure and the corresponding problem will be solved.

Take a log at `example.log` for example output.

## Done differently

With languages that provide the right abstraction you can go right for it in a few lines:

### Prolog

   * [Solving Sudoku with (Scryer) Prolog](https://www.metalevel.at/sudoku/) by Markus Triska
   * [In SWI-Prolog](https://www.swi-prolog.org/pldoc/man?section=clpfd-sudoku) using [library(clpfd)](https://www.swi-prolog.org/pldoc/man?section=clpfd) (also by Markus Triska I guess)
   * [](https://www.youtube.com/watch?v=5KUdEZTu06o) 

### MiniZinc
   
   * [heavens.mzn](https://github.com/MiniZinc/specialization-examples/blob/master/CP/heavens/heavens.mzn) by Peter Stuckey
      * This is from the course [Solving Algorithms for Discrete Optimization](https://www.coursera.org/learn/solving-algorithms-discrete-optimization)
   * [Sudoku using the 'all_different' constraint](https://github.com/hakank/hakank/blob/master/minizinc/sudoku_alldifferent.mzn) by Hakan Kjellerstrand (possibly old-ish code)
      * [Solving Pi Day Sudoku 2009 with the global cardinality constraint](http://www.hakank.org/constraint_programming_blog/2009/03/solving_pi_day_sudoku_2009_wit.html) 

## Some reading

   * [Sudoku as a Constraint Problem](https://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.88.2964) by Helmut Simonis
   * [Sudoku as a SAT Problem](http://sat.inesc-id.pt/~ines/publications/aimath06.pdf) by Inês Lynce and Joël Ouaknine. 
   * [Analysis of Sudoku Solving Algorithms ](http://www.enggjournals.com/ijet/docs/IJET17-09-03-043.pdf) by M.Thenmozhi, Palash Jain, Sai Anand, Saketh Ram (2017)
      * This paper references [Dancing links](https://arxiv.org/abs/cs/0011047) by Donald Knuth
   * [Complexity and Completeness of Finding Another Solution and Its Application to Puzzles](https://www-imai.is.s.u-tokyo.ac.jp/~yato/data2/SIGAL87-2.pdf) by Takayuki YATO and Takahiro SETA: The "Number Place" (i.e. "Sudoku") problem is [NP-complete](https://www.scottaaronson.com/democritus/lec6.html) (one of the "hardest" problems in NP).
   * [Sudoku Puzzle Complexity](https://www.researchgate.net/publication/264572573_Sudoku_Puzzle_Complexity) by Sian K. Jones, Paul A. Roach and Stephanie Perkins (2009): How "hard" a puzzle feels in an informal sense.
   * [A Hybrid Approach for the Sudoku Problem: Using Constraint Programming in Iterated Local Search](https://ieeexplore.ieee.org/document/7887637), 
     appears in _IEEE Intelligent Systems (Volume: 32, Issue: 2, Mar.-Apr. 2017)_ (paywalled)   
   * [Solving and Analyzing Sudokus with Cultural Algorithms](https://www.researchgate.net/publication/224330246_Solving_and_Analyzing_Sudokus_with_Cultural_Algorithms) by Timo Mantere and Janne Koljonen: On using genetic algorithms on Sudoku problems.
   
