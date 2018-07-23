AI Training
===========

The AI for this game is a neural network.
It is trained in a similar scheme to TD-Gammon.

#### AI Structure

#### Inputs

#### Training

The trainer runs by looping through practice games with the AI playing itself.
It is best to run at least 500,000 games.
For each game, the trainer runs through the following steps:

 1. Initialize game with the AI playing both players.
 2. For each turn, it chooses what it thinks is the best move.
 3. It then runs back-propagation with the input being the current state input
    and the expected output is the output of the chosen state.
 4. At the end of the game, it does 2 final corrections. First, it runs back-propagation
    with the inputs from the winner's perspective and the outputs {1,0}. Second,
    it runs back-propagation with the inputs from the loser's perspective and the
    outputs {0,1}. If there was a tie, both outputs would be {0,0}.

It runs through these steps till the net is trained.
There are 2 learning rates it uses, an in game rate and an after game rate.
Generally, the in game rate is very small, for example .005 and
the after game rate is larger, for example .01.

To help it learn completing paths, I set up training so for the first half of the games
a win only counted if it was completed by a path.
If a player won by running out of pieces or out of board locations,
the final output would be {0,0}.
Then for the second half I allowed a win of any kind to be counted.
