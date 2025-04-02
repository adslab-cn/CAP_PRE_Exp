package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;

public class FabricSecretKeyIBE {
    Element[] isk0;
    Element[] isk1;

    public FabricSecretKeyIBE(Element[] isk0, Element[] isk1) {
        this.isk0 = isk0;
        this.isk1 = isk1;
    }
    public int getByteLength(){
        int sum = 0 ;

        for (Element i : isk0){
            sum += i.getLengthInBytes();
        }
        for (Element i : isk1){
            sum += i.getLengthInBytes();
        }

        return sum ;
    }
}
