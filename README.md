# Classic Chess
A simple chess app for Android that offers two game modes.
- Local Game
Play against a friend on your phone.
- Computer Game
Play against a computer opponent in three available difficulties:
- Easy
- Normal
- Hard
### Implementation details
The computer opponent is implemented using a MinMax search algorithm with piece weights and square tables. Alpha-beta-pruning and a transposition table are used to improve performance.
The evaluation function is mostly taken [chessprogramming.org](https://www.chessprogramming.org/Simplified_Evaluation_Function).