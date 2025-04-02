package bupt.Fabric;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FabricPublicKey {
    Element g ;
    Element h ;
    Element H1 ;
    Element H2 ;
    Element G1 ;
    Element G2 ;
    Element T1 ;
    Element T2 ;
    Element HashH(byte[] bytes, Field F){
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = sha1.digest(bytes);
        Element e_digest = F.newElementFromHash(digest,0,digest.length).getImmutable();
        return e_digest;
    }
    Element[] HashF(Element gt, Field F){
        Element[] result = new Element[3];
        byte[] bytes = gt.toBytes();
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = sha1.digest(bytes);

        result[0] = F.newElementFromHash(digest,0,digest.length/3).getImmutable();
        result[1] = F.newElementFromHash(digest,digest.length/3,digest.length/3).getImmutable();
        result[2] = F.newElementFromHash(digest,digest.length*2/3,digest.length/3).getImmutable();
        return result;
    }

    public FabricPublicKey(Element g, Element h, Element h1, Element h2, Element g1, Element g2, Element t1, Element t2) {
        this.g = g;
        this.h = h;
        H1 = h1;
        H2 = h2;
        G1 = g1;
        G2 = g2;
        T1 = t1;
        T2 = t2;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.g.getLengthInBytes();
        sum += this.h.getLengthInBytes();
        sum += this.H1.getLengthInBytes();
        sum += this.H2.getLengthInBytes();
        sum += this.G1.getLengthInBytes();
        sum += this.G2.getLengthInBytes();
        sum += this.T1.getLengthInBytes();
        sum += this.T2.getLengthInBytes();

        return sum ;
    }
}
