package icom5015.domino.models.implementations;

import icom5015.domino.models.Board;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by enrique on 4/30/14.
 */
public class HumanPlayer extends Player {


    public HumanPlayer(List<Domino> hand, int myPlayer) {
        super(hand, myPlayer);
    }

    @Override
    public Move getDomino(Board board) {

        Scanner in = new Scanner(System.in);

        System.out.println("\nBoard:");
        System.out.println(board.toString() + "\n");

        System.out.println("\n Your Hand is:");
        System.out.println(toStringHand()+"\n");

        List<Domino> upperSide = getPosibleDominosList(board.getUpperValue());
        List<Domino> lowerSide = getPosibleDominosList(board.getLowerValue());

        //Pass
        if(upperSide.size() == 0 && lowerSide.size() == 0){
            System.out.println("Pass");
            return new Move(PASS, null);
        }

        System.out.println("For Upperside: ");
        System.out.println(Player.toStringDominoList(upperSide)+"\n");

        System.out.println("For lowerSide: ");
        System.out.println(Player.toStringDominoList(lowerSide)+"\n");

        int side;
        if(upperSide.size() == 0){
            side = 2;
        }
        else if(lowerSide.size() == 0){
            side = 1;
        }
        else{
            System.out.println("Select Side:");
            System.out.println("1. Upperside 2.Lowerside");
            side = in.nextInt();
        }
        System.out.println("Enter Index:");
        int index = in.nextInt();

        System.out.println("=====================================================================\n");
        if(side == 1){
            Domino domino = upperSide.get(index);
            hand.remove(domino);
            return new Move(Domino.UPPER_SIDE, domino);
        } else if(side == 2){
            Domino domino = lowerSide.get(index);
            hand.remove(domino);
            return new Move(Domino.LOWER_SIDE, domino);
        }

        return new Move(PASS, null);
    }

    @Override
    public Domino getFirstMoveDomino() {
        Scanner in = new Scanner(System.in);
        System.out.println("Hand: ");
        System.out.println(Player.toStringDominoList(hand)+"\n");

        System.out.println("Enter Index:");
        int index = in.nextInt();
        return hand.remove(index);
    }


}
