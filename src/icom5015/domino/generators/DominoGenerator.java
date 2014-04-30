package icom5015.domino.generators;

import icom5015.domino.models.Domino;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by enrique on 4/29/14.
 */
public class DominoGenerator {


    private List<Domino> boardList;
    //Level 2: Domino
    //Level 3:


    private DominoGenerator(){
        boardList = new LinkedList<Domino>();
    }

    public static List<Domino> generateDominoBoard(){
        List<Domino> dominoes = new LinkedList<Domino>();

        for(int i = 0; i < Domino.PLAYING_ALPHABET.length; i++){
            for(int j= 0; j<Domino.PLAYING_ALPHABET.length; j++){
                Domino e = new Domino(Domino.PLAYING_ALPHABET[i], Domino.PLAYING_ALPHABET[j]);
                if(!in(dominoes, e ))
                     dominoes.add(e);
            }
        }
        return  dominoes;
    }

    private static boolean in(List<Domino> dominoes, Domino domino){

        for(Domino e : dominoes){
            if(e.equals(domino)){
                return true;
            }
        }
        return false;

    }

}
