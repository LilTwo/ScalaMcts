package Demo

import bot._
import game.TicTacToe.TicTacToeNode
import game._

import scala.collection.mutable.ArrayBuffer

object Main extends App {
  def game(root: Node, players: Map[Int, Predict]) = {
    var node = root
    var moves = ArrayBuffer[Move]()
    while (node.winner.isEmpty) {
      val move = players(node.cur_chair).predict_best_move(node)
      moves += move
      node = node.make_move(move)
    }
    if(node.winner.get != 0) moves.foreach(println)
    moves.clear()
    node.winner.get
  }

  val start = System.currentTimeMillis()
  val players = Map(
    1 -> new MctsPredict(),
    -1 -> new TicTacToeHumanPredict()
  )
  val winner = collection.mutable.HashMap[Int,Int]()
  val count = 500

  for (i <- 1 to count) {
    val root = TicTacToeNode.createNewGame
    val win = game(root, players)
    println(i, win)
    winner.update(win,1+winner.getOrElse(win,0))
    players.foreach(_._2.reset())
//  println("result til now:",winner)
  }
  println("total winner:",winner)
  val dt = System.currentTimeMillis() - start
  println(s"total time $dt, avg time ${dt/count}")
}
