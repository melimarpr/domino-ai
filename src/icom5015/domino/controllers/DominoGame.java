package icom5015.domino.controllers;

import icom5015.domino.models.Player;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by enrique on 4/29/14.
 */
public abstract class DominoGame {


    protected int[] order = new int[Player.PLAYER_QUANTITY];
    protected Map<Integer, Player> players;


    public abstract void run();
    public abstract int winner();
    public abstract int score();


    protected void generateOrder(){

        Map.Entry<Integer, Player> first = null;
        for(Map.Entry<Integer, Player> e: players.entrySet()){
            if(e.getValue().hasDobleSix()){
                first = e;
                break;
            }
        }

        //Lazy Implementation
        switch (first.getKey()){
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




}
