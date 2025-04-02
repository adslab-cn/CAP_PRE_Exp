package bupt.CAPPRE;

import it.unisa.dia.gas.jpbc.Element;

public class CAPPRECipherTextPrime {
    Element C ;
    Element C_til ;
    Element C_0 ;
    Element[] C_i ;
    Element E ;
    Element E0 ;
    Element E1 ;
    Element E2 ;

    public CAPPRECipherTextPrime(Element c, Element c_til, Element c_0, Element[] c_i, Element e, Element e0, Element e1, Element e2) {
        C = c;
        C_til = c_til;
        C_0 = c_0;
        C_i = c_i;
        E = e;
        E0 = e0;
        E1 = e1;
        E2 = e2;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.C.getLengthInBytes();
        sum += this.C_til.getLengthInBytes();
        sum += this.C_0.getLengthInBytes();
        for (Element i : C_i){
            sum += i.getLengthInBytes();
        }
        sum += this.E.getLengthInBytes();
        sum += this.E0.getLengthInBytes();
        sum += this.E1.getLengthInBytes();
        sum += this.E2.getLengthInBytes();

        return sum ;
    }
}
