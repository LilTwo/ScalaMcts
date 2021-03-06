## Scala implementation of Monte Carlo Tree Search


## Node
A single game state is represented by an instance of this trait, now there is only one kind of game: 
TicTacToe.<br/>
New games can be added by writing new classes that extend Node.

## Mcts
The search logic of MCTS is implemented in this class.
The four steps of MCTS correspond to the following method: <br/>

<b>selection</b>: select_and_expand<br/>
<b>expansion</b>: select_and_expand<br/>
<b>simulation</b>: done by variable called estimator with type <b>Node => Map[Int, Double]</b><br/>
<b>backpropagation</b>: normal_update, update_search_done(when an overwhelmed node encountered)<br/>
<b>final decision</b>: vote, choose the child node with highest visit_count

predict takes two arguments<br/>
search_nodes means how many times these four steps are repeated.<br/>
search_time have no use currently.

## Estimator
The simulation step of MCTS is done in here, now I just have a simplest one: random play till end

## Predict
Each subclass of Predict represents a type of players: Mcts, Random, Human. If a HumanPredict is used, the user can type the input via stdin when it's user's turn.

## Main
A game can be started at here, you can play with mcts, or have two MCTS play against each other.<br/>
In the case of TicTacToe, two MCTS should draw everytime when search_nodes is large enough.
