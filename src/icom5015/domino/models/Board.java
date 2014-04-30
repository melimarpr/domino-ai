package icom5015.domino.models;

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


    public Board(Domino center){

        //Set Center Board
        centerNode = new DominoNode(null, null, center);
        upperBoardNode = null;
        lowerBoardNode = null;

        this.upperValue = center.getUpperSide();
        this.lowerValue = center.getUpperSide();

        updateValue(center);

    }


    public void setUpperValue(Domino upper){

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

    }

    public void setLowerValue(Domino lower){

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

    public int getTileCount(){
        return tileCount;
    }

    //Getters
    public int getLowerValue() {
        return lowerValue;
    }

    public int getUpperValue() {
        return upperValue;
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
