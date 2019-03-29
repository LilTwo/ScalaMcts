package game.TicTacToe

import game.Node
import org.scalatest.FunSuite

class TicTacToeTest extends FunSuite{
  test("TicTacNode.overwhelmed"){
    var game:Node = TicTacToeNode.createNewGame
    import TicTacToeMove.tuple2move

    game = game.make_move((2,2))
    assert(game.overwhelmed.isEmpty)

    game = game.make_move((2,1))
    assert(game.overwhelmed.isEmpty)

    game = game.make_move((2,0))
    assert(game.overwhelmed.isEmpty)

    game = game.make_move((0,1))
    assert(game.overwhelmed.isEmpty)

    game = game.make_move((1,0))
    game.print_game
    assert(game.overwhelmed.nonEmpty)

    game = game.make_move(game.overwhelmed.get._1)
    game.print_game
    assert(game.winner.get == -1)
  }
}
