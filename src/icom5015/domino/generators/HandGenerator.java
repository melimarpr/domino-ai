package icom5015.domino.generators;

import com.sun.java.browser.plugin2.DOM;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;

import java.util.*;

/**
 * Created by enrique on 4/29/14.
 */
public class HandGenerator {



    public List<Domino> dominoes;

    public HandGenerator(List<Domino> dominoes) {
        this.dominoes = dominoes;
    }


    //If No More Dominoes Empty List
    public List<Domino> generateHand(boolean manual){


        if(dominoes.size() == 0){
            return new ArrayList<Domino>();
        }

        if (manual){
            return manualSelectSeven();
        }

        return autoSelectSeven();

    }


    private List<Domino> manualSelectSeven(){

        //Scanner
        Scanner in = new Scanner(System.in);

        //List
        List<Domino> ret = new ArrayList<Domino>();

        //Title
        System.out.println("Select From Available Dominoes: ");

        for(int i=0; i<7; i++){
            System.out.println(Player.toStringDominoList(dominoes));
            int index = in.nextInt();
            ret.add(dominoes.remove(index));
        }

        return ret;
    }

    private List<Domino> autoSelectSeven(){

        Collections.shuffle(dominoes);
        List<Domino> ret = new ArrayList<Domino>();

        for(int i = 0; i<7; i++ ){
            ret.add(dominoes.remove(0));
        }
        return ret;
    }





}
