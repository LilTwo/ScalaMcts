package game.TicTacToe

import game.Move

case class TicTacToeMove(x: Int, y: Int) extends Move{
  override def toString: String = s"$x $y"
}

object TicTacToeMove{
  implicit def tuple2move(tuple:(Int,Int)) = TicTacToeMove(tuple._1,tuple._2)
  implicit def seq2move(seq:Seq[Int]) = TicTacToeMove(seq(0),seq(1))
}