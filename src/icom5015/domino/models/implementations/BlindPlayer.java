package icom5015.domino.models.implementations;

import icom5015.domino.models.Board;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;

import java.util.List;
import java.util.Scanner;

/**
 * Created by enrique on 4/30/14.
 */
public class BlindPlayer  extends Player{



    public BlindPlayer(List<Domino> hand, int myPlayer) {
        super(hand, myPlayer);
    }

    @Override
    public Move getDomino(Board board) {

        Scanner in = new Scanner(System.in);

        System.out.println("Select Side:");
        System.out.println("1. Upperside 2. Lowerside 3. Pass");
        int side = in.nextInt();


        if(side == 3){
            return new Move(PASS, null);
        }

        else if(side == 1){

            System.out.println("Upperside: ");
            int upper = in.nextInt();
            System.out.println("Lowerside: ");
            int lower = in.nextInt();
            hand.remove(0);
            return new Move(Domino.UPPER_SIDE, new Domino(upper, lower));
        }
        else if(side == 2){

            System.out.println("Upperside: ");
            int upper = in.nextInt();
            System.out.println("Loweside: ");
            int lower = in.nextInt();

            hand.remove(0);
            return new Move(Domino.LOWER_SIDE, new Domino(upper, lower));
        }

        return new Move(PASS, null);
    }

    @Override
    public Domino getFirstMoveDomino() {
        Scanner in = new Scanner(System.in);
        System.out.println("Upperside: ");
        int upper = in.nextInt();
        System.out.println("Lowerside: ");
        int lower = in.nextInt();
        hand.remove(0);
        return new Domino(upper, lower);
    }

    @Override
    public Domino getDoubleSix() {
        hand.remove(0);
        return new Domino(Domino.SIX, Domino.SIX);
    }
}
