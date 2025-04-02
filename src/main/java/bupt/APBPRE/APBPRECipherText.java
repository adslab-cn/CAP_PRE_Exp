package bupt.APBPRE;

import it.unisa.dia.gas.jpbc.Element;

public class APBPRECipherText {
    Element c1 ;
    Element c2 ;
    Element c3 ;



    public APBPRECipherText(Element c1, Element c2,Element c3) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    public int getByteLength(){
        int sum = 0 ;
        sum += this.c1.getLengthInBytes();
        sum += this.c2.getLengthInBytes();
        sum += this.c3.getLengthInBytes();

        return sum ;
    }
}
