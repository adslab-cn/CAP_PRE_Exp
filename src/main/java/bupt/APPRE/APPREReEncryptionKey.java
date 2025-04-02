package bupt.APPRE;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class APPREReEncryptionKey {
    public ArrayList<Element[]> rk ;

    public APPREReEncryptionKey(ArrayList<Element[]> rk) {
        this.rk = rk;
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
