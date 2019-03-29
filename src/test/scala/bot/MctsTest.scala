package bot

import game.Node
import game.TicTacToe.{TicTacToeMove, TicTacToeNode}
import org.scalatest.FunSuite
import game.TicTacToe.TicTacToeMove.tuple2move

class MctsTest extends FunSuite {
  test("test_by_TicTacToe"){
    var root:Node = TicTacToeNode.createNewGame
    root = root.make_move((1,1))
    root.print_game
    val mcts = new Mcts(root)
    val predict_results = mcts.predict(100,500)
    val best_move = predict_results.head._1
    val TicTacToeMove(x,y) = best_move
    assert(y != 1 && x != 1)
  }
}
