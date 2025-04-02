package bupt.IBET;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IBET_PublicKey {
    Element g1;
    Element u;
    Element ua;
    Element[] ha;
    Element egh;

    Element H0(byte[] bytes, Field Zr){
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = sha1.digest(bytes);
        Element e_digest = Zr.newElementFromHash(digest,0,digest.length).getImmutable();
        return e_digest;
    }

    Element H1(Element gt, Field G){
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = sha1.digest(gt.toBytes());
        Element e_digest = G.newElementFromHash(digest,0,digest.length).getImmutable();
        return e_digest;
    }

    public IBET_PublicKey(Element g1, Element u, Element ua, Element[] ha, Element egh) {
        this.g1 = g1;
        this.u = u;
        this.ua = ua;
        this.ha = ha;
        this.egh = egh;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.g1.getLengthInBytes();
        sum += this.u.getLengthInBytes();
        sum += this.ua.getLengthInBytes();
        for (Element i : ha){
            sum += i.getLengthInBytes();
        }
        sum += this.egh.getLengthInBytes();

        return sum ;
    }
}
