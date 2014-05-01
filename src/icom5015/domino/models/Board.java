package icom5015.domino.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enrique on 4/29/14.
 */
public class Board {




    private int upperValue;
    private int lowerValue;

    private int tileCount = 0;
    private int sum = 0;
    private int totals[] = new int[Domino.PLAYING_ALPHABET.length];


    public DominoNode centerNode;
    public DominoNode upperBoardNode;
    public DominoNode lowerBoardNode;

    private List<Domino> p1;
    private List<Domino> p2;
    private List<Domino> p3;
    private List<Domino> p4;



    public Board(Domino center, int player){

        p1 = new ArrayList<Domino>();
        p2 = new ArrayList<Domino>();
        p3 = new ArrayList<Domino>();
        p4 = new ArrayList<Domino>();

        //Set Center Board
        centerNode = new DominoNode(null, null, center);
        upperBoardNode = null;
        lowerBoardNode = null;

        this.upperValue = center.getUpperSide();
        this.lowerValue = center.getLowerSide();

        updateValue(center);
        updatePlayerList(player, center);



    }


    public void setUpperValue(Domino upper, int player){

        if(upper.getLowerSide() != this.upperValue){
            upper.rotate();
        }

        //Set Center Node
        if(this.upperBoardNode == null){

            this.upperBoardNode = new DominoNode(null, centerNode, upper);
            centerNode.upperSideConnection = this.upperBoardNode;
        }
        else{
            DominoNode tmpNode = new DominoNode(null, upperBoardNode, upper );
            upperBoardNode.upperSideConnection = tmpNode;
            upperBoardNode = tmpNode;
        }
        this.upperValue = upper.getUpperSide();

        updateValue(upper);
        updatePlayerList(player, upper);

    }

    public void setLowerValue(Domino lower, int player){

        if(lower.getUpperSide() != this.lowerValue){
            lower.rotate();
        }

        //Set Center Node
        if(this.lowerBoardNode == null){

            this.lowerBoardNode = new DominoNode(centerNode, null, lower);
            centerNode.lowerSideConnection = this.lowerBoardNode;


        }
        else{
            DominoNode tmpNode = new DominoNode( lowerBoardNode, null ,lower );
            lowerBoardNode.lowerSideConnection = tmpNode;
            lowerBoardNode = tmpNode;
        }

        this.lowerValue = lower.getLowerSide();
        updateValue(lower);
        updatePlayerList(player, lower);

    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        DominoNode currNode = upperBoardNode;
        if(currNode == null){
            currNode = centerNode;
        }
        do{
            stringBuilder.append(currNode.currentDomino.toString());
            stringBuilder.append("-");
            currNode = currNode.lowerSideConnection;
        } while (currNode != null);

        return stringBuilder.toString();
    }

    //All Ways Between [0, Domino.PLAYING_ALPHABET.length]
    private void updateValue(Domino domino) {

        if(domino.isDouble())
            totals[domino.getLowerSide()] += 1;
        else{
            totals[domino.getLowerSide()] += 1;
            totals[domino.getUpperSide()] += 1;
        }

        sum += domino.getLowerSide();
        sum += domino.getUpperSide();
       tileCount++;
    }

    private void updatePlayerList(int player, Domino domino){
        switch (player){
            case Player.PLAYER_1:
                p1.add(domino);
                break;
            case Player.PLAYER_2:
                p2.add(domino);
                break;
            case Player.PLAYER_3:
                p3.add(domino);
                break;
            case Player.PLAYER_4:
                p4.add(domino);
                break;

        }

    }



    //Getters
    public int getLowerValue() {
        return lowerValue;
    }

    public int getUpperValue() {
        return upperValue;
    }

    public int getTileCount(){
        return tileCount;
    }

    public int getTotalSum(){ return sum;}

    public int getTotalPlayedByTile(int tile){
        return totals[tile];
    }

    public void setPass(int player){
        switch (player){
            case Player.PLAYER_1:
                p1.add(new Domino(Domino.PASS_VALUE, Domino.PASS_VALUE));
                break;
            case Player.PLAYER_2:
                p2.add(new Domino(Domino.PASS_VALUE, Domino.PASS_VALUE));
                break;
            case Player.PLAYER_3:
                p3.add(new Domino(Domino.PASS_VALUE, Domino.PASS_VALUE));
                break;
            case Player.PLAYER_4:
                p4.add(new Domino(Domino.PASS_VALUE, Domino.PASS_VALUE));
                break;

        }
    }

    //If Domino Upperside || Lowerside == Pass
    // Move was pass
    public List<Domino> getMoveOfPlayer(int player){
        switch (player){
            case Player.PLAYER_1:
                return p1;
            case Player.PLAYER_2:
                return p2;
            case Player.PLAYER_3:
                return p3;
            case Player.PLAYER_4:
                return p4;

        }
        return null;
    }


    //Node Class
    public class DominoNode{

        public DominoNode upperSideConnection;
        public DominoNode lowerSideConnection;
        public Domino currentDomino;

        public DominoNode(DominoNode upperSideConnection, DominoNode lowerSideConnection, Domino currentDomino) {
            this.upperSideConnection = upperSideConnection;
            this.lowerSideConnection = lowerSideConnection;
            this.currentDomino = currentDomino;
        }



    }




}
