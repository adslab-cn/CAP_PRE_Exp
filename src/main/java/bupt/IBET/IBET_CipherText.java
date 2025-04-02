package bupt.IBET;

import it.unisa.dia.gas.jpbc.Element;

public class IBET_CipherText {
    Element C0;
    Element C1;
    Element C2;

    public IBET_CipherText(Element c0, Element c1, Element c2) {
        C0 = c0;
        C1 = c1;
        C2 = c2;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.C0.getLengthInBytes();
        sum += this.C1.getLengthInBytes();
        sum += this.C2.getLengthInBytes();

        return sum ;
    }
}
