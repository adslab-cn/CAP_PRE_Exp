package bupt.APBPRE;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class APBPREReEncryptionKey {
    public ArrayList<Element[]> rk ;
    public  Element omega;
    Element ti;

    public APBPREReEncryptionKey(ArrayList<Element[]> rk, Element omega, Element ti) {
        this.rk = rk;
        this.omega = omega;
        this.ti = ti;
    }

    public int getByteLength(){
        int sum = 0 ;
        for (Element[] r : rk){
            for (Element i  : r)
            sum += i.getLengthInBytes();
        }
        return sum ;
    }
}
