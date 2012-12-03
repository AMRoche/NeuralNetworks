/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnets;

import java.math.*;

/**
 *
 * @author AMRoche
 */
public class node {
    public String nodeName;
    public double weight;
    public String calcType;
    public double tier;
    public double error;
    public double output;

    public double calculation(double input) {
        if (calcType == "sigmoid") {
            return 1 / (1 + (Math.exp(input*-1)));
        } 
        if(calcType == "summation"){
            output = input;
            return output;
        }
        if(calcType == "none"){
            output = input;
            return output;
        }
        else {
            return 0;
        }
    }
    node(){}
    node(String Nodie, String Calculon, double tyer, double out){
     if(Calculon != ""){
         calcType = Calculon;  
        }
        else{
         calcType = "sigmoid";
        }
     this.nodeName = Nodie;
     this.output = out;
     this.tier = tyer;
    }
}
