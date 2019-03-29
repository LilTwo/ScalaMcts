package game



import scala.collection.mutable

trait Node {
  var visit_count: Double = 0
  val search_scores: mutable.HashMap[Int, Double] //chair -> score
  var search_done_move: Option[Move] = None
  var search_done_winner: Option[Int] = None
  var search_done_score: Option[Double] = None

  def make_move(move: Move): Node

  def winner: Option[Int]

  def overwhelmed: Option[(Move,Int)]

  def print_game:Unit = println("no impl")

  def update_search_done(move: Move, winner: Int, score: Double):Unit = {
    if (search_done_move.nonEmpty) throw new Exception("call update_search_done on a done room.room")
    search_done_move = Some(move)
    search_done_winner = Some(winner)
    search_done_score = Some(score)
  }

  val all_moves: Array[Move]
  lazy val children:Seq[Node] = all_moves.map(make_move)
  val cur_chair: Int
  def move_chair(chair:Int,displace:Int):Int
  val all_chair:Array[Int]
}