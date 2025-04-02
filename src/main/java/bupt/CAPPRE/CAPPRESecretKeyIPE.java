package bupt.CAPPRE;

import it.unisa.dia.gas.jpbc.Element;

public class CAPPRESecretKeyIPE {
    Element K0 ;
    Element K1 ;
    Element K2 ;

    public CAPPRESecretKeyIPE(Element k0, Element k1, Element k2) {
        K0 = k0;
        K1 = k1;
        K2 = k2;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.K0.getLengthInBytes();
        sum += this.K1.getLengthInBytes();
        sum += this.K2.getLengthInBytes();

        return sum ;
    }
}
