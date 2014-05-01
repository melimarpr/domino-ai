package icom5015.domino;

import icom5015.domino.controllers.DominoGame;
import icom5015.domino.controllers.implementation.ConsoleDominoGame;
import icom5015.domino.models.Player;

/**
 * Created by enrique on 5/1/14.
 */
public class ConsoleMain {

    public static void main(String[] args){

        DominoGame dominoGame = new ConsoleDominoGame();
        dominoGame.run();
        System.out.println("Winner "+ Player.toStringPlayer(dominoGame.winner())+" with score: "+dominoGame.score());

    }

}
