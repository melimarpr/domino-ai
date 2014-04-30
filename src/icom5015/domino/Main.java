package icom5015.domino;

import icom5015.domino.controllers.DominoGame;
import icom5015.domino.controllers.implementation.ConsoleDominoGame;
import icom5015.domino.generators.DominoGenerator;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        //TODO: Change For Multiple Runs and Genetic Mutation
        //Main to Run the Game
        DominoGame dominoGame = new ConsoleDominoGame();
        dominoGame.run();
        System.out.println("Winner "+ Player.toStringPlayer(dominoGame.winner())+" with score: "+dominoGame.score());



    }
}
