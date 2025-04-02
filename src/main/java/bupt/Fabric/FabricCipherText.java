package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;

public class FabricCipherText {
    Element c0 ;
    Element[] c1 ;
    Element[] c2 ;
    Element[] c3 ;

    public FabricCipherText(Element c0, Element[] c1, Element[] c2, Element[] c3) {
        this.c0 = c0;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.c0.getLengthInBytes();
        for (Element i : c1){
            sum += i.getLengthInBytes();
        }
        for (Element i : c2){
            sum += i.getLengthInBytes();
        }
        for (Element i : c3){
            sum += i.getLengthInBytes();
        }

        return sum ;
    }
}
