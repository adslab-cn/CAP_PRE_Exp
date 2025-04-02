package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

public class FabricSecretKeyABE {
    Element[] sk0;
    Map<String,Element[]> sky;
    Element[] skp;

    String [] S ;

    public FabricSecretKeyABE(Element[] sk0, Map<String, Element[]> sky, Element[] skp, String[] s) {
        this.sk0 = sk0;
        this.sky = sky;
        this.skp = skp;
        this.S =s ;
    }
    public int getByteLength(){
        int sum = 0 ;
        for (Element i : sk0){
            sum += i.getLengthInBytes();
        }
        for (Element i : skp){
            sum += i.getLengthInBytes();
        }
        for (Element[] s : sky.values()){
            for (Element i :s){
                sum += i.getLengthInBytes();
            }
        }

        return sum ;
    }
}
