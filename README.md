# dnd-dice-probability

D&D Ability Score Array Probability Checker is designed to provide the likelihood of receiving a specific array upon rolling for stats. It also calculates point buy costs for 5e and Pathfinder.

It is not restricted to 6 scores of 4d6k3; it can account for any number of scores rolled (so Honor and Sanity can be included), and can calculate scores based on, say, 3d6, 2d6+6, or 4d3-8 (4dF). Therefore, it can be used to calculate the likelihood of rolls other than ability scores.

It calculates probabilities deterministically: it iterates through every possible combination of outcomes, rather than generating a certain number of arrays to compare the input array against. Via the multinomial theorem*, the algorithm runs significantly faster and with significantly less memory than this may suggest*. It saves the list of determined arrays, so as long as the number of abilities and the "AdXkB+C" selected do not change subsequent passes usually take less than a second, regardless of the amount of time to generate the arrays. There was a random mode planned, but this is currently not present.

* Consider 12,12,13; 12,13,12; and 13,12,12. These arrays have the same probability of occuring, and ultimately refer to the same case: 2 12s and 1 13. The number of permutations of an array can be calculated by the multinomial theorem. Thus, for N abilities of AdXkB+C, let Z = (X-1)×B+1. Then, the naive number of calculated arrays is Z<sup>N</sup>. However, the actual number of calculated arrays is the Zth [Simplical N-polytopic number](https://oeis.org/wiki/Simplicial_polytopic_numbers).

* This is not to say that it does not use significant amounts of memory. While it is indeed quite fast (8 x 4d6k3+0 first pass took less than 10 seconds on a 2.2 GHz processor, subsequent passes took less than a second), it can be somewhat taxing on memory (400 MB for above example, although without the modification it would approach 3.5 TB).

It calculates two different probabilities. The first is "Better Total", useful for DMs/GMs to determine if the dice are perhaps loaded. The second is "Strictly Better", which is useful for players who are preplanning characters and need to know how likely the dice will roll the minimum requirements.
