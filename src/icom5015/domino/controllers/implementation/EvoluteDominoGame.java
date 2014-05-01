package icom5015.domino.controllers.implementation;

import com.sun.deploy.util.StringUtils;
import icom5015.domino.controllers.DominoGame;
import icom5015.domino.models.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by root on 5/1/14.
 *
 1. 45.0 1.2836022375495837,2.8537857927115584,8.51168713564064,4.3202510645277545,5.585125202645818,0.603549145451765
 2. 65.0 5.8592800547607045,3.2785503145962593,2.163006050216251,8.64943754681923,9.738439852279445,2.292954302716528
 3. 65.0 1.997464922943143,6.335622172249318,7.935902727559916,8.077740493998823,3.6891620061449606,2.546376304018938
 4. 55.00000000000001 3.3042331022478844,8.261263044638122,0.817858344255894,5.278576384594292,6.92581590839379,7.581521466239992
 5. 60.0 6.0003793711414035,2.3672334724811703,8.477034352107404,0.5256682213144193,8.681502739401754,1.8227039737264006
 6. 55.00000000000001 9.608137325176939,2.3967247376348464,2.6568907604054015,5.627963205650771,8.258120609340786,9.337645924581283
 7. 60.0 5.175037028606173,5.648057471428819,0.8641566008427837,6.194859063187417,4.423883715268422,2.606788824013476
 8. 55.00000000000001 1.3034896289402986,6.515374095873852,0.6447132486183993,9.392518685112304,6.586342643915629,8.28070885980614
 9. 55.00000000000001 0.2705829103536217,9.042368328871923,7.855075204494158,5.445838640567446,7.800185182041197,1.1996677482718854
 10. 50.0 7.193649248922687,1.355216670337328,1.7707523527094626,3.692143113754005,5.509935224569585,4.062651484402583
 11. 55.00000000000001 8.733198150537536,1.060215561568627,4.901888247184884,7.228459652621423,3.787426161124714,2.8769183014694946
 12. 50.0 6.2246188733657295,7.902900573515356,0.6605476172176339,9.167100110240403,5.406800370887959,2.856723259367624
 13. 35.0 7.760074848079364,5.923984718573036,4.930889939454549,3.7694320697317476,6.239065578040885,6.8064752447736065
 14. 40.0 7.509076129134095,2.0864219716663124,9.59301614948939,4.605416501481198,7.826362561268343,0.8342437920628243
 15. 60.0 9.676473508998415,4.449630956620847,7.128143041635479,2.054855255416941,3.9667695674404757,7.691140570027172
 16. 55.00000000000001 3.473766712778147,2.1364797000033873,0.03724027521280315,0.718629627486892,1.4470687961333661,5.935199801207245
 17. 50.0 9.66829905090721,8.720358117594731,7.045605503540991,2.998655657506506,7.509337530782969,6.303665555838743
 18. 60.0 3.0264799415147414,9.246195487195706,5.813511778310287,2.975195827621185,1.8717409705790888,7.34656661827776
 19. 55.00000000000001 3.2542383376860986,4.471031392997147,7.220475920297783,2.547448080095699,2.9976147150368835,1.2784055772473635
 20. 55.00000000000001 5.324250691145486,6.244492755403137,7.122866318056518,1.3003899722158074,0.6802671472329047,4.735082125222712
 21. 55.00000000000001 2.647504979190689,3.112853480801918,8.870757572030266,9.068832880418253,7.469823582685371,4.441693507493039
 22. 75.0 5.047870075331547,1.2987057267231517,8.236080462568253,8.928114594828912,8.382832320687903,0.7351352596204275

 71.0 3.777402310801171,8.69369228109311,9.824239228554687,8.586762778748142,8.743093900621716,4.78991340262086
 */
public class EvoluteDominoGame {
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
        List<Coefficients> randomRun = firstRun();
        List<Coefficients> top = keepTop10(randomRun);
        List<String> evolutions = new ArrayList<String>();

        Coefficients temp;

        // With the Top 10 generated genetic formulas, we will do 5 runs to evolute it, using two-point cross-over.
        for (int m = 0; m < 5; m++) {
            evolutions.clear();

            for (int i = 0; i < top.size() - 1; i++) {
                evolutions.add(crossOver(top.get(i).getCoefficients(), top.get(i + 1).getCoefficients()));
                evolutions.add(crossOver(top.get(i + 1).getCoefficients(), top.get(i).getCoefficients()));
            }

            for (String gen : evolutions) {
                saveGenToFile(gen);

                temp = trialRun();
                temp.setMethod(Coefficients.CROSSOVER);

                top.add(temp);
            }

            Collections.sort(top, new CoefficientsComparator());
        }

        for (int i = 0; i < top.size(); i++) {
            if (i >= top.size()) {
                break;
            }

            System.out.println((i+1) + ". " + top.get(i));
        }
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
            //System.out.println("Run: " + ++c);

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
        int games = 100;
        int won = 0;

        for (int i = 0; i < games; i++) {
            DominoGame dominoGame = new AutomationDominoGame();
            dominoGame.run();
            System.out.println("Winner " + Player.toStringPlayer(dominoGame.winner()) + " with score: " + dominoGame.score());

            if (dominoGame.winner() == 0 || dominoGame.winner() == 2) {
                won++;
            }
        }

        Coefficients cofs = new Coefficients(games, won, actualGen);

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
            System.out.println(gen);
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
