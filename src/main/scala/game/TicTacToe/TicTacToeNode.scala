package game.TicTacToe

import game.{Move,Node}
import scala.collection.mutable

class TicTacToeNode(board: Array[Array[Int]], val cur_chair: Int) extends Node {
  override val all_chair = Array(-1,1)
  override val search_scores: mutable.HashMap[Int, Double] = mutable.HashMap(-1->0,1->0)
  override val all_moves: Array[Move] = (0 to 2).flatMap(y => (0 to 2).collect{case x if board(y)(x) == 0 => TicTacToeMove(x,y)}).toArray
  private val symbol_map = Map(1->"O",-1->"X",0->" ")

  override def overwhelmed: Option[(Move,Int)] = {
    val ow_move = all_moves.find(move => make_move(move).winner.nonEmpty)
    if(ow_move.nonEmpty) Some((ow_move.get,make_move(ow_move.get).winner.get))
    else None
  }

  override def make_move(move: Move): Node = {
    val TicTacToeMove(x, y) = move
    val newBoard = Array.tabulate(3)(i => Array.tabulate(3)(j => board(i)(j)))
    newBoard(y)(x) = cur_chair
    new TicTacToeNode(newBoard, move_chair(cur_chair, 1))
  }

  override def winner: Option[Int] = {
    if( all_moves.isEmpty )
      Some(0)
    else if(check_row || check_col || check_diag){
      Some(move_chair(cur_chair,1))
    }
    else
      None
  }

  override def print_game: Unit = {
    val symbol = board.map(_.map(symbol_map))

    println(s"${symbol(0)(0)}|${symbol(0)(1)}|${symbol(0)(2)}")
    println("------")
    println(s"${symbol(1)(0)}|${symbol(1)(1)}|${symbol(1)(2)}")
    println("------")
    println(s"${symbol(2)(0)}|${symbol(2)(1)}|${symbol(2)(2)}")
    println
  }

  private def check_row = (0 to 2).map(ri => board(ri).sum / 3).exists(_ != 0)

  private def check_col = (0 to 2).map(x => (0 to 2).foldLeft(0)((sn, y) => sn + board(y)(x))/3).exists(_ != 0)

  private def check_diag = {
    val d1 = (board(0)(0) + board(1)(1) + board(2)(2)) / 3 != 0
    val d2 = (board(0)(2) + board(1)(1) + board(2)(0)) / 3 != 0
    d1 || d2
  }

  override def move_chair(chair:Int,displace:Int):Int = chair*scala.math.pow(-1,displace).toInt

}

object TicTacToeNode{
  def createNewGame = new TicTacToeNode(Array.fill(3)(Array.fill(3)(0)),1)
}