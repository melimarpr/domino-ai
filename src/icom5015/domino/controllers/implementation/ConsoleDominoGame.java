package icom5015.domino.controllers.implementation;

import icom5015.domino.controllers.DominoGame;
import icom5015.domino.generators.DominoGenerator;
import icom5015.domino.generators.HandGenerator;
import icom5015.domino.models.Board;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;
import icom5015.domino.models.implementations.BlindPlayer;
import icom5015.domino.models.implementations.GeneticPlayer;
import icom5015.domino.models.implementations.HumanPlayer;
import icom5015.domino.models.implementations.RandomPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleDominoGame extends DominoGame {

    //Constants
    private static final int RUNNING = -1;

    //Variables
    private boolean printAll = false;
    private boolean blind = false;

    private int winner = RUNNING;
    private int score = 0;

    private void init(){

        //Init Vars
        Scanner in = new Scanner(System.in);
        players = new HashMap<Integer, Player>();

        System.out.println("Welcome to the Domino Game");
        System.out.println("==========Notes==========");
        System.out.println("This is a 4 player Game");
        System.out.println("Scoring is Individual");


        System.out.println("Print Everything: \n1.Yes 2.No");
        int printEverything = in.nextInt();
        if(printEverything == 1){
            printAll = true;
        }

        //Generate Tiles
        List<Domino> tiles = DominoGenerator.generateDominoBoard();
        HandGenerator handGenerator = new HandGenerator(tiles);


        System.out.println("\nPlayer 1:");
        System.out.println("1. Human Player 2.Random AI, 3.Genetic AI, 4. RandomAI (Select Tiles), 5.Genetic AI (Selected Tiles), 6. Human Player (Selected Tile), 7. Blind Player");
        int playerType = in.nextInt();
        setPlayerMap(handGenerator, Player.PLAYER_1, playerType);

        System.out.println("\nPlayer 2:");
        System.out.println("1. Human Player 2.Random AI, 3.Genetic AI, 4. RandomAI (Select Tiles), 5.Genetic AI (Selected Tiles), 6. Human Player (Selected Tile), 7. Blind Player");
        playerType = in.nextInt();
        setPlayerMap(handGenerator, Player.PLAYER_2, playerType);

        System.out.println("\nPlayer 3:");
        System.out.println("1. Human Player 2.Random AI, 3.Genetic AI, 4. RandomAI (Select Tiles), 5.Genetic AI (Selected Tiles), 6. Human Player (Selected Tile), 7. Blind Player");
        playerType = in.nextInt();
        setPlayerMap(handGenerator, Player.PLAYER_3, playerType);

        System.out.println("\nPlayer 4:");
        System.out.println("1. Human Player 2.Random AI, 3.Genetic AI, 4. RandomAI (Select Tiles), 5.Genetic AI (Selected Tiles), 6. Human Player (Selected Tile), 7. Blind Player");
        playerType = in.nextInt();
        setPlayerMap(handGenerator, Player.PLAYER_4, playerType);



    }

    private void setPlayerMap(HandGenerator handGenerator, int player, int playerType){
        switch (playerType){
            case 1:
                players.put(player, new HumanPlayer(handGenerator.generateHand(false), player));
                break;
            case 3:
                players.put(player, new GeneticPlayer(handGenerator.generateHand(false), player));
                break;
            case 4:
                players.put(player, new RandomPlayer(handGenerator.generateHand(true), player));
                break;
            case 5:
                players.put(player, new GeneticPlayer(handGenerator.generateHand(true), player));
                break;
            case 6:
                players.put(player, new HumanPlayer(handGenerator.generateHand(true), player));
                break;
            case 7:
                players.put(player, new BlindPlayer(handGenerator.blindHand(), player));
                blind = true;
                break;
            default:
                players.put(player, new RandomPlayer(handGenerator.generateHand(false), player));
                break;
        }

    }


    @Override
    public void run() {
        init();


        if(blind){
            Scanner in = new Scanner(System.in);
            System.out.println("Select Player with Double Six");
            System.out.println("1. Player 1, 2. Player 2, 3. Player 3, 4. Player 4");
            int player = in.nextInt();
            switch (player){

                case 1:
                    player = Player.PLAYER_1;
                    break;
                case 2:
                    player = Player.PLAYER_2;
                    break;
                case 3:
                    player = Player.PLAYER_3;
                    break;
                case 4:
                    player = Player.PLAYER_4;
                    break;

            }
            generateOrderBlind(player);

        } else{
            generateOrder();
        }





        //Pass Counter
        int passCounter = 0;

        Player firstPlayer = players.get(order[0]);
        Domino doubleSix = firstPlayer.getDoubleSix();
        Board board = new Board(doubleSix, order[0]);

        if (printAll) {
            System.out.println("First Player: " + Player.toStringPlayer(order[0]));
            System.out.println("Double Six");
        }

        //Other Players
        for (int i = 1; i < order.length; i++) {

            if (printAll) {
                System.out.println("\nPlayer: " + Player.toStringPlayer(order[i]));
                System.out.println("Board:");
                System.out.println(board.toString() + "\n");
            }

            Player actualPlayer = players.get(order[i]);
            Player.Move move = actualPlayer.getDomino(board);


            if (move.getPlayedSide() == Domino.UPPER_SIDE) {
                board.setUpperValue(move.getDomino(), order[i]);
                passCounter = 0;
            } else if (move.getPlayedSide() == Domino.LOWER_SIDE) {
                board.setLowerValue(move.getDomino(), order[i]);
                passCounter = 0;
            } else { //Pass
                board.setPass(order[i]);
                passCounter++;
                if(printAll){
                    System.out.println("Pass");
                }
            }



        }


        //Other Rounds
        while (winner == RUNNING) {

            for (int i = 0; i < order.length; i++) {

                //Print
                if (printAll) {
                    System.out.println("\nPlayer" + Player.toStringPlayer(order[i]));
                    System.out.println("Board:");
                    System.out.println(board.toString() + "\n");
                }

                Player actualPlayer = players.get(order[i]);
                if(printAll && !(actualPlayer instanceof HumanPlayer)){
                    System.out.println("\n Player Hand is:");
                    System.out.println(actualPlayer.toStringHand()+"\n");
                }
                Player.Move move = actualPlayer.getDomino(board);




                if (move.getPlayedSide() == Domino.UPPER_SIDE) {
                    board.setUpperValue(move.getDomino(), order[i]);
                    passCounter = 0;
                } else if (move.getPlayedSide() == Domino.LOWER_SIDE) {
                    board.setLowerValue(move.getDomino(), order[i]);
                    passCounter = 0;
                } else { //Pass
                    board.setPass(order[i]);
                    passCounter++;
                    if(printAll){
                        System.out.println("Pass");
                    }
                }

                //Check For Winners
                if (actualPlayer.emptyHand()) {
                    winner = order[i];
                    score = getTotalScore();
                    break;
                }

                //Check if Pass
                if (passCounter == 4) {
                    winner = getWinnerForFullPass(order[i]);
                    score = getTotalScore();
                    break;
                }


            }
        }
    }

    private void generateOrderBlind(int player) {
        switch (player){
            case Player.PLAYER_1:
                order = new int[]{Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_3, Player.PLAYER_4};
                break;
            case Player.PLAYER_2:
                order = new int[]{Player.PLAYER_2, Player.PLAYER_3, Player.PLAYER_4, Player.PLAYER_1};
                break;
            case Player.PLAYER_3:
                order = new int[]{Player.PLAYER_3, Player.PLAYER_4, Player.PLAYER_1, Player.PLAYER_2};
                break;
            case Player.PLAYER_4:
                order = new int[]{Player.PLAYER_4, Player.PLAYER_1, Player.PLAYER_2, Player.PLAYER_3};
                break;
        }
    }

    private int getTotalScore() {

        int sum = 0;
        for(Map.Entry<Integer, Player> e: players.entrySet()){
            sum += e.getValue().getHandSum();
        }
        return sum;
    }

    public int getWinnerForFullPass(int lockPlayer) {

        int winner = lockPlayer;
        int min = players.get(winner).getHandSum();
        for(Map.Entry<Integer, Player> e: players.entrySet()){
            if(e.getValue().getHandSum() < min){
                winner = e.getKey();
                min = e.getValue().getHandSum();
            }
        }
        return winner;
    }



    @Override
    public int winner() {
        return winner;
    }

    @Override
    public int score() {
        return score;
    }


}
