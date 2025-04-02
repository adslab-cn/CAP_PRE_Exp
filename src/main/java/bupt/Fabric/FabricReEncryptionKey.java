package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.Map;

public class FabricReEncryptionKey {
    Element[] rk0;
    Map<String, ArrayList<Element>> rk1;
    Element[] rk2;
    Element[] rk3;
    Element[] rk4;

    String policy;

    public FabricReEncryptionKey(Element[] rk0, Map<String, ArrayList<Element>> rk1, Element[] rk2, Element[] rk3, Element[] rk4, String policy) {
        this.rk0 = rk0;
        this.rk1 = rk1;
        this.rk2 = rk2;
        this.rk3 = rk3;
        this.rk4 = rk4;
        this.policy = policy;
    }
    public int getByteLength(){
        int sum = 0 ;
        for (Element i : rk0){
            sum += i.getLengthInBytes();
        }
        for (Element i : rk2){
            sum += i.getLengthInBytes();
        }
        for (Element i : rk3){
            sum += i.getLengthInBytes();
        }
        for (Element i : rk4){
            sum += i.getLengthInBytes();
        }
        for (ArrayList<Element> s : rk1.values()){
            for (Element i :s){
                sum += i.getLengthInBytes();
            }
        }
        return sum ;
    }
}
