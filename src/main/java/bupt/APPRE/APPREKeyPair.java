package bupt.APPRE;

import it.unisa.dia.gas.jpbc.Element;

public class APPREKeyPair {
    public Element sk ;
    public Element pk ;

    public APPREKeyPair(Element sk, Element pk) {
        this.sk = sk;
        this.pk = pk;
    }
    public int getByteLength(){

        return sk.getLengthInBytes() ;
    }
}
