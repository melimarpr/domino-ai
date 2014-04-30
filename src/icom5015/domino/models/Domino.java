package icom5015.domino.models;

import java.util.Map;

/**
 * Created by enrique on 4/29/14.
 */
public class Domino {

    //Side Constance
    public static final int UPPER_SIDE = -1;
    public static final int LOWER_SIDE = -2;
    public static final int ERROR = Integer.MIN_VALUE;


    //Play Numbers
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE  = 5;
    public static final int SIX = 6;


    public static final int[] PLAYING_ALPHABET = {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX};


    //Intance Fields
    private int upperSide;
    private int lowerSide;


    public Domino(int upperSide, int lowerSide){
        this.lowerSide = lowerSide;
        this.upperSide = upperSide;
    }

    public int getLowerSide() {
        return lowerSide;
    }

    public int getUpperSide() {
        return upperSide;
    }

    public int getFullSum(){
        return lowerSide+upperSide;
    }

    public boolean isDouble(){
        return upperSide == lowerSide;
    }


    public int compare(int number){
        if (number == upperSide)
                return UPPER_SIDE;
        else if(number == lowerSide)
                return LOWER_SIDE;
        else
            return ERROR;

    }

    //Print Domino Style
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append(" | ");
        str.append(this.upperSide);
        str.append(" | ");
        str.append(this.lowerSide);
        str.append(" | ");

        return str.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Domino domino = (Domino) obj;
        return (this.lowerSide == domino.lowerSide && this.upperSide == domino.upperSide) || (this.upperSide == domino.lowerSide && this.lowerSide == domino.upperSide);

    }

    //Rotate UpperSide and Lower Side
    public void rotate(){
        int tmpUpper = this.upperSide;
        this.upperSide = this.lowerSide;
        this.lowerSide = tmpUpper;
    }
}
