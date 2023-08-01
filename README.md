# Classic Chess
A simple chess app for Android that offers two game modes.
- Local Game: Play against a friend on your phone.
- Computer Game: Play against a computer opponent in three available difficulties:
  - Easy
  - Normal
  - Hard

### Screenshots
Main Menu            |  Local Game | Computer Game
:-------------------------:|:-------------------------:|:-------------------------:
![1](https://github.com/sebmueller91/ClassicChess/assets/19948723/319a8bb8-8ec0-4118-8633-63574cade82e)| ![2](https://github.com/sebmueller91/ClassicChess/assets/19948723/9cb593fb-94ea-4ee1-97be-4d079c2fa71a)| ![3](https://github.com/sebmueller91/ClassicChess/assets/19948723/6a10933c-d62f-47db-ac73-9fa349b9b0f6)

### Implementation details
The computer opponent is implemented using a MinMax search algorithm with piece weights and square tables. Alpha-beta-pruning and a transposition table are used to improve performance.
The evaluation function is mostly taken from [chessprogramming.org](https://www.chessprogramming.org/Simplified_Evaluation_Function).
