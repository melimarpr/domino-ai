package icom5015.domino.controllers.implementation;

import com.sun.deploy.util.StringUtils;
import icom5015.domino.controllers.DominoGame;
import icom5015.domino.models.Player;

import javax.naming.ContextNotEmptyException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class EvoluteDominoGame {
    static final int trialGames = 20;
    static final int roundsOfEvolution = 3;

    String actualGen;

    class Coefficients {
        public static final int RANDOM = 0;
        public static final int CROSSOVER = 1;

        int method;
        int games;
        int won;
        double winningRate;

        String coefficients;

        public Coefficients(int games, int won, String coefficients) {
            this.games = games;
            this.won = won;
            this.coefficients = coefficients;
            this.winningRate = ((double)won / (double)games) * 100.0;
            this.method = RANDOM;
        }

        public double getWinningRate() {
            return winningRate;
        }

        public String getCoefficients() {
            return coefficients;
        }

        public String toString() {
            return this.getWinningRate() + "% " + this.getCoefficients() + " (" + this.getMethod() + ")";
        }

        private void setMethod(int method) {
            this.method = method;
        }

        private String getMethod() {
            if (method == RANDOM)
                return "Random";
            else if (method == CROSSOVER)
                return "Two-Point Crossover";
            else
                return "Unknown";
        }
    }

    class CoefficientsComparator implements Comparator<Coefficients> {
        public int compare(Coefficients o1, Coefficients o2) {
            return (int)(o2.getWinningRate() - o1.getWinningRate());
        }
    }

    public void start() {
        double winningRates = 0;
        Coefficients cof;

        for (int i = 0; i < this.trialGames; i++) {
            cof = trialRun();
            winningRates += cof.getWinningRate();
        }

        System.out.println(winningRates / this.trialGames);
    }

    public void start2() {
        List<Coefficients> randomRun = firstRun();
        List<Coefficients> top = keepTop10(randomRun);
        List<String> evolutions = new ArrayList<String>();

        Coefficients temp;

        // With the Top 10 generated genetic formulas, we will do 5 runs to evolute it, using two-point cross-over.
        for (int m = 0; m < this.roundsOfEvolution; m++) {
            evolutions.clear();

            /*for (int i = 0; i < top.size() - 1; i++) {
                evolutions.add(crossOver(top.get(i).getCoefficients(), top.get(i + 1).getCoefficients()));
                evolutions.add(crossOver(top.get(i + 1).getCoefficients(), top.get(i).getCoefficients()));
            }*/

            for (int i = 0; i < top.size(); i++) {
                for (int j = 0; j < top.size(); j++) {
                    if (i == j)
                        continue;
                    evolutions.add(crossOver(top.get(i).getCoefficients(), top.get(j).getCoefficients()));
                    evolutions.add(crossOver(top.get(j).getCoefficients(), top.get(i).getCoefficients()));
                }
            }

            for (String gen : evolutions) {
                saveGenToFile(gen);

                temp = trialRun();
                temp.setMethod(Coefficients.CROSSOVER);

                top.add(temp);
            }

            Collections.sort(top, new CoefficientsComparator());
        }

        // Top 20 generated records
        for (int i = 0; i < 20; i++) {
            if (i >= top.size()) {
                break;
            }

            System.out.println((i+1) + ". " + top.get(i));
        }

        System.out.println("Total Generated Evolutions: " + top.size());
        System.out.println("Saving to file the best generated Genetic Formula... " + top.get(0));
        saveGenToFile(top.get(0).getCoefficients());
    }

    // Two point cross over. X Y Y X Y
    public String crossOver(String gen1, String gen2) {
        String[] gen1List = StringUtils.splitString(gen1, ",");
        String[] gen2List = StringUtils.splitString(gen2, ",");
        String[] genOut = {gen1List[0], gen2List[1], gen2List[2], gen1List[3], gen2List[4]};

        return StringUtils.join(Arrays.asList(genOut), ",");
    }

    private List<Coefficients> firstRun() {
        // This first run generate random numbers until it gets more than 70% in winning rate. After that, it picks
        // the best 10 combinations (with greater winning rate) to use that to start doing evolutions to the genetic algorithm.

        List<Coefficients> runs = new ArrayList<Coefficients>();

        Coefficients cofs;
        int c = 0;

        while (true) {
            genRandom();
            cofs = trialRun();

            if (cofs.getWinningRate() > 60) {
                runs.add(cofs);
            }

            c++;

            if (cofs.getWinningRate() > 70.0) {
                break;
            }
        }

        Collections.sort(runs, new CoefficientsComparator());

        for (int i = 0; i < 10; i++) {
            if (i >= runs.size()) {
                break;
            }

            System.out.println((i+1) + ". " + runs.get(i));
        }

        System.out.println("Total runs: " + c);

        return runs;
    }

    private Coefficients trialRun() {
        int won = 0;

        for (int i = 0; i < this.trialGames; i++) {
            DominoGame dominoGame = new AutomationDominoGame();
            dominoGame.run();
            //System.out.println("Winner " + Player.toStringPlayer(dominoGame.winner()) + " with score: " + dominoGame.score());

            if (dominoGame.winner() == 0 || dominoGame.winner() == 2) {
                won++;
            }
        }

        Coefficients cofs = new Coefficients(this.trialGames, won, actualGen);

        return cofs;
    }

    private void genRandom() {
        Random rnd = new Random();

        List<String> coefficients = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            coefficients.add(i, String.valueOf(rnd.nextDouble() * 10.0));
        }

        saveGenToFile(StringUtils.join(coefficients, ","));
    }

    private void saveGenToFile(String gen) {
        try {
            //System.out.println(gen);
            actualGen = gen;

            PrintWriter writer = new PrintWriter("actualc.txt", "UTF-8");
            writer.println(gen);
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private List<Coefficients> keepTop10(List<Coefficients> cofs) {
        List<Coefficients> cofsTop = new ArrayList<Coefficients>();

        for (int i = 0; i < 10; i++) {
            if (i >= cofs.size()) {
                break;
            }

            cofsTop.add(cofs.get(i));
        }

        cofs.clear();
        return cofsTop;
    }
}
