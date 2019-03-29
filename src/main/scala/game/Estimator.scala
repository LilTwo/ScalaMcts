package game

import scala.collection.mutable
import scala.util.Random

object Estimator {
  type t_estimator = Node => Map[Int, Double]

  def by_random(sim_time:Int)(node: Node):Map[Int,Double] = {
    val score = mutable.Map[Int,Double]()
    node.all_chair.foreach(score(_)=0)
    for (i <- 1 to sim_time){
      var _node = node
      while (_node.winner.isEmpty) {
        val move_idx = Random.nextInt(_node.all_moves.length)
        _node = _node.make_move(_node.all_moves(move_idx))
      }
      val winner = _node.winner.get
      node.all_chair.foreach(chair => {if(chair == winner) score(chair)+=1})
    }
    score.toMap
  }
}
