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
//class HumanPredict extends Predict {
//  def predict_moves_with_score(room: Room): Seq[(Move, Double, String)] = {
//    println()
//    println(s"counter: 3 4 5 6 7 8 9 T J Q K A 2")
//    println(s"         ${room.get_hand(Chair.next(room.cur_chair)).cnt.zip(room.get_hand(Chair.prev(room.cur_chair)).cnt).map{ case (x, y) => x + y}.mkString(" ")}")
//    println(s"pre_play: ${room.pre_play}; chair ${Chair.next(room.cur_chair)} has ${room.get_hand(Chair.next(room.cur_chair)).left}, chair ${Chair.prev(room.cur_chair)} has ${room.get_hand(Chair.prev(room.cur_chair)).left}")
//    println(s"your hand: ${room.get_hand(room.cur_chair)}")
//    print("enter your move: ")
//    val move = Moves.parse(scala.io.StdIn.readLine())
//    Seq((move, 1, "human play"))
//  }
//}
//
class MctsPredict(search_nodes:Int = 500) extends Predict {
  def predict_moves_with_score(room: Node): Seq[(Move, Double, String)] = {
    val mcts = new Mcts(room)
    mcts.predict(100, search_nodes)
  }
}