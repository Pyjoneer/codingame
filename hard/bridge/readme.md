Solution of exercise [SKYNET - THE BRIDGE](https://www.codingame.com/training/hard/the-bridge-episode-2).

Class `State` represents one of possible states of bikes on road tracks. In every simulation step set of new states is created using functions from `Action` object. These next states are derived using list head as:
[state_0_0, state_0_1, ..., state_0_n]
[state_0, state_1, ..., state_n]

When win criteria is met, solution path is first element of each list in list states.

This solution does not hesitate to sacrifice poor mototerminators :( :D