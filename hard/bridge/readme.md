# SKYNET - THE BRIDGE
Solution to exercise [SKYNET - THE BRIDGE](https://www.codingame.com/training/hard/the-bridge-episode-2).

`Context` such as road configuration and how many bikes must survive is passed using implicit parameters.

Class `State` represents one of possible states of bikes on road tracks (speed, position). In every simulation step set of new states is created using functions from `Action` object. These next states are derived using list head as:

```
[state_0_0_0, state_0_0_1, ..., state_0_0_n]
[state_0_0, state_0_1, ..., state_0_n]
[state_0, state_1, ..., state_n]
```

If state is dead end -- eq. doesn't lead to any win state -- then this state is removed from list and simulation continues using another state. 

To perform simulation state must be valid eq. alive bikes count must be bigger or equal to number `mustSurvive` from `Context` and bikes speed must be positive.

Winning state must be valid and all bikes must be beyond whole road.

When win criteria is met, solution path is first element of each list in list states.

This solution does not hesitate to sacrifice poor mototerminators, so Biker achievement wasn't unlocked :(