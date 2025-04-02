package bupt.CAPPRE;

import it.unisa.dia.gas.jpbc.Element;

public class CAPPRECipherText {
    Element C ;
    Element C0 ;
    Element C1 ;
    Element C2 ;

    public CAPPRECipherText(Element c, Element c0, Element c1, Element c2) {
        C = c;
        C0 = c0;
        C1 = c1;
        C2 = c2;
    }

    public int getByteLength(){
        int sum = 0 ;
        sum += this.C.getLengthInBytes();
        sum += this.C0.getLengthInBytes();
        sum += this.C1.getLengthInBytes();
        sum += this.C2.getLengthInBytes();

        return sum ;
    }
}
