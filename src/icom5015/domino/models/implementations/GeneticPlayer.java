package icom5015.domino.models.implementations;

import icom5015.domino.models.Board;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;

import java.util.List;

/**
 * Created by enrique on 4/30/14.
 */
public class GeneticPlayer extends Player {

    //TODO: Implement

    public GeneticPlayer(List<Domino> hand) {
        super(hand);
    }

    @Override
    public Move getDomino(Board board) {
        return null;
    }
}
