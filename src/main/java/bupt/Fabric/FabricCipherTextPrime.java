package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.Map;

public class FabricCipherTextPrime {
    Element c0p;
    Element[] c1p;
    Element[] c2p;
    Map<String, ArrayList<Element>> c3p;
    Element[] c4p;

    String policy;

    public FabricCipherTextPrime(Element c0p, Element[] c1p, Element[] c2p, Map<String, ArrayList<Element>> c3p, Element[] c4p, String policy) {
        this.c0p = c0p;
        this.c1p = c1p;
        this.c2p = c2p;
        this.c3p = c3p;
        this.c4p = c4p;
        this.policy = policy;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.c0p.getLengthInBytes();

        for (Element i : c1p){
            sum += i.getLengthInBytes();
        }
        for (Element i : c2p){
            sum += i.getLengthInBytes();
        }
        for (Element i : c4p){
            sum += i.getLengthInBytes();
        }
        for (String s : c3p.keySet()){
            for (Element i : c3p.get(s)){
                sum +=i.getLengthInBytes();
            }
        }

        return sum ;
    }
}
