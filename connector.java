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
public class connector {
     public node start;
     public node end;
     public double tier;
     public double weight;
     public double output;
     public double error;
     public double newweight;

    public double calculation(double input) {
        output = this.weight*input;
        return output;
    }
    
    connector(node Origin, node Endpoint, double Weighted, double tyer){
    start = Origin;
    end = Endpoint;
    weight = Weighted;
    tier = tyer;
    }
}
