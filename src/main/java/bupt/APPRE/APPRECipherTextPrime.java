package bupt.APPRE;

import it.unisa.dia.gas.jpbc.Element;

public class APPRECipherTextPrime {
    Element c1 ;
    Element c2 ;
    Element c3 ;
    Element c4 ;

    public APPRECipherTextPrime(Element c1, Element c2, Element c3, Element c4) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.c1.getLengthInBytes();
        sum += this.c2.getLengthInBytes();
        sum += this.c3.getLengthInBytes();
        sum += this.c4.getLengthInBytes();

        return sum ;
    }
}
