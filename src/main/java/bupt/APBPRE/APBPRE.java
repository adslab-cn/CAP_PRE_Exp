package bupt.APBPRE;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APBPRE {
    private Pairing curveParams;
    private Field G, G1, Zr;
    private APBPREPublicKey pk ;

    public APBPRE(Pairing curveParams) {
        this.curveParams = curveParams;
        G = curveParams.getG1();
        G1 = curveParams.getGT();
        Zr = curveParams.getZr();
    }

    public void setup(){
        Element g = G.newRandomElement().getImmutable();
        Element g1 = G.newRandomElement().getImmutable();
        pk = new APBPREPublicKey(g,g1);
    }

    public APBPREKeyPair keyGen(){
        Element x = Zr.newRandomElement().getImmutable();
        Element gx = pk.g.powZn(x).getImmutable();
        return new APBPREKeyPair(x,gx) ;
    }
    public APBPREPath createPath(Element pki, Element[] pks){
        Element[] path = new Element[pks.length+1];
        path[0] = pki.getImmutable() ;
        for (int i = 1;i< pks.length+1;i++){
            path[i] = pks[i-1].getImmutable();
        }
        return new APBPREPath(pks.length,path);
    }
    public APBPREReEncryptionKey mPRKGen(Element sk, APBPREPath path){
        ArrayList<Element[]> rk = new ArrayList<Element[]>();

        Element xi = G1.newRandomElement().getImmutable();
        Element ri = Zr.newRandomElement().getImmutable();
        Element hxi = pk.H(xi,Zr).getImmutable();
        Element[] rk1 = new Element[3] ;
        rk1[0] = pk.g.powZn(ri).getImmutable();
        rk1[1] = xi.mul(curveParams.pairing(pk.g1, path.Pa[1].powZn(ri))).getImmutable();
        rk1[2] = pk.g1.powZn(hxi.sub(sk)).getImmutable();
        rk.add(rk1);
        for (int j = 1; j< path.l; j++){
            Element[] rki = new Element[3] ;
            ri = Zr.newRandomElement().getImmutable();
            xi = G1.newRandomElement().getImmutable();
            rki[0] = pk.g.powZn(ri).getImmutable() ;
            rki[1] = xi.mul(curveParams.pairing(pk.g1,path.Pa[j+1].powZn(ri))).getImmutable() ;
            Element hxi_1 = hxi.getImmutable();
            hxi = pk.H(xi,Zr).getImmutable();
            rki[2] = pk.g1.powZn(hxi.sub(hxi_1)).getImmutable() ;
            rk.add(rki) ;
        }
        Element omega = G1.newRandomElement().getImmutable();
        Element ti = pk.g1.powZn(pk.H(omega, Zr)).getImmutable();
        return new APBPREReEncryptionKey(rk,omega,ti) ;
    }
    public APBPRECipherText encrypt(Element pki, Element omega, Element m){
        //Element m = G1.newRandomElement().getImmutable();
        //System.out.println("message:"+m);
        Element r = Zr.newRandomElement().getImmutable();
        Element c1 = pk.g.powZn(r).getImmutable();
        Element hOmega = pk.H(omega, Zr).getImmutable();
        Element c2 = m.mul(curveParams.pairing(pki, pk.g1.powZn(hOmega)).powZn(r)).getImmutable();
        Element c3 = pk.g.powZn(hOmega.mul(r)).getImmutable();
        return new APBPRECipherText(c1,c2,c3);
    }
    public APBPRECipherTextPrime reEncrypt(APBPREPath path, Element pkij, Element pkij1, Element[] rki, APBPRECipherText ct ){
        List<Element> elements = Arrays.asList(path.Pa);
        boolean b1 = elements.contains(pkij);
        boolean b2 = elements.contains(pkij1);
        if (!(b1 && b2)){
            System.err.println("public key does not on the path. ");
            System.exit(1);
        }
        Element c2 = ct.c2.mul(curveParams.pairing(ct.c3, rki[2])).getImmutable();
        return new APBPRECipherTextPrime(ct.c1, c2, ct.c3, rki[0],rki[1]) ;
    }
    public APBPRECipherTextPrime reEncrypt(APBPREPath path, Element pkij, Element pkij1, Element[] rki, APBPRECipherTextPrime ct ){
        List<Element> elements = Arrays.asList(path.Pa);
        boolean b1 = elements.contains(pkij);
        boolean b2 = elements.contains(pkij1);
        if (!(b1 && b2)){
            System.err.println("public key does not on the path.");
            System.exit(1);
        }
        Element c2 = ct.c2.mul(curveParams.pairing(ct.c3, rki[2])).getImmutable();
        return new APBPRECipherTextPrime(ct.c1,c2,ct.c3,rki[0],rki[1]);
    }

    public Element decrypt1(APBPRECipherText ct, Element sk){
        Element p = curveParams.pairing(ct.c3, pk.g1.powZn(sk)).getImmutable();
        Element m = ct.c2.div(p).getImmutable();
        return  m ;
    }
    public Element decrypt2(APBPRECipherTextPrime ct, Element sk){
        Element p = curveParams.pairing(pk.g1.powZn(sk), ct.c4).getImmutable();
        Element xj = ct.c5.div(p).getImmutable();
        Element hxj = pk.H(xj, Zr).getImmutable();
        Element p2 = curveParams.pairing(ct.c3, pk.g1.powZn(hxj)).getImmutable();
        Element m = ct.c2.div(p2).getImmutable();
        return m;
    }
}
