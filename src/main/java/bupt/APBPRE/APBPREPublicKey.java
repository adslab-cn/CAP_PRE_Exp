package bupt.APBPRE;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class APBPREPublicKey {
    Element g;
    Element g1 ;

    public APBPREPublicKey(Element g, Element g1) {
        this.g = g;
        this.g1 = g1;
    }

    public Element H(Element input, Field F){
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = sha1.digest(input.toBytes());
        Element e_digest = F.newElementFromHash(digest,0,digest.length).getImmutable();
        return e_digest;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.g.getLengthInBytes();
        sum += this.g1.getLengthInBytes();

        return sum ;
    }
}
