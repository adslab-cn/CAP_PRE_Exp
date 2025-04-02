package bupt.IBET;

import it.unisa.dia.gas.jpbc.Element;

public class IBET_PrivateKey {
    Element sk ;

    public IBET_PrivateKey(Element sk) {
        this.sk = sk;
    }
    public int getByteLength(){


        return sk.getLengthInBytes();
    }
}
