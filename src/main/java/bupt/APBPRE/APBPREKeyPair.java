package bupt.APBPRE;

import it.unisa.dia.gas.jpbc.Element;

public class APBPREKeyPair {
    public Element sk ;
    public Element pk ;

    public APBPREKeyPair(Element sk, Element pk) {
        this.sk = sk;
        this.pk = pk;
    }
    public int getByteLength(){
        return sk.getLengthInBytes() ;
    }
}
