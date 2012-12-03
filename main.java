/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnets;

import java.math.*;
import java.util.*;

/**
 *
 * @author AMRoche
 */
public class main {
    //calc type, level, output
 //   private int[] errormin = new int[2];
    public static node alpha = new node("alpha", "sigmoid", 0, 0);
    public static node gamma = new node("gamma", "sigmoid", 0, 0);
    public static node delta = new node("delta", "sigmoid", 0, 0);
    public static node sum = new node("sum", "summation", 1, 0);
    public static node x1 = new node("x1", "none", -1, 0);
    public static node x2 = new node("x2", "none", -1, 0);
    public static connector source1Alpha = new connector(x1, alpha, -0.15, 0);
    public static connector source1Sum = new connector(x1, sum, -0.1, 1);
    public static connector source1Delta = new connector(x1, delta, 0.1, 0);
    public static connector source2Gamma = new connector(x2, gamma, -0.4, 0);
    public static connector source2Delta = new connector(x2, delta, -0.3, 0);
    public static connector AlphaSum = new connector(alpha, sum, 0.2, 1);
    public static connector GammaSum = new connector(gamma, sum, 0.15, 1);
    public static connector DeltaSum = new connector(delta, sum, 0.4, 1);
    public static double learnRate = 1;
    public static int[][] learnExamples = {{1, 0, 1}, {1, 1, 0}};
    public static boolean setup = false;
    public static ArrayList<connector> connectors = new <connector>ArrayList();
    public static ArrayList<node> nodes = new <node>ArrayList();
    static double tiermax = -1;
    public static double minError[] = {0.000,1000.000};
    /**
     * @param args the command line arguments
     */
    public static void setNetwork() {
        if (setup == false) {
            nodes.add(alpha);
            nodes.add(gamma);
            nodes.add(delta);
            nodes.add(sum);
            nodes.add(x1);
            nodes.add(x2);
            connectors.add(source1Alpha);
            connectors.add(source1Sum);
            connectors.add(source1Delta);
            connectors.add(source2Gamma);
            connectors.add(source2Delta);
            connectors.add(AlphaSum);
            connectors.add(GammaSum);
            connectors.add(DeltaSum);
            setup = true;
         for (int v = 0; v < nodes.size(); v++) {
            if (((node) (nodes.get(v))).tier > tiermax) {
                tiermax = ((node) (nodes.get(v))).tier;
            }
        }
            System.out.println(tiermax + "--------------------------");
            //   System.out.println("LOL:"+alpha.getClass().getName());
            // System.out.println(((connector)(connectors.get(0))).end == ((node)(nodes.get(0))));
        }
    }

    public static void forwardPass(int tests) {
        for (int tiers = -1; tiers <= tiermax; tiers++) {
            int test = 0;
            //cycle through connectors first, then through nodes.
            for (int cSize = 0; cSize < connectors.size(); cSize++) {
                //System.out.println(cSize);   
                if (((connector) (connectors.get(cSize))).tier == tiers) {
                    ((connector) (connectors.get(cSize))).output = ((connector) (connectors.get(cSize))).calculation(((connector) (connectors.get(cSize))).start.output);
                }
            }
            //input nodes are on the -1 tier, and inputs are set from the array further up.
            for (int nSize = 0; nSize < nodes.size(); nSize++) {
                double addedInputs = 0;
                if (((node) (nodes.get(nSize))).tier == tiers && tiers == -1) {
                    ((node) (nodes.get(nSize))).output = learnExamples[tests][test];
                    test = test + 1;
                }
                if (((node) (nodes.get(nSize))).tier == tiers && tiers > -1) {
                    ArrayList inputs = new ArrayList();

                    for (int cs = 0; cs < connectors.size(); cs++) {
                        if (((connector) (connectors.get(cs))).end == ((node) (nodes.get(nSize)))) {
                            inputs.add(((connector) (connectors.get(cs))).output);
                        }
                    }
                    for (int added = 0; added < inputs.size(); added++) {
                        addedInputs += (double) (inputs.get(added));
                    }
                    ((node) (nodes.get(nSize))).output = ((node) (nodes.get(nSize))).calculation(addedInputs);
                    if (tiers == tiermax) {
                        ArrayList thing = new ArrayList();
                        for (int cSize = 0; cSize < connectors.size(); cSize++) {
                            thing.add(((connector) (connectors.get(cSize))).weight);
                        }
                        // System.out.println(thing);
                        if (tiers == tiermax) {
                            System.out.println("final output: " + learnExamples[tests][2] + ":" + ((node) (nodes.get(nSize))).output); //outputs final solution from thingy.
                        }
                        thing.clear();
                        inputs.clear();
                    }
                }
            }
            test = 0;
        }
    }

    public static void backPass(int tests, int gens) {
        for (int tiers = (int) tiermax; tiers >= -1; tiers--) {
            for (int nSize = 0; nSize < nodes.size(); nSize++) {
                if (((node) (nodes.get(nSize))).tier == tiers && tiers == tiermax) {
                    ((node) (nodes.get(nSize))).error = learnExamples[tests][2] - (nodes.get(nSize)).output;
                    if((((node) (nodes.get(nSize))).error * ((node) (nodes.get(nSize))).error) < minError[1]){
                        minError[0] = (double)gens;
                        minError[1] = ((double)((node) (nodes.get(nSize))).error * (double)((node) (nodes.get(nSize))).error);
                    }
                }
                
                else if (((node) (nodes.get(nSize))).tier == tiers && tiers < tiermax && tiers > -1) {
                    ArrayList tester = new ArrayList();
                    double testerSet = 0;
                    for (int cSize = 0; cSize < connectors.size(); cSize++) {
                        if (((connector) (connectors.get(cSize))).start == ((node) (nodes.get(nSize)))) {
                            tester.add(((((connector) (connectors.get(cSize))).end).error) * (((connector) (connectors.get(cSize))).weight));
                        }
                    }
                    for (int testerLength = 0; testerLength < tester.size(); testerLength++) {
                        testerSet += (double) tester.get(testerLength);
                    }
                    ((node) (nodes.get(nSize))).error = ((node) (nodes.get(nSize))).output * (1 - ((node) (nodes.get(nSize))).output) * testerSet;
                    tester.clear();
                    testerSet = 0;
                }
            }
            for (int cSize = 0; cSize < connectors.size(); cSize++) {
                if (((connector) (connectors.get(cSize))).tier == tiers) {
                    ((connector) (connectors.get(cSize))).newweight = learnRate * ((((connector) (connectors.get(cSize))).end).error) * ((((connector) (connectors.get(cSize))).start).output);
                    System.out.println(((connector) (connectors.get(cSize))).start.nodeName + ":" + ((connector) (connectors.get(cSize))).start.error);
                }
            }
        }
        for (int cSize = 0; cSize < connectors.size(); cSize++) {
            ((connector) (connectors.get(cSize))).weight += ((connector) (connectors.get(cSize))).newweight;
            ((connector) (connectors.get(cSize))).newweight = 0;
        }
    }

    public static void main(String[] args) {
        setNetwork();
        //change this for number of generations to run over.
        for (int generations = 0; generations < 1000; generations++) {
//substitute 1 for learnExamples.length once done
            for (int tests = 0; tests < learnExamples.length; tests++) {
//replace -1 with tiermax
                forwardPass(tests);
                //Forward pass is up to this point. Outputs have all been set. Now for back-passes, error calculations and weight mods.
                backPass(tests , generations);
              //  sum.error;
            }
        }
    System.out.println("Min Error at [Generation, Error]; "+java.util.Arrays.toString(minError));
    }
}
