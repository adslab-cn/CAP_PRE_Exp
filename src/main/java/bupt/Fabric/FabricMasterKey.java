package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;

public class FabricMasterKey {
    Element[] gdN ;
    Element []aN ;
    Element b1 ;
    Element b2 ;

    public FabricMasterKey(Element[] gdn, Element[] an, Element b1, Element b2) {
        this.gdN = gdn;
        this.aN = an;
        this.b1 = b1;
        this.b2 = b2;
    }

    public int getByteLength(){
        int sum = 0 ;

        for (Element i : gdN){
            sum += i.getLengthInBytes();
        }
        for (Element i : aN){
            sum += i.getLengthInBytes();
        }
        sum += this.b1.getLengthInBytes();
        sum += this.b2.getLengthInBytes();


        return sum ;
    }
}
