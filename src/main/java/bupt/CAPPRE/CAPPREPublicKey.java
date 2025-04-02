package bupt.CAPPRE;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CAPPREPublicKey {
    Element g ;
    Element g_bar ;
    Element g_til ;
    Element g_0 ;
    Element[] gi ;
    Element g_hat ;
    Element h ;
    Element h_bar ;
    Element h_til ;
    Element u ;
    Element v ;
    Element Y ;

    public CAPPREPublicKey(Element g, Element g_bar, Element g_til, Element g_0, Element[] gi, Element g_hat, Element h, Element h_bar, Element h_til, Element u, Element v, Element y) {
        this.g = g;
        this.g_bar = g_bar;
        this.g_til = g_til;
        this.g_0 = g_0;
        this.gi = gi;
        this.g_hat = g_hat;
        this.h = h;
        this.h_bar = h_bar;
        this.h_til = h_til;
        this.u = u;
        this.v = v;
        Y = y;
    }
    public int getByteLength(){
        int sum = 0 ;
        sum += this.g.getLengthInBytes();
        sum += this.g_bar.getLengthInBytes();
        sum += this.g_til.getLengthInBytes();
        sum += this.g_0.getLengthInBytes();
        for (Element i : gi){
            sum += i.getLengthInBytes();
        }
        sum += this.g_hat.getLengthInBytes();
        sum += this.h.getLengthInBytes();
        sum += this.h_bar.getLengthInBytes();
        sum += this.h_til.getLengthInBytes();
        sum += this.u.getLengthInBytes();
        sum += this.v.getLengthInBytes();
        sum += this.Y.getLengthInBytes();

        return sum ;
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

}
