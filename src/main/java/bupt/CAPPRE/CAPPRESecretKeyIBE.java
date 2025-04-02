package bupt.CAPPRE;

import it.unisa.dia.gas.jpbc.Element;

public class CAPPRESecretKeyIBE {
    Element sk0 ;
    Element sk1 ;

    public CAPPRESecretKeyIBE(Element sk0, Element sk1) {
        this.sk0 = sk0;
        this.sk1 = sk1;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.sk0.getLengthInBytes();
        sum += this.sk1.getLengthInBytes();

        return sum ;
    }
}
