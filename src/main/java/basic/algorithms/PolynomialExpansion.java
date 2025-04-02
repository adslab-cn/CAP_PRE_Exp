package basic.algorithms;
/**
 *
 *Created by Genghui Chi on 2023/4/21
 *
 * calculate the coefficients of polynomial（x+a1）(x+a2)...(x+an)
 */

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

public class PolynomialExpansion {
    public static Element[] calculateCoefficient(Element[] aN, Field F){
        int n = aN.length ;
        Element[] coefficients = new Element[n + 1];
        coefficients[n] = F.newOneElement().getImmutable();
        for (int i = 0; i < n ; i++){
            coefficients[i] = F.newZeroElement().getImmutable();
        }
        for (int k = 0; k < n; k++) {
            for (int i = n - k - 1; i < n - 1; i++) {
                coefficients[i] = coefficients[i].add(coefficients[i + 1].mulZn(aN[k])).getImmutable();
            }
            coefficients[n-1] = coefficients[n-1].add(aN[k]).getImmutable();
        }
        return coefficients ;
    }
}
