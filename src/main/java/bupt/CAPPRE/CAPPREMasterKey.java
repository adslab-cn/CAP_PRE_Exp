package bupt.CAPPRE;

import it.unisa.dia.gas.jpbc.Element;

public class CAPPREMasterKey {
    Element h_hat;
    Element h_0;
    Element[] hi;

    public CAPPREMasterKey(Element h_hat, Element h_0, Element[] hi) {
        this.h_hat = h_hat;
        this.h_0 = h_0;
        this.hi = hi;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.h_hat.getLengthInBytes();
        sum += this.h_0.getLengthInBytes();
        for (Element i : hi){
            sum += i.getLengthInBytes();
        }

        return sum ;
    }
}
