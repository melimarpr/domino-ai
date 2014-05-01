package icom5015.domino;

import com.sun.deploy.util.StringUtils;
import icom5015.domino.controllers.DominoGame;
import icom5015.domino.controllers.implementation.AutomationDominoGame;
import icom5015.domino.controllers.implementation.ConsoleDominoGame;
import icom5015.domino.controllers.implementation.EvoluteDominoGame;
import icom5015.domino.generators.DominoGenerator;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GeneticMain {

   public static void main(String[] args) {
        EvoluteDominoGame e = new EvoluteDominoGame();
        e.start();

        //TODO: Change For Multiple Runs and Genetic Mutation
        //Main to Run the Game
        /*DominoGame dominoGame = new ConsoleDominoGame();
        dominoGame.run();
        System.out.println("Winner "+ Player.toStringPlayer(dominoGame.winner())+" with score: "+dominoGame.score());*/



    }
}
