package icom5015.domino.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enrique on 4/29/14.
 */
public abstract class Player {

    //Constants
    public static final int PLAYER_1 = 0;
    public static final int PLAYER_2 = 1;
    public static final int PLAYER_3 = 2;
    public static final int PLAYER_4 = 3;

    public static final int PLAYER_QUANTITY = 4;

    public static final int PASS = -3;


    protected List<Domino> hand;


    public Player(List<Domino> hand){
        this.hand = hand;
    }


    //Null if No Domino Found
    public abstract Move getDomino(Board board);

    public String toStringHand(){
        return toStringDominoList(hand);
    }

    public static String toStringDominoList(List<Domino> dominoes){
        StringBuilder builder = new StringBuilder();
        for(int j=0; j< dominoes.size(); j++){
            builder.append("Index " + j + " : " + dominoes.get(j).toString());
            if(j != dominoes.size()-1){
                builder.append(" - ");
            }
        }
        return builder.toString();
    }

    public boolean hasDobleSix(){
        Domino doubleSix = new Domino(6, 6);
        for(Domino e: hand){
            if(e.equals(doubleSix)){
                return true;
            }
        }
        return false;
    }

    public Domino getDoubleSix(){
        Domino doubleSix = new Domino(6, 6);
        for(int i=0; i<hand.size(); i++){
            if(hand.get(i).equals(doubleSix)){
                return hand.remove(i);
            }
        }
        return null;


    }


    public int getHandSum(){
        int sum = 0;
        for(Domino e: hand){
            sum +=e.getFullSum();
        }
        return sum;
    }


    public boolean emptyHand(){
        return hand.size() == 0;
    }

    public int handSize(){
        return hand.size();
    }


    public class Move{

        private int playedSide;
        private Domino domino;

        public Move(int playedSide, Domino domino) {
            this.playedSide = playedSide;
            this.domino = domino;
        }

        public int getPlayedSide() {
            return playedSide;
        }

        public Domino getDomino() {
            return domino;
        }
    }


    public static String toStringPlayer(int playerType){
          switch (playerType){
              case PLAYER_1:
                  return "Player 1";
              case PLAYER_2:
                  return "Player 2";
              case PLAYER_3:
                  return "Player 3";
              case PLAYER_4:
                  return "Player 4";
              default:
                  return "Error";
          }

    }

    public List<Domino> getPosibleDominosList(int value){
        List<Domino> ret = new ArrayList<Domino>();
        for(Domino e: hand){
            if(e.compare(value) == Domino.LOWER_SIDE || e.compare(value) == Domino.UPPER_SIDE){
                ret.add(e);
            }
        }
        return ret;
    }



}
