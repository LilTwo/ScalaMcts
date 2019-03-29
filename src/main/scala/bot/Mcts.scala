package bot

import game._

import scala.collection._
import scala.collection.mutable.ArrayBuffer


class Mcts(root: Node, var estimator: Estimator.t_estimator = Estimator.by_random(5)) {
  val cur_chair: Int = root.cur_chair
  val decay = 0.999
  private val search_path = ArrayBuffer.empty[Node]
  private val search_path_i = ArrayBuffer.empty[Int]

  def predict(search_time: Int, search_nodes: Int): Seq[(Move, Double, String)] = {
    if (root.all_moves.length == 1) return Seq((root.all_moves.head, root.search_scores(cur_chair) / root.visit_count, "single choice"))

    for (_ <- 1 to search_nodes) {
      if (root.search_done_move.nonEmpty) return Seq((root.search_done_move.get, root.search_done_score.get, s"search done, winner ${root.search_done_winner.get}"))
      clear_path()
      select_and_expand()
      update()
    }
    vote()
  }

  def clear_path() = {
    search_path.clear()
    search_path_i.clear()
  }

  def select_and_expand() = {
    var cur = root
    search_path += cur
    while (cur.visit_count != 0 && !is_overwhelmed(cur)) {
      val open_choices = cur.children.indices.filterNot(cur.children(_).search_done_move.nonEmpty)
      val scores = open_choices.map(ucb_score(cur, _))
      val i = open_choices.maxBy(ucb_score(cur, _))
      cur = cur.children(i)
      search_path_i += i
      search_path += cur
    }
  }

  def is_overwhelmed(room: Node) = {
    val overwhelmed = room.overwhelmed
    if (overwhelmed.nonEmpty){
      val ow_winner = overwhelmed.get._2
      val search_done_score = if(ow_winner == room.cur_chair) 1 else 0
      room.update_search_done(overwhelmed.get._1, overwhelmed.get._2, search_done_score)
    }
    overwhelmed.nonEmpty
  }

  def ucb_score(cur: Node, i: Int) = {
    val child = cur.children(i)
    val ucb = child.search_scores(cur.cur_chair) / (child.visit_count + 1e-5) + 1.414 * Math.sqrt(Math.log(cur.visit_count) / (child.visit_count + 1e-5))
    ucb
  }

  def update() = {
    var last_i = search_path.indices.last
    if (search_path(last_i).search_done_move.nonEmpty) last_i = update_search_done(last_i)

    if (root.search_done_winner.isEmpty) normal_update(last_i)
  }

  def normal_update(last_i: Int) = {
    var pow = 0
    val score = estimator(search_path(last_i))
    for (i <- (0 to last_i).reverse) {
      val cur = search_path(i)
      cur.all_chair.foreach(c => cur.search_scores.update(c, cur.search_scores(c) + score(c) * Math.pow(decay, pow)))
      cur.visit_count = cur.visit_count + 1
      pow += 1
    }
  }

  def update_search_done(last_i: Int): Int = {
    val winner = search_path.last.search_done_winner.get
    var search_done_score = search_path.last.search_done_score.get
    for (i <- (0 until last_i).reverse) {
      search_done_score = search_done_score * decay
      val leaf = search_path(i)
      val move = leaf.all_moves(search_path_i(i))
      if (leaf.all_moves.length == 1 || winner == leaf.cur_chair) { //winner is cur_chair
        leaf.update_search_done(move, winner, search_done_score)
      }
      else if (leaf.children.forall(_.search_done_winner.nonEmpty)) { //all branched will lose or tie
        val tie = leaf.all_moves.zipWithIndex.find{case (_,i) => leaf.children(i).search_done_winner.get == 0}
        if(tie.nonEmpty) leaf.update_search_done(tie.get._1, 0, search_done_score)
        else {
          val slowest = leaf.all_moves.zipWithIndex.maxBy { case (_, i) => -leaf.children(i).search_done_score.get }._1
          leaf.update_search_done(slowest, winner, search_done_score)
        }
      }
      else return i
    }
    0
  }

  def vote() = {
    val unsorted = root.all_moves.zip(root.children).map { case (move, room) => (move, room.visit_count, s"search score ${room.search_scores(cur_chair)}, visit count ${room.visit_count}") }
    unsorted.sortBy(-_._2)
  }
}