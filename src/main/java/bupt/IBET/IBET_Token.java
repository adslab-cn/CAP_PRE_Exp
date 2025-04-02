package bupt.IBET;

import it.unisa.dia.gas.jpbc.Element;

public class IBET_Token {
    Element d1 ;
    Element d2 ;
    Element d3 ;
    Element d4 ;

    public IBET_Token(Element d1, Element d2, Element d3, Element d4) {
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
        this.d4 = d4;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.d1.getLengthInBytes();
        sum += this.d2.getLengthInBytes();
        sum += this.d3.getLengthInBytes();
        sum += this.d4.getLengthInBytes();

        return sum ;
    }
}
