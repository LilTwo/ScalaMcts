package bot

import game._

import scala.util.Random

trait Predict {
  def predict_moves_with_score(node: Node): Seq[(Move, Double, String)]

  def predict_best_move_with_score(node: Node): (Move, Double) = {
    val result = predict_moves_with_score(node).head
    (result._1, result._2)
  }

  def predict_best_move(node: Node) = predict_best_move_with_score(node)._1

  def reset()={}
}

class RandomPredict extends Predict {
  def predict_moves_with_score(node: Node): Seq[(Move, Double, String)] = {
    val moves = node.all_moves
    val i = Random.nextInt(moves.length)
    Seq((moves(i), 1, "random"))
  }
}

//
class TicTacToeHumanPredict extends Predict {
  def predict_moves_with_score(node: Node): Seq[(Move, Double, String)] = {
    import game.TicTacToe.TicTacToeMove.seq2move
    println("player's turn")
    node.print_game
    val move = scala.io.StdIn.readLine().split("\\s+").map(_.toInt)
    Seq((seq2move(move), 1, "human play"))
  }
}
//
class MctsPredict(search_nodes:Int = 500) extends Predict {
  def predict_moves_with_score(room: Node): Seq[(Move, Double, String)] = {
    val mcts = new Mcts(room)
    mcts.predict(100, search_nodes)
  }
}