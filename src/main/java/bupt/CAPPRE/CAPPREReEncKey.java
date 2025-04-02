package bupt.CAPPRE;

import it.unisa.dia.gas.jpbc.Element;

public class CAPPREReEncKey {
    Element d0 ; //or d_{j-1 to j}
    Element d1 = null;
    Element R;
    Element R_til;
    Element R_0;
    Element [] Ri ;

    public CAPPREReEncKey(Element d0, Element d1, Element r, Element r_til, Element r_0, Element[] ri) {
        this.d0 = d0;
        this.d1 = d1;
        R = r;
        R_til = r_til;
        R_0 = r_0;
        Ri = ri;
    }

    public CAPPREReEncKey(Element d0, Element r, Element r_til, Element r_0, Element[] ri) {
        this.d0 = d0;
        R = r;
        R_til = r_til;
        R_0 = r_0;
        Ri = ri;
    }

    public int getByteLength1(){
        int sum = 0 ;
        sum += this.d0.getLengthInBytes();
        sum += this.d1.getLengthInBytes();
        sum += this.R.getLengthInBytes();
        sum += this.R_til.getLengthInBytes();
        sum += this.R_0.getLengthInBytes();
        for (Element i : Ri){
            sum += i.getLengthInBytes();
        }
        return sum ;
    }
    public int getByteLength2(){
        int sum = 0 ;
        sum += this.d0.getLengthInBytes();
        sum += this.R.getLengthInBytes();
        sum += this.R_til.getLengthInBytes();
        sum += this.R_0.getLengthInBytes();
        for (Element i : Ri){
            sum += i.getLengthInBytes();
        }
        return sum ;
    }

}
