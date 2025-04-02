package bupt.APPRE;

import it.unisa.dia.gas.jpbc.Element;

public class APPRECipherText {
    Element c1 ;
    Element c2 ;

    public APPRECipherText(Element c1, Element c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public int getByteLength(){
        int sum = 0 ;
        sum += this.c1.getLengthInBytes();
        sum += this.c2.getLengthInBytes();

        return sum ;
    }
}
