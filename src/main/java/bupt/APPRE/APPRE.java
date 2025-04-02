package bupt.APPRE;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APPRE {
    private Pairing curveParams;
    private Field G, G1, Zr;
    private APPREPublicKey pk ;

    public APPRE(Pairing curveParams) {
        this.curveParams = curveParams;
        G = curveParams.getG1();
        G1 = curveParams.getGT();
        Zr = curveParams.getZr();
    }

    public void setup(){
        Element g = G.newRandomElement().getImmutable();
        Element g1 = G.newRandomElement().getImmutable();
        pk = new APPREPublicKey(g,g1);
    }

    public APPREKeyPair keyGen(){
        Element x = Zr.newRandomElement().getImmutable();
        Element gx = pk.g.powZn(x).getImmutable();
        return new APPREKeyPair(x,gx) ;
    }
    public APPREPath createPath(Element pki, Element[] pks){
        Element[] path = new Element[pks.length+1];
        path[0] = pki.getImmutable() ;
        for (int i = 1;i< pks.length+1;i++){
            path[i] = pks[i-1].getImmutable();
        }
        return new APPREPath(pks.length,path);
    }
    public APPREReEncryptionKey rKGen(Element sk, APPREPath path){
        ArrayList<Element[]> rk = new ArrayList<Element[]>();

        Element xi = G1.newRandomElement().getImmutable();
        Element ri = Zr.newRandomElement().getImmutable();
        Element hxi = pk.H(xi,G).getImmutable();
        Element[] rk1 = new Element[3] ;
        rk1[0] = pk.g.powZn(ri).getImmutable();
        rk1[1] = xi.mul(curveParams.pairing(pk.g1, path.Pa[1].powZn(ri))).getImmutable();
        rk1[2] = hxi.mul(pk.g1.powZn(sk.negate())).getImmutable();
        rk.add(rk1);
        for (int j = 1; j< path.l; j++){
            Element[] rki = new Element[3] ;
            ri = Zr.newRandomElement().getImmutable();
            xi = G1.newRandomElement().getImmutable();
            rki[0] = pk.g.powZn(ri).getImmutable() ;
            rki[1] = xi.mul(curveParams.pairing(pk.g1,path.Pa[j+1].powZn(ri))).getImmutable() ;
            Element hxi_1 = hxi.getImmutable();
            hxi = pk.H(xi,G).getImmutable();
            rki[2] = hxi.div(hxi_1).getImmutable() ;
            rk.add(rki) ;
        }
        return new APPREReEncryptionKey(rk) ;
    }
    public APPRECipherText encrypt(Element pki,Element m ){
        //Element m = G1.newRandomElement().getImmutable();
        //System.out.println("message:"+m);
        Element r = Zr.newRandomElement().getImmutable();
        Element c1 = pk.g.powZn(r).getImmutable();
        Element c2 = m.mul(curveParams.pairing(pk.g1, pki).powZn(r)).getImmutable();
        return new APPRECipherText(c1,c2);
    }
    public APPRECipherTextPrime reEncrypt(APPREPath path,Element pkij, Element pkij1, Element[] rki, APPRECipherText ct ){
        List<Element> elements = Arrays.asList(path.Pa);
        boolean b1 = elements.contains(pkij);
        boolean b2 = elements.contains(pkij1);
        if (!(b1 && b2)){
            System.err.println("public key does not on the path. ");
            System.exit(1);
        }
        Element c2 = ct.c2.mul(curveParams.pairing(ct.c1, rki[2])).getImmutable();
        return new APPRECipherTextPrime(ct.c1, c2, rki[0],rki[1]) ;
    }
    public APPRECipherTextPrime reEncrypt(APPREPath path,Element pkij, Element pkij1, Element[] rki, APPRECipherTextPrime ct ){
        List<Element> elements = Arrays.asList(path.Pa);
        boolean b1 = elements.contains(pkij);
        boolean b2 = elements.contains(pkij1);
        if (!(b1 && b2)){
            System.err.println("public key does not on the path.");
            System.exit(1);
        }
        Element c2 = ct.c2.mul(curveParams.pairing(ct.c1, rki[2])).getImmutable();
        return new APPRECipherTextPrime(ct.c1,c2,rki[0],rki[1]);
    }

    public Element decrypt1(APPRECipherText ct, Element sk){
        Element p = curveParams.pairing(ct.c1, pk.g1.powZn(sk)).getImmutable();
        Element m = ct.c2.div(p).getImmutable();
        return  m ;
    }
    public Element decrypt2(APPRECipherTextPrime ct, Element sk){
        Element p = curveParams.pairing(pk.g1.powZn(sk), ct.c3).getImmutable();
        Element xj = ct.c4.div(p).getImmutable();
        Element hxj = pk.H(xj, G).getImmutable();
        Element p2 = curveParams.pairing(ct.c1, hxj).getImmutable();
        Element m = ct.c2.div(p2).getImmutable();
        return m;
    }
}
