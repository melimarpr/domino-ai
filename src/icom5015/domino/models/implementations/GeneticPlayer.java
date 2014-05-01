package icom5015.domino.models.implementations;

import com.sun.deploy.util.StringUtils;
import icom5015.domino.models.Board;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by enrique on 4/30/14.
 */
public class GeneticPlayer extends Player {
    int totalAtStart = 0;
    List<Double> coefficients;

    public GeneticPlayer(List<Domino> hand, int myPlayer) {
        super(hand, myPlayer);

        for(Domino d : hand) {
            this.totalAtStart += d.getFullSum();
        }

        coefficients = new ArrayList<Double>();
        try {
            Scanner s = new Scanner(new File("actualc.txt"));
            //System.out.println("Im alive!");
            if (s.hasNextLine()) {
                String line = s.nextLine();
                //System.out.println(line);
                String[] temp = StringUtils.splitString(line, ",");

                for (String n : temp) {
                    //System.out.println(n);
                    coefficients.add(Double.parseDouble(n));
                }
            }

            s.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    @Override
    public Move getDomino(Board board) {
        List<Domino> upperSide = getPosibleDominosList(board.getUpperValue());
        List<Domino> lowerSide = getPosibleDominosList(board.getLowerValue());

        if (upperSide.size() == 0 && lowerSide.size() == 0) {
            return new Move(PASS, null);
        }

        if (upperSide.size() == 0 && lowerSide.size() == 1) {
            hand.remove(lowerSide.get(0));
            return new Move(Domino.LOWER_SIDE, lowerSide.get(0));
        }

        if (lowerSide.size() == 0 && upperSide.size() == 1) {
            hand.remove(upperSide.get(0));
            return new Move(Domino.UPPER_SIDE, upperSide.get(0));
        }

        int points;

        double genValue;
        double maxLowerPoints = 0.0;
        Domino maxLowerDomino = null;
        double maxUpperPoints = 0.0;
        Domino maxUpperDomino = null;

        points = board.getUpperValue();
        for (Domino d : upperSide) {
            genValue = geneticFormula(board, d, points);

            if (genValue > maxUpperPoints) {
                maxUpperPoints = genValue;
                maxUpperDomino = d;
            }
        }

        points = board.getLowerValue();
        for (Domino d : lowerSide) {
            genValue = geneticFormula(board, d, points);

            if (genValue > maxLowerPoints) {
                maxLowerPoints = genValue;
                maxLowerDomino = d;
            }
        }

        if (maxLowerPoints > maxUpperPoints) {
            //System.out.println("LOWER... " + maxLowerPoints + " > " + maxUpperPoints);
            hand.remove(maxLowerDomino);
            return new Move(Domino.LOWER_SIDE, maxLowerDomino);
        } else if (maxUpperPoints > maxLowerPoints) {
            //System.out.println("UPPER... " + maxUpperPoints + " > " + maxLowerPoints);
            hand.remove(maxUpperDomino);
            return new Move(Domino.UPPER_SIDE, maxUpperDomino);
        } else {
            Random rnd = new Random();

            if (rnd.nextDouble() > 0.5) {
                //System.out.println("RANDOM LOWER... " + maxLowerPoints + " > " + maxUpperPoints);
                hand.remove(maxLowerDomino);
                return new Move(Domino.LOWER_SIDE, maxLowerDomino);
            } else {
                //System.out.println("RANDOM UPPER... " + maxUpperPoints + " > " + maxLowerPoints);
                hand.remove(maxUpperDomino);
                return new Move(Domino.UPPER_SIDE, maxUpperDomino);
            }
        }
    }

    @Override
    public Domino getFirstMoveDomino() {
        double genValue;
        double maxPoints = 0.0;
        Domino maxDomino = null;

        for (Domino d : this.hand) {
            // Special formula for first tile.
            // The other variables and coefficients are not important because they depend on enemies and partners
            // and since this is the first play, it will not have any effect.
            genValue = coefficients.get(0) * d.getFullSum() + coefficients.get(1) * this.getV2(d);

            if (genValue > maxPoints) {
                maxPoints = genValue;
                maxDomino = d;
            }
        }

        hand.remove(maxDomino);

        return maxDomino;
    }

    private double geneticFormula(Board board, Domino tile, int points) {
        return coefficients.get(0) * this.getV1(board, tile) +
                coefficients.get(1) * this.getV2(tile) +
                coefficients.get(2) * this.getV3(board, points) +
                coefficients.get(3) * this.getV4(board, points) +
                coefficients.get(4) * this.getV5(board, points);
    }

    // Sum of Points in Board if the tile where throwed.
    private double getV1(Board board, Domino tile) {
        return (double)(board.getTotalSum() + tile.getFullSum());
    }

    // How much points will be left on my hand after throwing that tile.
    private double getV2(Domino tile) {
        return this.getHandSum() - tile.getFullSum();
    }

    // Percent of Tiles used for points to be thrown.
    public double getV3(Board board, int points) {
        return (double)board.getTotalPlayedByTile(points) / 7.0;
    }

    // Percent of tiles with this points did my partner use.
    public double getV4(Board board, int points) {
        int count = 0;
        List<Domino> partnerMoves = board.getMoveOfPlayer(this.getPartner());

        for (Domino d : partnerMoves) {
            if (d.getUpperSide() == points || d.getLowerSide() == points) {
                count++;
            }
        }

        return (double)count / 7.0;
    }

    // Percent of tiles with this points my nearest enemy used.
    public double getV5(Board board, int points) {
        int count = 0;
        List<Domino> partnerMoves = board.getMoveOfPlayer(this.getNextEnemy());

        for (Domino d : partnerMoves) {
            if (d.getUpperSide() == points || d.getLowerSide() == points) {
                count++;
            }
        }

        return (double)count / 7.0;
    }

    private int getPartner() {
        switch (this.getMyPlayer()) {
            case PLAYER_1:
                return PLAYER_3;
            case PLAYER_2:
                return PLAYER_4;
            case PLAYER_3:
                return PLAYER_1;
            case PLAYER_4:
                return PLAYER_2;
        }

        return -1;
    }

    private int getNextEnemy() {
        //System.out.println(this.getMyPlayer() + " Enemy: " + (this.getMyPlayer() + 1) % 4);
        return (this.getMyPlayer() + 1) % 4;
    }

}
